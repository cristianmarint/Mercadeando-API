package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.LoginRequest;
import com.api.mercadeando.domain.service.AuthService;
import com.api.mercadeando.testdata.AuthTestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CATEGORIAS_V1;
import static com.api.mercadeando.testdata.CategoriaTestdata.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author cristianmarint
 * @Date 2021-02-05 8:49
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoriaControllerTest {
    private static final LoginRequest loginRequest = new AuthTestData().usarUsuarioAdminLogin();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AuthService authService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    public String getAuthorizationBearerToken(LoginRequest loginRequest) {
        return authService.login(loginRequest).getAuthenticationToken();
    }

    @Test
    public void getAllCategorias_statusOkAndContentJSon_IfAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_CATEGORIAS_V1)
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))

                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoria_statusOkAndContentJson_ifAuthenticated() throws Exception {
        String expected = "{\"id\":1,\"self\":{\"rel\":\"self\",\"type\":\"GET\",\"href\":\"/api/v1/categorias/1\"},\"nombre\":\"Cuidado Personal\",\"descripcion\":\"La autoprotección, velar por el bienestar propio y la imagen que transmitimos a los demás, hacen parte del cuidado personal. Muchos lo asocian con aseo e higiene que permite que el cuerpo y la mente se encuentren saludables.\\n\",\"productos\":[]}";

        mvc.perform(MockMvcRequestBuilders.get(URL_CATEGORIAS_V1 + "/1")
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected))
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoria_statusNotFound_ifAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_NO_EXISTENTE)
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCategoria_statusBadRequest_ifAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_CATEGORIAS_V1 + "/badRequest/")
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCategoria_statusCreated_ifAllDataIsValidAndAuthenticated() throws Exception {
        mvc.perform(post(URL_CATEGORIAS_V1)
                .header("Authorization", getAuthorizationBearerToken(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BODY_VALID)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void addCategoria_statusBadRequest_ifDataIsNotValidAndAuthenticated() throws Exception {
        mvc.perform(post(URL_CATEGORIAS_V1)
                .header("Authorization", getAuthorizationBearerToken(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BODY_INVALID)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCategoria_statusOk_ifDataIsValidAndAuthenticated() throws Exception {
        mvc.perform(put(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_EDIT_READ)
                .header("Authorization", getAuthorizationBearerToken(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BODY_VALID_EDIT)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void editCategoria_statusBadRequest_ifDataIsInValidAndAuthenticated() throws Exception {
        mvc.perform(put(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_EDIT_READ)
                .header("Authorization", getAuthorizationBearerToken(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BODY_INVALID)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCategoria_statusNotFound_ifDataIsValidAndAuthenticated() throws Exception {
        mvc.perform(put(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_NO_EXISTENTE)
                .header("Authorization", getAuthorizationBearerToken(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(BODY_VALID)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCategory_statusNotContent_ifAuthenticated() throws Exception {
        mvc.perform(delete(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_DELETE)
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCategory_statusNotFound_ifAuthenticated() throws Exception {
        mvc.perform(delete(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_NO_EXISTENTE)
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllCategoriaProductos_statusOk_ifAuthenticated() throws Exception {
        mvc.perform(get(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_EDIT_READ + "/productos")
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCategoriaProductos_statusNotFound_ifAuthenticated() throws Exception {
        mvc.perform(get(URL_CATEGORIAS_V1 + "/" + CATEGORIAID_NO_EXISTENTE + "/productos")
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllCategoriaProductos_statusBadRequest_ifAuthenticated() throws Exception {
        mvc.perform(get(URL_CATEGORIAS_V1 + "/badrequest/productos")
                .header("Authorization", getAuthorizationBearerToken(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
