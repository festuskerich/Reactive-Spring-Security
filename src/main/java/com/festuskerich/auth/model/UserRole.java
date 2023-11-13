package com.festuskerich.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_roles")
public class UserRole {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    @Column("role_id")
    private Long roleId;
    
}
