package network.server.actions;

import java.net.Socket;

import security.CryptHelper;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.NetworkLoginInfo;
import network.Server;

/**
 * check login and register socket of nickname in server
 * Input: Instance of class 'NetworkLoginInfo'
 * Output: Instance of class 'NetworkLoginInfo', with isLoginSuccess() set to result of login attempt.
 */

public class NetworkLoginInfoAction extends Action {

	private CryptHelper crypt = new CryptHelper();
	private Object extra = null;
	public NetworkLoginInfoAction(Object netowrkObject, Server server) {
		super(netowrkObject, server);
		// TODO Auto-generated constructor stub
	}

	public NetworkLoginInfoAction(Object networkObject, Server server,
			Object extra) {
		super(networkObject, server);
		this.extra = extra;
	}

	@Override
	public void exectue() {
		//get socket
		Socket socket = (Socket) extra;
		
		//check login
		NetworkLoginInfo loginInfo = (NetworkLoginInfo) networkObject;

		//check if username exists
		String username = loginInfo.getUsername().toLowerCase();
		Integer id = server.getNickToIdMap().get(username);
		
		if(id != null){
			//check if password matches
			loginInfo.setLoginSuccess(crypt.passwordsMatch((byte[]) server.getNickToPass().get(username), loginInfo.getHashedPassword()));
		}
		
		//register socket.
		if(loginInfo.isLoginSuccess()){
			//save current socket
			server.getIdToSocket().put(id, socket);
			
		}
		
		response = new NetworkItem(NetworkItemType.NetworkLoginInfo, loginInfo);
		
	}

}
