package shared;

/**
 * The IllegalEmailException is thrown when a user tries to create a new account
 * using an email that is not unique.
 *
 * @author group 12
 */
public class IllegalEmailException extends Exception {

	public IllegalEmailException(String message) {
		super(message);
	}

}
