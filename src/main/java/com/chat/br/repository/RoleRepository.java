package com.chat.br.repository;

import com.chat.br.enums.RoleEnum;
import com.chat.br.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    RoleModel findByRoleName(RoleEnum roleEnum);
}
