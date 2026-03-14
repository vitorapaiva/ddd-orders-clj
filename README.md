# DDD Orders - Clojure

Serviço de Pedidos implementado em Clojure seguindo Domain-Driven Design e Arquitetura Hexagonal.

## Estrutura do Projeto

```
src/orders/
├── domain/                    # Núcleo do Domínio
│   ├── entities/
│   │   └── order.clj          # Entidade Pedido (raiz do agregado)
│   ├── logic/                 # Lógica de validação
│   │   ├── address.clj
│   │   ├── item.clj
│   │   ├── order.clj
│   │   └── status_transitions.clj
│   ├── value_objects/         # Objetos de Valor
│   │   ├── address.clj
│   │   └── item.clj
│   └── events/                # Eventos de Domínio
│       ├── order_created.clj
│       └── order_updated.clj
├── ports/                     # Portas (Interfaces)
│   ├── inbound/               # Casos de Uso
│   │   ├── close_order.clj
│   │   ├── list_orders.clj
│   │   ├── get_order.clj
│   │   └── update_order_status.clj
│   ├── outbound.clj           # Protocolos
│   └── event_handler.clj     # Protocolo de event handlers
├── adapters/
│   ├── inbound/               # JSON, DB → domínio
│   │   ├── order_json_adapter.clj
│   │   └── order_db_adapter.clj
│   └── outbound/              # Domínio → resposta, DB
│       ├── order_response_adapter.clj
│       ├── order_db_adapter.clj
│       └── products_adapter.clj
└── infra/
    ├── http/                  # Handlers, server, produtos client
    ├── persistence/            # Database, repository
    └── event_handlers/        # OrderCreated, OrderUpdated
```

## Arquitetura Hexagonal

### Núcleo (Domain)
- **Entidades**: Pedido
- **Value Objects**: Address, Item
- **Lógica**: validações e transições de status
- **Eventos**: OrderCreated, OrderUpdated

### Portas (Ports)
- **Inbound**: Casos de uso (close-order, list-orders, get-order, update-order-status)
- **Outbound**: Protocolos (OrderRepository, ProductsService, EventPublisher)

### Adaptadores (Adapters)
- **HTTP**: Ring/Reitit
- **MySQL**: next.jdbc
- **HTTP Client**: Serviço de Produtos

## API

### POST /order/close
Fecha um novo pedido.

**Request:**
```json
{
  "customer-id": "uuid-do-cliente",
  "shipping-address": {
    "street-type": "Rua",
    "street-name": "das Flores",
    "number": "123",
    "complement": "Apto 45",
    "district": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zip-code": "01234-567"
  },
  "billing-address": {
    "street-type": "Rua",
    "street-name": "das Flores",
    "number": "123",
    "complement": "Apto 45",
    "district": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zip-code": "01234-567"
  },
  "items": [
    {"product-id": "uuid-produto", "quantity": 2, "price": 29.90}
  ]
}
```

### GET /orders
Lista todos os pedidos.

### GET /orders/:id
Consulta um pedido por ID.

### PUT /orders/:id/status
Atualiza o status de um pedido.

**Request:**
```json
{
  "status": "products-reserved"
}
```

## Executando

```bash
# Baixar dependências e executar
clj -M:run
```

## Banco de Dados

O serviço cria automaticamente a tabela `orders` no MySQL.

```sql
CREATE TABLE IF NOT EXISTS orders (
  id VARCHAR(36) PRIMARY KEY,
  customer_id VARCHAR(36) NOT NULL,
  shipping_address JSON NOT NULL,
  billing_address JSON NOT NULL,
  items JSON NOT NULL,
  total DECIMAL(10, 2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
```
