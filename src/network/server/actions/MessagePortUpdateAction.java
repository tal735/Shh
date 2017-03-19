package network.server.actions;

import database.DBConnector;

public class MessagePortUpdateAction extends Action {


	/**
	 * Update port of message reciever socket at client so we can send him messages
	 * Input: Integer[] :	
	 *						[0] = id of contact
	 *						[1] = listening port at client's end
	 *
	 * Output: nothing
	 */
	
	public MessagePortUpdateAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exectue() {
			//read id of contact + port number
			Integer values[] = (Integer []) networkObject;
			//[0] = id of contact
			//[1] = port of listening to messages of contact
			Integer id = values[0];
			Integer newPort = values[1];
			
			//update port
			if(id!=null && newPort!=null){
					System.out.println("updating port " + newPort + " to id " + id);
					this.databaseConnector.getIdToPort().put(id, newPort);
			}
			//no need to return anything
			//response = null;
		}
}
