(ns orders.domain.events)

(defn order-created
  [order]
  {:type :order-created
   :data {:order-id (:id order)
          :customer-id (:customer-id order)
          :items (:items order)
          :total (:total order)
          :shipping-address (:shipping-address order)
          :billing-address (:billing-address order)}
   :timestamp (java.time.Instant/now)})

(defn order-updated
  [order previous-status]
  {:type :order-updated
   :data {:order-id (:id order)
          :previous-status previous-status
          :current-status (:status order)}
   :timestamp (java.time.Instant/now)})

(defn products-reserved
  [order-id products]
  {:type :products-reserved
   :data {:order-id order-id
          :products products}
   :timestamp (java.time.Instant/now)})
