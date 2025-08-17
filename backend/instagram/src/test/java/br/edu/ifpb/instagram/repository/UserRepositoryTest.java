package br.edu.ifpb.instagram.repository;

import br.edu.ifpb.instagram.model.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setUsername("johndoe");
        user.setEncryptedPassword("hashedpassword");
        return userRepository.save(user);
    }

    @Test
    @DisplayName("Verificar se email já existe")
    void testExistsByEmail() {
        createUser();
        assertThat(userRepository.existsByEmail("john@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nope@example.com")).isFalse();
    }

    @Test
    @DisplayName("Verificar se username já existe")
    void testExistsByUsername() {
        createUser();
        assertThat(userRepository.existsByUsername("johndoe")).isTrue();
        assertThat(userRepository.existsByUsername("notfound")).isFalse();
    }

    @Test
    @DisplayName("Buscar usuário por username")
    void testFindByUsername() {
        createUser();
        Optional<UserEntity> found = userRepository.findByUsername("johndoe");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }


    @Test
    @DisplayName("Atualizar parcialmente o usuário")
    void testUpdatePartialUser() {
        UserEntity user = createUser();

        int updatedRows = userRepository.updatePartialUser(
                "Novo Nome", "novo@email.com", null, null, user.getId()
        );

        assertThat(updatedRows).isEqualTo(1);

        entityManager.flush();
        entityManager.clear();  // limpa o cache para buscar do banco

        Optional<UserEntity> updated = userRepository.findById(user.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getFullName()).isEqualTo("Novo Nome");
        assertThat(updated.get().getEmail()).isEqualTo("novo@email.com");
        assertThat(updated.get().getUsername()).isEqualTo("johndoe");
    }
}

