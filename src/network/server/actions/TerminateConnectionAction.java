package network.server.actions;

import java.io.IOException;
import java.net.Socket;

import database.DBConnector;

/**
 * User sends termination signal (closes application)
 * Input: none
 * Output: none
 */

public class TerminateConnectionAction extends Action {
	private Object extra;
	public TerminateConnectionAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
	}

	public TerminateConnectionAction(Object networkObject, DBConnector databaseConnector,
			Object extra) {
		super(networkObject, databaseConnector);
		this.extra = extra;	
	}

	@Override
	public void exectue() {
		System.out.println("Closing socket..");
		Socket socket = (Socket) extra;
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket=null;
	}
}
