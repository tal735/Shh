package network.server.actions;

import database.DBConnector;
import network.NetworkItem;

public abstract class Action {
	protected DBConnector databaseConnector;
	protected Object networkObject;
	protected NetworkItem response = null;
	
	public Action(Object networkObject, DBConnector databaseConnector){
		this.databaseConnector = databaseConnector;
		this.networkObject = networkObject;
	}

	public abstract void exectue();;
	
	protected void setResponse(NetworkItem response){
		this.response = response;
	};
	
	public NetworkItem getGeneratedResponse(){
		return this.response;
	}
}

