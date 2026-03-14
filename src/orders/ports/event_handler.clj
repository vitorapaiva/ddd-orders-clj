(ns orders.ports.event-handler
  "Protocol for event handlers. ISP: interface focused on support and execution.")

(defprotocol EventHandler
  (supports? [this event] "Returns true if this handler processes the event.")
  (handle [this event] "Processes the event."))
