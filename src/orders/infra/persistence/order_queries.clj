(ns orders.infra.persistence.order-queries)

(def insert
  "INSERT INTO orders (id, customer_id, shipping_address, billing_address, items, total, status, created_at, updated_at)
   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")

(def find-by-id
  "SELECT * FROM orders WHERE id = ?")

(def update-status
  "UPDATE orders SET status = ?, updated_at = ? WHERE id = ?")

(def list-all
  "SELECT * FROM orders ORDER BY created_at DESC")
