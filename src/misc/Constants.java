package misc;


public class Constants {
	public static String ERROR_OOPS = "OOPS! Something happened... Try again later";
	public static String ERROR_BAD_USERPASS = "You forgot to fill username and/or password.";
	public static String ERROR_AUTHENTICATION = "Invalid Username / Password. Try again.";
	public static String ERROR_EMPTY_CONTACT_TEXTFIELD = "Please enter a name first";
	public static int APPLICATION_NETWORK_PORT = 4456;
	public static String APP_SERVER_HOSTNAME = "talsim02";
	
	public static enum NetworkItemType{
		NetworkLoginInfo, 		//credentials login
		Message, 				//transfer message
		//Id,					//get id of contact (name)
		NullItem, 				//for error handling
		AddContact,				//add a friend to a user
		TerminateConnection, 	//signal that user sends to indicate he quits the app
		GetFriends,				//get friends of contact
		Unfriend,				//remove friend
		GetContact,				//get specific contact
		MessagePortUpdate, 		//update port of client that listens to incoming messages
		GetStatus,				//get availability status (online/offline) for user id(s)
		UpdateStatus,			//update status at server and notify other contacts
		GetQueuedMessages		//get messages that were sent to you while you were offline
	}
}
