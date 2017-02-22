package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Operations {

	static public  	void 			sendItem(NetworkItem netItem, Socket targetSocket){
		//send item to server
		if(targetSocket == null || netItem == null){
			return;
		}
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(targetSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			oos.writeObject(netItem);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	static public  	NetworkItem 	recieveItem(Socket targetSocket){
		//get response
		try{
			ObjectInputStream ois = new ObjectInputStream(targetSocket.getInputStream());
			Object obj = ois.readObject();
			return ((NetworkItem)obj);
		}catch(Exception e){
			System.out.println("Error receiving item..");
			return new NetworkItem();
		}
	}
	
}
