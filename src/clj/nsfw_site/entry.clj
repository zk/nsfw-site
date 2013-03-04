(ns nsfw-site.entry
  (:require [nsfw.webapp :as webapp]))

(def routes
  (webapp/routes
   [""] (webapp/cs :app
                   :css [:nsfw :app]
                   :google-maps true)))