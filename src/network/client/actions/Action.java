package network.client.actions;

import network.Client;


public abstract class Action {
	protected Object input;
	protected Object output = null;
	protected Client client;
	
	public Action(Object input, Client client){
		this.input = input;
		this.client = client;
	}

	public abstract void exectue();
	
	public Object getOutput(){
		return this.output;
	}
	
	protected void setOutput(Object output){
		this.output = output;
	}	
}