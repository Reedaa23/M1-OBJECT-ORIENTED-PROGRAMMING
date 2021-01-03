package connections;
import java.util.*;

import be.kuleuven.cs.som.annotate.*;
import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalLengthException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;

/**
 * A class of roads, involving an identification, end points, a length, a speed limit, an average speed and a current delay. 
 * @invar  The identification of each road must be a valid identification for any
 *         road.
 *       | isValidIdentification(getIdentification())
 * @invar  The first end point of each road must be a valid end point for any
 *         road.
 *       | isValidEndPoint(getEndPoint1())
 * @invar  The second end point of each road must be a valid end point for any
 *         road.
 *       | isValidEndPoint(getEndPoint2())
 * @invar  The length in meters of each road must be a valid length for any
 *         road.
 *       | canHaveAsLength(getLength())
 * @invar  The speed limit in meters per second that should not be exceeded when traversing any road.
 *       | isValidSpeedLimit(getSpeedLimit())
 * @invar  The average speed in meters per second that is obtained while traversing any road under standard conditions.
 *       | isValidAverageSpeed(getAverageSpeed())
 * @invar  The current delay in seconds on each road must be a valid current delay for any
 *         road.
 *       | isValidCurrentDelay(getCurrentDelay())
 * @invar  All the identifications created by the creation of all the roads must be valid identifications for any road.
 *       | for each identification in allIdentificationsCreated 
 *       |  isValidIdentification(identification) == true
 *       | 	Collections.frequency(allIdentificationsCreated,identification) == 1
 * @invar  The lengths wanted for the structure of the identification of this road must be valid lengths for any road.
 *       | for each length in validIdentificationLengths
 *       | 	canHaveAsLength(length) == true
 * @invar  Each road must have a proper set of routes.
 * 	     | hasProperRoutes()
 * 
 */

public abstract class Road extends Segment {
	/**
	 * Variable registering the speed of light.
	 */
	public static final float SPEED_OF_LIGHT = 299792458.0F;
	/**
	 * ArrayList registering all the identifications created by the creation of all the roads.
	 */
	private static final ArrayList<String> allIdentificationsCreated = new ArrayList<String>();
	/**
	 * ArrayList registering the lengths wanted for the structure of the identification of this road (excluding 2 and 3).
	 */
	private static final ArrayList<Integer> validIdentificationLengths=new ArrayList<Integer>();
	/**
	 * ArrayList registering the characters wanted for following the capital letter in the identification of this road (here all the digits).
	 */
	private static final ArrayList<Character> validCharactersAfterCapitalLetter=new ArrayList<Character>();
	/**
	 * Variable registering the identification of this road.
	 */
	private String identification;
	
	/**
	* Variable registering the first end point of this road.
	*/
	private final double[] endPoint1;
	/**
	* Variable registering the second end point of this road.
	*/
	private final double[] endPoint2;
	/**
	* Variable registering the length in meters of this road.
	*/
	private int length;
	/**
	 * Variable registering the speed limit in meters per second of this road that should not be exceeded.
	 */
	private float speedLimit;
	/**
	 * Variable registering the average speed in meters per second of this road that is obtained while traversing it under standard conditions.
	 */
	private float averageSpeed;
	/**
	 * Variable registering the current delay in seconds on the direction from the first end point to the second end point of this road.
	 */
	protected float currentDelayForth;
	/**
	 * Variable registering the current delay in seconds on the direction from the second end point to the first end point of this road.
	 */
	protected float currentDelayOpposite;
	/**
	 * Variable indicating whether or not the direction from the first end point to the second end point of this road is currently blocked.
	 */
	protected boolean blockedForth;
	/**
	 * Variable indicating whether or not the direction from the second end point to the first end point of this road is currently blocked.
	 */
	protected boolean blockedOpposite;
	/**
	 * Variable referencing the routes to which this road belongs.
	 */
	private final Set<Route> routes= new HashSet<Route>();
    /**
     * Variable registering whether or not this road has been
     * terminated.
     */
    private boolean isTerminated = false;
    /**
     * Variable registering the valid start locations of this road.
     */
    private final ArrayList<double[]> validStartLocations= new ArrayList<double[]>();
    /**
     * Variable registering the valid end locations of this road.
     */
    private final ArrayList<double[]> validEndLocations= new ArrayList<double[]>();

	/**
	 * Set the list of valid lengths of identification of this road to the 2 and 3.
	 * @post   Lengths 2 and 3 are allowed.
	 * 		 | validIdentificationLengths.contains(2)
	 * 		 | validIdentificationLengths.contains(3)
	 */
	public static void setValidIdentificationLengths(){
		validIdentificationLengths.clear();
		validIdentificationLengths.add(2);
		validIdentificationLengths.add(3);
	}	

	
	/**
	 * Set the list of valid lengths of identification of this road to the allowed given lengths and to 2 and 3.
	 * @param lengths
	 *        The possible lengths of identification we want to allow, excluding 2 and 3 that are also allowed.
	 * @post   Lengths 2 and 3 are allowed.
	 * 		 | validIdentificationLengths.contains(2)
	 * 		 | validIdentificationLengths.contains(3)
	 * @post   The valid given lengths are added to the list of valid.
	 * 		 | for each length in lengths 
	 * 		 | 	validIdentificationLengths.contains(length)
	 * @post   Negative numbers are not allowed.
	 * 		 | for each number from Integer.MIN_VALUE to 1 (1 excluded)
	 *       | 	!validIdentificationLengths.contains(number)
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	public static void setValidIdentificationLengths(int[] lengths) throws IllegalLengthException {
		validIdentificationLengths.clear();
		validIdentificationLengths.add(2);
		validIdentificationLengths.add(3);
		for(int i=0;i<lengths.length;i++) {
			if(!canHaveAsLength(lengths[i])) {
				throw new IllegalLengthException(lengths[i]);
			}
			validIdentificationLengths.add(lengths[i]);
		}
		
	}
	
	/**
	 * Set the list of valid characters that can follow the capital letter in the identification of this road to the digits.
	 *
	 * @post  The digits are allowed.
	 * 		 | for each digit between 0 and 9 included
	 * 		 |	validCharactersAfterCapitalLetter.contains(digit)
	 */
	public static void setValidCharactersAfterCapitalLetter() {
		validCharactersAfterCapitalLetter.clear();
		for(char c='0';c<='9';c++) {
			validCharactersAfterCapitalLetter.add(c);
		}
	}
	
	/**
	 * Set the list of valid characters that can follow the capital letter in the identification of this road to the given characters and to the digits.
	 *
	 * @param characters
	 *		  The characters we want to allow just after the capital letter in identification of this road, excluding the digits that are also allowed.
	 * @post  The digits are allowed.
	 * 		 | for each digit between 0 and 9 included
	 * 		 |	validCharactersAfterCapitalLetter.contains(digit)
     * @post  All the given characters are allowed.
     *		 | for each character in characters
     *		 | 	validCharactersAfterCapitalLetter.contains(character)
	 */
	public static void setValidCharactersAfterCapitalLetter(char[] characters) {
		validCharactersAfterCapitalLetter.clear();
		for(char c='0';c<='9';c++) {
			validCharactersAfterCapitalLetter.add(c);
		}
		for(char c : characters) {
			validCharactersAfterCapitalLetter.add(c);
		}
	}


	
	/**
	 * Return the highest possible value for the coordinates of each end point of this
	 * road.
	 * 
	 * @return The highest possible value for the coordinates of each end point of this road is a lower value than 70.
	 *       | result <=70
	 */
	public static int getMaxDegrees() {
		return 70;
	}
	/**
	 * Return the lowest possible value for the coordinates of each end point of this
	 * road.
	 * 
	 * @return The lowest possible value for the coordinates of each end point of this road is a non-negative value.
	 *       | result >=0
	 */
	public static int getMinDegrees() {
		return 0;
	}
	

	/**
	 * Initialize this new road with given identification, first end point, second end point, length, speed limit and average speed, knowing that for the identification valid lengths are 2 and 3, and valid characters after the capital letter are digits.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  speedLimit
	 * 		   The new speed limit for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to the given speed limit.
	 * 		 | new.getSpeedLimit == speedLimit
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification.
	 * 		 | setIdentification(identification)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
	protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit, float averageSpeed) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification);
			setLength(length);
			setSpeedLimit(speedLimit);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}

	/**
	 * Initialize this new road with given identification, first end point, second end point, length and average speed, knowing that for the identification valid lengths are 2 and 3, and valid characters after the capital letter are digits.
	 * The speed limit is not explicit.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to 19.5 meters per second.
	 * 		 | new.getSpeedLimit == 19.5
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification.
	 * 		 | setIdentification(identification)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
	protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float averageSpeed) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification);
			setLength(length);
			setSpeedLimit(19.5F);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	} 
	
	/*/**
	 * Initialize this new road with given identification, first end point, second end point, length, speed limit and average speed, knowing that for the identification valid lengths are 2,3 and the given lengths, and valid characters after the capital letter are digits.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  speedLimit
	 * 		   The new speed limit for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @param  lengths
	 * 		   The allowed lengths for the identification of this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to the given speed limit.
	 * 		 | new.getSpeedLimit == speedLimit
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification, according to the given allowed lengths for the identification of this new road, excluding 2 and 3 that are also allowed.
	 * 		 | setIdentification(identification,lengths)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
     * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	/*protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit, float averageSpeed, int[] lengths) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification,lengths);
			setLength(length);
			setSpeedLimit(speedLimit);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}*/

	/*/**
	 * Initialize this new road with given identification, first end point, second end point, length and average speed, knowing that for the identification valid lengths are 2, 3 and the given lengths, and valid characters after the capital letter are digits.
	 * The speed limit is not explicit.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @param  lengths
	 * 		   The allowed lengths for the identification of this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to 19.5 meters per second.
	 * 		 | new.getSpeedLimit == 19.5
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification, according to the given allowed lengths for the identification of this new road, excluding 2 and 3 that are also allowed.
	 * 		 | setIdentification(identification,lengths)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	/*protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float averageSpeed, int[] lengths) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification,lengths);
			setLength(length);
			setSpeedLimit(19.5F);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}*/ 
	
	/*/**
	 * Initialize this new road with given identification, first end point, second end point, length, speed limit and average speed, knowing that for the identification valid lengths are 2 and 3, and valid characters after the capital letter are digits and the given characters.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  speedLimit
	 * 		   The new speed limit for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @param  characters
	 * 		   The allowed characters after the capital letter of the identification of this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to the given speed limit.
	 * 		 | new.getSpeedLimit == speedLimit
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification, according to the given allowed characters after the capital letter for the identification of this new road, excluding digits that are also allowed.
	 * 		 | setIdentification(identification,characters)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
     * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	/*protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit, float averageSpeed, char[] characters) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification,characters);
			setLength(length);
			setSpeedLimit(speedLimit);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}*/

	/*/**
	 * Initialize this new road with given identification, first end point, second end point, length and average speed, knowing that for the identification valid lengths are 2 and 3, and valid characters after the capital letter are digits and the given characters.
	 * The speed limit is not explicit.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @param  characters
	 * 		   The allowed characters after the capital letter of the identification of this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to 19.5 meters per second.
	 * 		 | new.getSpeedLimit == 19.5
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification, according to the given allowed characters after the capital letter for the identification of this new road, excluding digits that are also allowed.
	 * 		 | setIdentification(identification,characters)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	/*protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float averageSpeed, char[] characters) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification,characters);
			setLength(length);
			setSpeedLimit(19.5F);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}*/
	
	/*/**
	 * Initialize this new road with given identification, first end point, second end point, length, speed limit and average speed, knowing that for the identification valid lengths are 2,3 and the given lengths, and valid characters after the capital letter are digits and the given characters.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  speedLimit
	 * 		   The new speed limit for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @param  lengths
	 * 		   The allowed lengths for the identification of this new road.
	 * @param  characters
	 * 		   The allowed characters after the capital letter of the identification of this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to the given speed limit.
	 * 		 | new.getSpeedLimit == speedLimit
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification, according to the given allowed lengths and characters after the capital letter for the identification of this new road, excluding 2 and 3 for lengths and digits for characters that are also allowed.
	 * 		 | setIdentification(identification,lengths,characters)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
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
     * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	/*protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit, float averageSpeed, int[] lengths, char[] characters) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification,lengths,characters);
			setLength(length);
			setSpeedLimit(speedLimit);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}*/

	/*/**
	 * Initialize this new road with given identification, first end point, second end point, length and average speed, knowing that for the identification valid lengths are 2,3 and the given lengths, and valid characters after the capital letter are digits and the given characters.
	 * The speed limit is not explicit.
	 * 
	 * @param  identification
	 * 		   The new identification for this new road.
	 * @param  endPoint1
	 *         The new first end point for this new road.
	 * @param  endPoint2
	 *         The new second end point for this new road.
	 * @param  length
	 * 		   The new length for this new road.
	 * @param  averageSpeed
	 * 		   The new average speed for this new road.
	 * @param  lengths
	 * 		   The allowed lengths for the identification of this new road.
	 * @param  characters
	 * 		   The allowed characters after the capital letter of the identification of this new road.
	 * @pre    The given first end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint1)
	 * @pre    The given second end point must be a valid end point for a
	 *         road.
	 *       | isValidendPoint(endPoint2)
	 * @post   The identification of this new road is equal to the given identification.
	 * 		 | new.getIdentification == identification	
	 * @post   The first end point of this new road is equal to the given first
	 *         end point.
	 *       | new.getEndPoint1 == new double[] {endPoint1[0],endPoint1[1]}
	 * @post   The second end point of this new road is equal to the given second
	 *         end point.
	 *       | new.getEndPoint2 == new double[] {endPoint2[0],endPoint2[1]}
	 * @post   The length of this new road is equal to the given length.
	 * 		 | new.getLength == length
	 * @post   The speed limit of this new road is equal to 19.5 meters per second.
	 * 		 | new.getSpeedLimit == 19.5
	 * @post   The average speed of this new road is equal to the given average speed.
	 * 		 | new.getAverageSpeed == averageSpeed
	 * @post   The given identification is added to the list of all identifications that are created.
	 * 		 | allIdentificationsCreated.contains(identification)==true
	 * @post   The given identification, once created, must be unique among the created identifications.
	 * 		 | Collections.frequency(allIdentificationsCreated,identification)==1
	 * @effect The identification of this new road is set to the given identification, according to the given allowed lengths and characters after the capital letter for the identification of this new road, excluding 2 and 3 for lengths and digits for characters that are also allowed.
	 * 		 | setIdentification(identification,lengths,characters)
	 * @effect The length of this new road is set according to the given length.
	 * 		 | setLength(length)
	 * @effect The speed limit of this new road is set to the given speed limit.
	 * 		 | setSpeedLimit(speedLimit)
	 * @effect The average speed of this new road is set to the given average speed.
	 * 		 | setAverageSpeed(averageSpeed)
	 * @effect The current delay on the direction from the first end point to the second end point is set to 0 seconds.
	 * 		 | setCurrentDelayForth(0F)
	 * @effect This road is set to the non-blocked state in the direction from its first end point to its second end point.
	 * 		 | setBlockedForth(false)
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   Either the given identification is equal to one of the previous created identifications.
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
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 */
	/*protected Road(String identification, double[] endPoint1, double[] endPoint2, int length, float averageSpeed, int[] lengths, char[] characters) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		assert isValidEndPoint(endPoint1);
		this.endPoint1=endPoint1;
		assert isValidEndPoint(endPoint2);
		this.endPoint2=endPoint2;
		if(!allIdentificationsCreated.contains(identification)) {
			setIdentification(identification,lengths,characters);
			setLength(length);
			setSpeedLimit(19.5F);
			setAverageSpeed(averageSpeed);
			setCurrentDelayForth(0F);
			setBlockedForth(false);
			allIdentificationsCreated.add(identification);
		}
		else {
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		}
	}*/
	
	/**
	 * Return the identification of this road.
	 */
	@Basic @Raw
	public String getIdentification() {
		return this.identification;
	}
	
	/**
	 * Check whether the given identification is a valid identification for
	 * any road, knowing that only 2 or 3 can be the length of valid identifications,
	 * and that only digits can follow the capital letter.
	 *  
	 * @param  identification 
	 *         The identification to check.
	 * @return False if the identification length is not a valid length.
	 *       | if (!validIdentificationLengths.contains(identification.length()))
	 *       | 	then result == false
	 * @return False if the identification length is valid but the first character is not a capital letter.  
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)<'A' || identification.charAt(0)>'Z'))
	 * 		 |	then result == false
	 * @return False if the identification length is valid, the first character is a capital letter, but the at least one of the next following characters is not a valid character.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( at least one character of identification is not contained in validCharactersAfterCapitalLetter )
	 * 		 | 	then result == false
	 * @return True if the identification length is valid, the first character is a capital letter, and the next following characters are valid characters.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( for each position i from 1 to identification.length validCharactersAfterCapitalLetter.contains(identification.charAt(i)) ) 
	 * 		 | 	then result == true
	 * @effect The list of valid lengths for identification is set to the allowed identification's lengths.
	 * 		 | setValidIdentificationLengths()
	 * @effect The list of valid characters after the capital letter is set to the allowed characters.
	 * 		 | setValidCharactersAfterCapitalLetter()
	*/

  public static boolean isValidIdentification(String identification) {
		setValidIdentificationLengths();
		setValidCharactersAfterCapitalLetter(); 
		if(validIdentificationLengths.contains(identification.length())==true) {
			if(identification.charAt(0)>='A' && identification.charAt(0)<='Z') {
				for(int i=1;i<identification.length();i++) {
					if(!validCharactersAfterCapitalLetter.contains(identification.charAt(i))) {
						return false;
					}
				}
			return true;
			}
			return false;
			
		}
		return false;
	}

    
	/*/**
	 * Check whether the given identification is a valid identification for
	 * any road, according to the given lengths that will be allowed excluding 2 and 3 that are also allowed.
	 * Valid characters after the capital letter are still digits only.
	 *  
	 * @param  identification 
	 *         The identification to check.
     * @param lengths   
     *		  The possible lengths of identification we want to allow, excluding 2 and 3 that are also allowed.
	 * @return False if the identification length is not a valid length.
	 *       | if (!validIdentificationLengths.contains(identification.length()))
	 *       | 	then result == false
	 * @return False if the identification length is valid but the first character is not a capital letter.  
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)<'A' || identification.charAt(0)>'Z'))
	 * 		 |	then result == false
	 * @return False if the identification length is valid, the first character is a capital letter, but the at least one of the next following characters is not a valid character.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( at least one character of identification is not contained in validCharactersAfterCapitalLetter )
	 * 		 | 	then result == false
	 * @return True if the identification length is valid, the first character is a capital letter, and the next following characters are valid characters.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( for each position i from 1 to identification.length validCharactersAfterCapitalLetter.contains(identification.charAt(i)) ) 
	 * 		 | 	then result == true
	 * @effect The list of valid lengths for identification is set to the allowed identification's lengths.
	 * 		 | setValidIdentificationLengths(lengths)
	 * @effect The list of valid characters after the capital letter is set to the allowed characters.
	 * 		 | setValidCharactersAfterCapitalLetter()
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	*/

  /*public static boolean isValidIdentification(String identification, int[] lengths) throws IllegalLengthException {
		setValidIdentificationLengths(lengths);
		setValidCharactersAfterCapitalLetter(); 
		if(validIdentificationLengths.contains(identification.length())==true) {
			if(identification.charAt(0)>='A' && identification.charAt(0)<='Z') {
				for(int i=1;i<identification.length();i++) {
					if(!validCharactersAfterCapitalLetter.contains(identification.charAt(i))) {
						return false;
					}
				}
			return true;
			}
			return false;
			
		}
		return false;
	}*/	
	
	/*/**
	 * Check whether the given identification is a valid identification for
	 * any road, according to the given characters that will be allowed after the capital letter.
	 * Valid lengths are still 2 and 3 only.
	 *  
	 * @param  identification 
	 *         The identification to check.
     * @param characters
     *		  The characters we want to allow just after the capital letter in identification of this road, excluding the digits that are also allowed.
	 * @return False if the identification length is not a valid length.
	 *       | if (!validIdentificationLengths.contains(identification.length()))
	 *       | 	then result == false
	 * @return False if the identification length is valid but the first character is not a capital letter.  
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)<'A' || identification.charAt(0)>'Z'))
	 * 		 |	then result == false
	 * @return False if the identification length is valid, the first character is a capital letter, but the at least one of the next following characters is not a valid character.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( at least one character of identification is not contained in validCharactersAfterCapitalLetter )
	 * 		 | 	then result == false
	 * @return True if the identification length is valid, the first character is a capital letter, and the next following characters are valid characters.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( for each position i from 1 to identification.length validCharactersAfterCapitalLetter.contains(identification.charAt(i)) ) 
	 * 		 | 	then result == true
	 * @effect The list of valid lengths for identification is set to the allowed identification's lengths.
	 * 		 | setValidIdentificationLengths()
	 * @effect The list of valid characters after the capital letter is set to the allowed characters.
	 * 		 | setValidCharactersAfterCapitalLetter(characters)
	*/

  /*public static boolean isValidIdentification(String identification, char[] characters) {
		setValidIdentificationLengths();
		setValidCharactersAfterCapitalLetter(characters);
		if(validIdentificationLengths.contains(identification.length())==true) {
			if(identification.charAt(0)>='A' && identification.charAt(0)<='Z') {
				for(int i=1;i<identification.length();i++) {
					if(!validCharactersAfterCapitalLetter.contains(identification.charAt(i))) {
						return false;
					}
				}
			return true;
			}
			return false;
			
		}
		return false;
	}*/
	
	/*/**
	 * Check whether the given identification is a valid identification for
	 * any road, according to the given valid lengths excluding 2 and 3 that are also allowed, 
	 * and to the given valid characters excluding the digits that are also allowed.
	 * @param  identification 
	 *         The identification to check.
     * @param lengths   
     *		  The possible lengths of identification we want to allow, excluding 2 and 3 that are also allowed.
     * @param characters
     *		  The characters we want to allow just after the capital letter in identification of this road, excluding the digits that are also allowed.
	 * @return False if the identification length is not a valid length.
	 *       | if (!validIdentificationLengths.contains(identification.length()))
	 *       | 	then result == false
	 * @return False if the identification length is valid but the first character is not a capital letter.  
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)<'A' || identification.charAt(0)>'Z'))
	 * 		 |	then result == false
	 * @return False if the identification length is valid, the first character is a capital letter, but the at least one of the next following characters is not a valid character.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( at least one character of identification is not contained in validCharactersAfterCapitalLetter )
	 * 		 | 	then result == false
	 * @return True if the identification length is valid, the first character is a capital letter, and the next following characters are valid characters.
	 * 		 | if (!validIdentificationLengths.contains(identification.length()) && (identification.charAt(0)>='A' || identification.charAt(0)<='Z') && ( for each position i from 1 to identification.length validCharactersAfterCapitalLetter.contains(identification.charAt(i)) ) 
	 * 		 | 	then result == true
	 * @effect The list of valid lengths for identification is set to the allowed identification's lengths.
	 * 		 | setValidIdentificationLengths(lengths)
	 * @effect The list of valid characters after the capital letter is set to the allowed characters.
	 * 		 | setValidCharactersAfterCapitalLetter(characters)
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	*/

  /*public static boolean isValidIdentification(String identification, int[] lengths, char[] characters) throws IllegalLengthException{
		setValidIdentificationLengths(lengths);
		setValidCharactersAfterCapitalLetter(characters);
		if(validIdentificationLengths.contains(identification.length())==true) {
			if(identification.charAt(0)>='A' && identification.charAt(0)<='Z') {
				for(int i=1;i<identification.length();i++) {
					if(!validCharactersAfterCapitalLetter.contains(identification.charAt(i))) {
						return false;
					}
				}
			return true;
			}
			return false;
			
		}
		return false;
	}*/
	

	
	/**
	 * Set the identification of this road to the given identification, knowing that the given identification's length has to be equal to 2 or 3,
	 * and that the characters after the capital letter must be digits.
	 * 
	 * @param  identification
	 *         The new identification for this road.
	 * @post   The old identification, if existing, is removed from the list of identifications that have been created.
	 * 		 | !allIdentificationsCreated.contains(this.getIdentification())
	 * @post   The identification of this new road is equal to
	 *         the given identification.
	 *       | new.getIdentification() == identification
	 * @throws IllegalStructureException
	 *         The given identification is not a valid identification for any
	 *         road.
	 *       | ! isValidIdentification(getIdentification())
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   The given identification already belongs to a road.
	 * 		 | allIdentificationsCreated.contains(identification)
	 */
	@Raw
	public void setIdentification(String identification) 
			throws IllegalStructureException, IllegalAllIdentificationsCreatedException {
		if (! isValidIdentification(identification))
			throw new IllegalStructureException(identification);
		if(allIdentificationsCreated.contains(identification))
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		allIdentificationsCreated.remove(this.identification);
		this.identification = identification;
	}

	/*/**
	 * Set the identification of this road to the given identification, knowing that the given identification's length has to be equal to 2,3 or to one of the given lengths,
	 * and that the characters after the capital letter must be digits.
	 * 
	 * @param  identification
	 *         The new identification for this road.
	 * @param  lengths
	 * 		   The lengths that are allowed for the new identification, excluding 2 and 3 that are also allowed.
	 * @post   The old identification, if existing, is removed from the list of identifications that have been created.
	 * 		 | !allIdentificationsCreated.contains(this.getIdentification())
	 * @post   The identification of this new road is equal to
	 *         the given identification.
	 *       | new.getIdentification() == identification
	 * @throws IllegalStructureException
	 *         The given identification is not a valid identification for any
	 *         road.
	 *       | ! isValidIdentification(getIdentification())
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   The given identification already belongs to a road.
	 * 		 | allIdentificationsCreated.contains(identification)
	 */
	/*@Raw
	public void setIdentification(String identification, int[] lengths) 
			throws IllegalStructureException, IllegalLengthException, IllegalAllIdentificationsCreatedException{
		if (! isValidIdentification(identification, lengths))
			throw new IllegalStructureException(identification);
		if(allIdentificationsCreated.contains(identification))
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		allIdentificationsCreated.remove(this.identification);
		this.identification = identification;
	}*/

	/*/**
	 * Set the identification of this road to the given identification, knowing that the given identification's length has to be equal to 2 or 3,
	 * and that the characters after the capital letter must be digits or must belong to the given characters.
	 * 
	 * @param  identification
	 *         The new identification for this road.
	 * @param  characters
	 *         The characters that are allowed for the new identification after the capital letter, excluding the digits that are also allowed.
	 * @post   The old identification, if existing, is removed from the list of identifications that have been created.
	 * 		 | !allIdentificationsCreated.contains(this.getIdentification())
	 * @post   The identification of this new road is equal to
	 *         the given identification.
	 *       | new.getIdentification() == identification
	 * @throws IllegalStructureException
	 *         The given identification is not a valid identification for any
	 *         road.
	 *       | ! isValidIdentification(getIdentification())
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   The given identification already belongs to a road.
	 * 		 | allIdentificationsCreated.contains(identification)
	 */
	/*@Raw
	public void setIdentification(String identification, char[] characters) 
			throws IllegalStructureException, IllegalAllIdentificationsCreatedException {
		if (! isValidIdentification(identification, characters))
			throw new IllegalStructureException(identification);
		if(allIdentificationsCreated.contains(identification))
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		allIdentificationsCreated.remove(this.identification);
		this.identification = identification;
	}*/

	/*/**
	 * Set the identification of this road to the given identification, knowing that the given identification's length has to be equal to 2,3 or to one of the given lengths,
	 * and that the characters after the capital letter must be digits or must belong the given characters.
	 * 
	 * @param  identification
	 *         The new identification for this road.
	 * @param  lengths
	 * 		   The lengths that are allowed for the new identification, excluding 2 and 3 that are also allowed.
	 * @param  characters
	 *         The characters that are allowed for the new identification after the capital letter, excluding the digits that are also allowed.
	 * @post   The old identification, if existing, is removed from the list of identifications that have been created.
	 * 		 | !allIdentificationsCreated.contains(this.getIdentification())
	 * @post   The identification of this new road is equal to
	 *         the given identification.
	 *       | new.getIdentification() == identification
	 * @throws IllegalStructureException
	 *         The given identification is not a valid identification for any
	 *         road.
	 *       | ! isValidIdentification(getIdentification())
	 * @throws IllegalLengthException
	 * 		   At least one of the given lengths is non-positive or is not smaller than Java's largest value of type int.
	 *       | !canHaveAsLength(length) for at least one length in lengths.
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   The given identification already belongs to a road.
	 * 		 | allIdentificationsCreated.contains(identification)
	 */
	/*@Raw
	public void setIdentification(String identification, int[] lengths, char[] characters) 
			throws IllegalStructureException, IllegalLengthException, IllegalAllIdentificationsCreatedException {
		if (! isValidIdentification(identification, lengths, characters))
			throw new IllegalStructureException(identification);
		if(allIdentificationsCreated.contains(identification))
			throw new IllegalAllIdentificationsCreatedException(allIdentificationsCreated);
		allIdentificationsCreated.remove(this.identification);
		this.identification = identification;
	}*/
	
	
	/**
	 * Return the first end point of this road.
	 */
	@Basic @Raw
	public double[] getEndPoint1() {
		return this.endPoint1;
	}
	/**
	 * Return the second end point of this road.
	 */
	@Basic @Raw
	public double[] getEndPoint2() {
		return this.endPoint2;
	}
	
	/**
	 * Return both end points of this road.
	 */
	@Basic @Raw
	public double[][] getEndPoints(){
		return new double[][] {getEndPoint1(),getEndPoint2()};
	}
	
	/**
	 * Check whether the given end point is a valid end point for
	 * any road.
	 *  
	 * @param  endPoint
	 *         The end point to check.
	 * @return True if and only if the length of the given end point is 2 and if the given latitude and longitude of the given end point are both between 0 and the maximum of degrees.
	 *       | result == (endPoint.length==2) && (endPoint[0] >= getMinDegrees()) && (endPoint[0]<= getMaxDegrees()) && (endPoint[1] >= getMinDegrees()) && (endPoint[1]<= getMaxDegrees())
	*/
	public static boolean isValidEndPoint(double[] endPoint) {
		return (endPoint.length==2) && (endPoint[0] >= getMinDegrees()) && (endPoint[0]<= getMaxDegrees()) && (endPoint[1] >= getMinDegrees()) && (endPoint[1]<= getMaxDegrees());
	}
	
	
	
	/**
	 * Return the length of this road.
	 */
	@Basic @Raw @Immutable
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Check whether this road can have the given length as its length.
	 *  
	 * @param  length
	 *         The length to check.
	 * @return True if and only if the given length is strictly positive and smaller than Java's largest value of type int.
	 *       | result == (length>0 && length<Integer.MAX_VALUE)
	*/
	@Raw
	public static boolean canHaveAsLength(int length) {
		return length>0 && length<Integer.MAX_VALUE;
	}
	
	/**
	 * Set the length for road according to the given length.
	 *
	 * @param	length
	 *			The new length for this road.
	 * @post    If the given length is between 0 excluded and the Java's largest value of type int excluded, the length of this road is equal to the given length.
	 *        | if(length>0 && length<Integer.MAX_VALUE)
	 *        |   then new.getLength()==length
	 * @post   If the given length is between Integer.MIN_VALUE+2 included and 0 excluded, the length of this road is equal to the absolute value of the given length.
	 *        | if (length <0 && length>=Integer.MIN_VALUE+2)
	 *        |     then new.getLength() == Math.abs(length)
	 * @post   If the given length is equal to 0, the length of this road is equal to 1.
	 * 		  | if(length==0)
	 * 		  | 	then new.getLength() == 1
	 * @post   If the given length is greater or equal than Integer.MAX_VALUE or lower or equal than Integer.MIN_VALUE+1, the length of this road is equal to Integer.MAX_VALUE-1 (the maximum positive number allowed).
	 * 		  |	else
	 * 		  | 	then new.getLength() == Integer.MAX_VALUE-1
	 */
	public void setLength(int length) {
		if(canHaveAsLength(length)==true) {
			this.length=length;
		}
		else if(length<0 && length>=Integer.MIN_VALUE+2) {
			this.length=Math.abs(length);
		}
		else if(length==0) {
			this.length=1;
		}
		else {
			this.length=Integer.MAX_VALUE-1;
		}
	}
	
	
	/**
	 * Return the speed limit of this road.
	 */
	@Basic @Raw
	public float getSpeedLimit() {
		return this.speedLimit;
	}
	
	/**
	 * Check whether the given speed limit is a valid speed limit for
	 * any road.
	 *  
	 * @param  speed limit
	 *         The speed limit to check.
	 * @return 
	 *       | result == speedLimit>0 && speedLimit<=SPEED_OF_LIGHT
	*/
	public static boolean isValidSpeedLimit(float speedLimit) {	
		return (speedLimit>0 && speedLimit<=SPEED_OF_LIGHT);
	}
	
	/**
	 * Set the speed limit of this road to the given speed limit.
	 * 
	 * @param  speedLimit
	 *         The new speed limit for this road.
	 * @post   The speed limit of this new road is equal to
	 *         the given speed limit.
	 *       | new.getSpeedLimit() == speedLimit
	 * @throws IllegalSpeedLimitException
	 *         The given speed limit is not a valid speed limit for any
	 *         road.
	 *       | ! isValidSpeedLimit(getSpeedLimit())
	 * @throws IllegalAverageSpeedException
	 * 		   The given speed limit is not a valid speed limit for any road that has a greater average speed.
	 * 		 | ! isValidAverageSpeed(getAverageSpeed())
	 */
	@Raw
	public void setSpeedLimit(float speedLimit) 
			throws IllegalSpeedLimitException, IllegalAverageSpeedException {
		if (! isValidSpeedLimit(speedLimit))
			throw new IllegalSpeedLimitException(speedLimit);
		if (! isValidAverageSpeed(this.averageSpeed, speedLimit))
			throw new IllegalAverageSpeedException(speedLimit);
		this.speedLimit = speedLimit;
	}
	
	
	/**
	 * Return the average speed of this road.
	 */
	@Basic @Raw
	public float getAverageSpeed() {
		return this.averageSpeed;
	}
	
	/**
	 * Check whether the given average speed is a valid average speed for
	 * any road.
	 *  
	 * @param  average speed
	 *         The average speed to check.
	 * @return 
	 *       | result == averageSpeed>=0 && averageSpeed<=speedLimit
	*/
	public static boolean isValidAverageSpeed(float averageSpeed, float speedLimit) {
		return averageSpeed>=0 && averageSpeed<=speedLimit;
	}
	
	/**
	 * Set the average speed of this road to the given average speed.
	 * 
	 * @param  averageSpeed
	 *         The new average speed for this road.
	 * @post   The average speed of this new road is equal to
	 *         the given average speed.
	 *       | new.getAverageSpeed() == averageSpeed
	 * @throws IllegalAverageSpeedException
	 *         The given average speed is not a valid average speed for any
	 *         road.
	 *       | ! isValidAverageSpeed(getAverageSpeed())
	 */
	@Raw
	public void setAverageSpeed(float averageSpeed) 
			throws IllegalAverageSpeedException {
		if (! isValidAverageSpeed(averageSpeed,getSpeedLimit()))
			throw new IllegalAverageSpeedException(averageSpeed);
		this.averageSpeed = averageSpeed;
	}



	/**
	 * Return the current delay on the direction from the first end point to the second end point of this road.
	 */
	@Basic @Raw
	public float getCurrentDelayForth() {
		return currentDelayForth;
	}
	
	/**
	 * Return the current delay on the direction from the second end point to the first end point of this road.
	 */
	@Basic @Raw
	public abstract float getCurrentDelayOpposite();
	
	/**
	 * Check whether the given current delay is a valid current delay for
	 * any road.
	 *  
	 * @param  current delay
	 *         The current delay to check.
	 * @return True if and only if the given currentDelay is non-negative.
	 *       | result == (currentDelay>=0)
	*/
	public static boolean isValidCurrentDelay(float currentDelay) {
		return currentDelay>=0;
	}
	
	/**
	 * Set the current delay on the direction from the first end point to the second end point of this road to the given current delay.
	 * 
	 * @param  currentDelay
	 *         The new current delay on the direction from the first end point to the second end point for this road.
	 * @pre    The given current delay must be a valid current delay on the direction from the first end point to the second end point for any
	 *         road.
	 *       | isValidCurrentDelay(currentDelay)
	 * @post   The current delay on the direction from the first end point to the second end point of this road is equal to the given
	 *         current delay.
	 *       | new.getCurrentDelayForth() == currentDelay
	 */
	@Raw
	public void setCurrentDelayForth(float currentDelay) {
		assert isValidCurrentDelay(currentDelay);
		this.currentDelayForth = currentDelay;
	}
	
	/**
	 * Set the current delay on the direction from the second end point to the first end point of this road to the given current delay.
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
	@Raw
	public abstract void setCurrentDelayOpposite(float currentDelay);
	
	/**
	 * Return the current state (blocked or not) of the direction from the first end point to the second end point of this road.
	 */
	@Basic @Raw
	public boolean getBlockedForth() {
		return blockedForth;
	}

	/**
	 * Return the current state (blocked or not) of the direction from the second end point to the first end point of this road.
	 */
	@Basic @Raw
	public abstract boolean getBlockedOpposite();
	
	/**
	 * Set the direction from the first end point to the second end point of this road to a blocked direction.
	 * 
	 * @param  flag
	 * 		 | A flag indicating that the direction from the first end point to the second end point is blocked or not.
	 * @post   The current state of the direction from the first end point to the second end point of this road is equal to true.
	 *       | new.getBlockedForth() == true
	 */
	public void setBlockedForth(boolean flag) {
		this.blockedForth=flag;
	}
	
	/**
	 * Set the direction from the second end point to the first end point of this road to a blocked direction.
	 * 
	 * @param  flag
	 * 		 | A flag indicating that the direction from the second end point to the first end point is blocked or not.
	 * @post   The current state of the direction from the second end point to the first end point of this road is equal to true.
	 *       | new.getBlockedOpposite() == true
	 */
	public abstract void setBlockedOpposite(boolean flag);
	
	/**
	 * Terminate this road.
	 * 
	 * @post   This road is terminated.
	 *       | new.isTerminated()
	 * @post   This road is no longer blocked.
	 *       | ! new.isBlocked()
	 * @post   This road no longer references an effective route.
	 *       | new.getRoute() == null
	 * @post   If this road was not yet terminated, this road
	 *         is no longer one of the roads for the route to which
	 *         this road belonged.
	 *       | if (! isTerminated())
	 *       |   then ! (new getRoute()).hasAsRoad(this))
	 * @post   If this road was not yet terminated, the number of
	 *         roads of the routes involved in this road is 
	 *         decremented by 1.
	 *       | if (! isTerminated())
	 *       |   then for(Route route : getRoutes())
	 *       |  	(new getRoutes()).getNbSegments() ==
	 *       |            getRoutes().getNbRoads() - 1
	 * @post   If this road was not yet terminated, all roads
	 *         of the routes involved in this road registered at an
	 *         index beyond the index at which this road was registered,
	 *         are shifted one position to the left.
	 *       | for (Route route  : getRoutes())
	 *       | 	for each I,J in 1..route.getNbSegments():
	 *       |   	if ( route.getSegmentAt(I) == road) and (I < J) )
	 *       |     		then (new route).getSegmentAt(J-1) == route.getSegmentAt(J)
	 * @post   The length of this road is fixed to 1.
	 * 		 | new.getLength()==1
	 * @post   The average speed and speed limit are fixed to 0.1
	 *       | new.getAverageSpeed()==0.1 && new.getSpeedLimit==0.1
	 * @post   The identification of this road can be re-used.
	 * 		 | !allIdentificationsCreated.contains(identification)
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
	@Override
	public void terminate() throws IllegalStructureException, IllegalAllIdentificationsCreatedException, IllegalAverageSpeedException, IllegalSpeedLimitException {
		if (!isTerminated()) {
			this.isTerminated=true;
			Set<Route> oldRoutes = getRoutes();
			this.routes.clear();
			if(oldRoutes.size()!=0) {
				for (Route route : oldRoutes) {
					while(route.getSegments().contains(this)) {
						route.removeSegment(this);
					}
				}
			}
			allIdentificationsCreated.remove(this.identification);
			setLength(1);
			setAverageSpeed(0.1F);
			setSpeedLimit(0.1F);
		}
	}
	
	/**
	 * Check whether this road is already terminated.
	 */
	@Raw
	public boolean isTerminated () {
		return this.isTerminated;
	}
	
	
	/**
	 * Return the routes to which this road belongs.
	 */
	@Basic @Raw
	public Set<Route> getRoutes() {
		return this.routes;
	}

	/**
	 * Check whether this road can have the given route as one of
	 * its routes.
	 * 
	 * @param  route
	 * 		   The route to check.
	 * @return If this road is terminated, true if and only if the
	 *         given route is not effective.
	 *       | if (this.isTerminated())
	 *       |   then result == (route == null)
	 * @return If this road is not terminated, true if and only if the given
	 *         route is effective and not yet terminated.
	 *       | if (! this.isTerminated())
	 *       |   then result ==
	 *       |     (route != null) &&
	 *       |     (! route.isTerminated())
	 */
	@Raw
	public boolean canHaveAsRoute(Route route) {
		if (this.isTerminated())
			return route == null;
		return (route != null)
				&& (!route.isTerminated());
	}

	/**
	 * Check whether this road has proper routes.
	 * 
	 * @return True if and only if there are no routes for this road,
	 * 		   or if this road can have its routes as its
	 *         route and they all have this road as one of their roads.
	 *       | result ==
	 *       |   (getRoutes().size()==0 || 
	 *       |   (for each route in routes     		
	 *       |   	canHaveAsRoute(route)) && route.hasAsSegment(this))
	 *       
	 */
	@Raw
	public boolean hasProperRoutes() {
		if(getRoutes().size()==0)
			return true;
		for(Route route : routes) {
			if(!canHaveAsRoute(route))
				return false;
			if(!route.hasAsSegment(this))
				return false;
		}
		return true;
	}

	
	 /**
     * Check whether this road has the given route as one
     * of its routes.
     * 
     * @param	route
     * 			The route to check.
     * @return	True if and only if the given route is effective and that the routes of this road contain the given route.
     * @throws	NullPointerException
     * 			The given route is not effective.
     * 			| route == null
     */
    @Basic @Raw
    public boolean hasAsRoute(@Raw
    Route route) {
        try {
            return routes.contains(route);
        }
        catch (NullPointerException exc) {
            assert (route == null);
            return false;
        }
    }

    
    /**
     * Return the number of routes of this road.
     * 
     * @return  The total number of routes collected in this road.
     *        | result ==
     *        |   card({route:routes | hasAsRoute(route)})
     */
    public int getNbRoutes() {
        return routes.size();
    }

	/**
	 * Add the given route to the routes of this road.
	 * 
	 * @param  route
	 *         The new route to add to the routes of this road.
	 * @post   The given route is registered as one of the
     *         routes for this road.
     *       | new.hasAsRoad(route)
	 * @throws IllegalArgumentException
	 *         This road cannot have the given route as one of its routes.
	 *       | ! canHaveAsRoute(route)
     * @post    The size of the routes of this road is incremented by 1.
     * 	 	 | new.getNbRoutes()==getNbRoutes()+1  
	 */
	@Raw
	void addRoute(Route route) {
		if (!canHaveAsRoute(route))
			throw new IllegalArgumentException("Inappropriate route!");
		this.routes.add(route);
	}

    /**
     * Remove the given route from the routes of this road.
     * 
     * @param   route
     *          The route to be removed.
     * @pre     The given route is registered as one of the routes
     *          for this road.
     *        | hasAsRoute(route)
     * @pre     The given route is not referencing this road as one of its segments.
     * 		  | !route.getSegments().contains(this)
     * @post    This road no longer has the given route as one
     *          of its routes.
     *        | ! new.hasAsRoute(route)
     * @post    The size of the routes of this road is decremented by 1.
     * 	 	  | new.getNbRoutes()==getNbRoutes()-1
     */
    void removeRoute(@Raw
    Route route) {
        assert hasAsRoute(route);
        assert !route.getSegments().contains(this);
        routes.remove(route);
    }
    
    /**
     * Replace the given old route in the sequence of routes of this road
     * by the given new route.
     * 
     * @param	oldRoute
     * 		    The route to be replaced in the routes of this road.
     * @param   newRoute
     * 		    The route to be added in the routes of this road instead of the old route.
     * @effect  The old route is removed from the routes of this road.
     * 		  | removeRoute(oldRoute)
     * @effect  The new route is added to the routes of this road.
     * 		  | addRoute(newRoute)
     */
    void changeRoute(@Raw
    Route oldRoute, Route newRoute) {
    	removeRoute(oldRoute);
    	addRoute(newRoute);
    }
    
	/**
	 * Return the valid start locations of this road.
	 */
	@Basic @Raw
	public ArrayList<double[]> getValidStartLocations() {
		return this.validStartLocations;
	}
	
	/**
	 * Return the valid end locations of this road.
	 */
	@Basic @Raw
	public ArrayList<double[]> getValidEndLocations() {
		return this.validEndLocations;
	}
	
	/**
	 * Add the given start location to the set of valid start locations of this road.
	 * 
	 * @param  startLocation
	 *         The new start location to add to the set of valid start locations of this road.
	 * @pre    The given start location must be a valid end point for this road.
	 *       | isValidEndPoint(startLocation)
	 * @post   The given start location is registered as one of the
     *         set of valid start locations of this road.
     *       | new.validStartLocations.contains(route)
     * @post    The size of the set of valid start locations of this road is incremented by 1.
     * 	 	 | new.validStartLocations.size()==validStartLocations.size()+1  
	 */
	@Raw
    void addValidStartLocation(double[] startLocation) {
    	if(isValidEndPoint(startLocation))
    		validStartLocations.add(startLocation);
    	else {
    		assert false;
    	}
    }
	
	/**
	 * Add the given end location to the set of valid end locations of this road.
	 * 
	 * @param  endLocation
	 *         The new end location to add to the set of valid end locations of this road.
	 * @pre    The given end location must be a valid end point for this road.
	 *       | isValidEndPoint(endLocation)
	 * @post   The given end location is registered as one of the
     *         set of valid end locations of this road.
     *       | new.validEndLocations.contains(route)
     * @post    The size of the set of valid end locations of this road is incremented by 1.
     * 	 	 | new.validEndLocations.size()==validEndLocations.size()+1  
	 */
	@Raw
    void addValidEndLocation(double[] endLocation) {
    	if(isValidEndPoint(endLocation))
    		validEndLocations.add(endLocation);
    	else {
    		assert false;
    	}
    }
	
	/**
	 * Check whether this road has the given segment as
	 * one of its subsegments.
	 *
	 * @return  True if and only if the given segment is the same
	 *          segment as this road.
	 *        | result == (segment == this)
	 */
	@Override
	public final boolean hasAsSubSegment(Segment segment) {
		return segment == this;
	}
}
