package network;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import network.client.actions.Action;
import network.client.actions.ActionFactory;
import main.MainContacts;
import misc.Constants;
import misc.Constants.NetworkItemType;
import types.Message;

public class Client {
	
	private  Socket clientSocket = null;
	private  ServerSocket messageSocket = null;
	private  ActionFactory actionFactory = new ActionFactory(this);
	private MainContacts mainContactsForm = null;
	
	public void setMainContacts(MainContacts mc){
		this.mainContactsForm = mc;
	}
	
	public Client(){	
		//create socket
		try {
			if(clientSocket==null){
				String hostAddress = InetAddress.getByName(Constants.APP_SERVER_HOSTNAME).getHostAddress();
				clientSocket = new Socket(hostAddress, Constants.APPLICATION_NETWORK_PORT);
			}
		} catch (Exception e) {
			//print error message
		}

		//create socket for receiving messages
		if(messageSocket==null){
			try {
				messageSocket = new ServerSocket(0);
				//start listening
				Thread incomingMsgThread = new Thread() {
					public void run() {
						try {
							readMessagesLoop();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}  
				};

				incomingMsgThread.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public void readMessagesLoop() throws IOException{
		while(true){
			Socket serverSocket = messageSocket.accept();
			//get message
			NetworkItem ni = Operations.recieveItem(serverSocket);
			if(ni.getNetworkItemType().equals(NetworkItemType.Message)){
				Message m = (Message) ni.getNetworkItem();
				//update text in chat GUI
				this.mainContactsForm.updateChat(m);
			}
			if(ni.getNetworkItemType().equals(NetworkItemType.GetStatus)){
				System.out.println("IM HERE");
				//get avail. of logeed on/off user from server
				Object[] values = (Object[]) ni.getNetworkItem();
				int id = (Integer) values[0];
				boolean isOnline = (Boolean) values[1];	
				
				System.out.println("id="+id +", status=" + isOnline);
				//update gui
				this.mainContactsForm.setAvailabilityIconForId(id, isOnline);
			}
			//done
			serverSocket.close();
		}
	}
	
	public  ServerSocket getMessageSocket(){
		return messageSocket;
	}
	
	public  Socket getSocket(){
		return clientSocket;
	}

	//perform action and return output
	public Object doAction(NetworkItemType type, Object input){
		Action action = actionFactory.getAction(type, input);
		action.exectue();
		return action.getOutput();
	}
}