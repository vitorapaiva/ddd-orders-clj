(ns orders.ports.event-handler
  "Protocolo para handlers de eventos. ISP: interface focada em suporte e execução.")

(defprotocol EventHandler
  (supports? [this event] "Retorna true se este handler processa o evento.")
  (handle [this event] "Processa o evento."))
