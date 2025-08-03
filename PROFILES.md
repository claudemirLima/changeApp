# Spring Profiles - Configuração por Ambiente

## Visão Geral

O projeto utiliza Spring Profiles para configurar diferentes ambientes de execução. Cada API possui suas próprias configurações específicas.

## Ambientes Disponíveis

### 1. **dev** (Desenvolvimento)
- **Banco:** H2 em memória
- **DDL:** create-drop (recria tabelas a cada startup)
- **Logs:** DEBUG (logs detalhados)
- **Console H2:** Habilitado
- **Portas:**
  - ExchangeApi: 8081
  - TransactionApi: 8082
  - ManagerProductApi: 8083

### 2. **test** (Testes)
- **Banco:** H2 em memória
- **DDL:** create-drop
- **Logs:** INFO/WARN (logs reduzidos)
- **Console H2:** Desabilitado
- **Portas:** Mesmas do dev

### 3. **hm** (Homologação)
- **Banco:** PostgreSQL local
- **DDL:** validate (não altera estrutura)
- **Logs:** INFO
- **Console H2:** Desabilitado
- **Portas:** Mesmas do dev

### 4. **prd** (Produção)
- **Banco:** PostgreSQL configurável
- **DDL:** validate
- **Logs:** WARN (logs mínimos)
- **Console H2:** Desabilitado
- **Portas:** Configuráveis via variáveis de ambiente

## Como Executar

### Via Maven
```bash
# Desenvolvimento (padrão)
mvn spring-boot:run -pl exchangeApi

# Teste
mvn spring-boot:run -pl exchangeApi -Dspring.profiles.active=test

# Homologação
mvn spring-boot:run -pl exchangeApi -Dspring.profiles.active=hm

# Produção
mvn spring-boot:run -pl exchangeApi -Dspring.profiles.active=prd
```

### Via Variável de Ambiente
```bash
# Desenvolvimento
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run -pl exchangeApi

# Produção
export SPRING_PROFILES_ACTIVE=prd
export DB_HOST=prod-server
export DB_PORT=5432
export DB_NAME=exchange_prod
export DB_USERNAME=prod_user
export DB_PASSWORD=prod_password
mvn spring-boot:run -pl exchangeApi
```

### Via JAR
```bash
# Desenvolvimento
java -jar exchangeApi/target/exchangeApi.jar --spring.profiles.active=dev

# Produção
java -jar exchangeApi/target/exchangeApi.jar \
  --spring.profiles.active=prd \
  --DB_HOST=prod-server \
  --DB_PORT=5432 \
  --DB_NAME=exchange_prod \
  --DB_USERNAME=prod_user \
  --DB_PASSWORD=prod_password
```

## Variáveis de Ambiente (Produção)

### ExchangeApi
- `DB_HOST`: Host do banco PostgreSQL
- `DB_PORT`: Porta do banco (padrão: 5432)
- `DB_NAME`: Nome do banco
- `DB_USERNAME`: Usuário do banco
- `DB_PASSWORD`: Senha do banco
- `SERVER_PORT`: Porta da aplicação (padrão: 8081)

### TransactionApi
- Mesmas variáveis, mas `SERVER_PORT` padrão: 8082
- `DB_NAME` padrão: transaction_prd_db

### ManagerProductApi
- Mesmas variáveis, mas `SERVER_PORT` padrão: 8083
- `DB_NAME` padrão: product_prd_db

## Configuração de Banco

### H2 (dev/test)
- Banco em memória
- Dados perdidos ao reiniciar
- Console H2 disponível em `/h2-console` (apenas dev)

### PostgreSQL (hm/prd)
- Banco persistente
- Dados mantidos entre reinicializações
- Configuração via variáveis de ambiente

## Logs

### dev
- Logs detalhados (DEBUG)
- SQL visível
- Parâmetros SQL visíveis

### test
- Logs reduzidos (INFO/WARN)
- SQL oculto
- Foco em testes

### hm
- Logs informativos (INFO)
- SQL oculto
- Monitoramento básico

### prd
- Logs mínimos (WARN)
- SQL oculto
- Performance otimizada

## Estrutura de Arquivos

```
src/main/resources/
├── application.yml              # Configuração base
├── application-dev.yml          # Configuração dev
├── application-test.yml         # Configuração test
├── application-hm.yml           # Configuração homologação
└── application-prd.yml          # Configuração produção
```

## Dicas

1. **Desenvolvimento:** Use sempre o profile `dev` para desenvolvimento local
2. **Testes:** Use `test` para execução de testes automatizados
3. **Homologação:** Use `hm` para ambiente de homologação
4. **Produção:** Use `prd` com variáveis de ambiente configuradas
5. **Console H2:** Disponível apenas em `dev` em `http://localhost:8081/h2-console` 