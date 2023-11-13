package com.festuskerich.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import java.util.Set;

@Table("roles")
public class Role {
    @Id
    private Long id;
    String name;
     @MappedCollection(idColumn = "role_id")
    private Set<RolePermission> rolePermissions;
}
