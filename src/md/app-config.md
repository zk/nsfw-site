## App Config <a id="intro"></a>

Configuration values should be passed in through the environment. NSFW
provides several functions to get this done:


    (require '[nsfw.env :as env])

    (env/str :user) ;;> "zk"

    ;; $PORT=8080
    (env/int :port) ;;> 8080

    ;; $DEBUG=true
    (env/bool :debug) ;;> true


See this in action in the
[NSFW Starter Kit](https://github.com/zk/nsfw-starter), where the
server's port is set from an environment variable.


    (nsfw/app :repl-port 7888
              :server-port (env/int :port 8080)
              :autoload "src/clj")
