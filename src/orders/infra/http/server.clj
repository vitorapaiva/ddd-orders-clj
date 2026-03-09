(ns orders.infra.http.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [reitit.ring :as ring]
            [orders.infra.http.routes :as routes]))

(defn create-app
  [deps]
  (-> (ring/ring-handler
       (ring/router (routes/create-routes deps))
       (ring/create-default-handler))
      (wrap-json-body {:keywords? true})
      wrap-json-response))

(defn start
  [app port]
  (println "Starting HTTP server on port" port)
  (jetty/run-jetty app {:port port :join? true}))
