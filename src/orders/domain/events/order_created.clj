(ns orders.domain.events.order-created
  "Domain event: order created.")

(defn order-created
  [order]
  {:type :order-created
   :data {:order-id (:id order)
          :customer-id (:customer-id order)
          :items (map #(select-keys % [:product-id :quantity]) (:items order))
          :total (:total order)
          :shipping-address (:shipping-address order)
          :billing-address (:billing-address order)}
   :timestamp (java.time.Instant/now)})
