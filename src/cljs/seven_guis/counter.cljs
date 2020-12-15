(ns seven-guis.counter
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(def counter (atom 0))

(defn page []
  (fn [] [:div
          [:h1 "Counter"]
          [:input {:type "number" :readOnly true :value @counter}]
          [:button {:on-click #(swap! counter inc)} "Count"]]))
