package network.server.actions;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import types.Message;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Operations;
import network.Server;

/**
 * 
 * Transfer a message from user A to user B
 * Input: 
 * Output: 
 *
 */
public class MessageAction extends Action{

	public MessageAction(Object networkObject, Server server) {
		super(networkObject, server);
	}

	@Override
	public void exectue() {
		Boolean isSent = false;
		//get message from source
		Message m = (Message) networkObject;
		int idTo = m.getToContact().getId();
		
		/**
		 * send message to user
		 */
		NetworkItem messageItem = new NetworkItem(NetworkItemType.Message, m);
		//get socket of destination user
		Socket toSocket = server.getIdToSocket().get(idTo);
		if(toSocket!=null){
			//prepare socket of target user
			Socket toMessageSocket = null;
			try {
				toMessageSocket = new Socket(toSocket.getInetAddress().getHostAddress(), server.getIdToPort().get(idTo));
				//send message
				Operations.sendItem(messageItem, toMessageSocket);
				//set message flag to sent
				isSent = true;
			}catch (UnknownHostException e1) {
				isSent = false;
			}catch (IOException e1) {
				isSent = false;
			}
			catch (Exception e) {
				isSent = false;
			}
			finally{
				try {
					if(toMessageSocket!=null)
						toMessageSocket.close();
				} catch (IOException e) {
					System.out.println("IOException toMessageSocket.close ");
					e.printStackTrace();
				}
			}
		}
		
		//save in queue to send when user gets online
		if(!isSent){
			if(!server.getIdToUnsentMessages().containsKey(idTo)){
				server.getIdToUnsentMessages().put(idTo, new ArrayList<Message>());
				System.out.println("Created queue for id " + idTo);
			}
			
			List<Message> mlist = server.getIdToUnsentMessages().get(idTo);
			mlist.add(m);
			System.out.println("Enqueued message: " + m.toString());
		}
		
		//set status of whether message was sent
		m.setSendStatus(isSent);
		
		//send back if success or fail
		response = messageItem;
	}

}
