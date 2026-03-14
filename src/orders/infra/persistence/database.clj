(ns orders.infra.persistence.database
  (:require [next.jdbc :as jdbc]))

(defn create-datasource
  "Creates a MySQL datasource from config map (host, port, database, user, password)."
  [{:keys [host port database user password]}]
  (jdbc/get-datasource
   {:dbtype "mysql"
    :host host
    :port port
    :dbname database
    :user user
    :password password}))

(def sql-create-orders-table
  "CREATE TABLE IF NOT EXISTS orders (
     id VARCHAR(36) PRIMARY KEY,
     customer_id VARCHAR(36) NOT NULL,
     shipping_address JSON NOT NULL,
     billing_address JSON NOT NULL,
     items JSON NOT NULL,
     total DECIMAL(10, 2) NOT NULL,
     status VARCHAR(50) NOT NULL,
     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP NOT NULL,
     INDEX idx_customer_id (customer_id),
     INDEX idx_status (status)
   )")

(defn create-tables!
  "Creates the orders table if it does not exist."
  [datasource]
  (jdbc/execute! datasource [sql-create-orders-table]))
