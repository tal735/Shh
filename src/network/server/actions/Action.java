package network.server.actions;

import network.NetworkItem;
import network.Server;


public abstract class Action {
	protected Server server;
	protected Object networkObject;
	protected NetworkItem response = null;
	
	public Action(Object networkObject, Server server){
		this.server = server;
		this.networkObject = networkObject;
	}

	public abstract void exectue();
	
	public NetworkItem getGeneratedResponse(){
		return this.response;
	}
	
	
}

