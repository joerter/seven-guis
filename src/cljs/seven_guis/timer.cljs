(ns seven-guis.timer
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(defn tick [s]
  (swap! s inc))

(defn page []
  (let [seconds-elapsed (atom 0)
        duration-seconds (atom 100)]
    (fn []
      (js/setTimeout #(tick seconds-elapsed) 1000)
      [:div
       [:h1 "Timer"]
       [:div
        [:label {:for "guage"} "Elapsed Time:"]
        [:progress {:id "guage"
                    :max @duration-seconds
                    :value (float (* 100 (/ @seconds-elapsed @duration-seconds) ))}]]
       [:div
        [:p {:id "elapsed-timer"} (str @seconds-elapsed "s")]]
       [:div
        [:label {:for "slider"} "Duration:"]
        [:input {:id "slider" :type "range" :min "0" :max "10"}]]
       [:button {:type "button"} "Reset"]])))
