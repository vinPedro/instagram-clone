package br.edu.ifpb.instagram.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import br.edu.ifpb.instagram.exception.FieldAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import br.edu.ifpb.instagram.model.dto.UserDto;
import br.edu.ifpb.instagram.model.entity.UserEntity;
import br.edu.ifpb.instagram.repository.UserRepository;

@SpringBootTest
public class UserServiceImplTest {

    @MockitoBean
    UserRepository userRepository; // Repositório simulado

    @MockitoBean
    PasswordEncoder passwordEncoder; //

    @Autowired
    UserServiceImpl userService; // Classe sob teste

    @Test
    void testFindById_ReturnsUserDto() {
        // Configurar o comportamento do mock
        Long userId = 1L;

        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(userId);
        mockUserEntity.setFullName("Paulo Pereira");
        mockUserEntity.setEmail("paulo@ppereira.dev");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserEntity));

        // Executar o método a ser testado
        UserDto userDto = userService.findById(userId);

        // Verificar o resultado
        assertNotNull(userDto);
        assertEquals(mockUserEntity.getId(), userDto.id());
        assertEquals(mockUserEntity.getFullName(), userDto.fullName());
        assertEquals(mockUserEntity.getEmail(), userDto.email());

        // Verificar a interação com o mock
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_ThrowsExceptionWhenUserNotFound() {
        // Configurar o comportamento do mock
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Executar e verificar a exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findById(userId);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());

        // Verificar a interação com o mock
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testCreateUser_ShouldSaveAndReturnUser_WhenValid() {

        UserDto dto = new UserDto(null,"Pedro teste", "Pedro", "pedro@example.com", "123456", null);

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByUsername(dto.username())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);
        savedEntity.setFullName("Pedro teste");
        savedEntity.setUsername("Pedro");
        savedEntity.setEmail("pedro@example.com");
        savedEntity.setEncryptedPassword("encodedPass");

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        // Executar o método a ser testado
        UserDto result = userService.createUser(dto);

        // Verificar o resultado
        assertNotNull(result);
        assertEquals("Pedro", result.username());
        assertEquals("pedro@example.com", result.email());
        assertEquals("Pedro teste", result.fullName());

        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode("123456");
        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository).existsByUsername(dto.username());

    }


    @Test
    void testCreateUser_ShouldThrowException_WhenEmailExists() {

        UserDto dto = new UserDto(null,"Pedro teste", "Pedro", "pedro@example.com", "123456", null);

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        FieldAlreadyExistsException ex = assertThrows(FieldAlreadyExistsException.class,
                () -> userService.createUser(dto));

        assertEquals("E-email already in use.", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUser_ShouldThrowException_WhenUsernameExists() {

        UserDto dto = new UserDto(null,"Pedro teste", "Pedro", "pedro@example.com", "123456", null);

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByUsername(dto.username())).thenReturn(true);

        FieldAlreadyExistsException ex = assertThrows(FieldAlreadyExistsException.class,
                () -> userService.createUser(dto));

        assertEquals("Username already in use.", ex.getMessage());

        verify(userRepository, never()).save(any());
        verify(userRepository).existsByEmail(dto.email());
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenUserDtoIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(null));

        assertEquals("UserDto or UserDto.id must not be null", ex.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenUserDtoIdIsNull() {
        UserDto dto = new UserDto(null, "Pedro teste", "Pedro", "pedro@example.com", "123456", null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(dto));

        assertEquals("UserDto or UserDto.id must not be null", ex.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenUserNotFound() {
        UserDto dto = new UserDto(1L, "Pedro teste", "Pedro", "pedro@example.com", "123456", null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.updateUser(dto));

        assertEquals("User not found with id: 1", ex.getMessage());

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_ShouldUpdateUser_WhenPasswordIsNull() {
        UserDto dto = new UserDto(1L, "Pedro atualizado", "PedroUser", "pedro@example.com", null, null);

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setFullName("Pedro antigo");
        existingUser.setUsername("Pedro");
        existingUser.setEmail("pedro@example.com");
        existingUser.setEncryptedPassword("oldPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.updateUser(dto);

        assertEquals("Pedro atualizado", result.fullName());
        assertEquals("PedroUser", result.username());
        assertEquals("pedro@example.com", result.email());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_ShouldUpdateUser_WhenPasswordIsProvided() {
        UserDto dto = new UserDto(1L, "Pedro atualizado", "PedroUser", "pedro@example.com", "novaSenha", null);

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setFullName("Pedro antigo");
        existingUser.setUsername("Pedro");
        existingUser.setEmail("pedro@example.com");
        existingUser.setEncryptedPassword("oldPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("novaSenha")).thenReturn("encodedNovaSenha");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.updateUser(dto);

        assertEquals("Pedro atualizado", result.fullName());
        assertEquals("PedroUser", result.username());
        assertEquals("pedro@example.com", result.email());

        verify(passwordEncoder).encode("novaSenha");
        verify(userRepository).save(any(UserEntity.class));
    }

}
