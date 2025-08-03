# Sistema de Migrations - ChangeApp

## Visão Geral

O ChangeApp utiliza o **Flyway** para gerenciar migrations do banco de dados, com cada API tendo suas próprias migrations dentro do seu respectivo projeto.

## Estrutura por Projeto

```
changeApp/
├── exchangeApi/
│   └── src/main/resources/db/migration/
│       ├── V1__create_exchange_schema.sql
│       └── V2__insert_exchange_initial_data.sql
├── transactionApi/
│   └── src/main/resources/db/migration/
│       └── V1__create_transaction_schema.sql
└── managerProductApi/
    └── src/main/resources/db/migration/
        ├── V1__create_product_kingdom_schema.sql
        └── V2__insert_product_kingdom_data.sql
```

## Migrations por Projeto

### ExchangeApi
- **V1__create_exchange_schema.sql** - Schema de moedas e taxas de câmbio
- **V2__insert_exchange_initial_data.sql** - Dados iniciais de moedas

### TransactionApi
- **V1__create_transaction_schema.sql** - Schema de transações

### ManagerProductApi
- **V1__create_product_kingdom_schema.sql** - Schema de produtos e reinos
- **V2__insert_product_kingdom_data.sql** - Dados iniciais de produtos e reinos

## Scripts Disponíveis

### Executar Migrations de Projeto Específico
```bash
# Executar migrations de um projeto
./scripts/migrate-project.sh exchangeApi dev migrate
./scripts/migrate-project.sh transactionApi dev migrate
./scripts/migrate-project.sh managerProductApi dev migrate

# Ver informações
./scripts/migrate-project.sh exchangeApi dev info

# Validar migrations
./scripts/migrate-project.sh exchangeApi dev validate
```

### Executar Migrations de Todos os Projetos
```bash
# Executar todas as migrations
./scripts/migrate-all.sh dev migrate

# Ver informações de todos
./scripts/migrate-all.sh dev info

# Validar todas
./scripts/migrate-all.sh dev validate
```

### Criar Nova Migration
```bash
# Criar migration para projeto específico
./scripts/create-migration.sh exchangeApi "add_currency_audit"
# Resultado: exchangeApi/src/main/resources/db/migration/V3__add_currency_audit.sql
```

## Configuração do Spring Boot

### application.yml
```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
    clean-disabled: false
```

### Dependência Maven
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

## Ambientes

### Desenvolvimento (dev)
- **Database:** changeapp_dev
- **Porta:** 5433
- **Execução:** Automática no `start-dev.sh`

### Teste (test)
- **Database:** changeapp_test
- **Porta:** 5434

### Homologação (hm)
- **Database:** changeapp_hm
- **Porta:** 5432

### Produção (prd)
- **Database:** changeapp
- **Porta:** 5432

## Vantagens da Separação por Projeto

### 1. Responsabilidade Clara
- ✅ Cada API gerencia suas próprias migrations
- ✅ Separação clara de domínios
- ✅ Independência entre projetos

### 2. Desenvolvimento Paralelo
- ✅ Equipes podem trabalhar independentemente
- ✅ Sem conflitos de versão entre projetos
- ✅ Deploy independente

### 3. Manutenção Simplificada
- ✅ Migrations organizadas por contexto
- ✅ Fácil localização de mudanças
- ✅ Rollback específico por projeto

## Como Usar

### Desenvolvimento
```bash
# Iniciar ambiente (inclui migrations automáticas)
./scripts/start-dev.sh

# Executar migrations manualmente
./scripts/migrate-all.sh dev migrate
```

### Produção
```bash
# Executar migrations de todos os projetos
./scripts/migrate-all.sh prd migrate

# Ou projeto específico
./scripts/migrate-project.sh exchangeApi prd migrate
```

## Boas Práticas

### 1. Nomenclatura
- Use prefixo `V` seguido de número sequencial
- Use descrição clara em snake_case
- Exemplo: `V1__create_user_table.sql`

### 2. Conteúdo
- Inclua cabeçalho com descrição e autor
- Use `IF NOT EXISTS` para idempotência
- Use `ON CONFLICT` para dados

### 3. Ordem de Execução
- Execute migrations na ordem correta
- Considere dependências entre projetos
- Teste em ambiente de desenvolvimento

### 4. Rollback
- Sempre pense no rollback
- Teste migrations antes de aplicar
- Mantenha backups antes de migrations críticas

## Troubleshooting

### Problemas Comuns

1. **Migration já aplicada:**
   ```bash
   ./scripts/migrate-project.sh exchangeApi dev info
   ```

2. **Erro de validação:**
   ```bash
   ./scripts/migrate-project.sh exchangeApi dev validate
   ```

3. **Projeto não encontrado:**
   ```bash
   # Verificar projetos disponíveis
   ls -d */ | grep Api
   ```

### Comandos Úteis

```bash
# Ver status de projeto específico
flyway -url="jdbc:postgresql://localhost:5433/changeapp_dev" \
       -user="changeapp" -password="changeapp123" \
       -locations=filesystem:exchangeApi/src/main/resources/db/migration info

# Executar migration específica
flyway -url="jdbc:postgresql://localhost:5433/changeapp_dev" \
       -user="changeapp" -password="changeapp123" \
       -locations=filesystem:exchangeApi/src/main/resources/db/migration migrate -target=2
```

## Integração com Docker

### Ambiente de Desenvolvimento
As migrations são executadas automaticamente ao iniciar:

```bash
./scripts/start-dev.sh
```

### Ambiente de Produção
Execute migrations manualmente após deploy:

```bash
./scripts/migrate-all.sh prd migrate
```

## Monitoramento

### Tabela flyway_schema_history
Cada projeto mantém seu próprio histórico:

```sql
-- ExchangeApi
SELECT * FROM flyway_schema_history 
WHERE script LIKE 'V%' 
ORDER BY installed_rank DESC;

-- TransactionApi
SELECT * FROM flyway_schema_history 
WHERE script LIKE 'V%' 
ORDER BY installed_rank DESC;
```

### Logs
Verifique logs para problemas:

```bash
# Logs do Flyway
docker-compose logs postgres-dev

# Logs das aplicações
docker-compose logs exchange-api-dev
``` 