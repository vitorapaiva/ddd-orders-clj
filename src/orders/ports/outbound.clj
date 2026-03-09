(ns orders.ports.outbound)

(defprotocol OrderRepository
  (save [this order])
  (find-by-id [this id])
  (update-order [this order])
  (list-all [this]))

(defprotocol ProductsService
  (reserve-products [this order-id items])
  (release-reservation [this order-id]))

(defprotocol EventPublisher
  (publish [this event]))
