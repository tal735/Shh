package network;

import java.io.Serializable;

public class NetworkItem implements Serializable {

	NetworkItemType type;
	Object item;
	
	public static enum NetworkItemType{
		NetworkLoginInfo, 
		Message, 
		Id,
		NullItem, //for error handling
		AddContact,
		TerminateConnection,
		GetFriends,
		GetContact,
		MessagePortUpdate 
	}
	
	public NetworkItem(NetworkItemType type, Object item) {
		this.type = type;
		this.item = item;
	}
	
	public NetworkItem() {
		this.type = NetworkItemType.NullItem;
	}
	
	public void setType(NetworkItemType type){
		this.type = type;
	}
	
	public NetworkItemType getNetworkItemType(){
		return this.type;
	}
	
	public void setNetowrkItem(Object item){
		this.item = item;
	}
	
	public Object getNetworkItem(){
		return this.item;
	}
}
