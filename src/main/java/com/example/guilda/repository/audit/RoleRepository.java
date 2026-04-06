package com.example.guilda.repository.audit;

import com.example.guilda.domain.audit.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
