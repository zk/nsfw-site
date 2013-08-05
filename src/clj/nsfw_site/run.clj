(ns nsfw-site.run
  (:require [nsfw]
            [nsfw.env :as env]))

(defn -main [& args]
  (nsfw/app :repl-port (env/int :repl-port 7888)
            :server-port (env/int :port 8080)
            :autoload "src/clj/nsfw_site"))
