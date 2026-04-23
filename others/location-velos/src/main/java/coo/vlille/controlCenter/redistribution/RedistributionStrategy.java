package coo.vlille.controlCenter.redistribution;

import java.util.List;

import coo.vlille.station.Station;
import coo.vlille.vehicle.state.util.VehicleException;

/**
 * Interface that represents the Srategy of redistribution 
 */
public interface RedistributionStrategy {
    /**
     * execute the redistribution
     * @param stations the elements that contains the objects to handle
     */
    void redistribute(List<Station> stations) throws VehicleException;
}
