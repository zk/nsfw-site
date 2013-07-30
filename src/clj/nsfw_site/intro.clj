(ns nsfw-site.intro
  (:require [nsfw.http :as http]
            [nsfw.html :as html]
            [nsfw-site.layouts :as layouts]))

(def index-body
  [:div.intro-body.container
   [:div.row.call-out
    [:div.col-lg-12
     [:section
      [:h1 "Wat?"]
      [:p.lead "NSFW is a framework designed to scale along with your needs.
           We value understandability through introspection: it should
           be easy to understand what's going on in your app, even as it
           scales to thousands of lines of code, and millions of users."]]]]
   [:div.row
    [:div.col-lg-10
     [:section
      [:h2 "ZOMG a Clojure framework?!"]
      [:p "While there are a ton of great libraries for web programming in
           Clojure, something's missing for GSD in that space. Frameworks
           provide convention, which helps you scale; in features, in users,
           and in team size."]
      [:p "Don't worry, we won't lock you in. Try NSFW out and you'll see
           what you've been missing for so long."]]]
    [:div.col-lg-2
     [:img {:src "/img/clojure-logo.png"
            :width "120"
            :height "120"}]]]
   [:div.row
    [:div.col-lg-5
     [:section
      [:h2 "Clean Design Built-In"]
      [:p "Easy-to-use grid system, sane font defaults. NSFW makes it easy to
           make great looking apps. NSFW is built on great technologies like Bootstrap."]]]
    [:div.col-lg-7.design-images
     [:img {:src "http://f.cl.ly/items/1U0J0J300b110z0R0S02/Screen%20Shot%202013-01-18%20at%205.32.09%20PM.png"}]
     [:img {:src "http://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Grid2aib.svg/250px-Grid2aib.svg.png"}]
     [:img {:src "http://f.cl.ly/items/0T1a2j330V3V3t3x0e18/Screen%20Shot%202013-01-18%20at%205.33.34%20PM.png"}]]]
   [:div.row.mobile-optimized
    [:div.col-lg-6
     [:section
      [:h2 "Mobile Optimized"]
      [:p "All NSFW components are tested on a wide range of screen sizes for mobile bliss. The base CSS for NSFW is brought to you by " [:a {:href "http://getbootstrap.com"} "Bootstrap"] ", so you know your styling will stay future-proof."]]]
    [:div.col-lg-6
     [:img {:src "http://f.cl.ly/items/1c2N0X3n2h473w3G3L0f/Screen%20Shot%202013-07-29%20at%2012.37.46%20AM.png"}]]]
   [:br]
   [:br]
   [:br]])



(defn
  ^{:route "/"}
  index [r]
  (-> {:title "NSFW -- For the love of god, don't use this."
       :body index-body}
      layouts/intro
      http/html))


(defn
  ^{:route "/about"}
  about [r]
  (-> {:body (str "ABOUT!!!!!!!"
                  " "
                  (:route r))}
      layouts/main
      http/html))

(defn
  ^{:route "/getting-started"}
  getting-started [r]
  (-> {:body "GETTING STARTED !!!!!"}
      layouts/main
      http/html))