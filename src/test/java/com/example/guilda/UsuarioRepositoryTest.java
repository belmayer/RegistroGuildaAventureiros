package com.example.guilda;

import com.example.guilda.domain.audit.*;
import com.example.guilda.repository.audit.OrganizacaoRepository;
import com.example.guilda.repository.audit.PermissionRepository;
import com.example.guilda.repository.audit.RoleRepository;
import com.example.guilda.repository.audit.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    void deveCarregarUsuarioComRelacionamentos() {

        // 🔹 organização
        Organizacao org = organizacaoRepository.save(
                Organizacao.builder()
                        .nome("Guilda Teste")
                        .ativo(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        // 🔹 permission
        Permission perm = permissionRepository.save(
                Permission.builder()
                        .code("CREATE_USER")
                        .descricao("Criar usuário")
                        .build()
        );

        // 🔹 role
        Role role = roleRepository.save(
                Role.builder()
                        .nome("ADMIN")
                        .descricao("Administrador")
                        .organizacao(org)
                        .createdAt(OffsetDateTime.now())
                        .permissions(Set.of(perm))
                        .build()
        );

        // 🔹 usuario 1
        Usuario usuario1 = usuarioRepository.save(
                Usuario.builder()
                        .nome("Isabella")
                        .email("isa@email.com")
                        .senhaHash("123")
                        .status("ATIVO")
                        .organizacao(org)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .roles(Set.of(role))
                        .build()
        );

        // 🔹 usuario 2 (IMPORTANTE PRA TESTE)
        usuarioRepository.save(
                Usuario.builder()
                        .nome("Outro")
                        .email("outro@email.com")
                        .senhaHash("123")
                        .status("ATIVO")
                        .organizacao(org)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .roles(Set.of(role))
                        .build()
        );

        // 🔹 buscar
        var usuarios = usuarioRepository.findAll();
        Usuario u = usuarios.get(0);

        // 🔥 TESTES (COMPLETOS)

        // ✔ múltiplos usuários
        assertThat(usuarios.size()).isGreaterThan(1);

        // ✔ organização
        assertThat(u.getOrganizacao()).isNotNull();
        assertThat(u.getOrganizacao().getId()).isEqualTo(org.getId());

        // ✔ roles
        assertThat(u.getRoles()).isNotEmpty();
        assertThat(u.getRoles().size()).isEqualTo(1);

        // ✔ nome da role
        Role r = u.getRoles().iterator().next();
        assertThat(r.getNome()).isEqualTo("ADMIN");

        // ✔ permissions via role
        assertThat(r.getPermissions()).isNotEmpty();
        assertThat(r.getPermissions().size()).isEqualTo(1);

        // ✔ testar role direto (IMPORTANTE)
        Role roleSalvo = roleRepository.findAll().get(0);
        assertThat(roleSalvo.getPermissions()).isNotEmpty();
    }
}