package network;

import java.io.Serializable;

import misc.Constants.NetworkItemType;

public class NetworkItem implements Serializable {

	private static final long serialVersionUID = 3033908552417302591L;
	NetworkItemType type;
	Object item;
	
	public NetworkItem(NetworkItemType type, Object item) {
		this.type = type;
		this.item = item;
	}
	
	public NetworkItem() {
		this.type = NetworkItemType.NullItem;
	}
	
	public NetworkItemType getNetworkItemType(){
		return this.type;
	}
	
	public Object getNetworkItem(){
		return this.item;
	}
}
