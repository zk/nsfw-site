(ns nsfw-site.run
  (:require [nsfw]
            [nsfw.env :as env]))

(nsfw/load-nss ['nsfw-site.intro
                'nsfw-site.demos
                'nsfw-site.demos.todo])

(defn -main [& args]
  (nsfw/app :repl-port (env/int :repl-port 7888)
            :server-port (env/int :port 8080)))