package ch.maybites.prj.eShelter;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.illposed.osc.*;

/**
 * Before using CommunicationHub you need to call the setup method.
 * 
 * @author maf
 *
 */
public class CommunicationHub {

	static private CommunicationHub _instance = null;
	
	static private OSCPortOut sender;
	static private OSCPortIn receiver;
	
	/**
	 * this class is based on the singleton pattern and makes sure
	 * only one instance can be created and shared with all the involved
	 * classes
	 * 
	 * @param ref
	 * @param _port
	 */
	private CommunicationHub(int _listenerPort, int _senderPort, String _senderAdress){
		try{
			sender = new OSCPortOut(java.net.InetAddress.getByName(_senderAdress), _senderPort);
	        receiver = new OSCPortIn(_listenerPort);
	        receiver.startListening();
		} catch (SocketException e){
			Debugger.getInstance().fatalMessage(this.getClass(), "Cant create OSC Receiver: "+ e.getMessage());
		} catch (UnknownHostException e){
			Debugger.getInstance().fatalMessage(this.getClass(), "Cant create OSC Sender: "+ e.getMessage());
		}
	}

	/**
	 * it is required to call this method at least once, right at the beginning
	 * 
	 * @param ref	applet reference
	 * @param _port listener port
	 */
	static public void setup(int _listenerPort, int _senderPort, String _senderAdress) {
		if (_instance == null) {
			synchronized(CommunicationHub.class) {
				if (_instance == null){
					_instance = new CommunicationHub(_listenerPort, _senderPort, _senderAdress);
				}
			}
		}
	}

	/**
	 * every class in this project can get the same instance of this hub and ask it
	 * to pass on certain messages to certain functions.
	 * 
	 * @return
	 */
	static public CommunicationHub getInstance(){
		return _instance;
	}

	/**
	 * if the class has an method like
	 * 
	 * acceptMessage(java.util.Date time, OSCMessage message) 
	 * 
	 * it can register itself to the listener of the CommunicationHub
	 * 
	 * @param _oscAdress
	 * @param _listener
	 */
	public void addListener(String _oscAdress, OSCListener _listener) {
		receiver.addListener(_oscAdress, _listener);
	}
	
	public void sendOscMessage(OSCPacket _message){
		try {
			sender.send(_message);
		} catch (IOException e) {
			Debugger.getInstance().fatalMessage(this.getClass(), "Cant send OSC Message: "+ e.getMessage());
		}
	}
	
}
