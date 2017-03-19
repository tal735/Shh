package network.server.actions;

import database.DBConnector;
import network.NetworkItem;

public class ActionFactory {

	private DBConnector databaseConnector;
	
	public ActionFactory(DBConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}

	public Action getAction(NetworkItem item, Object extra){
		Action action = null;
		Object networkObject = item.getNetworkItem();
		
		System.out.println("item.getNetworkItemType: " + item.getNetworkItemType().toString());
		
		switch (item.getNetworkItemType()){
		case AddContact:
			action = new AddContactAction(networkObject, databaseConnector);
			break;
		case GetContact:
			action = new GetContactAction(networkObject, databaseConnector);
			break;
		case GetFriends:
			action = new GetFriendsAction(networkObject, databaseConnector);
			break;
		case Message:
			action = new MessageAction(networkObject, databaseConnector);
			break;
		case MessagePortUpdate:
			action = new MessagePortUpdateAction(networkObject, databaseConnector);
			break;
		case NetworkLoginInfo:
			action = new NetworkLoginInfoAction(networkObject, databaseConnector, extra);
			break;
		case NullItem:
			action = new NullAction(networkObject, databaseConnector);
			break;
		case TerminateConnection:
			action = new TerminateConnectionAction(networkObject, databaseConnector, extra);
			break;
		case Unfriend:
			action = new UnfriendAction(networkObject, databaseConnector);
			break;
		case GetStatus:
			action = new GetStatusAction(networkObject, databaseConnector);
			break;
		case UpdateStatus:
			action = new UpdateStatusAction(networkObject, databaseConnector);
			break;
		case GetQueuedMessages:
			action = new GetQueuedMessages(networkObject, databaseConnector);
			break;
		default:
			action = new NullAction(networkObject, databaseConnector);
			break;
		}
			
		return action;
	}
	
}
