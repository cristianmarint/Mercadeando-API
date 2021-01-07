package com.api.mercadeando.entidades;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.Instant;

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
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "Password es requerido")
    @Column(nullable = false)
    private String password;

    @Email
    @NotBlank(message = "Email es requerido")
    @Column(nullable = false)
    private String email;

    @Builder.Default
    private Timestamp createdAt= Timestamp.from(Instant.now());

    @Builder.Default
    private Boolean activo=false;
}
