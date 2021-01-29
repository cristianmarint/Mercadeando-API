# Mercadeando API

_REST API para comprar ricuras! Ordenes, pagos (efectivo, cheques e integración con la API de PayPal), roles y permisos_

## Comenzando 🚀

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas._

Mira **Despliegue** para conocer como desplegar el proyecto.


### Pre-requisitos 📋

_Que cosas necesitas para instalar el software y como instalarlas_

```
Maven
IntelliJ es recomendado
```

### Instalación 🔧

```
    git clone https://github.com/cristianmarint/Mercadeando-API
    Abre el directorio con tu IDE de preferencia, recomiendo IntelliJ
    Pon las credenciales para el servidor de Email y Paypal credencials
    y ahora ejecuta la app!
```

_Puedes usar el archivo de Postman para realizar peticiones Usa el archivo en `Resources Mercadeando Test.postman_collection.json` y la respuesta se verá así:_
```
// GET http://localhost:8080/api/v1/ordenes/2

{
    "id": 2,
    "self": {
        "rel": "self",
        "type": "GET",
        "href": "/api/v1/ordenes/2"
    },
    "activo": true,
    "fecha": "31-12-2020 20:01:01",
    "total": 38.20,
    "estado": "PAGADO",
    "pago": {
        "id": 2,
        "self": {
            "rel": "self",
            "type": "GET",
            "href": "/api/v1/pagos/2"
        },
        "total": 38.20,
        "metodo": "TARJETA_CREDITO",
        "orden": {
            "rel": "self",
            "type": "GET",
            "href": "/api/v1/ordenes/2"
        },
        "paypal_payment_id": null,
        "paypal_url": null,
        "moneda": "COP",
        "fecha": "31-12-2020 20:01:01"
    },
    "cliente": {
        "rel": "cliente",
        "type": "GET",
        "href": "/api/v1/clientes/1"
    },
    "productos": [
        {
            "id": 4,
            "codigo": "CAM4",
            "nombre": "Garbanzo Maritza premium",
            "cantidad": 3,
            "precioUnidad": 2.59,
            "precioTotal": 7.77,
            "self": {
                "rel": "self",
                "type": "GET",
                "href": "/api/v1/productos/4"
            }
        },
        {
            "id": 3,
            "codigo": "CAM3",
            "nombre": "Frijol bola roja Maritza premium",
            "cantidad": 3,
            "precioUnidad": 8.99,
            "precioTotal": 26.97,
            "self": {
                "rel": "self",
                "type": "GET",
                "href": "/api/v1/productos/3"
            }
        },
        {
            "id": 2,
            "codigo": "CAM2",
            "nombre": "Lenteja Diana",
            "cantidad": 2,
            "precioUnidad": 1.73,
            "precioTotal": 3.46,
            "self": {
                "rel": "self",
                "type": "GET",
                "href": "/api/v1/productos/2"
            }
        }
    ]
}
```

```
// POST http://localhost:8080/api/v1/auth/login

{
    "username": "admin",
    "authenticationToken": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYxMTg0NDk4OSwiZXhwIjoxNjExODQ1NTg5fQ.BCbVNMwrqyYEELCMsboWMXI87QtDcra4IeMOdrUoSK3GkHkw9rQvgiGsXDvcdIesUfjNTAIGuwFP0gnYqdELWK_cM8fXVX5gzCXsYgyi0VyT8JiC2MqeTdeVUny7MUwyBt81ky1D-LvPzWCQPR61vxcRJ-4J9gZrM4tLSgSeNyj7yGOMa7m2FPzxDuAuL-6zvqoAlQ4jwvDcLtQICvZz1iRuQL3dq0Vi71Qs2z82iwffryOfOtSErE5X3t7u1DCsluoJBUOMwx-pgMjfUPvJV4LQFOyfQ9HadmuBshHmOuy4pKfS56T-mdB5qPt4sG4DEEFjRvk6vVjxvdEGtOAw8g",
    "refreshToken": "5831eba0-1672-4162-90e0-ad2d2268b8fc",
    "expiresAt": 1611845589.966120700,
    "roles": [
        "ADMIN",
        "CAJERO"
    ],
    "permisos": [
        "EDIT_CLIENTE",
        "READ_CATEGORIA",
        "BROWSE_PAGO",
        "DELETE_CLIENTE",
        "READ_PRODUCTO",
        "ADD_PRODUCTO",
        "DELETE_PRODUCTO",
        "BROWSE_ORDEN",
        "BROWSE_CATEGORIA",
        "DELETE_CATEGORIA",
        "READ_PAGO",
        "DELETE_PAGO",
        "ADD_ORDEN",
        "EDIT_ORDEN",
        "BROWSE_PRODUCTO",
        "READ_ORDEN",
        "ADD_PAGO",
        "EDIT_CATEGORIA",
        "EDIT_PAGO",
        "EDIT_PRODUCTO",
        "READ_CLIENTE",
        "ADD_CATEGORIA",
        "DELETE_ORDEN",
        "ADD_CLIENTE",
        "BROWSE_CLIENTE"
    ]
}
```

## Despliegue 📦

_Recuerda establecer las credenciales para Email y Paypal_

## Construido con 🛠️

  * [Spring Boot un framework de Java ☕](https://spring.io/projects/spring-boot)
  * [H2 & JPA](#)
  * [PayPal API ☕](https://developer.paypal.com/home/)


## Contribuyendo 🖇️

Si tienes algo en mente crea una pull requests. 👏🏾

## Versionado 📌

Usamos [SemVer](http://semver.org/) para el versionado. Para todas las versiones disponibles, mira los [tags en este repositorio](https://github.com/cristianmarint/Mercadeando-API/tags).

## Autores ✒️

_Menciona a todos aquellos que ayudaron a levantar el proyecto desde sus inicios_

* **Cristian Marín** - *Autor original* -
    [cristianmarint](https://github.com/cristianmarint)

También puedes mirar la lista de todos los [contribuyentes](https://github.com/cristianmarint/Mercadeando-API/contributors) quíenes han participado en este proyecto.

## Licencia 📄

Este proyecto está bajo la Licencia (MIT) - mira el archivo [LICENSE](LICENSE) para detalles

## Acknowledgments
 - Chill not everything's perfect anyways.

---
⌨️ con ❤️ por [cristianmarint](https://github.com/cristianmarint) 😊
