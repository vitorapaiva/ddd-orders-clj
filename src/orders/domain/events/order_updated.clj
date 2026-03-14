(ns orders.domain.events.order-updated
  "Domain event: order status updated.")

(defn order-updated
  [order previous-status]
  {:type :order-updated
   :data {:order-id (:id order)
          :previous-status previous-status
          :current-status (:status order)}
   :timestamp (java.time.Instant/now)})
