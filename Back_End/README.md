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
  > - ![1 - Executando-Spring](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/c2febd0c-2fb0-48e3-96ed-f364f8830d09)

  > - Abrir e configurar o Postman conforme as seguintes imagens:
  
  > - 127.0.0.1 representa o localhost da máquina
  > - :8080 porta da aplicação Spring
  > - /comandas -> URI para mapear a solicitação -> é definido na classe CategoriaController
      
  - Register
    >  POST -> Envio de dados

    ![BaseZerada](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/22035627-14f1-42c1-8664-5415ac6acee5)

    ![register](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/eb260954-d901-4880-b21c-21d9d342caf1)

    ![register](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/b90b1518-78af-4e8b-b6a1-b10c414cbca0)

  - Update
    > PUT -> Atualiza dados

     ![update](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/cbaa9a0a-d82c-4624-bcd3-0aa735ce738b)

    ![update](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/e0331f30-7189-4517-9db7-f810c49ad5f0)

  - FindALL
    > GET -> Recupera todos os dados
    
     ![findAll](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/4de253eb-f405-4869-8e1b-99a6f0bf525d)

  - FindById
    > GET -> Recupera dados de acordo com parâmetro (id) informado na classe CategoriaController

      ![findById](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/e780bfce-80ad-46e4-aeda-8dc76ee18bbf)

  - Delete
    > DELETE -> Deleta dados

      ![delete](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/d0835485-e8cf-41fd-a555-913c75fccc13)

      ![delete](https://github.com/LuizAmancioCeub/TAP_ComandaDigital/assets/134547510/ae56a3ef-8e42-4e95-acb0-879be713d649)

    











