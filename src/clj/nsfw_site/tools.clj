(ns nsfw-site.tools
  (:require [nsfw.html :as html]
            [nsfw.http :as http]
            [nsfw-site.layouts :as layouts]))


(defn
  ^{:route "/tools"}
  index [r]
  (-> {:body [:div.container
              [:div.row
               [:div.col-lg-10.col-offset-1
                [:div.page-lead
                 [:h1 "Tools"]
                 [:p "A collection of useful utilities for developing webapps, baked-in to NSFW"]]
                (-> "src/md/tools.md"
                    slurp
                    html/markdown)]]]}
      layouts/main
      http/html))