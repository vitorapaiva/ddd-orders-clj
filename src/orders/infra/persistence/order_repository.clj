(ns orders.infra.persistence.order-repository
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [orders.infra.persistence.order-queries :as queries]
            [orders.ports.outbound :as ports]
            [orders.adapters.inbound.order-db-adapter :as in.order-db-adapter]
            [orders.adapters.outbound.order-db-adapter :as out.order-db-adapter]))

(def ^:private row-opts {:builder-fn rs/as-unqualified-lower-maps})

(defrecord MySQLOrderRepository [datasource]
  ports/OrderRepository
  
  (save [_ order]
    (let [db-order (out.order-db-adapter/order->db order)
          params (mapv db-order queries/insert-columns)]
      (jdbc/execute! datasource (into [queries/insert] params))
      order))
  
  (find-by-id [_ id]
    (let [row (jdbc/execute-one! datasource [queries/find-by-id id] row-opts)]
      (in.order-db-adapter/db->order row)))
  
  (update-order [_ order]
    (let [db-order (out.order-db-adapter/order->db order)
          params (mapv db-order queries/update-status-columns)]
      (jdbc/execute! datasource (into [queries/update-status] params))
      order))
  
  (list-all [_]
    (let [rows (jdbc/execute! datasource [queries/list-all] row-opts)]
      (mapv in.order-db-adapter/db->order rows))))

(defn create-repository
  "Creates a MySQL-backed OrderRepository for the given datasource."
  [datasource]
  (->MySQLOrderRepository datasource))
