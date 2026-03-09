# DDD Orders - Clojure

Serviço de Pedidos implementado em Clojure seguindo Domain-Driven Design e Arquitetura Hexagonal.

## Estrutura do Projeto

```
src/orders/
├── domain/                    # Núcleo do Domínio
│   ├── entities.clj           # Entidade Pedido (raiz do agregado)
│   ├── value_objects.clj      # Objetos de Valor (Endereço, Item, Status)
│   └── events.clj             # Eventos de Domínio
├── ports/                     # Portas (Interfaces)
│   ├── inbound.clj            # Casos de Uso (entrada)
│   └── outbound.clj           # Protocolos para repositório e serviços externos
└── adapters/                  # Adaptadores (Implementações)
    ├── inbound/
    │   └── http.clj           # API REST
    └── outbound/
        ├── repository.clj     # Persistência MySQL
        ├── products_client.clj # Cliente HTTP para serviço de Produtos
        └── event_publisher.clj # Publicador de eventos
```

## Arquitetura Hexagonal

### Núcleo (Domain)
O núcleo contém as regras de negócio puras, sem dependências externas:
- **Entidades**: Pedido com seus itens
- **Objetos de Valor**: Endereço, Item, Status
- **Eventos**: PedidoCriado, PedidoAtualizado

### Portas (Ports)
Interfaces que definem como o núcleo se comunica com o mundo externo:
- **Inbound**: Casos de uso (fechar-pedido, atualizar-status)
- **Outbound**: Protocolos (PedidoRepository, ProdutosService, EventPublisher)

### Adaptadores (Adapters)
Implementações concretas das portas:
- **HTTP**: API REST usando Ring/Reitit
- **MySQL**: Persistência usando next.jdbc
- **HTTP Client**: Comunicação com serviço de Produtos

## API

### POST /order/close
Fecha um novo pedido.

**Request:**
```json
{
  "cliente-id": "uuid-do-cliente",
  "endereco-entrega": {
    "tipo-logradouro": "Rua",
    "nome-logradouro": "das Flores",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "endereco-cobranca": {
    "tipo-logradouro": "Rua",
    "nome-logradouro": "das Flores",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "itens": [
    {"produto-id": "uuid-produto", "quantidade": 2, "preco": 29.90}
  ]
}
```

### GET /pedidos
Lista todos os pedidos.

### GET /pedidos/:id
Consulta um pedido por ID.

### PUT /pedidos/:id/status
Atualiza o status de um pedido.

## Executando

```bash
# Baixar dependências e executar
clj -M:run
```

## Banco de Dados

O serviço cria automaticamente a tabela `pedidos` no MySQL.

```sql
CREATE TABLE IF NOT EXISTS pedidos (
  id VARCHAR(36) PRIMARY KEY,
  cliente_id VARCHAR(36) NOT NULL,
  endereco_entrega JSON NOT NULL,
  endereco_cobranca JSON NOT NULL,
  itens JSON NOT NULL,
  valor_total DECIMAL(10, 2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  criado_em TIMESTAMP NOT NULL,
  atualizado_em TIMESTAMP NOT NULL
);
```
