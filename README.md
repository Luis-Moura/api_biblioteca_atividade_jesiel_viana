# Biblioteca Spring

Este é um projeto de uma biblioteca desenvolvido com Spring Boot, Java, e PostgreSQL. Ele permite gerenciar livros,
usuários e empréstimos de livros.
Esse projeto é parte de uma atividade do professor Jesiel Viana.

## Tecnologias Utilizadas

- Java 23
- Spring Boot 3.4.3
- PostgreSQL 15
- Flyway 10.0.0
- Docker

## Configuração do Ambiente

### Pré-requisitos

- Docker
- Docker Compose
- JDK 23
- Maven

### Passos para Configuração

1. Clone o repositório:
    ```sh
    git clone https://github.com/seu-usuario/biblioteca_spring.git
    cd biblioteca_spring
    ```

2. Configure o banco de dados PostgreSQL usando Docker:
    ```sh
    docker-compose up -d
    ```

3. Execute as migrações do banco de dados com Flyway:
    ```sh
    mvn flyway:migrate
    ```

4. Compile e execute a aplicação:
    ```sh
    mvn spring-boot:run
    ```

## Endpoints da API

### Livros

- **POST /books**: Cria um novo livro.
- **GET /books**: Retorna todos os livros.
- **GET /books/available**: Retorna todos os livros disponíveis.
- **GET /books/{id}**: Retorna um livro pelo ID.
- **PATCH /books/{id}**: Atualiza um livro pelo ID.
- **DELETE /books/{id}**: Deleta um livro pelo ID.

### Usuários

- **POST /users**: Cria um novo usuário.
- **GET /users**: Retorna todos os usuários.
- **GET /users/{id}**: Retorna um usuário pelo ID.
- **PATCH /users/{id}**: Atualiza um usuário pelo ID.
- **DELETE /users/{id}**: Deleta um usuário pelo ID.

### Empréstimos

- **POST /loans/{userId}/{bookId}**: Cria um novo empréstimo.
- **GET /loans**: Retorna todos os empréstimos.
- **GET /loans/user/{userId}**: Retorna todos os empréstimos de um usuário.
- **GET /loans/{id}**: Retorna um empréstimo pelo ID.
- **PATCH /loans/{id}**: Marca um empréstimo como devolvido.
- **DELETE /loans/{id}**: Deleta um empréstimo pelo ID.

## Estrutura de Pastas

- **controller**: Contém os controladores REST que lidam com as requisições HTTP.
   - `BookController.java`
   - `UserController.java`
   - `LoanController.java`

- **domain**: Contém as classes de domínio e repositórios.
   - **book**: Classes relacionadas ao domínio de livros.
      - `Book.java`
      - `BookRepository.java`
      - `dto` (Data Transfer Objects para livros)
   - **user**: Classes relacionadas ao domínio de usuários.
      - `User.java`
      - `UserRepository.java`
      - `dto` (Data Transfer Objects para usuários)
   - **loan**: Classes relacionadas ao domínio de empréstimos.
      - `Loan.java`
      - `LoanRepository.java`
      - `dto` (Data Transfer Objects para empréstimos)

- **infra**: Contém classes de infraestrutura, como exceções.
   - `exception` (Classes de exceção)

- **db/migration**: Scripts de migração do banco de dados.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Faça o push para a branch (`git push origin feature/nova-feature`).
5. Crie um novo Pull Request.