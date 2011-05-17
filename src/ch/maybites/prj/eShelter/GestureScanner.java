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


import gestalt.Gestalt;
import gestalt.shape.*;
import gestalt.shape.material.MaterialPlugin;
import gestalt.shape.material.TexturePlugin;
import gestalt.texture.Bitmaps;
import gestalt.candidates.glsl.ShaderManager;
import gestalt.candidates.glsl.ShaderProgram;
import gestalt.context.GLContext;
import gestalt.render.bin.AbstractBin;

import javax.media.opengl.GL;

import mathematik.Vector3f;

import processing.core.*;

import com.illposed.osc.*;

public class GestureScanner implements OSCListener{
	private MessageQueue messageQueue;
	
	int size = 10;
	
	int currentID;
	
	SkeletonTracker myTracker;
	BoidsSim sim;
	private boolean ceoSwarm = false;

	static final int MODE_WAITING = 0;
	static final int MODE_CALLING_BOIDS = 1;
	static final int MODE_CONTROLLING_BOIDS = 2;
	static final int MODE_INCUBATE_BOIDS = 3;
	static final int MODE_INCUBATE_CEO = 4;
	static final int MODE_INCUBATING_BOIDS = 5;
	static final int MODE_INCUBATING_CEO = 6;
	static final int MODE_RELEASING_BOIDS = 7;
	
	static final int CALLING_SPEED_THRESHOLD = 150;
	
	int mode = MODE_WAITING;
	
	float incubeGestureFactor = 2.5f;
	float callingGestureFactor = 5.0f;


	Vector3f yAxis = new Vector3f(0, 1, 0);
	
	GestureScanner(SkeletonTracker _tracker, BoidsSim _sim) {
		messageQueue = new MessageQueue();
		myTracker = _tracker;
		sim = _sim;
	}
	
	private boolean trackGestureRegisterForTracking(float _angle1, float _angle2){
		if(myTracker.getCurrentLowerLeftArm().angle(myTracker.getCurrentLowerRightArm()) < _angle1){
			if(myTracker.getCurrentLowerLeftArm().angle(myTracker.getCurrentLowerRightArm()) < _angle1){
				Vector3f cross1 = myTracker.getCurrentUpperLeftArm();
				cross1.cross(myTracker.getCurrentLowerLeftArm());
				Vector3f cross2 = myTracker.getCurrentUpperRightArm();
				cross2.cross(myTracker.getCurrentLowerRightArm());
				cross2.scale(-1.f);
				if(cross1.angle(cross2) < _angle2){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean trackGestureStretchedArms(float _handDistance, float _factor, float _angle1){
		if(_handDistance > myTracker.getCurrentShoulderRef(_factor)){
			if(		myTracker.getCurrentLeftHand().y > myTracker.getCurrentLeftShoulder().y - 20 &&
					myTracker.getCurrentRightHand().y > myTracker.getCurrentRightShoulder().y - 20){
				float angle1 = myTracker.getCurrentLowerLeftArm().angle(myTracker.getCurrentUpperLeftArm());
				float angle2 = myTracker.getCurrentLowerRightArm().angle(myTracker.getCurrentUpperRightArm());
				if(angle1 < _angle1){
					if(angle2 < _angle1){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean trackGestureStretchedArmsBelowHips(float _handDistance, float _factor, float _angle1){
		if(_handDistance > myTracker.getCurrentShoulderRef(_factor)){
			if(		myTracker.getCurrentLeftHand().y < myTracker.getCurrentLeftHip().y &&
					myTracker.getCurrentRightHand().y < myTracker.getCurrentRightHip().y){
				float angle1 = myTracker.getCurrentLowerLeftArm().angle(myTracker.getCurrentUpperLeftArm());
				float angle2 = myTracker.getCurrentLowerRightArm().angle(myTracker.getCurrentUpperRightArm());
				if(angle1 < _angle1){
					if(angle2 < _angle1){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean trackGestureCloseHandsBevoreStomach(float _distanceHand){
		if(		_distanceHand < myTracker.getCurrentShoulderRef(0.5f) &&
			myTracker.getCurrentRightHand().distance(myTracker.getCurrentTorso()) < myTracker.getCurrentShoulderRef(1f))
			return true;
		return false;
	}
		
	private int getSwarmID(){
		if(ceoSwarm)
			return sim.swarmProps.ceoSwarmType;
		else
			return myTracker.getSwarmID();
	}
		
	
	void run() {
		executeMessages();
		if(myTracker.currentIsActive()){
			Vector3f lh = myTracker.getCurrentLeftHand();
			Vector3f rh = myTracker.getCurrentRightHand();
			Vector3f handDirection = myTracker.getCurrentRightHand();
			handDirection.sub(myTracker.getCurrentLeftHand());
			float handDistance = handDirection.length();
			boolean actionZone = 
				myTracker.checkIfInActionZone(myTracker.getCurrentLeftHand()) && // hands in the actionZone
				myTracker.checkIfInActionZone(myTracker.getCurrentRightHand());
			
			switch(mode){
			case MODE_WAITING:
				// check for tracking init gesture
				if(trackGestureStretchedArms(handDistance, callingGestureFactor, 0.3f))
					mode = MODE_CALLING_BOIDS;
				else if(ceoSwarm && trackGestureStretchedArmsBelowHips(handDistance, incubeGestureFactor, 0.3f))
					mode = MODE_INCUBATE_CEO;
				else if(trackGestureStretchedArmsBelowHips(handDistance, incubeGestureFactor, 0.3f))
					mode = MODE_INCUBATE_BOIDS;
				break;
			case MODE_INCUBATE_BOIDS:
				Vector3f handPos = myTracker.getCurrentRightHand();
				handPos.add(myTracker.getCurrentLeftHand());
				handPos.scale(0.5f);
				Debugger.getInstance().infoMessage(this.getClass(), "incubate boids");
				sim.incubate(new PVector(handPos.x, handPos.y, handPos.z), sim.swarmProps.getIncubationSize(getSwarmID()), getSwarmID());
				float limitedhandDistance = (handDistance > myTracker.getCurrentShoulderRef(1f))? handDistance : myTracker.getCurrentShoulderRef(1f);
				sim.addArrousalXclusivMagnetSphere("incubator", 0, handPos.x, handPos.y, handPos.z, 0, 50, limitedhandDistance, 5f, 4f);
				mode = MODE_INCUBATING_BOIDS;
				break;
			case MODE_INCUBATING_BOIDS:
				handPos = myTracker.getCurrentRightHand();
				handPos.add(myTracker.getCurrentLeftHand());
				handPos.scale(0.5f);
				limitedhandDistance = (handDistance > myTracker.getCurrentShoulderRef(1f))? handDistance : myTracker.getCurrentShoulderRef(1f);
				sim.addArrousalXclusivMagnetSphere("incubator", 0, handPos.x, handPos.y, handPos.z, 0, 50, limitedhandDistance, 5f, 4f);
				if(		myTracker.getCurrentRightHand().y > myTracker.getCurrentNeck().y &&
						myTracker.getCurrentLeftHand().y > myTracker.getCurrentNeck().y || !actionZone){
					if(actionZone)
						Debugger.getInstance().infoMessage(this.getClass(), "released incubator because hands over neck");
					else
						Debugger.getInstance().infoMessage(this.getClass(), "released incubator because leaving hands from action zone");
					sim.releaseIncubator();
					sim.removeMagnet("incubator");
					mode = MODE_WAITING;
				}
				break;
			case MODE_INCUBATE_CEO:
				handPos = myTracker.getCurrentRightHand();
				handPos.add(myTracker.getCurrentLeftHand());
				handPos.scale(0.5f);
				Debugger.getInstance().infoMessage(this.getClass(), "incubate CEO boids");
				sim.incubate(new PVector(handPos.x, handPos.y, handPos.z), sim.swarmProps.getIncubationSize(getSwarmID()), getSwarmID());
				limitedhandDistance = (handDistance > myTracker.getCurrentShoulderRef(1f))? handDistance : myTracker.getCurrentShoulderRef(1f);
				sim.addArrousalXclusivMagnetSphere("incubator", 0, handPos.x, handPos.y, handPos.z, 2, 20, handDistance, 5f, 2f);
				mode = MODE_INCUBATING_CEO;
				break;
			case MODE_INCUBATING_CEO:
				handPos = myTracker.getCurrentRightHand();
				handPos.add(myTracker.getCurrentLeftHand());
				handPos.scale(0.5f);
				limitedhandDistance = (handDistance > myTracker.getCurrentShoulderRef(1f))? handDistance : myTracker.getCurrentShoulderRef(1f);
				sim.addArrousalXclusivMagnetSphere("incubator", 0, handPos.x, handPos.y, handPos.z, 2, 20, handDistance, 5f, 1f);
				if(		myTracker.getCurrentRightHand().y > myTracker.getCurrentNeck().y &&
						myTracker.getCurrentLeftHand().y > myTracker.getCurrentNeck().y || !actionZone){
					if(actionZone)
						Debugger.getInstance().infoMessage(this.getClass(), "released CEO incubator because hands over neck");
					else
						Debugger.getInstance().infoMessage(this.getClass(), "released CEO incubator because leaving hands from action zone");
					sim.releaseIncubator();
					sim.removeMagnet("incubator");
					mode = MODE_WAITING;
				}
				break;
			case MODE_CALLING_BOIDS:
				mode = MODE_CONTROLLING_BOIDS;
				Debugger.getInstance().infoMessage(this.getClass(), "calling boids!");
				break;
			case MODE_CONTROLLING_BOIDS:
				float innerRadius = (handDistance > myTracker.getCurrentShoulderRef(1f))? myTracker.getCurrentShoulderRef(1f): handDistance;
				//Debugger.getInstance().infoMessage(this.getClass(), "limitedhandDistance: " + limitedhandDistance + " getCurrentShoulderRef: " + myTracker.getCurrentShoulderRef(1f));
				sim.addArrousalMagnetSphere("leftCaller", getSwarmID(), lh.x, lh.y, lh.z, 0, 50, innerRadius * 5, 5f, 4f);
				sim.addArrousalMagnetSphere("rightCaller", getSwarmID(), rh.x, rh.y, rh.z, 0, 50,  innerRadius * 5, 5f, 4f);
				boolean maxSpeed = myTracker.getCurrentLeftHandSpeed() > CALLING_SPEED_THRESHOLD ||  //exit if handspeed execeeds max speed
									myTracker.getCurrentRightHandSpeed() > CALLING_SPEED_THRESHOLD;
				if(maxSpeed || !actionZone ){
					if(maxSpeed)
						Debugger.getInstance().infoMessage(this.getClass(), "droped caller magnets due to speed");
					else
						Debugger.getInstance().infoMessage(this.getClass(), "droped caller magnets due to exit action zone");
					
					sim.removeMagnet("leftCaller");
					sim.removeMagnet("rightCaller");
					mode = MODE_WAITING;
				}
				break;
			}
		}else{
			switch(mode){
			case MODE_CONTROLLING_BOIDS:
				sim.removeMagnet("leftCaller");
				sim.removeMagnet("rightCaller");
				Debugger.getInstance().infoMessage(this.getClass(), "droped caller magnets because of skelton exit");
				mode = MODE_WAITING;
				break;
			case MODE_INCUBATING_BOIDS:
				sim.removeMagnet("incubator");
				sim.releaseIncubator();
				Debugger.getInstance().infoMessage(this.getClass(), "released incubator because of skelton exit");
				mode = MODE_WAITING;
				break;
			case MODE_INCUBATING_CEO:
				sim.removeMagnet("incubator");
				sim.releaseIncubator();
				Debugger.getInstance().infoMessage(this.getClass(), "released CEO incubator because of skelton exit");
				mode = MODE_WAITING;
				break;
			}
		}
	}
		
	public void acceptMessage(java.util.Date time, OSCMessage _message){
		messageQueue.addMessage((OSCMessage)_message);
	}

	private void executeMessages(){
		OSCMessage _message;
		while(messageQueue.hasMoreMessages()){
			_message = messageQueue.removeNextMessage();
			try{
				if(_message.getAddress().equals("/gesture/ceo"))
					ceoSwarm = (((Integer)(_message.getArguments()[0])).intValue() == 1)? true: false;
				if(_message.getAddress().equals("/gesture/incubate"))
					incubeGestureFactor = ((Float)(_message.getArguments()[0])).floatValue();
				if(_message.getAddress().equals("/gesture/calling"))
					callingGestureFactor = ((Float)(_message.getArguments()[0])).floatValue();
			}catch (ArrayIndexOutOfBoundsException e){
				Debugger.getInstance().warningMessage(this.getClass(), "OSC Message in wrong format: "+ _message.getAddress());
			}catch (ClassCastException e){
				Debugger.getInstance().warningMessage(this.getClass(), "OSC Message Data in wrong format: "+ _message.getAddress() + " -> " + e.getMessage());
			}
		}
				
	}

	/*
	 * If this class needs to be listening to osc messages, call this message
	 * after instantiation is done.
	 */
	public void addToOSCListener(){
		CommunicationHub.getInstance().addListener("/gesture/ceo", this);
		CommunicationHub.getInstance().addListener("/gesture/incubate", this);
		CommunicationHub.getInstance().addListener("/gesture/calling", this);
	}

}
