package com.api.mercadeando.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author cristianmarint
 * @Date 2021-01-11 9:51
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToMany(
            mappedBy = "roles",
            targetEntity = User.class,
            fetch=FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
    @Fetch(value= FetchMode.SELECT)
    private List<User> users;
    public void addUser(User user){
        if (users == null){
            users = new ArrayList<User>(Collections.singleton(user));
        }else {
            users.add(user);
        }
    }
    public void removeUser(User user){
        if (users!=null){
            users.remove(user);
        }
    }

    @ManyToMany(cascade = CascadeType.MERGE,targetEntity = Permiso.class, fetch=FetchType.EAGER)
    @JoinTable(name="rol_permiso")
    private Set<Permiso> permisos;
    public void addPermiso(Permiso permiso){
        if (permisos == null){
            permisos = new HashSet<>(Arrays.asList(permiso));
        }else {
            permisos.add(permiso);
        }
    }
    public void removePermiso(Permiso permiso){
        if (permisos!=null){
            permisos.remove(permiso);
        }
    }
}
