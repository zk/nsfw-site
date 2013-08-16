(ns nsfw-site.demos.redux
  (:require [nsfw.util :as util]
            [nsfw.dom :as $]
            [nsfw.bind :as bind]
            [nsfw.bootstrap :as bootstrap]))
(defn html
  ([m struct]
     (assoc m :init
            (fn [opts]
              ($/node struct))))
  ([m atom struct]
     (assoc m :init
            (fn [opts]
              (bind/render2 atom struct)))))

(defn parse-sel-ev [sel-ev]
  (let [event (->> sel-ev
                   name
                   reverse
                   (take-while #(not= "." %))
                   reverse
                   (apply str))
        sel (->> sel-ev
                 name
                 reverse
                 (drop-while #(not= "." %))
                 (drop 1)
                 reverse
                 (apply str))]
    [sel event]))

(defn event
  [m sel-ev transform]
  (let [events (:events m)
        [sel ev] ($/parse-sel-ev sel-ev)
        events (concat events [{:selector sel
                                :event ev
                                :transform transform}])]
    (assoc m :events events)))

(defn handle
  ([m msg handler]
     (let [handlers (:msg-handlers m)]
       (assoc m :msg-handlers (concat handlers [{:msg-type msg
                                                 :action handler}]))))
  ([m handler] (handle m nil handler)))


(defn state
  [m atom]
  (assoc m :!state atom))


(defn bind
  ([m query-fn handler]
     (let [bindings (:data-bindings m)]
       (assoc m :data-bindings (concat bindings [{:query-fn query-fn
                                                  :handler handler}]))))
  ([m handler]
     (bind m identity handler)))


(defn list-item [!data item-text]
  (-> {}
      (state !data)

      (assoc :item-text item-text)

      (html [:li.item
             item-text
             [:span.pull-right "click to delete"]])

      (event :click (fn [e $el {:keys [item-text !state]}]
                      (swap! !state (fn [items]
                                      (remove #(= item-text %) items)))))

      $/build))


(defn list-redux [!data]
  (-> {}

      ;; The state of our checklist, which is a Clojure vector in an atom.
      (state !data)

      (html [:div.clearfix
             [:ul.list-unstyled]
             [:input.form-control {:type "text"}]
             [:button.btn.btn-success.pull-right "Add"]])

      (bind (fn [new old {:keys [$el]}]
              (-> ($/query $el :ul)
                  $/empty
                  ($/append (map #(list-item !data %) new)))))

      (event :input.keyup (fn [{:keys [key-code]} $el]
                            (condp = key-code
                              13 [:submit-text ($/val $el)]
                              27 [:reset-input]
                              nil)))

      (event :button.click (fn [_ _ {:keys [$el]}]
                             [:submit-text (-> $el
                                               ($/query :input)
                                               $/val)]))

      (handle :submit-text (fn [text {:keys [!state $el] :as o}]
                             (cond
                              (empty? text)
                              ($/send o [:input-error])

                              (some #(= text %) @!state)
                              ($/send o [:input-error])

                              :else (do
                                      ($/send o [:reset-input])
                                      (swap! !state (fn [items]
                                                      (concat items [text])))))))

      ;; Add an error class on an :input-error message
      (handle :input-error (fn [{:keys [$el]}]
                             (-> $el
                                 ($/query :input)
                                 ($/add-class :error))))

      (handle :reset-input (fn [{:keys [$el]}]
                             (-> ($/query $el :input)
                                 ($/val "")
                                 ($/rem-class :error))))

      (handle (fn [msg o]
                (util/lpr msg)))

      $/build))


(def data (atom ["one" "two" "three"]))

(defn ^:export main []
  (bootstrap/init)
  ($/append
   :#the-list-redux
   (list-redux data)))
