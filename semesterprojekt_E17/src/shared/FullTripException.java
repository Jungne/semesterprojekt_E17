package shared;

/**
 * FullTripException is thrown when a user tries to participate in a trip, that
 * has reached the maximum limit of participants.
 *
 * @author group 12
 */
public class FullTripException extends Exception {

	public FullTripException(String message) {
		super(message);
	}

}
