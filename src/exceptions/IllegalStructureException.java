package exceptions;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal structure for identifications of roads.
 * 
 */
public class IllegalStructureException extends Exception {
	/**
	 * Initialize this new illegal structure exception with given string of characters.
	 * 
	 * @param  characters
	 *         The string of characters for this new illegal structure exception.
	 * @post   The string of characters of this new illegal structure exception is equal
	 *         to the given string of characters.
	 *       | new.getCharacters() == characters
	 */
	public IllegalStructureException(String characters) {
		this.characters = characters;
	}

	/**
	 * Return the string of characters registered for this illegal structure exception.
	 */
	@Basic @Immutable
	public String getCharacters() {
		return this.characters;
	}

	/**
	 * Variable registering the string of characters involved in this illegal structure
	 * exception.
	 */
	private final String characters;

	/**
	 * The Java API strongly recommends to explicitly define a version
	 * number for classes that implement the interface Serializable.
	 */
	private static final long serialVersionUID = 2003001L;

}
