package network.client.actions;

import network.Client;
import misc.Constants.NetworkItemType;

public class ActionFactory {

	private Client client;
	public ActionFactory(Client client){
		this.client = client;
	}
	
	public Action getAction(NetworkItemType type, Object input){
		Action action = null;
		System.out.println("requesting action of type: " + type.toString());
		
		switch (type){
		case AddContact:
			action = new AddContactAction(input, client);
			break;
		case GetContact:
			action = new GetContactAction(input, client);
			break;
		case GetFriends:
			action = new GetFriendsAction(input, client);
			break;
		case Message:
			action = new MessageAction(input, client);
			break;
		case MessagePortUpdate:
			action = new MessagePortUpdateAction(input, client);
			break;
		case NetworkLoginInfo:
			action = new Autenticate(input, client);
			break;
		case NullItem:
			action = new NullAction(input, client);
			break;
		case TerminateConnection:
			action = new TerminateConnectionAction(input, client);
			break;
		case Unfriend:
			action = new UnfriendAction(input, client);
			break;
		case GetStatus:
			action = new GetStatusAction(input, client);
			break;
		case UpdateStatus:
			action = new UpdateStatusAction(input, client);
			break;
		case GetQueuedMessages:
			action = new GetQueuedMessagesAction(input, client);
			break;
		default:
			action = new NullAction(input, client);
			break;
		}
			
		return action;
	}	
}