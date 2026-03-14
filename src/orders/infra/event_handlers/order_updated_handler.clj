(ns orders.infra.event-handlers.order-updated-handler
  "Handler for OrderUpdated event. SRP: side-effects when status is updated (e.g. logging)."
  (:require [orders.ports.event-handler :as eh]))

(defrecord OrderUpdatedHandler []
  eh/EventHandler
  (supports? [_ event]
    (= :order-updated (:type event)))
  (handle [_ _event]
    ;; No action required currently; extensible in future (e.g. notifications)
    nil))

(defn create-handler
  []
  (->OrderUpdatedHandler))
