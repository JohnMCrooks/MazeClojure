(ns mazeclosure.core
  (:gen-class))

(def size 10)

(defn create-rooms []
  (vec ;Throw them in vectors so we can use get to pull info from it
    (for [row (range 0 size)] ;returns list going from 0-9
      (vec
        (for [col (range 0 size)]
          {:row row :col col :visited? false :bottom? true :right? true :start? (= row col 0) :end? false})))))

(defn possible-neighbors [rooms row col]
  (let [top-room (get-in rooms [(dec row) col])  ;decrease row to get room above current 
        bottom-room (get-in rooms [(inc row) col])  ; increase row num to get room below current
        left-room (get-in rooms [row (dec col)])
        right-room (get-in rooms [row(inc col)])]
    (filter 
      (fn [room] 
        (and (not (nil? room))             ;only keep rooms that aren't nil
             (not (:visited? room))))     ; and those where the visited boolean aren't true
      [top-room bottom-room left-room right-room])))


(defn random-neighbor[rooms row col]
  (let [neighbors (possible-neighbors rooms row col)]
    (if (> (count neighbors) 0)  ;Making sure there are neighbors 
      (rand-nth neighbors)    ;if greater than 0 grab a random neighbor from the set
      nil))) ;TRYING TO FIGURE THIS OUT ;(assoc-in rooms [row col :end?] true) ; otherwise do nothing

(defn tear-down-wall [rooms old-row old-col new-row new-col]  
  (cond
    (< new-row old-row)  ; Going up
    (assoc-in rooms [new-row new-col :bottom?] false)
    (> new-row old-row)  ; Going down
    (assoc-in rooms [old-row old-col :bottom?] false)
    (< new-col old-col)  ; Going Left
    (assoc-in rooms [new-row new-col :right?] false)
    (> new-col old-col)  ; Going right
    (assoc-in rooms [old-row old-col :right?] false)))

(declare create-maze)  ;have 

(def hit-end(atom false))  ;creatng a global variable for flagging the first end point

(defn create-maze-loop [rooms old-row old-col new-row new-col]
  (let [new-rooms (tear-down-wall rooms old-row old-col new-row new-col)
        new-rooms (create-maze new-rooms new-row new-col)]
    (if (= rooms new-rooms)
      (let [end(if (not @hit-end) true false)] ;use the @ sign to grab values from an atom global
        (reset! hit-end true)        ;reset the hit-end value to true so this doesn't occur more than once.      ;zac guided us thorugh this
        (assoc-in rooms [old-row old-col :end?] end))      ;    ;zac guided us thorugh this
      (create-maze-loop new-rooms old-row old-col new-row new-col))))

(defn create-maze [rooms row col]
  (let [rooms (assoc-in rooms [row col :visited?] true)
        next-room (random-neighbor rooms row col)]
    (if next-room
      (create-maze-loop rooms row col (:row next-room) (:col next-room))
      rooms)))


(defn -main []
  (let [rooms (create-rooms)  ; defining variable rooms))
        rooms (create-maze rooms 0 0)
        hit-end (atom false)]  
    (doseq [row rooms]         ;using this to print the top line of the grid
      (print " _"))
    (println)                  ;page break for teh ASCII art
    (doseq [row rooms]
      (print "|")
      (doseq [room row]        ;inner loop where we loop over a row        
        (print (cond
                 (:end? room) "X"
                 (:start? room) "O"
                 (:bottom? room) "_"
                 :else " ")) ; the equivalent of room.hasBottom is true print underscoer otherwise a space
        (print (if (:right? room) "|" " ")))
      (println))))
