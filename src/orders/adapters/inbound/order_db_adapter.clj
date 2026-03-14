(ns orders.adapters.inbound.order-db-adapter
  "Adapts database rows to domain entities."
  (:require [cheshire.core :as json]))

(defn db->order
  [row]
  (when row
    {:id (:id row)
     :customer-id (:customer_id row)
     :shipping-address (json/parse-string (:shipping_address row) true)
     :billing-address (json/parse-string (:billing_address row) true)
     :items (json/parse-string (:items row) true)
     :total (:total row)
     :status (keyword (:status row))
     :created-at (:created_at row)
     :updated-at (:updated_at row)}))
