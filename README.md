# DDD Orders - Clojure

Order service implemented in Clojure following Domain-Driven Design and Hexagonal Architecture.

## Hexagonal Architecture

### Core (Domain)
- **Entities**: Order
- **Value Objects**: Address, Item
- **Logic**: validations and status transitions
- **Events**: OrderCreated, OrderUpdated

### Ports
- **Inbound**: Use cases (close-order, list-orders, get-order, update-order-status)
- **Outbound**: Protocols (OrderRepository, ProductsService, EventPublisher)

### Adapters
- **HTTP**: Ring/Reitit
- **MySQL**: next.jdbc
- **HTTP Client**: Products Service

## API

### POST /order/close
Closes a new order.

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
Lists all orders.

### GET /orders/:id
Gets an order by ID.

### PUT /orders/:id/status
Updates an order status.

**Request:**
```json
{
  "status": "products-reserved"
}
```

## Running

```bash
# Download dependencies and run
clj -M:run
```

## Database

The service automatically creates the `orders` table in MySQL.

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
