package connections;

import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;

public abstract class Segment {
	
	/**
	 * Terminate this segment.
	 * 
	 * @post   This segment is terminated.
	 *       | new.isTerminated()
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   The given identification is equal to one of the previous created identifications.
	 * 		 | allIdentificationsCreated.contains(identification)
	 * @throws IllegalStructureException
	 *         The given identification is not a valid identification for any
	 *         road.
	 *       | ! isValidIdentification(getIdentification())
	 * @throws IllegalSpeedLimitException
	 *         The given speed limit is not a valid speed limit for any
	 *         road.
	 *       | ! isValidSpeedLimit(getSpeedLimit())
	 * @throws IllegalAverageSpeedException
	 *         The given average speed is not a valid average speed for any
	 *         road.
	 *       | ! isValidAverageSpeed(getAverageSpeed())
	 */
	public abstract void terminate() throws IllegalStructureException, IllegalAllIdentificationsCreatedException, IllegalAverageSpeedException, IllegalSpeedLimitException ;

	/**
	 * Check whether this segment has the given segment as one
	 * of its subsegments.
	 *
	 * @param  segment
	 *         The segment to be checked.
	 * @return True if the given segment is the same segment as this
	 *         segment.
	 *       | if (segment == this)
	 *       |   then result == true
	 * @return False if the given segment is not effective.
	 *       | if (segment == null)
	 *       |   then result == false
	 */
	public abstract boolean hasAsSubSegment(Segment segment);
	
}
