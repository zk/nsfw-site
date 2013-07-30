(ns nsfw-site.run
  (:require [nsfw.server :as server]
            [nsfw.env :as env]
            [nsfw.app :as app]
            [ring.middleware.reload-modified :as reload]
            [nsfw-site.entry :as entry]
            [clojure.tools.nrepl.server :as repl]))

(defn start-repl [port]
  (repl/start-server :port port))

(def root-entry
  (-> #'entry/app
      (reload/wrap-reload-modified ["src/clj"])
      (app/debug-exceptions true)))

(defn -main [& args]
  (start-repl (env/int :repl-port 7888))
  (server/start :entry root-entry
                :port (env/int :port 8080)))