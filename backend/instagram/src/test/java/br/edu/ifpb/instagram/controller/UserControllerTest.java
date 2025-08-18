package br.edu.ifpb.instagram.controller;

import br.edu.ifpb.instagram.model.entity.UserEntity;
import br.edu.ifpb.instagram.model.request.LoginRequest;
import br.edu.ifpb.instagram.model.request.UserDetailsRequest;
import br.edu.ifpb.instagram.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o UserController.
 * Testa os endpoints de CRUD de usuários (/users) que são protegidos por autenticação.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de Integração do UserController")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private UserEntity usuarioDeTeste;

    /**
     * Este método é executado ANTES de CADA teste.
     * Garante um ambiente limpo e um usuário/token novos para cada cenário,
     * assegurando total independência entre os testes.
     */
    @BeforeEach
    void inicializar() throws Exception {
        userRepository.deleteAll(); // Limpa o banco de dados

        // 1. Cria e salva um novo usuário de teste
        UserEntity user = new UserEntity();
        user.setUsername("usuario.teste");
        user.setFullName("Usuário de Teste");
        user.setEmail("teste@exemplo.com");
        user.setEncryptedPassword(passwordEncoder.encode("senha123"));
        this.usuarioDeTeste = userRepository.save(user);

        // 2. Faz login com este usuário para obter um token JWT
        LoginRequest loginRequest = new LoginRequest("usuario.teste", "senha123");

        MvcResult result = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 3. Extrai e armazena o token para ser usado no teste
        String responseBody = result.getResponse().getContentAsString();
        this.authToken = objectMapper.readTree(responseBody).get("token").asText();
    }

    /**
     * Testa o cenário de sucesso da listagem de todos os usuários.
     */
    @Test
    @DisplayName("Deve retornar lista de usuários quando autenticado")
    void listarUsuarios_DeveRetornarLista_QuandoAutenticado() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].username", is("usuario.teste")));
    }

    /**
     * Testa o cenário de sucesso da busca de um usuário por ID.
     */
    @Test
    @DisplayName("Deve retornar um usuário específico ao buscar por ID")
    void buscarUsuarioPorId_DeveRetornarUsuario_QuandoIdExistir() throws Exception {
        mockMvc.perform(get("/users/{id}", usuarioDeTeste.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                // CORREÇÃO APLICADA AQUI:
                .andExpect(jsonPath("$.id", is((int) usuarioDeTeste.getId())))
                .andExpect(jsonPath("$.username", is("usuario.teste")));
    }

    /**
     * Testa o cenário de sucesso da atualização de um usuário.
     */
    @Test
    @DisplayName("Deve atualizar dados do usuário com sucesso")
    void atualizarUsuario_DeveRetornarUsuarioComDadosNovos() throws Exception {
        UserDetailsRequest dadosParaAtualizar = new UserDetailsRequest(
                usuarioDeTeste.getId(), "email.novo@exemplo.com", null, "Nome Completo Novo", "username.novo"
        );

        mockMvc.perform(put("/users")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosParaAtualizar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("username.novo")))
                .andExpect(jsonPath("$.fullName", is("Nome Completo Novo")));
    }

    /**
     * Testa o cenário de sucesso da exclusão de um usuário.
     */
    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deletarUsuario_DeveRetornarMensagemDeSucesso() throws Exception {
        mockMvc.perform(delete("/users/{id}", usuarioDeTeste.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(content().string("user was deleted!"));
    }

    /**
     * Testa o cenário de falha ao tentar acessar sem um token de autenticação.
     */
    @Test
    @DisplayName("Deve retornar erro 403 (Forbidden) ao acessar sem token")
    void listarUsuarios_DeveRetornarAcessoNegado_QuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }
}