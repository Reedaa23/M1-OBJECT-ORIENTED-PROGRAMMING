package connections;

import be.kuleuven.cs.som.annotate.Raw;
import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;

/**
 * A class of two-way roads as a special kind of road. The direction in which one-way roads are traversable
 * is either from the first end point to the second end point or from the second end point to the first end point.
 * 
 */
public class TwoWayRoad extends Road {
	
	/**
     * Initialize this new two-way road with given identification, first end point, second end point,
     * length, speed limit and average speed. The direction in which this new two-way road is traversable
     * is either from the first end point to the second end point or from the second end point to the first end point.
	 * 
	 * @param  identification
	 * 		   The identification for this new two-way road.
	 * @param  endPoint1
	 * 		   The first end point for this new two-way road.
	 * @param  endPoint2
	 * 		   The second end point for this new two-way road.
	 * @param  length
	 * 		   The length for this new two-way road.
	 * @param  speedLimit
	 * 		   The speed limit for this new two-way road.
	 * @param  averageSpeed
	 * 		   The average speed for this new two-way road.
     * @effect This new two-way road is initialized as a new road with
     *         given identification, the given first end point, the given second end point,
     *         the given length, the given speed limit, the given average speed.
     *       | super(identification, endPoint1, endPoint2, length, speedLimit, averageSpeed)
     * @effect The current delay on the opposite direction of this new two-way road is set to 0F.
     * 		 | setCurrentDelayOpposite(0F)
	 * @effect This new two-way road is set to the non-blocked state in the direction from its second end point to its first end point.
	 * 		 | setBlockedOpposite(false)
     * @post   The start and end locations are added to the valid start and end locations 
     * 		   of this new two-way road.
	 *		 | validStartLocations.contains(endPoint1) &&
	 *		 | validStartLocations.contains(endPoint2) &&
	 *		 | validEndLocations.contains(endPoint1) &&
	 *       | validEndLocations.contains(endPoint2)
	 */
	public TwoWayRoad(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit, 
			float averageSpeed) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		super(identification, endPoint1, endPoint2, length, speedLimit, averageSpeed);
		setCurrentDelayOpposite(0F);
		setBlockedOpposite(false);
		addValidStartLocation(endPoint1);
		addValidStartLocation(endPoint2);
		addValidEndLocation(endPoint1);
		addValidEndLocation(endPoint2);
	}
	

	/**
	 * Return the current delay on the direction from the second end point to the first end point 
	 * of this two-way road.
	 */
	@Override
	public float getCurrentDelayOpposite() {
		return currentDelayOpposite;
	}
	
	/**
	 * Return the current state (blocked or not) of the direction from the second end point to the first end point 
	 * of this two-way road.
	 */
	@Override
	public boolean getBlockedOpposite() {
		return blockedOpposite;
	}
	
	
	/**
	 * Set the current delay on the direction from the second end point to the first end point of this two-way road to the given current delay.
	 * 
	 * @param  currentDelay
	 *         The new current delay on the direction from the second end point to the first end point for this road.
	 * @pre    The given current delay must be a valid current delay on the direction from the second end point to the first end point for any
	 *         road.
	 *       | isValidCurrentDelay(currentDelay)
	 * @post   The current delay on the direction from the second end point to the first end point of this road is equal to the given
	 *         current delay.
	 *       | new.getCurrentDelayOpposite() == currentDelay
	 */
	@Raw @Override
	public void setCurrentDelayOpposite(float currentDelay) {
		assert isValidCurrentDelay(currentDelay);
		this.currentDelayOpposite = currentDelay;
	}
	
	/**
	 * Set the direction from the second end point to the first end point of this road to a blocked direction.
	 * 
	 * @param  flag
	 * 		 | A flag indicating that the direction from the second end point to the first end point is blocked or not.
	 * @post   The current state of the direction from the second end point to the first end point of this road is equal to true.
	 *       | new.getBlockedOpposite() == true
	 */
	@Override
	public void setBlockedOpposite(boolean flag) {
		this.blockedOpposite=flag;
	}

}
