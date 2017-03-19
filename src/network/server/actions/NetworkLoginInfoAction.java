package network.server.actions;

import java.net.Socket;

import database.DBConnector;
import security.CryptHelper;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.NetworkLoginInfo;

/**
 * check login and register socket of nickname in server
 * Input: Instance of class 'NetworkLoginInfo'
 * Output: Instance of class 'NetworkLoginInfo', with isLoginSuccess() set to result of login attempt.
 */

public class NetworkLoginInfoAction extends Action {

	private CryptHelper crypt = new CryptHelper();
	private Object extra = null;
	public NetworkLoginInfoAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
		// TODO Auto-generated constructor stub
	}

	public NetworkLoginInfoAction(Object networkObject, DBConnector databaseConnector,
			Object extra) {
		super(networkObject, databaseConnector);
	}

	@Override
	public void exectue() {
		//get socket
		Socket socket = (Socket) extra;
		
		//check login
		NetworkLoginInfo loginInfo = (NetworkLoginInfo) networkObject;

		//check if username exists
		String username = loginInfo.getUsername().toLowerCase();
		Integer id = databaseConnector.getNickToIdMap().get(username);
		
		if(id != null){
			//check if password matches
			loginInfo.setLoginSuccess(crypt.passwordsMatch((byte[]) databaseConnector.getNickToPass().get(username), loginInfo.getHashedPassword()));
		}
		
		//register socket.
		if(loginInfo.isLoginSuccess()){
			//save current socket
			databaseConnector.getIdToSocket().put(id, socket);
			
		}
		
		//response = new NetworkItem(NetworkItemType.NetworkLoginInfo, loginInfo);
		setResponse( new NetworkItem(NetworkItemType.NetworkLoginInfo, loginInfo));
	}

}
