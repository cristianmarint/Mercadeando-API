package com.api.mercadeando.service;

import com.api.mercadeando.entity.Permiso;
import com.api.mercadeando.entity.Rol;
import com.api.mercadeando.entity.User;
import com.api.mercadeando.repository.PermisoRepository;
import com.api.mercadeando.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PermisoRepository permisoRepository;

    @Override
    @Transactional()
    public UserDetails loadUserByUsername(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado con username:"+ username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getActivo(),
                true,
                true,
                true,
                getAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Rol> rolSet) {
        return getGrantedAuthorities(getPermisos(rolSet));
    }

    private List<String> getPermisos(Set<Rol> rolSet) {
        List<String> permisos=new ArrayList<>();
        Set<String> roles=new HashSet<>();

        for (Rol r:rolSet) {
            roles.add(r.getName());
            Set<Permiso> tempo = permisoRepository.findByRolName(r.getName());
            for (Permiso p:tempo) {
                permisos.add(p.getName());
            }
        }
        return permisos;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permisos) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : permisos) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
