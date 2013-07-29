(ns nsfw-site.overview
  (:require [nsfw-site.overview.templates :as tpls]
            [nsfw-site.common :as common]))

(defn app []
  [:div.page-overview
   common/$navbar
   tpls/$hero])