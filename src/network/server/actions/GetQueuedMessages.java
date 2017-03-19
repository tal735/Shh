package network.server.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.DBConnector;
import types.Message;

public class GetQueuedMessages extends Action {

	public GetQueuedMessages(Object networkObject, DBConnector databaseConnector) {
		super(networkObject, databaseConnector);
	}

	@Override
	public void exectue() {
		//id of user to receive all messages
		Integer id = (Integer) networkObject;
		Set<Message> sentMessages = new HashSet<Message>();
		MessageAction messageAction;
		List<Message> messageList = databaseConnector.getIdToUnsentMessages().get(id);
		//send unsent messages
		if(messageList!=null){
			for(Message m : messageList){
				messageAction = new MessageAction(m, databaseConnector);
				messageAction.exectue();
				if(m.getSendStatus()){
					//removing message
					sentMessages.add(m);
				}
			}
		}
		
		for(Message sentMessage : sentMessages){
			messageList.remove(sentMessage);
			System.out.println("removed message " + sentMessage.toString());
		}
	}

}
