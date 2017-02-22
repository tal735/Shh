package security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import types.Contact;
import types.Message;

public class CryptHelper {
	
	public CryptHelper(){};
	
	public boolean passwordsMatch(byte[] src, byte[] dst){
		if(src==null || dst==null){
			return false;
		}
		
		return Arrays.equals(src, dst);
	}
	
	public String decode_Message(Message M){
		return M.getText();
	}
	
	public Contact decode_Contact(Message M){
		return M.getToContact();
	}
	
	public byte[] hash_string(char[] password) {
		//hash password
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(new String(password).getBytes("UTF-8"));	// Change this to "UTF-16" if needed
			digest = md.digest();
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
		}
		
		return digest;
	}
	
}
