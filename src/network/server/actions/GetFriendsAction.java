package network.server.actions;

import java.util.Collections;
import java.util.List;

import database.DBConnector;
import types.Contact;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
/**
 * Get friends list of contact by nickname
 * @author talsim
 * Input: 
 */
public class GetFriendsAction extends Action {

	public GetFriendsAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
	}

	@Override
	public void exectue() {
			//get input username
			String username = (String) networkObject;
			//get id of user
			Integer id = databaseConnector.getNickToIdMap().get(username);
			//get friends
			if(databaseConnector.getIdToFriends().get(id)!=null){
				List<Contact> contactList = databaseConnector.getIdToFriends().get(id);
				this.setResponse(new NetworkItem(NetworkItemType.GetFriends, contactList));
				//response = new NetworkItem(NetworkItemType.GetFriends, contactList);
			}else{
				//response = new NetworkItem(NetworkItemType.GetFriends, Collections.<Contact>emptyList());
				this.setResponse(new NetworkItem(NetworkItemType.GetFriends, Collections.<Contact>emptyList()));
			}
	}

}
