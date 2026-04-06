package com.example.guilda.repository.audit;

import com.example.guilda.domain.audit.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}

