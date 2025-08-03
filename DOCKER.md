# Docker - ChangeApp

## Visão Geral

Este documento descreve como executar o ChangeApp usando Docker e Docker Compose.

## Estrutura Docker

```
changeApp/
├── docker-compose.yml          # Ambiente de produção
├── docker-compose.dev.yml      # Ambiente de desenvolvimento
├── exchangeApi/
│   ├── Dockerfile              # Build de produção
│   └── Dockerfile.dev          # Build de desenvolvimento
├── transactionApi/
│   ├── Dockerfile              # Build de produção
│   └── Dockerfile.dev          # Build de desenvolvimento
├── managerProductApi/
│   ├── Dockerfile              # Build de produção
│   └── Dockerfile.dev          # Build de desenvolvimento
├── init-scripts/
│   └── 01-init.sql            # Script de inicialização do banco
└── scripts/
    ├── build.sh               # Build das aplicações
    ├── start.sh               # Iniciar produção
    ├── start-dev.sh           # Iniciar desenvolvimento
    ├── stop.sh                # Parar produção
    └── stop-dev.sh            # Parar desenvolvimento
```

## Ambientes

### Ambiente de Produção

**Características:**
- PostgreSQL 15
- Redis 7
- Nginx (opcional)
- Perfil Spring: `prd`
- Porta PostgreSQL: 5432

**Comandos:**
```bash
# Build e start
./scripts/build.sh
./scripts/start.sh

# Stop
./scripts/stop.sh

# Comandos manuais
docker-compose up -d
docker-compose down
```

### Ambiente de Desenvolvimento

**Características:**
- PostgreSQL 15 (porta 5433)
- Redis 7
- Perfil Spring: `dev`
- Hot reload habilitado
- Volumes montados para desenvolvimento

**Comandos:**
```bash
# Start
./scripts/start-dev.sh

# Stop
./scripts/stop-dev.sh

# Comandos manuais
docker-compose -f docker-compose.dev.yml up -d
docker-compose -f docker-compose.dev.yml down
```

## Serviços

### PostgreSQL

**Produção:**
- Container: `changeapp-postgres`
- Porta: 5432
- Database: `changeapp`
- Usuário: `changeapp`
- Senha: `changeapp123`

**Desenvolvimento:**
- Container: `changeapp-postgres-dev`
- Porta: 5433
- Database: `changeapp_dev`
- Usuário: `changeapp`
- Senha: `changeapp123`

### Redis

**Ambos os ambientes:**
- Container: `changeapp-redis` / `changeapp-redis-dev`
- Porta: 6379
- Sem autenticação (desenvolvimento)

### APIs

**ExchangeApi:**
- Porta: 8081
- Dependências: PostgreSQL, Redis
- Health Check: `/actuator/health`

**TransactionApi:**
- Porta: 8082
- Dependências: PostgreSQL, Redis
- Health Check: `/actuator/health`

**ManagerProductApi:**
- Porta: 8083
- Dependências: PostgreSQL
- Health Check: `/actuator/health`

## Redes

### Produção
- Nome: `changeapp-network`
- Subnet: `172.20.0.0/16`

### Desenvolvimento
- Nome: `changeapp-dev-network`
- Subnet: `172.21.0.0/16`

## Volumes

### Produção
- `postgres_data`: Dados do PostgreSQL
- `redis_data`: Dados do Redis

### Desenvolvimento
- `postgres_dev_data`: Dados do PostgreSQL
- `redis_dev_data`: Dados do Redis

## Health Checks

Todos os serviços possuem health checks configurados:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 60s
```

## Variáveis de Ambiente

### Comuns
```bash
SPRING_PROFILES_ACTIVE=prd|dev
JAVA_OPTS="-Xmx512m -Xms256m"
```

### Banco de Dados
```bash
# Produção
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/changeapp
SPRING_DATASOURCE_USERNAME=changeapp
SPRING_DATASOURCE_PASSWORD=changeapp123

# Desenvolvimento
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-dev:5432/changeapp_dev
SPRING_DATASOURCE_USERNAME=changeapp
SPRING_DATASOURCE_PASSWORD=changeapp123
```

### Redis
```bash
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
```

## Troubleshooting

### Problemas Comuns

1. **Porta já em uso:**
   ```bash
   # Verificar portas em uso
   netstat -tulpn | grep :8081
   
   # Parar serviços conflitantes
   sudo systemctl stop <servico>
   ```

2. **Container não inicia:**
   ```bash
   # Verificar logs
   docker-compose logs <servico>
   
   # Rebuild
   docker-compose build --no-cache <servico>
   ```

3. **Banco não conecta:**
   ```bash
   # Verificar se PostgreSQL está rodando
   docker-compose ps postgres
   
   # Verificar logs do PostgreSQL
   docker-compose logs postgres
   ```

### Comandos Úteis

```bash
# Ver status dos containers
docker-compose ps

# Ver logs de um serviço
docker-compose logs -f <servico>

# Executar comando em um container
docker-compose exec <servico> <comando>

# Limpar volumes
docker-compose down -v

# Rebuild sem cache
docker-compose build --no-cache
```

## URLs de Acesso

### Produção
- ExchangeApi: http://localhost:8081
- TransactionApi: http://localhost:8082
- ManagerProductApi: http://localhost:8083
- PostgreSQL: localhost:5432
- Redis: localhost:6379

### Desenvolvimento
- ExchangeApi: http://localhost:8081
- TransactionApi: http://localhost:8082
- ManagerProductApi: http://localhost:8083
- PostgreSQL: localhost:5433
- Redis: localhost:6379 