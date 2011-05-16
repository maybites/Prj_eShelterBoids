/*
 * eShelterBoids
 *
 * Copyright (C) 2011 Martin Fršhlich & Others
 *
 * Parts of the code used has been found on the internet:
 
 * 3D Boids Simulation: Matt Wetmore (http://www.openprocessing.org/visuals/?visualID=6910)
 *
 * This class is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This class is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * {@link http://www.gnu.org/licenses/lgpl.html}
 *
 */

package ch.maybites.prj.eShelter;

import processing.core.*;
import controlP5.*;
import processing.serial.*;
import gestalt.Gestalt;
import gestalt.p5.*;
import gestalt.util.FPSCounter;
import mathematik.*;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

public class eShelterMain extends PApplet implements OSCListener {
	private static final long serialVersionUID = 1L;

	// ConfigGUI myGUI;
	// Connector myConnector;
	// Dispatcher myDispatcher;
	Thread myIndependentDispatcher;
	// FileParameters myParameters;
	// PlugFactory myPlugFactory;

	GestaltPlugIn gestalt;

	private FPSCounter myFPSCounter;
	
	private int oscID;

	float angleX, angleY, transX, transY, transZ;

	int initBoidNum = 1000; // amount of boids to start the program with
	BoidsSim flock1;// ,flock2,flock3;
	SkeletonTracker skelton;
	GestureScanner scanner;
	float zoom = 800;
	boolean smoothEdges = false, avoidWalls = false;

	public void setup() {
		size(1200, 600, OPENGL);
		Debugger.getInstance();
		Debugger.setLevelToInfo();

		System.out.println("gotscha");

		GlobalPreferences.getInstance().setDataPath(this.dataPath(""));
		oscID = GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.SIM_ID, 1);
				
		//CommunicationHub.setup(9030, 9040, "127.0.0.1");
		CommunicationHub.setup(
				GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.OSC_LISTENPORT, 9030),
				GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.OSC_SEND_MUSIC_PORT, 9040),
				GlobalPreferences.getInstance().getStringProperty(GlobalPreferences.OSC_SEND_MUSIC_ADDRESS, "127.0.0.1"),				
				GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.OSC_SEND_SIM_PORT, 9040),
				GlobalPreferences.getInstance().getStringProperty(GlobalPreferences.OSC_SEND_SIM_ADDRESS, "127.0.0.1"));				
		CommunicationHub.getInstance().sendOscMusicMessage(
				new OSCMessage("/testtest"));

		Canvas.setup(this);
		gestalt = Canvas.getInstance().getPlugin();
		gestalt.camera().setMode(Gestalt.CAMERA_MODE_LOOK_AT);
		gestalt.camera().position().set(0f, -160f, 940f);
		gestalt.camera().lookat().set(0f, -41f, 0f);
		gestalt.camera().fovy = 91.0f;
		println("fovy: " + gestalt.camera().fovy);

		/* setup light */
		gestalt.light().enable = true;
		gestalt.light().position().set(0f, 0f, 0f);
		// gestalt.light().setPositionRef(gestalt.camera().position());

		// DisplayCapabilities.listDisplayDevices();

		// create and fill the list of boids
		flock1 = new BoidsSim(width, height, initBoidNum, 255);
		flock1.addToOSCListener();

		//create skeleton
		skelton = new SkeletonTracker(flock1);
		skelton.addToOSCListener();
		
		scanner = new GestureScanner(skelton, flock1);
		scanner.addToOSCListener();
		// Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D).add(_myCube);

		Debugger.getInstance().infoMessage(this.getClass(),
				"loading settings and preferences...");

		// myDispatcher = new Dispatcher(myPlugFactory);
		// myConnector = new Connector(myParameters, myDispatcher, this);
		// myConnector.setDefaultConnections();
		// myGUI = new ConfigGUI(this, myConnector, myParameters, myPlugFactory,
		// myDispatcher);
		// myGUI.setup(); // has to be done after declaration.

		// Debugger.setLevelToInfo();

		// myConnector.appleScript.speakVoice("Bridge started");

		/* fps counter */
		setup_fpsCounter();

		angleX = 0;
		angleY = 0;
		transX = 0;
		transY = 0;
		transZ = 0;
		
		addToOSCListener();

	}

	private void setup_fpsCounter() {
		myFPSCounter = new FPSCounter();

		/* set the interval of sampling */
		myFPSCounter.setInterval(20);

		/* create and a view of the FPS sampler */
		myFPSCounter.display().position.set(20, height / 2);
		myFPSCounter.display().color.set(1);
	}

	public void draw() {
		background(0);

		flock1.run(avoidWalls);
		flock1.render(this);
		skelton.run();
		scanner.run();

		myFPSCounter.loop();
	}

	public void keyPressed() {
		switch (keyCode) {
		case UP:
			zoom -= 10;
			break;
		case DOWN:
			zoom += 10;
			break;
		}
		switch (key) {
		case 's':
			smoothEdges = !smoothEdges;
			break;
		case 'a':
			avoidWalls = !avoidWalls;
			break;
		}
	}

	public void mouseDragged() {
		gestalt.camera().position().x += (mouseX - pmouseX);
		gestalt.camera().position().y += (mouseY - pmouseY);
	}
	
	private void setCameraFovy(float value){
		gestalt.camera().fovy = value;
	}
	
	/**
	 * dont works
	 * @param value
	 */
	private void setLightIntensity(float value){
		gestalt.light().intesity = value;
	}
	
	private void setCameraLookAt(int value){
		if(value == 1)
			gestalt.camera().setMode(Gestalt.CAMERA_MODE_LOOK_AT);
		else
			gestalt.camera().setMode(Gestalt.CAMERA_MODE_ROTATE_XYZ);
	}
	
	private void setLightEnable(int value){
		if(value == 1)
			gestalt.light().enable = true;
		else
			gestalt.light().enable = false;
	}
	
	private void setShowFPSCounter(int value){
		if(value == 1)
			gestalt.bin(gestalt.BIN_2D_FOREGROUND).add(myFPSCounter.display());
		else
			gestalt.bin(gestalt.BIN_2D_FOREGROUND).remove(myFPSCounter.display());
	}
	
	private void setCameraPosition(float posx, float posy, float posz){
		gestalt.camera().position().set(posx, posy, posz);
	}
	
	private void setLightPosition(float posx, float posy, float posz){
		gestalt.light().position().set(posx, posy, posz);
	}
	
	/**
	 * dont works
	 * @param posx
	 * @param posy
	 * @param posz
	 */
	private void setLightColor(float posx, float posy, float posz){
		gestalt.light().ambient.set(posx, posy, posz);
	}
	
	private void setCameraLookAt(float posx, float posy, float posz){
		gestalt.camera().lookat().set(posx, posy, posz);
	}

	public void acceptMessage(java.util.Date time, OSCMessage _message) {
		try {
			if (_message.getAddress().equals("/simulation"+oscID+"/light/intensity"))
				setLightIntensity(((Float) (_message.getArguments()[0])).floatValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/camera/fovy"))
				setCameraFovy(((Float) (_message.getArguments()[0])).floatValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/camera/setlookat"))
				setCameraLookAt(((Integer) (_message.getArguments()[0])).intValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/light/enable"))
				setLightEnable(((Integer) (_message.getArguments()[0])).intValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/fpscounter/show"))
				setShowFPSCounter(((Integer) (_message.getArguments()[0])).intValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/camera/position"))
				setCameraPosition(
						((Float) (_message.getArguments()[0])).floatValue(),
						((Float) (_message.getArguments()[1])).floatValue(),
						((Float) (_message.getArguments()[2])).floatValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/light/position"))
				setLightPosition(
						((Float) (_message.getArguments()[0])).floatValue(),
						((Float) (_message.getArguments()[1])).floatValue(),
						((Float) (_message.getArguments()[2])).floatValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/light/color"))
				setLightColor(
						((Float) (_message.getArguments()[0])).floatValue(),
						((Float) (_message.getArguments()[1])).floatValue(),
						((Float) (_message.getArguments()[2])).floatValue());
			if (_message.getAddress().equals("/simulation"+oscID+"/camera/lookat"))
				setCameraLookAt(
						((Float) (_message.getArguments()[0])).floatValue(),
						((Float) (_message.getArguments()[1])).floatValue(),
						((Float) (_message.getArguments()[2])).floatValue());
		} catch (ArrayIndexOutOfBoundsException e) {
			Debugger.getInstance().warningMessage(this.getClass(),
					"OSC Message in wrong format: " + _message.getAddress());
		} catch (ClassCastException e) {
			Debugger.getInstance().warningMessage(
					this.getClass(),
					"OSC Message Data in wrong format: "
							+ _message.getAddress() + " -> " + e.getMessage());
		}
	}

	public void addToOSCListener(){
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/fpscounter/show", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/light/enable", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/light/color", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/light/intensity", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/light/position", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/camera/position", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/camera/setlookat", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/camera/lookat", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/camera/fovy", this);
	}

	public void readArguments(){
		for(int i = 0; i < super.args.length; i++){
			if(super.args[i].equals("-simID")){
				oscID = Integer.parseInt(super.args[++i]);
			}
			if(super.args[i].equals("-listenerPort")){
				oscID = Integer.parseInt(super.args[++i]);
			}
			if(super.args[i].equals("-sendPort")){
				oscID = Integer.parseInt(super.args[++i]);
			}
			if(super.args[i].equals("-sendAddress")){
				oscID = Integer.parseInt(super.args[++i]);
			}
		}
	}
	
	static public void main(String args[]) {
		String[] newArgs = new String[args.length + 1];
		newArgs[0] = "ch.maybites.prj.eShelter.eShelterMain";
		int counter = 1;
		for(String arg: args){
			newArgs[counter++] = arg;
		}
		PApplet.main(newArgs);
	}

	public void destroy() {
		// myConnector.deconnect();
		super.destroy();
	}
}