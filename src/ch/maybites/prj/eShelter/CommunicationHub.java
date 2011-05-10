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
	
	static private OSCPortOut musicSender;
	static private OSCPortOut simSender;
	static private OSCPortIn receiver;
	
	/**
	 * this class is based on the singleton pattern and makes sure
	 * only one instance can be created and shared with all the involved
	 * classes
	 * 
	 * @param ref
	 * @param _port
	 */
	private CommunicationHub(int _listenerPort, int _senderMusicPort, String _senderMusicAdress, int _senderSimPort, String _senderSimAdress){
		try{
			musicSender = new OSCPortOut(java.net.InetAddress.getByName(_senderMusicAdress), _senderMusicPort);
			simSender = new OSCPortOut(java.net.InetAddress.getByName(_senderSimAdress), _senderSimPort);
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
	static public void setup(int _listenerPort, int _senderMusicPort, String _senderMusicAdress, int _senderSimPort, String _senderSimAdress) {
		if (_instance == null) {
			synchronized(CommunicationHub.class) {
				if (_instance == null){
					_instance = new CommunicationHub(_listenerPort, _senderMusicPort, _senderMusicAdress, _senderSimPort, _senderSimAdress);
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
	
	public void sendOscMusicMessage(OSCPacket _message){
		try {
			musicSender.send(_message);
		} catch (IOException e) {
			Debugger.getInstance().fatalMessage(this.getClass(), "Cant send OSC Message: "+ e.getMessage());
		}
	}

	public void sendOscSimMessage(OSCPacket _message){
		try {
			simSender.send(_message);
		} catch (IOException e) {
			Debugger.getInstance().fatalMessage(this.getClass(), "Cant send OSC Message: "+ e.getMessage());
		}
	}
	
	void sendMirrorOSCMessage(Boid b, int simID){
		OSCMessage msg = new OSCMessage("/sound"+simID+"/flock/mirror");
		msg.addArgument(new Integer(b.swarmID));
		msg.addArgument(new Float(b.pos.x));
		msg.addArgument(new Float(b.pos.y));
		msg.addArgument(new Float(b.pos.z));
		CommunicationHub.getInstance().sendOscMusicMessage(msg);		
	}

	/*
	 *  /sound/flock/collision <(int)type> <(float)posX> <(float)posY> <(float)posZ>
	 */
	void sendCollisionOSCMessage(Boid b, int simID){
		OSCMessage msg = new OSCMessage("/sound"+simID+"/flock/collision");
		msg.addArgument(new Integer(b.swarmID));
		msg.addArgument(new Float(b.pos.x));
		msg.addArgument(new Float(b.pos.y));
		msg.addArgument(new Float(b.pos.z));
		CommunicationHub.getInstance().sendOscMusicMessage(msg);		
	}
	
	void sendWarpOSCMessage(Boid b, int simID, int otherSimID){
		OSCMessage msg = new OSCMessage("/sound"+simID+"/flock/warp");
		msg.addArgument(new Integer(b.swarmID));
		msg.addArgument(new Float(b.pos.x));
		CommunicationHub.getInstance().sendOscMusicMessage(msg);	
		
		msg = new OSCMessage("/simulation"+otherSimID+"/manager/boid/warp");
		msg.addArgument(new Integer(b.swarmID));
		msg.addArgument(new Float(b.pos.x));
		msg.addArgument(new Float(b.pos.y));
		msg.addArgument(new Float(b.pos.z));
		msg.addArgument(new Float(b.vel.x));
		msg.addArgument(new Float(b.vel.y));
		msg.addArgument(new Float(b.vel.z));
		CommunicationHub.getInstance().sendOscSimMessage(msg);		
	}

}
