package network.server.actions;

import network.NetworkItem;
import network.Server;

public class ActionFactory {

	public Action getAction(NetworkItem item, Server server, Object extra){
		Action action = null;
		Object networkObject = item.getNetworkItem();
		
		System.out.println("item.getNetworkItemType: " + item.getNetworkItemType().toString());
		
		switch (item.getNetworkItemType()){
		case AddContact:
			action = new AddContactAction(networkObject, server);
			break;
		case GetContact:
			action = new GetContactAction(networkObject, server);
			break;
		case GetFriends:
			action = new GetFriendsAction(networkObject, server);
			break;
		case Message:
			action = new MessageAction(networkObject, server);
			break;
		case MessagePortUpdate:
			action = new MessagePortUpdateAction(networkObject, server);
			break;
		case NetworkLoginInfo:
			action = new NetworkLoginInfoAction(networkObject, server, extra);
			break;
		case NullItem:
			action = new NullAction(networkObject, server);
			break;
		case TerminateConnection:
			action = new TerminateConnectionAction(networkObject, server, extra);
			break;
		case Unfriend:
			action = new UnfriendAction(networkObject, server);
			break;
		case GetStatus:
			action = new GetStatusAction(networkObject, server);
			break;
		case UpdateStatus:
			action = new UpdateStatusAction(networkObject, server);
			break;
		case GetQueuedMessages:
			action = new GetQueuedMessages(networkObject, server);
			break;
		default:
			action = new NullAction(networkObject, server);
			break;
		}
			
		return action;
	}
	
}
