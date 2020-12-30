(ns seven-guis.timer
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(def min-duration
  0)
(def max-duration
  200)
(def default-duration
  100)

(defn tick [seconds-elapsed duration-seconds]
  (when (< @seconds-elapsed @duration-seconds) (swap! seconds-elapsed inc)))

(defn handle-duration-change [duration-seconds new-duration]
  (reset! duration-seconds new-duration))

(defn reset-timer [seconds-elapsed duration-seconds]
  (reset! seconds-elapsed 0)
  (reset! duration-seconds default-duration))

(defn page []
  (let [seconds-elapsed (atom 0) duration-seconds (atom 100)]
    (js/setInterval #(tick seconds-elapsed duration-seconds) 1000)
    (fn []
      [:div
       [:h1 "Timer"]
       [:div
        [:label {:for "guage"} "Elapsed Time:"]
        [:progress {:id "guage"
                    :max @duration-seconds
                    :value @seconds-elapsed}]]
       [:div
        [:p {:id "elapsed-timer"} (str @seconds-elapsed "s")]
        [:div
         [:label {:for "slider"} "Duration:"]
         [:input {:id "slider"
                  :type "range"
                  :min min-duration
                  :max max-duration
                  :value @duration-seconds
                  :on-change (fn [e] (handle-duration-change duration-seconds (.. e -target -value)))}]]]
       [:button {:type "button" :on-click #(reset-timer seconds-elapsed duration-seconds)} "Reset"]])))
