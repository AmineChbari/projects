package coo.vlille.station;

import java.util.ArrayList;
import java.util.List;

import coo.vlille.controlCenter.Observer;

/**
 * Represents an observable object in the Observer design pattern.
 * This class manages a list of observers and provides methods to add, remove, 
 * and notify observers when a change occurs.
 */
public class Observable {

    // A list of observers subscribed to this observable object.
    private List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}


