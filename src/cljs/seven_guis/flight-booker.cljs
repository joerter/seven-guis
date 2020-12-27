(ns seven-guis.flight-booker
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(def one-way-flight "one-way flight")
(def return-flight "return flight")
(def invalid-class "invalid")

(def selected-flight
  (atom one-way-flight))
(def start-date
  (atom "2020/12/25"))
(def end-date
  (atom "2020/12/25"))
(def end-date-is-disabled
  (atom true))
(def book-is-disabled
  (atom false))
(def start-date-class
  (atom nil))
(def end-date-class
  (atom nil))
(def confirmation-message
  (atom nil))

(defn valid-date? [d]
  (let [parsed (.parse js/Date d)]
    (not (.isNaN js/Number parsed))))

(defn end-date-is-before-start-date []
  (if (true? @end-date-is-disabled)
    false
    (let [start (js/Date. @start-date) end (js/Date. @end-date)]
        (< end start))))

(defn handle-flight-change [v]
  (reset! selected-flight v)
  (reset! end-date-is-disabled (= one-way-flight v)))

(defn handle-start-date-change [s]
  (reset! start-date s)
  (let [is-valid (valid-date? s)]
    (reset! start-date-class (if (true? is-valid) nil invalid-class))
    (reset! book-is-disabled (or (not is-valid) (end-date-is-before-start-date)))))

(defn handle-end-date-change [e]
  (reset! end-date e)
  (let [is-valid (valid-date? e)]
    (reset! end-date-class (if (true? is-valid) nil invalid-class))
    (reset! book-is-disabled (or (not is-valid) (end-date-is-before-start-date)))))

(defn handle-form-submit []
  (if (= one-way-flight @selected-flight)
    (reset! confirmation-message (str "You have booked a one-way flight on " @start-date))
    (reset! confirmation-message (str "You have booked a two-way flight starting on " @start-date " and returning on " @end-date))
    ))

(defn page []
  [:div {:class "flight-booker"}
   [:h1 "Flight Booker"]
   [:form {:on-submit (fn [e] (.preventDefault e) (handle-form-submit))}
    [:select {:on-change (fn [e] (handle-flight-change (.. e -target -value)))}
     [:option one-way-flight]
     [:option return-flight]]
    [:input {:id "start-date"
             :type "text"
             :class @start-date-class
             :placeholder "start date"
             :on-change (fn [e] (handle-start-date-change (.. e -target -value)))}]
    [:input {:id "end-date"
             :type "text"
             :class @end-date-class
             :placeholder "end date"
             :value @end-date
             :disabled @end-date-is-disabled
             :on-change (fn [e] (handle-end-date-change (.. e -target -value)))}]
    [:input {:type "submit" :value "Book" :disabled @book-is-disabled}]]
   [:div
    [:p {:id "confirmation-message"} @confirmation-message]]])
