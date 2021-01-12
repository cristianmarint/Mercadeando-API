package com.api.mercadeando.service;

import com.api.mercadeando.entity.Rol;
import com.api.mercadeando.entity.User;
import com.api.mercadeando.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional()
    public UserDetails loadUserByUsername(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(
                        ()->new UsernameNotFoundException("Usuario no encontrado con username:"+ username));
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

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Rol> roles) {
        String[] permisos = roles.stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .map(permiso -> permiso.getName())
                .toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(permisos);
    }
}
