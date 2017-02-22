package network.server.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import types.Message;
import network.Server;

public class GetQueuedMessages extends Action {

	public GetQueuedMessages(Object networkObject, Server server) {
		super(networkObject, server);
	}

	@Override
	public void exectue() {
		//id of user to receive all messages
		Integer id = (Integer) networkObject;
		Set<Message> sentMessages = new HashSet<Message>();
		MessageAction messageAction;
		List<Message> messageList = server.getIdToUnsentMessages().get(id);
		//send unsent messages
		if(messageList!=null){
			for(Message m : messageList){
				messageAction = new MessageAction(m, server);
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
