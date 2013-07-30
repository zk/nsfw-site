(ns nsfw-site.entry
  (:require [nsfw.app :as app]
            [nsfw-site.intro :as intro]))

(defonce session (app/session-store :encrypted-cookie))

(def app
  (app/route-default
   {}
   (app/load-routes '[nsfw-site.intro nsfw-site.demos.todo])))