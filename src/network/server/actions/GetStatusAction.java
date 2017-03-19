package network.server.actions;

import java.net.Socket;

import database.DBConnector;
import misc.Constants.NetworkItemType;
import network.NetworkItem;

public class GetStatusAction extends Action {

	public GetStatusAction(Object networkObject, DBConnector databaseConnector) {
		super(networkObject, databaseConnector);
	}

	@Override
	public void exectue() {
		//read ids of friends
		Integer values[] = (Integer []) networkObject;
		
		//availability of each user returned to caller
		Boolean[] availableArray = new Boolean[values.length];
		
		//find availability of each user
		for(int i=0; i<values.length; i++){
			Socket s = databaseConnector.getIdToSocket().get(values[i]);
			if(s==null){
				availableArray[i] = false;
			}else{
				availableArray[i] = !databaseConnector.getIdToSocket().get(values[i]).isClosed();//isConnected();
			}	
		}
		
		//return availability
		//response = new NetworkItem(NetworkItemType.GetStatus, availableArray);
		this.setResponse(new NetworkItem(NetworkItemType.GetStatus, availableArray));
	}

}
