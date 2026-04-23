package coo.vlille.vehicle;

import coo.vlille.staff.repairer.ConcreteRepairer;
import coo.vlille.vehicle.state.Available;
import coo.vlille.vehicle.state.Broken;
import coo.vlille.vehicle.state.VehicleState;
import coo.vlille.vehicle.state.util.VehicleException;
import coo.vlille.vehicle.util.Color;

/**
 * Class Vehicle
 * representing a vehicle of the network
 */
public abstract class Vehicle {

    /** The ID of the vehicle */
    protected int id;
    /** The number of times the vehicle has been used */
    protected int nbUsed;
    /** The color of the vehicle */
    protected Color color;
    
    /** The maximum number of times the vehicle can be used before it needs to be repaired */
    protected static final int NB_MAX_USE = 3;
    /** The default color of the vehicle */
    protected static final Color DEFAULT_COLOR = Color.BLUE;

    /** The state of the vehicle */
    protected VehicleState state;

    /**
     * Constructor for the vehicle.
     * 
     * @param id the vehicle id
     */
    protected Vehicle(int id) {
        this.id = id;
        this.nbUsed = 0;
        this.color = DEFAULT_COLOR;
        this.state = new Available();
    }
    
    /** 
     * Sets the vehicle's Id.
     * 
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the vehicle's Id.
     * 
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Customizes the vehicle's color.
     * @param color the color to use
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the current state of the vehicle.
     * @return The current state of the vehicle.
     */
    public VehicleState getState() {
        return this.state;
    }

    /**
     * Gets the vehicle's color.
     * 
     * @return the color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the state of the vehicle (can be changed by states themselves)
     * 
     * @param state the new state
     */
    public void setState(VehicleState state) {
        this.state = state;
    }

    /**
     * Gets the number of times the vehicle has been used.
     * 
     * @return the number of uses
     */
    public int getNbUse() {
        return this.nbUsed;
    }

    /**
     * Increases the usage count, called by the state when the vehicle is taken
     */
    public void increaseNbUse() {
        this.nbUsed++;
    }

    /**
     * Compares two vehicles by comparing their ids
     * 
     * @param anotherVehicle vehicle to compare with
     * @return true if the id given is equal to this vehicle id, false otherwise
     */
    @Override
    public boolean equals(Object anotherVehicle) {
        if (anotherVehicle instanceof Vehicle) {
            Vehicle other = (Vehicle) anotherVehicle;
            return this.id == other.id;
        }
        return false;
    }

    /**
     * Takes a vehicle for a ride (handled by the state)
     * 
     * @throws VehicleException raise an exception based on the vehicle state (a vehicle that is not in a station, broken or stolen can't be taken)
     */
    public void take() throws VehicleException {
        this.state.take(this);
        this.increaseNbUse();
    }

    /**
     * Puts back a vehicle (handled by the state)
     * 
     * @throws VehicleException raise an exception based on the vehicle state (a vehicle that is already in a station can't be put back)
     */
    public void put() throws VehicleException {
        if (this.nbUsed == 0) {
            this.state = new Available();
        } else if (this.nbUsed > NB_MAX_USE) {
            this.state = new Broken();
        } else {
            this.state.put(this);
            this.increaseNbUse();
        }
    }

    /**
     * Repairs the vehicle 
     * 
     * @param repairer the repairer that will repair the vehicle
     */
    public void repair(ConcreteRepairer repairer) {
        // to implement in subclasses
    }

    @Override
    public String toString() {
        return " Id : " + this.id + " , state: " + this.state;
    }
} 