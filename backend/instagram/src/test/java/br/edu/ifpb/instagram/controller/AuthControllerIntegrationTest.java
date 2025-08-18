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

/**
 * Esta é a classe de testes de integração para o AuthController.
 * O objetivo é testar os endpoints de autenticação (/signin) e registro (/signup)
 * de ponta a ponta, simulando requisições HTTP reais e verificando as respostas.
 *
 * @SpringBootTest: Carrega o contexto completo da aplicação Spring para o teste.
 * @AutoConfigureMockMvc: Configura o MockMvc, que é nossa ferramenta para fazer as requisições HTTP.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    // MockMvc é o objeto que nos permite simular as chamadas para a nossa API (GET, POST, etc.)
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper é um utilitário para converter objetos Java para JSON e vice-versa.
    @Autowired
    private ObjectMapper objectMapper;

    // Injetamos o UserRepository para ter acesso direto ao banco de dados (neste caso, o H2 em memória).
    @Autowired
    private UserRepository userRepository;

    /**
     * Este método, anotado com @AfterEach, é executado automaticamente após CADA teste.
     * Sua função é limpar todas as tabelas do banco de dados para garantir que um teste
     * não interfira no resultado do outro, mantendo a independência entre eles.
     */
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    /**
     * Teste para o cenário de sucesso do cadastro de usuário.
     * Dado: Um novo usuário com dados válidos que ainda não existe no sistema.
     * Quando: Uma requisição POST é enviada para /auth/signup com esses dados.
     * Então: A API deve retornar o status 201 (Created), e a resposta deve conter
     * os dados do usuário criado, incluindo um ID gerado pelo sistema.
     */
    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso e retornar status 201")
    void signUp_ShouldReturnCreated_WhenUserDetailsAreValid() throws Exception {
        // --- Arrange (Preparação) ---
        // Aqui, criamos o objeto (DTO) com os dados do novo usuário para enviar na requisição.
        UserDetailsRequest newUserRequest = new UserDetailsRequest(
                null, "joao.silva@email.com", "senhaForte123", "João da Silva", "joaosilva"
        );

        // --- Act (Ação) & Assert (Verificação) ---
        // Usamos o mockMvc para executar a requisição POST para o endpoint /auth/signup.
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON) // Informamos que o corpo da requisição é um JSON.
                        .content(objectMapper.writeValueAsString(newUserRequest))) // Convertemos o objeto para uma string JSON.
                // Agora, verificamos as respostas esperadas:
                .andExpect(status().isCreated()) // Esperamos que o status HTTP seja 201 (Created).
                .andExpect(jsonPath("$.id").exists()) // Verificamos se a resposta JSON contém um campo "id".
                .andExpect(jsonPath("$.username").value("joaosilva")) // Verificamos se o username na resposta é o correto.
                .andExpect(jsonPath("$.email").value("joao.silva@email.com")); // Verificamos se o email na resposta é o correto.
    }

    /**
     * Teste para o cenário de falha ao tentar cadastrar um email que já existe.
     * Dado: Um usuário já existente no banco de dados.
     * Quando: Uma nova requisição de cadastro é enviada com o MESMO e-mail do usuário existente.
     * Então: A API deve impedir a criação e retornar o status 409 (Conflict), indicando
     * que o recurso (usuário com aquele email) já existe.
     */
    @Test
    @DisplayName("Não deve cadastrar usuário com email duplicado e deve retornar status 409")
    void signUp_ShouldReturnConflict_WhenEmailIsDuplicate() throws Exception {
        // --- Arrange (Preparação) ---
        // 1. Primeiro, cadastramos um usuário para garantir que ele exista no banco.
        UserDetailsRequest existingUserRequest = new UserDetailsRequest(
                null, "email.repetido@email.com", "senha123", "Usuario Existente", "usuarioexistente"
        );
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingUserRequest)));

        // 2. Em seguida, criamos uma nova requisição com o mesmo e-mail.
        UserDetailsRequest newUserWithDuplicateEmail = new UserDetailsRequest(
                null, "email.repetido@email.com", "outrasenha456", "Novo Usuario", "novousuario"
        );

        // --- Act & Assert ---
        // Executamos a tentativa de cadastro do segundo usuário...
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserWithDuplicateEmail)))
                // ...e esperamos que a API retorne o status 409 (Conflict).
                .andExpect(status().isConflict());
    }

    /**
     * Teste para o cenário de sucesso do login de um usuário.
     * Dado: Um usuário previamente cadastrado no sistema.
     * Quando: Uma requisição POST é enviada para /auth/signin com o username e a senha corretos.
     * Então: A API deve autenticar o usuário, retornar o status 200 (OK) e um token JWT válido na resposta.
     */
    @Test
    @DisplayName("Deve autenticar com sucesso e retornar um token JWT para credenciais válidas")
    void signIn_ShouldReturnOkAndJwtToken_WhenCredentialsAreValid() throws Exception {
        // --- Arrange ---
        // 1. Primeiro, garantimos que um usuário exista no banco, cadastrando-o.
        UserDetailsRequest userToRegister = new UserDetailsRequest(
                null, "usuario.login@email.com", "senhaCorreta123", "Usuario de Login", "usuariologin"
        );
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToRegister)));
        
        // 2. Criamos o objeto de requisição de login com as credenciais corretas.
        LoginRequest loginRequest = new LoginRequest("usuariologin", "senhaCorreta123");

        // --- Act & Assert ---
        // Executamos a requisição de login...
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // ...e esperamos um status 200 (OK)...
                .andExpect(status().isOk())
                // ...e que a resposta JSON contenha um campo "token" que não esteja vazio.
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    /**
     * Teste para o cenário de falha no login com senha incorreta.
     * Dado: Um usuário cadastrado no sistema.
     * Quando: Uma requisição POST é enviada para /auth/signin com o username correto, mas uma senha inválida.
     * Então: A API deve negar o acesso e retornar o status 403 (Forbidden), protegendo a conta do usuário.
     */
    @Test
    @DisplayName("Não deve autenticar com senha incorreta e deve retornar status 403")
    void signIn_ShouldReturnForbidden_WhenPasswordIsIncorrect() throws Exception {
        // --- Arrange ---
        // 1. Cadastramos um usuário com uma senha conhecida.
        UserDetailsRequest userToRegister = new UserDetailsRequest(
                null, "usuario.senha.errada@email.com", "senhaCorreta123", "Usuario Senha Errada", "usuariosenhaerrada"
        );
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToRegister)));

        // 2. Criamos uma requisição de login com a senha intencionalmente ERRADA.
        LoginRequest loginRequest = new LoginRequest("usuariosenhaerrada", "senhaINCORRETA");

        // --- Act & Assert ---
        // Executamos a tentativa de login...
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // ...e esperamos que a API retorne o status 403 (Forbidden).
                .andExpect(status().isForbidden());
    }
}