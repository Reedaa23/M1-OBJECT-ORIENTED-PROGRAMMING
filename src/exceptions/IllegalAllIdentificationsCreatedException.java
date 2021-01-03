package exceptions;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import java.util.ArrayList;

/**
 * A class for signaling illegal list of identifications of roads that are new roads, i.e either identifications are not unique or the maximum of roads created is already reached.
 * 
 */
public class IllegalAllIdentificationsCreatedException extends Exception {
	/**
	 * Initialize this new illegal list of identifications exception with given list of identifications.
	 * 
	 * @param  allIdentificationsCreated
	 *         The list of strings of identifications for this new illegal list of identifications exception.
	 * @post   The list of strings of identifications of this new illegal list of identifications exception is equal
	 *         to the given list of identifications.
	 *       | new.getAllIdentificationsCreated() == allIdentificationsCreated
	 */
	public IllegalAllIdentificationsCreatedException(ArrayList<String> allIdentificationsCreated) {
		this.allIdentificationsCreated = allIdentificationsCreated;
	}

	/**
	 * Return the list of strings of identifications registered for this illegal list of identifications exception.
	 */
	@Basic @Immutable
	public ArrayList<String> getAllIdentificationsCreated() {
		return this.allIdentificationsCreated;
	}

	/**
	 * Variable registering the list of strings of identifications involved in this illegal list of identifications
	 * exception.
	 */
	private final ArrayList<String> allIdentificationsCreated;

	/**
	 * The Java API strongly recommends to explicitly define a version
	 * number for classes that implement the interface Serializable.
	 */
	private static final long serialVersionUID = 2003001L;

}
