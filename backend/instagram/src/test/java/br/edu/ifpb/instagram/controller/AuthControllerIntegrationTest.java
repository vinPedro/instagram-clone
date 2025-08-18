package br.edu.ifpb.instagram.controller;

import br.edu.ifpb.instagram.model.request.LoginRequest;
import br.edu.ifpb.instagram.model.request.UserDetailsRequest;
import br.edu.ifpb.instagram.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Essencial para converter objetos Java para JSON

    @Autowired
    private UserRepository userRepository; // Para limpar o banco de dados após cada teste

    // Este método garante que o banco de dados esteja limpo antes de cada novo teste.
    // Isso é crucial para que os testes sejam independentes um do outro!
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso e retornar status 201")
    void signUp_ShouldReturnCreated_WhenUserDetailsAreValid() throws Exception {
        // Arrange (Preparação)
        UserDetailsRequest newUserRequest = new UserDetailsRequest(
                null,                       // id
                "joao.silva@email.com",     // email
                "senhaForte123",            // password
                "João da Silva",            // fullName
                "joaosilva"                 // username
        );

        // Act (Ação) & Assert (Verificação)
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("joaosilva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário com email duplicado e deve retornar status 400")
    void signUp_ShouldReturnBadRequest_WhenEmailIsDuplicate() throws Exception {
        // Arrange (Preparação)
        // 1. Cadastra um usuário inicial para forçar o conflito
        UserDetailsRequest existingUserRequest = new UserDetailsRequest(
                null, "email.repetido@email.com", "senha123", "Usuario Existente", "usuarioexistente"
        );
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingUserRequest)));

        // 2. Prepara uma nova requisição com o mesmo e-mail
        UserDetailsRequest newUserWithDuplicateEmail = new UserDetailsRequest(
                null, "email.repetido@email.com", "outrasenha456", "Novo Usuario", "novousuario"
        );

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserWithDuplicateEmail)))
                .andExpect(status().isConflict()); // Espera um erro do tipo "Bad Request"
    }

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar um token JWT para credenciais válidas")
    void signIn_ShouldReturnOkAndJwtToken_WhenCredentialsAreValid() throws Exception {
        // Arrange
        // 1. Garante que um usuário exista no banco de dados
        UserDetailsRequest userToRegister = new UserDetailsRequest(
                null, "usuario.login@email.com", "senhaCorreta123", "Usuario de Login", "usuariologin"
        );
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToRegister)));
        
        // 2. Prepara a requisição de login com as credenciais corretas
        LoginRequest loginRequest = new LoginRequest("usuariologin", "senhaCorreta123");

        // Act & Assert
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // Espera o status 200 OK
                .andExpect(jsonPath("$.token").isNotEmpty()); // Verifica se o token foi retornado e não está vazio
    }

    @Test
    @DisplayName("Não deve autenticar com senha incorreta e deve retornar status 401")
    void signIn_ShouldReturnUnauthorized_WhenPasswordIsIncorrect() throws Exception {
        // Arrange
        // 1. Garante que um usuário exista no banco de dados
        UserDetailsRequest userToRegister = new UserDetailsRequest(
                null, "usuario.senha.errada@email.com", "senhaCorreta123", "Usuario Senha Errada", "usuariosenhaerrada"
        );
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToRegister)));

        // 2. Prepara a requisição de login com a senha ERRADA
        LoginRequest loginRequest = new LoginRequest("usuariosenhaerrada", "senhaINCORRETA");

        // Act & Assert
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden()); // Espera o status 401 Unauthorized
    }
}