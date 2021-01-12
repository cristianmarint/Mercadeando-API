package com.api.mercadeando.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username es requerido")
    @Column(nullable = false,unique = true)
    private String username;

    @NotBlank(message = "Password es requerido")
    @Column(nullable = false)
    private String password;

    @Email
    @NotBlank(message = "Email es requerido")
    @Column(nullable = false,unique = true)
    private String email;

    @Builder.Default
    private Timestamp createdAt= Timestamp.from(Instant.now());

    @Builder.Default
    private Boolean activo=false;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(targetEntity = Rol.class)
    @JoinTable(
            name = "user_rol",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id",referencedColumnName = "id")
    )
    private Set<Rol> roles = new HashSet<>();
    public void addRol(Rol rol){
        if (roles == null){
            roles = new HashSet<>(Collections.singleton(rol));
        }else {
            roles.add(rol);
        }
    }
    public void removeRol(Rol rol){
        if (roles!=null){
            roles.remove(rol);
        }
    }
}