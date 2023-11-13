package com.festuskerich.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("permissions")
public class Permission {
    @Id
    private Long id;
    String name;
}
