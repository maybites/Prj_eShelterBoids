/*
 * eShelterBoids
 *
 * Copyright (C) 2011 Martin Fr�hlich & Others
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
import mathematik.*;

public class eShelterMain extends PApplet {
	private static final long serialVersionUID = 1L;

	// ConfigGUI myGUI;
	// Connector myConnector;
	// Dispatcher myDispatcher;
	Thread myIndependentDispatcher;
	// FileParameters myParameters;
	// PlugFactory myPlugFactory;

	GestaltPlugIn gestalt;

	float angleX, angleY, transX, transY, transZ;
	
	int initBoidNum = 1000; // amount of boids to start the program with
	BoidsList flock1;// ,flock2,flock3;
	float zoom = 800;
	boolean smoothEdges = false, avoidWalls = false;

	public void setup() {
		size(640, 640, OPENGL);
		System.out.println("gotscha");

		Debugger.getInstance();
		Debugger.setLevelToInfo();

		GlobalPreferences.getInstance().setDataPath(this.dataPath(""));

		Canvas.setup(this);
		gestalt = Canvas.getInstance().getPlugin();
		gestalt.camera().setMode(Gestalt.CAMERA_MODE_LOOK_AT);
		gestalt.camera().position().set(0f, 0f, -600f);
		gestalt.camera().lookat().add(0f, 0f, 0f);

        /* setup light */
		gestalt.light().enable = true;
		//gestalt.light().setPositionRef(gestalt.camera().position());

		// DisplayCapabilities.listDisplayDevices();

		//size(800, 600, P3D);
		// create and fill the list of boids
		flock1 = new BoidsList(width, height, initBoidNum, 255);
		// flock2 = new BoidList(100,255);
		// flock3 = new BoidList(100,128);

		// Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D).add(_myCube);

		Debugger.getInstance().infoMessage(this.getClass(),
				"loading settings and preferences...");
		// myParameters = new FileParameters(this);
		Debugger.getInstance().infoMessage(this.getClass(),
				"... settings and preferences successfully loaded");

		Debugger.getInstance().infoMessage(this.getClass(),
				"loading plug selection and default mappings...");
		// myPlugFactory = new PlugFactory(this);
		Debugger.getInstance().infoMessage(this.getClass(),
				"... plug selection and default mappings successfully loaded");

		// myDispatcher = new Dispatcher(myPlugFactory);
		// myConnector = new Connector(myParameters, myDispatcher, this);
		// myConnector.setDefaultConnections();
		// myGUI = new ConfigGUI(this, myConnector, myParameters, myPlugFactory,
		// myDispatcher);
		// myGUI.setup(); // has to be done after declaration.

		// Debugger.setLevelToInfo();

		// myConnector.appleScript.speakVoice("Bridge started");

		angleX = 0;
		angleY = 0;
		transX = 0;
		transY = 0;
		transZ = 0;

	}

	public void draw() {
		background(0);
		// myDispatcher.draw(this);
		// clear screen
		
		/**
		beginCamera();
		camera();
		rotateX(map(mouseY, 0, height, 0, TWO_PI));
		rotateY(map(mouseX, width, 0, 0, TWO_PI));
		translate(0, 0, zoom);
		endCamera();
		background(205);
		directionalLight(255, 255, 255, 0, 1, -100);
		noFill();
		stroke(0);

		line(0, 0, 300, 0, height, 300);
		line(0, 0, 900, 0, height, 900);
		line(0, 0, 300, width, 0, 300);
		line(0, 0, 900, width, 0, 900);

		line(width, 0, 300, width, height, 300);
		line(width, 0, 900, width, height, 900);
		line(0, height, 300, width, height, 300);
		line(0, height, 900, width, height, 900);

		line(0, 0, 300, 0, 0, 900);
		line(0, height, 300, 0, height, 900);
		line(width, 0, 300, width, 0, 900);
		line(width, height, 300, width, height, 900);
		**/

		flock1.run(avoidWalls);
		flock1.render(this);
		// flock2.run();
		// flock3.run();
		if (smoothEdges)
			smooth();
		else
			noSmooth();
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

	void controlEvent(ControlEvent theEvent) {
		// myGUI.controlEvent(theEvent);
	}

	public Serial startSerialConnection(String id) {
		return new Serial(this, id, 115200);
	}

	public void serialEvent(Serial message) {
		// myConnector.receiveSerialMessage(message);
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "ch.maybites.prj.eShelter.eShelterMain" });
	}

	public void destroy() {
		// myConnector.deconnect();
		super.destroy();
	}
}