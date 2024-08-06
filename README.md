# API de Gerenciamento de Estacionamento

Esta é uma API para gerenciamento de estacionamento, que permite gerenciar clientes, funcionários, vagas, check-ins e check-outs, além de gerar relatórios do histórico do cliente.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Data JPA**
- **Spring Web**
- **Spring Security**
- **Spring Validation**
- **Springdoc OpenAPI**
- **MySQL Connector**
- **Lombok**
- **JJWT (Java JWT)**
- **JasperReports**
- **Maven**

## Configuração do Ambiente

### Pré-requisitos

- **Java 17**: Certifique-se de que o Java 17 está instalado em sua máquina.
- **Maven**: Para o gerenciamento de dependências.
- **Banco de Dados**: MySQL.

### Configuração do Banco de Dados

Crie um banco de dados e configure as credenciais no arquivo `application.properties`

### Documentação

![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)

> [!NOTE]
> Para visualizar a documentação completa é necessário executar a aplicação e acessar o link abaixo
> 
- [Detalhes da documentação](http://localhost:8080/swagger-ui/index.html#/)


### Modelo do Relatório

![img.png](img.png)

### Lógica dos Preços

- **Até 15 minutos:** R$ 5,00
- **Até 60 minutos:** R$ 9,25
- **Para cada 15 minutos adicionais:** R$ 1,75


**Descontos:**

- Clientes que realizam a 10ª visita recebem um desconto de 30% no valor total.


**Geração de Recibo:**

- Ao realizar um check-in, o número do recibo é gerado automaticamente no formato:
  `PLACA-YYYYMMDD-HHMMSS`
    

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.3.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.3.2/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

