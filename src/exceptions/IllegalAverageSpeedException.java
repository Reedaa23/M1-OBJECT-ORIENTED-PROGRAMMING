package exceptions;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal average speed of roads.
 * 
 */

public class IllegalAverageSpeedException extends Exception {
	/**
	 * Initialize this new illegal AverageSpeed exception with given float of AverageSpeed.
	 * 
	 * @param  AverageSpeed
	 *         The float of AverageSpeed for this new illegal average speed exception.
	 * @post   The float of AverageSpeed of this new illegal average speed exception is equal
	 *         to the given float of AverageSpeed.
	 *       | new.getAverageSpeed() == AverageSpeed
	 */
	public IllegalAverageSpeedException(float AverageSpeed) {
		this.AverageSpeed = AverageSpeed;
	}

	/**
	 * Return the float of AverageSpeed registered for this illegal average speed exception.
	 */
	@Basic @Immutable
	public float getAverageSpeed() {
		return this.AverageSpeed;
	}

	/**
	 * Variable registering the float of AverageSpeed involved in this illegal average speed
	 * exception.
	 */
	private final float AverageSpeed;

	/**
	 * The Java API strongly recommends to explicitly define a version
	 * number for classes that implement the interface Serializable.
	 */
	private static final long serialVersionUID = 2003001L;
	
}
