(ns orders.test-helpers
  "In-memory implementations for testing."
  (:require [orders.ports.outbound :as ports]))

(defn create-in-memory-repository
  "Creates an in-memory OrderRepository for tests."
  []
  (let [store (atom {})]
    (reify
      ports/OrderRepository
      (save [_ order]
        (swap! store assoc (:id order) order)
        order)
      (find-by-id [_ id]
        (get @store id))
      (update-order [_ order]
        (swap! store assoc (:id order) order)
        order)
      (list-all [_]
        (vec (vals @store))))))

(defn create-mock-event-publisher
  "Creates EventPublisher that collects published events."
  []
  (let [events (atom [])]
    {:publisher (reify
                  ports/EventPublisher
                  (publish [_ event]
                    (swap! events conj event)
                    event))
     :events events
     :get-events #(deref events)}))

(defn create-mock-products-service
  "Creates ProductsService that always returns success (for close-order tests)."
  []
  (reify
    ports/ProductsService
    (reserve-products [_ _order-id _items]
      {:success true :products []})
    (release-reservation [_ _order-id]
      {:success true})))
