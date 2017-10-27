package server;

import database.DBManager;
import interfaces.Trip;
import interfaces.User;
import java.util.List;

public class ServerTripHandler {

	public static void createTrip(Trip newTrip) {
		//Creates a list with all participants. That is both the normal participants and the instructors
		List<User> allParticipants = newTrip.getParticipants();
		allParticipants.addAll(newTrip.getInstructors());

		//Creates the group conversation
		int groupconversationId = createConversation(allParticipants);

		//TODO - the rest
	}

	private static int createConversation(List<User> users) {
		int conversationId = DBManager.getInstance().executeInsertAndGetId("INSERT INTO Conversation (conversationID) VALUES (DEFAULT);");
		
		//TODO - insert users into the conversation also
		return -1;
	}

	public static void deleteTrip(Trip trip) {
		String query = "DELETE FROM Trips WHERE tripID = " + trip.getId() + ";";

		DBManager.getInstance().executeUpdate(query);

	}
}
