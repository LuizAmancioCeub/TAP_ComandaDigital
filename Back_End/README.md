# Aplicação

Aplicação de uma API utilizando recursos e bibliotecas que prestam suporte para o Spring Boot 3.1.4.  Essa API é basicamente um CRUD para um Sistema de Comanda/Cardapio Digital para restaurantes

## Configuração DataSource
- Configure de acordo com sua máquina o DataSource da aplicação
  
> - Caminho do arquivo: Back_End/cardapio/src/main/resources/application.properties
> - mysql = SGDB; localhost:3306 = localização do seu banco; comanda = nome da dataBase
> - datasource.username = Nome do usuário do seu SGDB
> - datasource.password = senha do seu SGDB, caso tenha.

![image](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/b083b465-e858-4d55-8e2c-b345b4b927e7)

> - Execute a base de dados pelo seu SGDB

## Testando API
- Após todas dependências e base de dados instaladas e configuradas:
  > - Execute o arquivo CardapioApplication -> Back_End/cardapio/src/main/java/com/comanda/cardapio/CardapioApplication
  > - Abrir e configurar o Postman conforme as seguintes imagens:




