package network.server.actions;

import java.util.Collections;
import java.util.List;

import types.Contact;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Server;
/**
 * Get friends list of contact by nickname
 * @author talsim
 * Input: 
 */
public class GetFriendsAction extends Action {

	public GetFriendsAction(Object netowrkObject, Server server) {
		super(netowrkObject, server);
	}

	@Override
	public void exectue() {
			//get input username
			String username = (String) networkObject;
			//get id of user
			Integer id = server.getNickToIdMap().get(username);
			//get friends
			if(server.getIdToFriends().get(id)!=null){
				List<Contact> contactList = server.getIdToFriends().get(id);
				response = new NetworkItem(NetworkItemType.GetFriends, contactList);
			}else{
				response = new NetworkItem(NetworkItemType.GetFriends, Collections.<Contact>emptyList());
			}
	}

}
