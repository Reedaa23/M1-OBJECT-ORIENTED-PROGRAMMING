package connections;

import be.kuleuven.cs.som.annotate.Raw;
import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;

/**
 * A class of one-way roads as a special kind of road. The direction in which one-way roads are traversable
 * is from the first end point to the second end point.
 * 
 */
public class OneWayRoad extends Road {
	

	/**
     * Initialize this new one-way road with given identification, first end point, second end point,
     * length, speed limit and average speed. The direction in which this new one-way road is traversable
     * is from the first end point to the second end point.
	 * 
	 * @param  identification
	 * 		   The identification for this new one-way road.
	 * @param  endPoint1
	 * 		   The first end point for this new one-way road.
	 * @param  endPoint2
	 * 		   The second end point for this new one-way road.
	 * @param  length
	 * 		   The length for this new one-way road.
	 * @param  speedLimit
	 * 		   The speed limit for this new one-way road.
	 * @param  averageSpeed
	 * 		   The average speed for this new one-way road.
     * @effect This new one-way road is initialized as a new road with
     *         given identification, the given first end point, the given second end point,
     *         the given length, the given speed limit, the given average speed.
     *       | super(identification, endPoint1, endPoint2, length, speedLimit, averageSpeed)
     * @post   The first end point is added to the valid start locations of this new one way road
     * 		   and the second end point is added to the valid end locations of this new one-way road.
	 *		 |  validStartLocations.contains(endPoint1) &&
	 *		 | 	validEndLocations.contains(endPoint2)
	 */
	public OneWayRoad(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit, 
			float averageSpeed) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		super(identification, endPoint1, endPoint2, length, speedLimit, averageSpeed);
			addValidStartLocation(endPoint1);
			addValidEndLocation(endPoint2);	
	}
	
	
	/**
	 * Return the current delay on the direction from the second end point to the first end point 
	 * of this one-way road.
	 * @note It is not possible to ask for the current delay on the direction in which this one-way road
	 *       is not traversable.
	 */
	@Override
	public float getCurrentDelayOpposite() {
		assert false;
		return 0;
		
	}
	
	
	/**
	 * Return the current state (blocked or not) of the direction from the second end point to the first end point 
	 * of this one-way road.
	 * @note It is not possible to ask for the current state (blocked or not) on the direction in which this one-way road
	 * 		 is not traversable. 
	 */
	@Override
	public boolean getBlockedOpposite() {
		assert false;
		return true;
	}
	
	
	/**
	 * Set the current delay on the direction from the second end point to the first end point of this one-way road to the given current delay.
	 * 
	 * @param  currentDelay
	 *         The new current delay on the direction from the second end point to the first end point for this road.
	 * @note   It is not possible to change the current delay on the direction in which this one-way road
	 *         is not traversable.
	 */
	@Raw @Override
	public void setCurrentDelayOpposite(float currentDelay) {
		assert false;
	}
	
	/**
	 * Set the direction from the second end point to the first end point of this road to a blocked direction.
	 * 
	 * @param  flag
	 * 		 | A flag indicating that the direction from the second end point to the first end point is blocked or not.
	 * @note   It is not possible to change the current state (blocked or not) on the direction in which this one-way road
	 *         is not traversable.
	 */
	@Override
	public void setBlockedOpposite(boolean flag) {
		assert false;
	}
	

}
