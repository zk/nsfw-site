(ns nsfw-site.app
  (:use [nsfw.util :only [log]])
  (:require [nsfw.dom :as dom]
            [nsfw.bind :as bind]
            [nsfw.util :as util]
            [nsfw.storage :as storage]
            [nsfw.geo :as geo]
            [nsfw.components :as comp]
            [nsfw.chart :as chart]
            [cljs.reader :as reader]
            [nsfw.ani :as ani]
            [clojure.string :as str]))

(defn title->id
  "Convert a title to kebob-case, used for anchor targets."
  [title]
  (-> (str/lower-case title)
      (str/replace #"\s" "-")
      (str/replace #"/" "")
      (str/replace #"-+" "-")))

(defn section
  "Styleguide section template"
  [title & content]
  (let [id (title->id title)]
    (dom/$ [:div.container.section
            [:div.row
             [:div.span12
              [:h2 {:id id}
               [:a.section-link {:href (str "#" id)} "link"]
               title]]]
            content])))

(def supported-versions {:safari "Safari 1.0"})

(def $body (dom/$ "body"))

(def hero (comp/bleed-box
           {:img "/img/dog3.jpg"}
           [:div.navbar
            [:a.header-brand {:href "/"}
             [:h1
              [:span.nsfw-icon
               ;; alembic
               "⚗"]
              "NSFW"]]]
           [:div.hero-content
            [:h3 "Get web stuff done with Clojure"]
            [:p
             "Build modern webapps using HTML5, CSS3 and Clojurescript."]
            [:p "Warning, super-extra alpha."]
            [:br]
            (let [colors (cycle ["#00A8C6" "#40C0CB" "#AEE239" "#8FBE00"])]
              (->> ["Getting Started" "Templating" "Event Binding"
                    "Loading Indicators" "Local Storage"
                    "Mapping / Geolocation" "Bleed Box" "Charting"]
                   (map (fn [color title]
                          [:a.hero-link {:href (str "#" (title->id title))
                                         :style (str "background-color: " color ";")}
                           title])
                        colors)))]))

(def banner (dom/$ [:div.post-bleed-banner
                    "Divider!"]))


(defn data-input [atom & [initial-value]]
  (let [textarea   (dom/$ [:textarea {:spellcheck "false" :rows 5}
                           (or initial-value (pr-str @atom))])
        el         (dom/$ [:div.data-input textarea])
        on-change  (fn [v]
                     (try
                       (reset! atom (-> textarea
                                        dom/val
                                        reader/read-string))
                       (dom/rem-class textarea :error)
                       (catch js/Error e
                         (dom/add-class textarea :error))))]
    (dom/val-changed textarea on-change)
    el))

(defn text-atom-vis [atom]
  (bind/render
   (dom/$ [:div.code])
   atom
   (fn [new old el] (pr-str new))))

(defn html-atom-vis [atom]
  (bind/render
   (dom/$ [:div.html-vis])
   atom
   (fn [new old el]
     (try*
      (dom/$ new)
      (catch e
          [:div.err "Couldn't parse html."])))))

(def templating
  (let [a (atom [:div#my-div
                 [:h3.banner "hello world"]
                 [:ol
                  [:li "baz"]
                  [:li "bar"]]
                 [:input {:type "text" :placeholder "text here!"}]])]
    (section "Templating"
             [:div.row
              [:div.span6
               [:p "DOM elements are generated using code found in the "
                [:code "nsfw.dom"]
                " clojurescript namespace."]
               [:p
                "For example, to generate the snippet you see "
                "to the bottom right, you'd do someing like:"]
               [:pre
                "(def $body (dom/$ \"body\"))"
                "\n\n"
                "(dom/append $body [:div
                    [:h3.banner \"hello world\"]
                    [:ol
                      [:li \"foo\"]
                      [:li \"bar\"]
                      [:li \"baz\"]]])
"]
               [:p "It's common practice to prefix bindings holding DOM elements with "
                [:code "$"]
                ", i.e. "
                [:code "$body"]
                ", "
                [:code "$open-button"]
                ", etc."]
               [:p
                "Templates are functions that return a block of html, either as a "
                "DOM element, or as a nested data structure representing a DOM element."]]
              [:div.span6
               [:div.example.html-example
                (data-input a "[:div#my-div
  [:h3.banner \"hello world\"]
  [:ol
    [:li \"baz\"]
    [:li \"bar\"]]
  [:input {:type \"text\" :placeholder \"text here!\"}]]")
                (html-atom-vis a)]]])))

(defn local-storage-example []
  (let [input (dom/$ [:input {:type "text" :id "my-key" :placeholder "ex. [1 2 3]"}])
        output (dom/$ [:em (pr-str (:my-key storage/local))])]
    (dom/on-enter input (fn [e]
                          (try
                            (dom/prevent e)
                            (storage/lset! :my-key (reader/read-string (dom/val input)))
                            (dom/text output (pr-str (:my-key storage/local)))
                            (dom/rem-class input :error)
                            (catch js/Object e
                              (dom/add-class input :error)
                              (throw e)))))
    [:div.example.local-storage-example
     [:pre
      ";; Local storage acts like a map / hash."
      "\n\n"
      "(storage/lset! :my-key \"foo\")"
      "\n\n"
      "(:my-key storage/local) ;=> \"foo\""
      "\n\n"
      "(storage/lget! :my-key) ;=> \"foo\""
      "\n\n"
      "(storage/lget! :none \"default\")\n"
      ";=> \"default\""
      ]
     [:form.form-inline
      [:label.control-label {:for "my-key"} "my-key"]
      ": "
      input
      " -> "
      output
      " (last value)"]]))

(def html5-storage
  (section "Local Storage"
           [:div.row
            [:div.span6
             [:p "Require " [:code "[nsfw.storage :as storage]"]]
             [:p "Local storage persistent key-value database, providing the get / set interfaces. Keys can be strings, and values can be any JavaScript value type."]
             [:p "NSFW extends the JS "
              [:code "Storage"]
              " object to implement several of Clojure's lookup interface, "
              [:code "ILookup"]
              ", so you can access local storage like you would a Clojure Map."
              "However, keys are set on local storage using a specific setter, as local "
              "storage is mutable."]
             [:p "Notice the value you enter for "
              [:code "my-key"]
              [:em " persists through page reloads"]
              "."]
             [:p "HTML5 local storage only allows you to store serializable JS objects, meaning most clojure data structures are turned into strings when stored natively. Therefore, NSFW storage helpers use "
              [:code "pr-str"]
              " and "
              [:code "read-string"]
              " to store and retreive values respectively."]]
            [:div.span6
             (local-storage-example)]]))



(def html5-geoloc
  (section "Mapping / Geolocation"
           [:div.row
            [:div.span6
             [:p "Require " [:code "[nsfw.geo :as geo]"]]
             [:p "Geolocation provides locaiton data for your users."]
             [:pre
              ";; Define Map Div\n"
              "(def $map (dom/$ [:div#map]))"
              "\n\n"
              ""
              "(def map (geo/map $map\n"
              "                  {:zoom 3\n"
              "                   :center [39 -98]}))"
              "\n\n"
              "(geo/pos (fn [{:keys [latlng]}]\n"
              "           (geo/center map latlng))"]]
            [:div.span6
             (let [$map (dom/$ [:div.map "MAP"])
                   map (geo/map $map {:zoom 3 :center [39 -98]})]
               [:div.geoloc-example.example
                $map
                [:div
                 (let [btn (dom/$ [:a.btn "Zoom To My Location"])]
                   (dom/click btn (fn [e]
                                    (dom/add-class btn :loading)
                                    (geo/pos
                                     (fn [{:keys [latlng]}]
                                       (dom/rem-class btn :loading)
                                       (geo/center map latlng)
                                       (geo/zoom map 10))
                                     (fn [err]
                                       (log "err")
                                       (dom/rem-class btn :loading))))))]])]]))


(def bleed-box-example
  (comp/bleed-box
   {:mp4 "/vid/flarez.mp4"
    :webm "/vid/flarez.webm"
    :ogv "/vid/flarez.ogv"}
   [:div.example.bleed-box-example
    [:div.container
     [:div.row
      [:div.span6
       [:h2
        [:a.section-link {:href "#bleed-box"} "link"]
        "Bleed Box"]
       [:p "Require " [:code "[nsfw.components :as comp]"]]
       [:p "Full-bleed background images or video."]]
      [:div.span6
       [:div.example
        [:pre
         ";; Image Bleed Box"
         "\n"
         "(comp/bleed-box\n"
         "  {:img \"/path/to/image.jpg\"}\n"
         "  [:h1 \"Bleed Box Content\"])"
         "\n\n"
         ";; Video Bleed Box\n"
         "(comp/bleed-box\n"
         "  {:poster \"/path/to/poster.jpg\"\n"
         "   :mp4 \"/path/to.mp4\"\n"
         "   :webm \"/path/to.webm\"\n"
         "   :ogv \"/path/to.ogv\"\n"
         "   :delay 2000}\n"
         "  [:h1 \"Bleed Box Content\"])"]]]]]]))

(def event-binding
  (section
   "Event Binding"
   [:div.row
    [:div.span6
     [:p "Require " [:code "[nsfw.dom :as dom]"]]
     [:p
      "Plain ol' event binding. All major event types like "
      [:code "click"] ", " [:code "keyup"] ", and " [:code "mouseover"]
      " are represented."
      " These functions are built on "
      [:code "dom/listen"] ":"]
     [:pre
      ";; Listen for submit action"
      "\n"
      "(dom/listen $el \"submit\" #(log \"submitted\"))"]
     [:p
      "Event callbacks take two parameters, a map representing the event "
      "fired, and the bound DOM element."]
     [:pre
      ";; Handle callback\n"
      (str '(dom/click $el (fn [e el] ...))) "\n\n"
      ";;=>  e: {:type \"click\" :offset-x 123 ...}" "\n"
      ";;=> el: $el"]
     [:p
      "See "
      [:a {:href "http://code.google.com/p/closure-library/source/browse/trunk/closure/goog/events/browserevent.js#16"} "here"]
      " for a full list of "
      [:code "e"] "'s properties, which are translated from camel- to kebob-case."]]
    [:div.span6
     [:div.example
      [:pre
       "(def $el\n"
       "  (-> (dom/$ [:a.btn.btn-block \"click\"])\n"
       "      (dom/click (fn [e el]\n"
       "                   (js/alert (pr-str e)))\n"]
      (-> (dom/$ [:a.btn.btn-block "click"])
          (dom/click #(js/alert (pr-str %)))
          (dom/style {:margin-bottom :10px}))
      [:pre
       "(-> (dom/$ [:div \"mouseover me\"])\n"
       "    (dom/style {:text-align :center\n"
       "                :padding \"10px\"\n"
       "                :background-color \"#eee\"})\n"
       "    (dom/mouseover\n"
       "      (fn [e el]\n"
       "        (dom/text el \"over\")\n"
       "        (dom/add-class el :bg-green)))\n"
       "    (dom/mouseout\n"
       "      (fn [e el]\n"
       "        (dom/text el \"mouseover me\")\n"
       "        (dom/rem-class el :bg-green))))"]
      (-> (dom/$ [:div "mouseover me"])
          (dom/style {:text-align :center
                      :padding "10px"
                      :background-color "#eee"})
          (dom/mouseover (fn [e el]
                           (dom/text el "over!")
                           (dom/add-class el :bg-green)))
          (dom/mouseout (fn [e el]
                          (dom/text el "mouseover me")
                          (dom/rem-class el :bg-green))))]]]))

(defn button-example [cls]
  (let [button (dom/$ [:a {:class (str "btn loading " cls)}
                       "Click Me!"])
        el (dom/$ [:div.button-row
                   [:label (str ".btn." cls ".loading")]
                   button])]
    (dom/click button (fn [e]
                        (dom/prevent e)
                        (dom/rem-class button :loading)
                        (util/timeout #(dom/add-class button :loading) 2000)))
    el))

(def loading-indicators
  (section
   "Loading Indicators"
   [:div.row
    [:div.span6
     [:p "Enable a loading state by adding a "
      [:code "loading"]
      " css class to many nsfw components."]
     [:p "Loading is supported by buttons, and most block-level elements."]]
    [:div.span6
     [:div.example.loading-example
      (map button-example ["btn-large" "btn" "btn-small" "btn-mini"])
      [:div.button-row
       (-> (dom/$ [:a.btn.btn-block.loading ".btn.btn-block.loading"])
           (dom/click (fn [e el]
                        (dom/rem-class el :loading)
                        (dom/prevent e)
                        (util/timeout #(dom/add-class el :loading) 2000))))]
      [:div.div-loading.loading
       [:code "[:div.loading]"]]
      [:textarea.loading
       "[:textarea.loading]"]]]]))

(defn fnt [name]
  [:code name])

(def css3-transitions
  (section
   "CSS3 Transitions"
   [:div.row
    [:div.span8
     [:div.ani-example
      (let [ball (dom/$ [:div.ball " "])]
        (dom/style ball {:left "50%"
                         :margin-left "-25px"})
        (util/interval (fn []
                         (dom/style ball {:top "0px"})
                         (ani/css ball :top 250))
                       2000)
        ball)]]]))

(def animations
  (section
   "Animations"
   [:div.row
    [:div.span6
     [:p "Reqiure " [:code "[nsfw.ani :as ani]"]]]]))

(defn rand-data [n]
  (repeatedly n #(* 100 (rand))))

(defn chart-param [type examples notes default]
  [:tr
   [:td [:code type]]
   [:td (->> examples
             (map #(vector :code (pr-str %)))
             (interpose ", "))]
   [:td notes]
   [:td [:code (pr-str default)]]])

(def charts
  (section
   "Charting"
   [:div.row
    [:div.span5
     [:p "Require " [:code "[nsfw.chart :as chart]"]]
     [:p "Provides a basic wrapper around Google image charts."]
     [:div.example
      [:pre
       "(chart/line [{:data [1 2 4 8 16 32]\n"
       "              :color \"ff0000\"}]\n"
       "            ;; chart params\n"
       "            {:bg \"ffffff00\"})\n"
       "\n\n"
       [:span.chart-result ";;=>"]
       (chart/line
        [{:data [1 2 4 8 16 32] :color "0000ff"}]
        {:bg "ffffff00"
         :width 290
         :height 100
         :left-labels [0 16 32]})]]]
    [:div.span7
     [:div.table-params-wrapper
      [:h4 "Chart Params"]
      [:table.params
       [:tr [:th "key"] [:th "examples"] [:th "notes"] [:th "default"]]
       (->> [[:bg ["ff0000aa"] "4th byte specifies opacity" "ffffffff"]
             [:max [100 200] "y-axis maximum" nil]
             [:min [0 -10 10] "y-axis minimum" nil]
             [:title ["deploys"] "chart title" nil]
             [:left-labels [[0 16 32]] "seq of numbers" nil]]
            (map #(apply chart-param %)))]]]]
   [:div.row
    [:div.span12
     [:h3 "Data Sets"]]
    [:div.span7
     [:p
      "Data sets can be passed in directly as vectors, in which case "
      "they'll be rendered using the default data set parameters. Pass "
      "in a map to modify default data sets display parameters."]
     [:h4 "Data Set Params"]
     [:table.params
      [:tr [:th "key"] [:th "examples"] [:th "notes"] [:th "default"]]
      (->> [[:data [[20 53 12 19]] "vector of values" nil]
            [:color ["ff0000aa"] "4th byte is opacity" "000000"]
            [:legend ["memes"] "legend title" nil]]
           (map #(apply chart-param %)))]]
    [:div.span5
     [:div.example
      [:pre
       ";; vertical bar graph\n"
       "(chart/vbg [[10 39 54 12]]\n"
       "           {:min 0 :max 100\n"
       "            :left-labels [0 100]})\n\n"
       [:span.chart-result ";;=>"]
       (chart/vgb [[10 39 54 12]]
                  {:bg "ffffff00"
                   :height 100
                   :width 200
                   :min 0
                   :max 100
                   :left-labels [0 100]})
       "\n\n\n"
       "(chart/vbg [{:data [10 39 54 12]\n"
       "             :color \"ff0000\"\n"
       "             :legend \"cats\"}]\n"
       "           {:min 0 :max 100})\n\n"
       [:span.chart-result ";;=>"]
       (chart/vgb [{:data [10 39 54 12]
                    :color "ff0000"
                    :legend "cats"}]
                  {:bg "ffffff00"
                   :height 100
                   :width 200
                   :min 0
                   :max 100})]]]]))

(def getting-started
  (section "Getting Started"))

(def $body (dom/$ "body"))

(defn mouseover-image [src text]
  [:div.mouseover-image
   [:img {:src src}]
   [:span text]])

(def design
  (section
   "Designy Things"
   [:div.container.design
    [:div.row
     [:div.span5
      [:p "As developers, no longer can we ignore basic design principles when writing software."]]
     [:div.span7.grid-images
      (mouseover-image "http://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Grid2aib.svg/250px-Grid2aib.svg.png"
                       "Grids")
      (mouseover-image "http://f.cl.ly/items/0T1a2j330V3V3t3x0e18/Screen%20Shot%202013-01-18%20at%205.33.34%20PM.png"
                       "Typography")
      (mouseover-image "http://f.cl.ly/items/1U0J0J300b110z0R0S02/Screen%20Shot%202013-01-18%20at%205.32.09%20PM.png"
                       "Whitespace")]]
    [:div.row.grids
     [:div.span12
      [:h4 "Grids"]
      [:div.row
       [:div.span5 "foo"]
       [:div.span7]]]]
    [:div.row
     [:div.span12
      [:h4 "Whitespace"]]]
    [:div.row
     [:div.span12
      [:h4 "Paragraph Widths"]
      [:p
       "Paragraph line widths should be between " [:em "45 and 75 characters"] ". "
       [:a {:href "http://trentwalton.com/2013/01/07/flexible-foundations/"}
        "link"]]]]
    [:div.row
     [:div.span6.para-width-ex
      [:h5 "Good"]
      [:p
       "But actually, he thought as he readjusted the Ministry of
        Plenty's figures, it was not even forgery. It was merely the
        substitution of one piece of nonsense for another. Most of the
        material that you were dealing with had no connection with
        anything in the real world, not even the kind of connection
        that is contained in a direct lie. Statistics were just as
        much a fantasy in their original version as in their rectified
        version."]]
     [:div.span4.para-width-ex
      [:h5 "Borderline"]
      [:p
       "But actually, he thought as he readjusted the Ministry of
        Plenty's figures, it was not even forgery. It was merely the
        substitution of one piece of nonsense for another. Most of the
        material that you were dealing with had no connection with
        anything in the real world, not even..."]]
     [:div.span2.para-width-ex [:h5 "Horrible"]
      [:p
       "But actually, he thought as he readjusted the Ministry of
        Plenty's figures, it was not even forgery. It was merely..."]]]]))

 (-> $body
    (dom/append hero)
    (dom/append banner)
    (dom/append getting-started)
    (dom/append design)
    (dom/append templating)
    (dom/append event-binding)
    (dom/append animations)
    (dom/append loading-indicators)
    (dom/append html5-storage)
    (dom/append html5-geoloc)
    (dom/append bleed-box-example)
    (dom/append charts)
    (dom/append [:div (repeat 10 [:br])]))
