package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import database.DBConnector;
import misc.Constants;
import network.server.actions.Action;
import network.server.actions.ActionFactory;

public class Server{
	private ServerSocket listener=null;
	
	//database reference
	private DBConnector databaseConnector = new DBConnector();
	
	public Server(){
		initServerSocket();
	}
	
	public void initServerSocket(){
		if(listener==null){
			try {
				listener = new ServerSocket(Constants.APPLICATION_NETWORK_PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		databaseConnector.initTestValues();	//remove this when going live
	}

	public void mainServerLoop() throws IOException{
		try {
			System.out.println("Server is running...");
			while (true) {
				Socket socket = listener.accept();
				//open client thread
				Thread thread = new Thread(new clientLoop(socket));
				thread.start();
			}
		}
		catch	(Exception e)	{} 
		finally {	listener.close();	}
	}

	/**
	 * 
	 * each connected user will be running this loop inside the server. The server will wait for request from client and will handle the requests
	 */
	class clientLoop implements Runnable{
		private Socket socket;
		
		public clientLoop (Socket socket) {
		       this.socket = socket;
		   }
		@Override
		public void run() {
			while(!socket.isClosed()){
				NetworkItem response = null;
				//get item from client
				NetworkItem ni = Operations.recieveItem(socket);
				//handle item
				response = handleItem(ni, socket);
				//send response
				Operations.sendItem(response, socket);
			}
			System.out.println("Terminated connection with " + socket.toString());
		}
	}

	//handle requests from user
	private ActionFactory actionFactory = new ActionFactory(this.databaseConnector);
	
	private NetworkItem handleItem(NetworkItem ni, Socket socket) {
		Action action = actionFactory.getAction(ni, socket);
		action.exectue();
		return action.getGeneratedResponse();
	}

}
