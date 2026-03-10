(ns orders.main
  (:require [orders.config :as config]
            [orders.infra.http.server :as server]
            [orders.infra.http.products-client :as products-client]
            [orders.infra.persistence.database :as database]
            [orders.infra.persistence.order-repository :as order-repository]
            [orders.infra.event-publisher :as event-publisher]
            [orders.infra.event-handlers.order-created-handler :as order-created-handler]
            [orders.infra.event-handlers.order-updated-handler :as order-updated-handler])
  (:gen-class))

(defn create-dependencies
  [config]
  (let [datasource (database/create-datasource (:db config))]
    (database/create-tables! datasource)
    (let [repository (order-repository/create-repository datasource)
          products-client (products-client/create-client (get-in config [:products-service :base-url]))
          order-created-handler (order-created-handler/create-handler products-client)
          order-updated-handler (order-updated-handler/create-handler)
          event-publisher (event-publisher/create-publisher order-created-handler order-updated-handler)]
      {:repository repository
       :event-publisher event-publisher})))

(defn -main
  [& _args]
  (println "Initializing Orders service...")
  (let [cfg (config/get-config)
        deps (create-dependencies cfg)
        app (server/create-app deps)
        port (get-in cfg [:server :port])]
    (server/start app port)))
