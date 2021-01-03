package exceptions;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal length for identifications of roads.
 * 
 */
public class IllegalLengthException extends Exception {
	/**
	 * Initialize this new illegal length exception with given int of length.
	 * 
	 * @param  length
	 *         The int of length for this new illegal length exception.
	 * @post   The int of length of this new illegal length exception is equal
	 *         to the given int of length.
	 *       | new.getLength() == length
	 */
	public IllegalLengthException(int length) {
		this.length = length;
	}

	/**
	 * Return the int of length registered for this illegal length exception.
	 */
	@Basic @Immutable
	public int getLength() {
		return this.length;
	}

	/**
	 * Variable registering the int of length involved in this illegal length
	 * exception.
	 */
	private final int length;

	/**
	 * The Java API strongly recommends to explicitly define a version
	 * number for classes that implement the interface Serializable.
	 */
	private static final long serialVersionUID = 2003001L;

}
