(ns orders.infra.persistence.order-repository
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [orders.infra.persistence.order-queries :as queries]
            [orders.ports.outbound :as ports]
            [orders.adapters.inbound.order-adapter :as inbound-adapter]
            [orders.adapters.outbound.order-adapter :as outbound-adapter]))

(defrecord MySQLOrderRepository [datasource]
  ports/OrderRepository
  
  (save [_ order]
    (let [db-order (outbound-adapter/order->db order)]
      (jdbc/execute! datasource
                     [queries/insert
                      (:id db-order)
                      (:customer_id db-order)
                      (:shipping_address db-order)
                      (:billing_address db-order)
                      (:items db-order)
                      (:total db-order)
                      (:status db-order)
                      (:created_at db-order)
                      (:updated_at db-order)])
      order))
  
  (find-by-id [_ id]
    (let [row (jdbc/execute-one! datasource
                                  [queries/find-by-id id]
                                  {:builder-fn rs/as-unqualified-lower-maps})]
      (inbound-adapter/db->order row)))
  
  (update-order [_ order]
    (let [db-order (outbound-adapter/order->db order)]
      (jdbc/execute! datasource
                     [queries/update-status
                      (:status db-order)
                      (:updated_at db-order)
                      (:id db-order)])
      order))
  
  (list-all [_]
    (let [rows (jdbc/execute! datasource
                               [queries/list-all]
                               {:builder-fn rs/as-unqualified-lower-maps})]
      (mapv inbound-adapter/db->order rows))))

(defn create-repository
  [datasource]
  (->MySQLOrderRepository datasource))
