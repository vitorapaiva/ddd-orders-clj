(ns orders.adapters.outbound.order-db-adapter
  "Adapta entidades de domínio para o formato de persistência no banco."
  (:require [cheshire.core :as json]))

(defn order->db
  [order]
  {:id (:id order)
   :customer_id (:customer-id order)
   :shipping_address (json/generate-string (:shipping-address order))
   :billing_address (json/generate-string (:billing-address order))
   :items (json/generate-string (:items order))
   :total (:total order)
   :status (name (:status order))
   :created_at (:created-at order)
   :updated_at (:updated-at order)})
