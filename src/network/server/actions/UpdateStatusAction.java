package network.server.actions;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import database.DBConnector;
import types.Contact;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Operations;

/**
 * 
 * @author talsim
 *	update status of user
 *input: Object[]:
 *					1. Integer - id of user
 *					2. boolean - connected/disconnected (will be later changed with types of statuses) 
 *
 *output: none
 */

public class UpdateStatusAction extends Action {

	public UpdateStatusAction(Object networkObject, DBConnector databaseConnector) {
		super(networkObject, databaseConnector);
	}

	@Override
	public void exectue() {
		Object[] values = (Object[]) networkObject;
		Integer id = (Integer) values[0];
		Boolean isOnline = (Boolean) values[1];
		
		for(Contact c : databaseConnector.getIdToFriends().get(id)){
			Socket toSocket = databaseConnector.getIdToSocket().get(c.getId());
			if(toSocket!=null){
				//prepare socket of target user
				Socket dstSocket = null;
				try {
					dstSocket = new Socket(toSocket.getInetAddress().getHostAddress(), databaseConnector.getIdToPort().get(c.getId()));
					//send message
					Operations.sendItem(new NetworkItem(NetworkItemType.GetStatus, new Object[] {id, isOnline}), dstSocket);
					
				}catch (UnknownHostException e1) {
					
				}catch (IOException e1) {
				}
				catch (Exception e){
				}
				finally{
					try {
						if(dstSocket!=null)
							dstSocket.close();
					} catch (IOException e) {
						System.out.println("IOException toMessageSocket.close ");
						e.printStackTrace();
					}
				}
			}
		}
	}

}
