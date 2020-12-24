(ns seven-guis.temperature-converter
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(def temperature-celsius
  (atom 0))

(def temperature-fahrenheit
  (atom 32))

(defn precise [n]
  (float (.toFixed n 2)))

(defn celsius->fahrenheit [c]
  (precise (+ 32 (* c (/ 9 5)))))

(defn fahrenheit->celsius [f]
  (precise (* (- f 32) (/ 5 9))))

(defn when-valid-input [input actionFn]
  (let [parsed (js/parseFloat input)]
    (when (not (.isNaN js/Number parsed)) (actionFn parsed))))

(defn calculate-fahrenheit [celsius]
  (when-valid-input celsius
    (fn [c] (reset! temperature-fahrenheit (celsius->fahrenheit c))))
  (reset! temperature-celsius celsius))

(defn calculate-celsius [fahrenheit]
  (when-valid-input fahrenheit
    (fn [c] (reset! temperature-celsius (fahrenheit->celsius c))))
  (reset! temperature-fahrenheit fahrenheit))

(defn page []
  (fn [] [:div
          [:h1 "Temperature Converter"]
          [:div
           [:input
            {:id "celsius"
             :type "text"
             :value @temperature-celsius
             :on-focus (fn [e] (.select (.-target e)))
             :on-change (fn [e] (calculate-fahrenheit (.. e -target -value)))}]
           [:span "Celsius = "]
           [:input
            {:id "fahrenheit"
             :type "text"
             :value @temperature-fahrenheit
             :on-focus (fn [e] (.select (.-target e)))
             :on-change (fn [e] (calculate-celsius (.. e -target -value)))}]
           [:span "Fahrenheit"]]]))
