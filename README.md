# Sistema de Conversão de Moedas - Reino SRM

## Visão Geral

Sistema de conversão de moedas para o reino SRM, implementado com três APIs modulares seguindo princípios de microserviços:

- **ExchangeApi**: Gestão de taxas de câmbio e conversões
- **TransactionApi**: Registro e consulta de transações
- **ManagerProductApi**: Gestão de produtos, reinos e valorização

## 🚀 Inicialização Rápida

### Pré-requisitos

- **Java 21** (JDK)
- **Maven 3.9+**
- **Docker** e **Docker Compose**
- **Git**

### Processo de Inicialização

1. **Clone o repositório:**
   ```bash
   git clone <repository-url>
   cd changeApp
   ```

2. **Compile o projeto:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Inicie os serviços:**
   ```bash
   docker-compose up -d
   ```

4. **Verifique o status:**
   ```bash
   docker-compose ps
   ```

### URLs das APIs

- **ExchangeApi**: http://localhost:8081
  - Swagger: http://localhost:8081/swagger-ui.html
- **TransactionApi**: http://localhost:8082
  - Swagger: http://localhost:8082/swagger-ui.html
- **ManagerProductApi**: http://localhost:8083
  - Swagger: http://localhost:8083/swagger-ui.html

### Portas dos Bancos de Dados

- **PostgreSQL**: localhost:5433
- **MongoDB**: localhost:27018
- **Redis**: localhost:6379

## Arquitetura de Microserviços

### 🏗️ **Princípios Aplicados:**
- **Banco de Dados por Serviço**: Cada API possui seu próprio banco de dados
- **Independência**: Serviços podem ser desenvolvidos, testados e implantados independentemente
- **Isolamento**: Falhas em um serviço não afetam outros
- **Escalabilidade**: Cada serviço pode ser escalado independentemente

### 🗄️ **Bancos de Dados:**

#### **ExchangeApi (PostgreSQL)**
- **Banco**: `changeapp_dev`
- **Porta**: 5433 (Desenvolvimento)
- **Usuário**: `changeapp`
- **Tabelas**: `currencies`, `exchange_rates`, `product_exchange_rates`

#### **ManagerProductApi (PostgreSQL)**
- **Banco**: `changeapp_product_dev`
- **Porta**: 5433 (Desenvolvimento)
- **Usuário**: `changeapp`
- **Tabelas**: `kingdoms`, `products`

#### **TransactionApi (MongoDB)**
- **Banco**: `changeapp_transactions_dev`
- **Porta**: 27018 (Desenvolvimento)
- **Usuário**: `changeapp`
- **Collections**: `transactions`

### 🐳 **Vantagens da Arquitetura:**
- **Flexibilidade**: Cada serviço usa a tecnologia mais adequada
- **Performance**: MongoDB para consultas complexas de transações
- **Escalabilidade**: Cada banco pode ser otimizado independentemente
- **Manutenção**: Isolamento de falhas e atualizações

## Estrutura do Projeto

```
changeApp/
├── exchangeApi/           # API de Conversão de Moedas
│   ├── src/main/java/com/exchange/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   └── domain/        # Entidades e DTOs
│   ├── src/main/resources/db/migration/  # Flyway migrations
│   └── pom.xml
├── transactionApi/        # API de Transações
│   ├── src/main/java/com/transaction/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   └── domain/        # Entidades e DTOs
│   ├── src/main/resources/db/  # MongoDB scripts
│   └── pom.xml
├── managerProductApi/     # API de Gestão de Produtos
│   ├── src/main/java/com/product/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Acesso a dados
│   │   └── domain/        # Entidades e DTOs
│   ├── src/main/resources/db/migration/  # Flyway migrations
│   └── pom.xml
├── docker-compose.yml     # Orquestração dos containers
├── init-databases.sql     # Script de inicialização dos bancos
└── pom.xml               # POM principal
```

## Tecnologias

- **Java 21**
- **Spring Boot 3.3.2**
- **Maven** (Multi-módulo)
- **PostgreSQL** (ExchangeApi + ManagerProductApi)
- **MongoDB** (TransactionApi)
- **Redis** (Cache compartilhado)
- **Flyway** (Migrations PostgreSQL)
- **Lombok** (Redução de boilerplate)
- **Docker** (Containerização)

## APIs

### ExchangeApi
Responsável por:
- Gestão de taxas de câmbio entre moedas
- Conversões em tempo real
- Configuração de taxas por produto
- **Banco**: `changeapp_dev` (PostgreSQL)

### TransactionApi
Responsável por:
- Registro de transações comerciais
- Histórico de transações
- Relatórios e consultas avançadas
- **Banco**: `changeapp_transactions_dev` (MongoDB)

### ManagerProductApi
Responsável por:
- Gestão de produtos (peles, madeira, hidromel)
- Gestão de reinos
- Sistema de valorização/desvalorização
- Controle de inflação por reino
- **Banco**: `changeapp_product_dev` (PostgreSQL)

## Comandos Úteis

### Desenvolvimento

```bash
# Compilar apenas um módulo
mvn clean package -pl exchangeApi -am -DskipTests

# Executar testes
mvn test

# Ver logs dos containers
docker-compose logs -f exchange-api-dev

# Parar todos os serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Reconstruir containers
docker-compose build --no-cache
```

### Banco de Dados

```bash
# Conectar ao PostgreSQL
docker exec -it changeapp-postgres-dev psql -U changeapp -d changeapp_dev

# Conectar ao MongoDB
docker exec -it changeapp-mongodb-dev mongosh -u changeapp -p changeapp123 --authenticationDatabase admin

# Ver logs do PostgreSQL
docker-compose logs postgres-dev

# Ver logs do MongoDB
docker-compose logs mongodb-dev
```

### Monitoramento

```bash
# Status dos containers
docker-compose ps

# Uso de recursos
docker stats

# Health checks
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

## Migrações de Banco

### Flyway (PostgreSQL)
As migrações são executadas automaticamente na inicialização das aplicações:

- **ExchangeApi**: `exchangeApi/src/main/resources/db/migration/`
- **ManagerProductApi**: `managerProductApi/src/main/resources/db/migration/`

### MongoDB
O script de inicialização é executado automaticamente:
- **TransactionApi**: `transactionApi/src/main/resources/db/init-mongo.js`

## Troubleshooting

### Problemas Comuns

1. **"no main manifest attribute"**
   - Execute: `mvn clean package -DskipTests`
   - Verifique se o `spring-boot-maven-plugin` está configurado

2. **Porta já em uso**
   - Verifique se não há outros containers rodando
   - Execute: `docker-compose down`

3. **Erro de conexão com banco**
   - Aguarde os bancos inicializarem completamente
   - Verifique os logs: `docker-compose logs postgres-dev`

4. **Permissões de arquivo**
   - Execute: `sudo rm -rf */target`
   - Recompile: `mvn clean package -DskipTests`

### Logs Importantes

```bash
# Logs de inicialização
docker-compose logs exchange-api-dev | grep "Started"

# Logs de erro
docker-compose logs --tail=50 exchange-api-dev | grep ERROR

# Logs de migração
docker-compose logs exchange-api-dev | grep Flyway
```

## Contribuição

1. Faça o fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. 