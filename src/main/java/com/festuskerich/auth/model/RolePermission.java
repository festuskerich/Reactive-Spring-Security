package com.festuskerich.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("role_permissions")
public class RolePermission {
    @Id
    private Long id;
    @Column("role_id")
    private Long roleId;
    @Column("permission_id")
    private Long permissionId; 
}
