package network.server.actions;

import java.net.Socket;

import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Server;

public class GetStatusAction extends Action {

	public GetStatusAction(Object networkObject, Server server) {
		super(networkObject, server);
	}

	@Override
	public void exectue() {
		//read ids of friends
		Integer values[] = (Integer []) networkObject;
		
		//availability of each user returned to caller
		Boolean[] availableArray = new Boolean[values.length];
		
		//find availability of each user
		for(int i=0; i<values.length; i++){
			Socket s = server.getIdToSocket().get(values[i]);
			if(s==null){
				availableArray[i] = false;
			}else{
				availableArray[i] = !server.getIdToSocket().get(values[i]).isClosed();//isConnected();
			}	
		}
		
		//return availability
		response = new NetworkItem(NetworkItemType.GetStatus, availableArray);
	}

}
