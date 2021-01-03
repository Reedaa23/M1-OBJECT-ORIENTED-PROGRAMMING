package exceptions;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal speed limit of roads.
 * 
 */

public class IllegalSpeedLimitException extends Exception {
	/**
	 * Initialize this new illegal speedLimit exception with given float of speedLimit.
	 * 
	 * @param  speedLimit
	 *         The float of speedLimit for this new illegal speed limit exception.
	 * @post   The float of speedLimit of this new illegal speed limit exception is equal
	 *         to the given float of speedLimit.
	 *       | new.getspeedLimit() == speedLimit
	 */
	public IllegalSpeedLimitException(float speedLimit) {
		this.speedLimit = speedLimit;
	}

	/**
	 * Return the float of speedLimit registered for this illegal speed limit exception.
	 */
	@Basic @Immutable
	public float getspeedLimit() {
		return this.speedLimit;
	}

	/**
	 * Variable registering the float of speedLimit involved in this illegal speed limit
	 * exception.
	 */
	private final float speedLimit;

	/**
	 * The Java API strongly recommends to explicitly define a version
	 * number for classes that implement the interface Serializable.
	 */
	private static final long serialVersionUID = 2003001L;	
}
