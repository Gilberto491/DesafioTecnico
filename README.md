# API de Pautas de Votação

<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4bNPBNh_Fi7JnwSBAzyHHW6bKiatUbDWWaA&s" alt="Sicredi Logo" width="50"/> 

## Teste em Nuvem
A API pode ser testada em nuvem no seguinte endereço: [https://desafiotecnico-ulo5.onrender.com](https://desafiotecnico-ulo5.onrender.com)

> **Observação:** Os associados já estão inseridos no banco de dados com o mesmo script que se encontra na pasta `sql` em `resources`.

## Descrição
Esta API permite a criação de pautas para votação. As pautas só podem ser votadas quando uma sessão é aberta. Após a sessão ser aberta, cada associado pode votar apenas uma vez. O tempo padrão para a pauta ficar aberta para votação é de 1 minuto, mas o usuário pode especificar um tempo diferente no endpoint de abertura de sessão.

## Endpoints da API
- **Criação de pauta:** `POST /pauta/v1/criar`
- **Abrir sessão da pauta:** `POST /pauta/v1/abrir-sessao/{id}`
- **Cancelar a pauta:** `PUT /pauta/v1/cancelar/{id}`
- **Votar na pauta:** `POST /votacao/v1/votar/{id}`
- **Visualizar resultado da pauta:** `GET /votacao/v1/resultado/{id}`

## Ferramentas Utilizadas
- **Postman:** Para testes de API.
- **IntelliJ IDEA:** Ambiente de desenvolvimento integrado.
- **PgAdmin 4:** Ferramenta de administração para PostgreSQL.

## Tecnologias
- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Swagger**

## Modelagem de Dados
<img src="https://drive.google.com/uc?export=view&id=1dAD1Aeg_HNpVC5DCTxxVfF3SkT508uaf" alt="Modelagem de Dados" width="400"/>

A modelagem de dados acima mostra a estrutura das tabelas e seus relacionamentos.

## Endpoints
Os endpoints estão documentados no Swagger, acessível pelo seguinte link após iniciar a aplicação:
[Swagger UI](http://localhost:8080/swagger-ui/index.html)

Além disso, uma coleção do Postman contendo todos os endpoints e exemplos está incluída no código dentro da pasta postman.

## Cenários de Teste
- Criação de pauta sem um dos campos obrigatórios.
- Criação de pauta e abertura de sessão.
- Criação de pauta e tentativa de abrir uma sessão inválida.
- Criação de pauta e cancelamento posterior.
- Criação de pauta e cancelamento inválido.
- Criação de pauta, abertura de sessão e votação.
- Criação de pauta, abertura de sessão e votação com usuário inválido.
- Criação de pauta, abertura de sessão e votação com pauta inválida.
- Criação de pauta, abertura de sessão e votação com uma opção de pauta inválida.
- Criação de pauta, abertura de sessão e votação múltipla pelo mesmo usuário.
- Criação de pauta e abertura de sessão após término de sessão anterior.
- Criação de pauta, abertura de sessão e tentativa de votação após término da sessão.
- Criação de pauta, abertura de sessão e tentativa de abrir outra sessão enquanto a primeira ainda está aberta.
- Criação de pauta e tentativa de obter resultado antes de fechar a sessão.
- Criação de pauta e tentativa de obter resultado depois de fechar a sessão.
- Criação de pauta e tentativa de obter resultado de uma pauta inválida.

## Orientações para Teste
Para facilitar os testes, dentro da pasta sql em resources há um script para executar os inserts dos associados. Caso queira testar apenas os endpoints de abrir sessão, cancelar pauta, votar na pauta e resultado da pauta, também há inserts para as tabelas pauta e pauta_opcao.

## Tarefas Bônus
- **Tarefa Bônus 1:** A API de validação não está funcionando, porém eu utilizaria a biblioteca OpenFeign para consultar a validade de um CPF.
- **Tarefa Bônus 2:** Testes de performance com Threads para enviar uma quantidade de requisições e verificar o tempo de resposta. Estes testes estão incluídos no código.
- **Tarefa Bônus 3:** Especificação da versão nas URLs das chamadas e configuração da versão no Swagger para melhor visibilidade e facilidade de uso.

## Como Executar
1. Clone o repositório:
   ```sh
   git clone https://github.com/Gilberto491/DesafioTecnico.git
   ```

2. Importe o projeto em sua IDE de preferência.

3. Instale e configure o pgAdmin para utilizar o PostgreSQL.

4. Configure o arquivo application.properties com os dados do seu banco de dados PostgreSQL:

   ```sh
   spring.datasource.url=jdbc:postgresql://localhost:5432/sicredi
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
    ```
5. Certifique-se de que você está usando o JDK 17.

6. Agora, é só iniciar a aplicação :)
