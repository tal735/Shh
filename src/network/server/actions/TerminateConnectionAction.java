package network.server.actions;

import java.io.IOException;
import java.net.Socket;

import network.Server;

/**
 * User sends termination signal (closes application)
 * Input: none
 * Output: none
 */

public class TerminateConnectionAction extends Action {
	private Object extra;
	public TerminateConnectionAction(Object netowrkObject, Server server) {
		super(netowrkObject, server);
	}

	public TerminateConnectionAction(Object networkObject, Server server,
			Object extra) {
		super(networkObject, server);
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
