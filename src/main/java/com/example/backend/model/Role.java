package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "Role_ID", nullable = false)
    private int roleId;
    @Column(name = "Role_Name", nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "role")
    Set<User> user = new HashSet<>();
}
