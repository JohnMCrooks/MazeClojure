(ns mazeclosure.core
  (:gen-class))

(def size 10)

(defn create-rooms []
  (vec ;Throw them in vectors so we can use get to pull info from it
    (for [row (range 0 size)] ;returns list going from 0-9
      (vec
        (for [col (range 0 size)]
          {:row row :col col :visited? false :bottom? true :right? true})))))

(defn -main []
  (let [rooms (create-rooms)]  ; defining variable rooms))
    (doseq [row rooms]         ;using this to print the top line of the grid
      (print " _"))
    (println)                  ;page break for teh ASCII art
    (doseq [row rooms]
      (print "|")
      (doseq [room row]        ;inner loop where we loop over a row        
        (print (if (:bottom? room) "_" " ")) ; the equivalent of room.hasBottom is true print underscoer otherwise a space
        (print (if (:right? room) "|" " ")))
      (println))))
