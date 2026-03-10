(ns orders.infra.event-handlers.order-updated-handler
  "Handler para evento OrderUpdated. SRP: side-effects ao atualizar status (ex: log)."
  (:require [orders.ports.event-handler :as eh]))

(defrecord OrderUpdatedHandler []
  eh/EventHandler
  (supports? [_ event]
    (= :order-updated (:type event)))
  (handle [_ _event]
    ;; Nenhuma ação necessária atualmente; extensível no futuro (ex: notificações)
    nil))

(defn create-handler
  []
  (->OrderUpdatedHandler))
