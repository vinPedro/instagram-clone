# Instagram Clone

Este é um projeto de um clone simples do Instagram utilizando Docker, MariaDB, Spring Boot para o backend e NextJS com Tailwind CSS para o frontend. O objetivo deste projeto é permitir que vocês desenvolvam testes unitários para as funcionalidades implementadas.

## Pré-requisitos

Antes de começar, verifique se você tem as seguintes ferramentas instaladas em sua máquina:

- Git
- Docker
- Docker Compose

## Passos para executar o projeto

### 1. Clone o repositório

Clone o repositório para a sua máquina local:

```bash
git clone https://github.com/ppereiradev/instagram-clone.git
cd instagram-clone
```

### 2. Suba os containers Docker

Execute o seguinte comando para iniciar os containers do Docker com o backend (Spring Boot), frontend (NextJS) e o banco de dados (MariaDB):

```bash
docker compose up -d
```

### 3. Executando o Backend

Para rodar o backend (Spring Boot) dentro do container Docker, siga os passos abaixo:

1. Entre no terminal do container do backend:

```bash
docker container exec -it instagram-clone-backend bash
```

2. Execute o backend utilizando o Maven:

```bash
mvn clean spring-boot:run
```

Agora, o backend estará em execução.

### 4. Executando o Frontend
Para rodar o frontend (NextJS) dentro do container Docker, siga os passos abaixo:

1. Entre no terminal do container do frontend:

```bash
docker container exec -it instagram-clone-frontend bash
```

2. Execute o frontend utilizando o npm:

```bash
npm run dev
```

Agora, você pode acessar a aplicação frontend no navegador, indo para `http://localhost:3000`.

### 5. Executando os Testes Unitários do Backend

Para executar os testes unitários no backend, execute o comando Maven abaixo dentro do container do backend:

```bash
mvn clean test
```

Os testes unitários devem ser executados corretamente, garantindo que as funcionalidades do backend estão funcionando conforme esperado.

### 6. Conclusão

Este é o ambiente completo para o desenvolvimento do clone simples do Instagram. O próximo passo é realizar a implementação dos testes unitários, com base nas funcionalidades presentes no backend, e garantir que o código esteja funcionando de acordo com as expectativas.

