package network.client.actions;

import javax.swing.JOptionPane;

import security.CryptHelper;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.NetworkLoginInfo;
import network.Operations;

/**
 * Authenticate user+pass at server
 * Input: Object[] - [0]:String = username, [1]:char[] = hashed password
 * Output: none
 */

	
public class Autenticate extends Action {

	public Autenticate(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		String username = (String) (((Object[])this.input) [0]);
		char[] password = (char[]) (((Object[])this.input) [1]);
		
		
		CryptHelper cryptHelper = new CryptHelper();
		
		//hash password
		byte[] hash_pass = null;
		try {
			hash_pass = cryptHelper.hash_string(password);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, misc.Constants.ERROR_OOPS);
			this.setOutput(false);
			return;
		}
		
		//validate credentials with server
		try {
			NetworkItem networkItem = new NetworkItem(NetworkItemType.NetworkLoginInfo, new NetworkLoginInfo(username, hash_pass));
			//send creds to sever
			Operations.sendItem(networkItem, this.client.getSocket());
			//get response from server
			NetworkItem recNetItem = (NetworkItem) Operations.recieveItem(this.client.getSocket());
			//check if user is allowed to log
			System.out.println(recNetItem.getNetworkItemType());
			if(recNetItem.getNetworkItemType().equals(NetworkItemType.NetworkLoginInfo)){
				this.setOutput(((NetworkLoginInfo)recNetItem.getNetworkItem()).isLoginSuccess());
				return;
			}
		} catch (Exception e) {
			this.setOutput(false);
			return;
		}
		
		this.setOutput(false);
		return;
	}
}
