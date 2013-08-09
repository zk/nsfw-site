(ns nsfw-site.intro
  (:require [nsfw.html :as html]
            [nsfw]))

(defn
  ^{:comp-tag :masthead}
  masthead [opts body]
  (let [{:keys [active-tab]} opts]
    [:header.navbar.navbar-inverse
     {:data-nsfw-component :masthead}
     [:a.navbar-brand {:href "/"}
      [:h1
       [:span.nsfw-icon
        ;; alembic
        "⚗"] "NSFW"]]
     [:ul.navbar-nav.nav.pull-right
      [:li {:class (when (= :home active-tab) "active")}
       [:a {:href "/"} "Home"]]
      [:li {:class (when (= :demos active-tab) "active")}
       [:a {:href "/demos/the-list"} "Demos"]]
      [:li {:class (when (= :tools active-tab) "active")}
       [:a {:href "/tools"} "Tools"]]]]))

(defn
  ^{:comp-tag :default-head}
  default-head
  [{:keys [title]} body]
  [:head
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   (html/stylesheet "/css/bootstrap.min.css")
   (html/stylesheet "/css/app.css")
   (html/script "/js/app.js")
   [:title (or title "NSFW -- The next-gen web framework.")]])


(defn
  ^{:comp-tag :default-footer}
  default-footer [opts body]
  [:footer
   [:div.row
    [:div.col-lg-4
     "Brought to you by "
     [:a {:href "https://twitter.com/heyzk"} "@heyzk"]
     "."]
    [:div.col-lg-4]
    [:div.col-lg-4]]])

(defn
  ^{:comp-tag :markdown}
  markdown [{:keys [src]} body]
  [:div.row
   [:div.col-lg-12
    (when src
      (-> src
          slurp
          html/markdown))]])

(defn
  ^{:comp-tag :page-body}
  page-body
  [{:keys [class active-tab scripts js-entry]} body]
  [:body
   {:class class}
   [:masthead {:active-tab active-tab}]
   body
   #_[:default-footer]
   (map html/script scripts)
   (when js-entry
     [:script {:type "text/javascript"}
      js-entry])])

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
   [:div.row.rationale
    [:div.col-lg-12
     [:h2 "ZOMG a Clojure framework?!"]]
    [:div.col-lg-6
     [:section
      [:p "While there are a ton of great libraries for web programming in
           Clojure, something's missing for GSD in that space. Frameworks like
           NSFW provide convention, which helps you scale; in features, in users,
           and in team size."]
      [:p "Don't worry, we won't lock you in. Try NSFW out and you'll see
           what you've been missing for so long."]]]
    [:div.col-lg-6.images
     [:a {:href "http://clojure.org"}
      [:img.clojure-logo
       {:src "/img/clojure-logo.png"
        :width "120"
        :height "120"}]]
     [:a {:href "http://h"}]
     [:img.html5-logo
      {:src "http://www.w3.org/html/logo/downloads/HTML5_Badge_512.png"
       :width "120"
       :height "120"}]
     [:img.sass-logo
      {:src "http://sass-lang.com/images/sass.gif"
       :width "120"
       :height "120"}]]]
   [:div.row.good-design
    [:div.col-lg-12
     [:h2 "Clean Design Built-In"]]
    [:div.col-lg-5
     [:section
      [:p "Easy-to-use grid system, sane font defaults. NSFW makes it easy to
           make great looking apps. NSFW is built on great technologies like Bootstrap."]]]
    [:div.col-lg-7.images
     [:img {:src "http://f.cl.ly/items/1U0J0J300b110z0R0S02/Screen%20Shot%202013-01-18%20at%205.32.09%20PM.png"}]
     [:img {:src "http://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Grid2aib.svg/250px-Grid2aib.svg.png"}]
     [:img {:src "http://f.cl.ly/items/0T1a2j330V3V3t3x0e18/Screen%20Shot%202013-01-18%20at%205.33.34%20PM.png"}]]]
   [:div.row.mobile-optimized
    [:div.col-lg-12
     [:h2 "Mobile Optimized"]]
    [:div.col-lg-6
     [:section
      [:p "All NSFW components are tested on a wide range of screen sizes for mobile bliss. The base CSS for NSFW is brought to you by " [:a {:href "http://getbootstrap.com"} "Bootstrap"] ", so you know your styling will stay future-proof."]]]
    [:div.col-lg-6
     [:img {:src "http://f.cl.ly/items/1c2N0X3n2h473w3G3L0f/Screen%20Shot%202013-07-29%20at%2012.37.46%20AM.png"}]]]
   [:br]
   [:br]
   [:br]])

(defn
  ^{:route "/"}
  index [r]
  (nsfw/render
   [:default-head
    {:title "No Such Framework"}]
   [:page-body
    {:class "page-intro"
     :active-tab :home}
    [:div.bleed-box
     [:div.full-bleed {:style "background-image: url('/img/dog3.jpg');"}]
     [:div.bleed-box-content
      [:div.hero-content
       [:h2 "Get web stuff done with Clojure"]
       [:p
        "NSFW is a framework that optimizes for introspection and "
        "understanding, that helps you build modern webapps using "
        "HTML5, CSS3, and Clojure."]
       [:p "Super-extra alpha."]]]]
    index-body]))

(defn
  ^{:route "/styleguide"}
  lib [r]
  (nsfw/render
   [:default-head {:title "Component Styleguide"}]
   [:page-body
    {:class "library"}
    [:div.container
     [:h1 "Component Styleguide"]
     [:hr]
     [:h2 "Masthead"]
     [:pre "[:masthead {:active-tab :home}]"]
     [:div.libcont
      [:masthead]
      [:br]
      [:masthead {:active-tab :home}]]
     [:br]
     [:h2 "Footer"]
     [:div.libcont
      [:default-footer]]
     [:br]
     [:div.demo-nav-container
      [:h2 "Demo Nav"]
      [:div.libcont
       [:demo-nav]
       [:demo-nav {:tab :the-list}]
       [:demo-nav {:tab :the-list-redux}]]]
     (repeat 100 [:br])]]))