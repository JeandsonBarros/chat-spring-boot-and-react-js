package com.chat.br.models;

import com.chat.br.enums.RoleEnum;
import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
public class RoleModel implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleEnum roleName;

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }
}
