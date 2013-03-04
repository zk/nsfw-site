(ns nsfw-site.common
  (:require [nsfw.dom :as dom]))

(def $navbar [:header.navbar
              [:a.header-brand {:href "/"}
               [:h1
                [:span.nsfw-icon
                 ;; alembic
                 "⚗"]
                "NSFW"]]])