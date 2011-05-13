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
	
	GestureScanner(SkeletonTracker _tracker, BoidsSim _sim) {
		messageQueue = new MessageQueue();
		myTracker = _tracker;
		sim = _sim;
	}

	private boolean isInAxis(float _v1, float _v2, float v3){
		
		return true;
	}
	
	private boolean trackGestureCallBoids(float _angle1, float _angle2){
		if(myTracker.getCurrentLowerLeftArm().angle(myTracker.getCurrentLowerRightArm()) < _angle1){
			if(myTracker.getCurrentLowerLeftArm().angle(myTracker.getCurrentLowerRightArm()) < _angle2){
				Vector3f cross1 = myTracker.getCurrentUperLeftArm();
				cross1.cross(myTracker.getCurrentLowerLeftArm());
				Vector3f cross2 = myTracker.getCurrentUpperRightArm();
				cross2.cross(myTracker.getCurrentLowerRightArm());
				cross2.scale(-1.f);
				if(cross1.angle(cross2) < 0.6f){
					return true;
				}
			}
		}
		return false;
	}
	
	static final int MODE_WAITING = 0;
	static final int MODE_CALLING_BOIDS = 1;
	static final int MODE_CONTROLLING_BOIDS = 2;
	
	int mode = 0;
	boolean cond_callingBoidsLeft = false;
	boolean cond_callingBoidsRight = false;
	
	void setMode(int _mode){
		if(mode == MODE_WAITING){
			mode = _mode;
		}
	}
	
	void run() {
		executeMessages();
		Vector3f lh = myTracker.getCurrentLeftHand();
		Vector3f rh = myTracker.getCurrentRightHand();
		Vector3f ls = myTracker.getCurrentLeftShoulder();
		Vector3f rs = myTracker.getCurrentRightShoulder();
		Vector3f lel = myTracker.getCurrentLeftElbow();
		Vector3f rel = myTracker.getCurrentRightElbow();
		Vector3f neck = myTracker.getCurrentNeck();
		Vector3f head = myTracker.getCurrentHead();
		if(myTracker.currentIsActive()){
			if(trackGestureCallBoids(0.6f, 0.6f)){
				//Debugger.getInstance().infoMessage(this.getClass(), "action!!");
				setMode(MODE_CALLING_BOIDS);
			}
			//check for parallel upperarms
			
			if(myTracker.getCurrentRightHand().distance(myTracker.getCurrentLeftHand()) < myTracker.getCurrentShoulderRef(1.5f)){
			}
		}
		switch(mode){
		case MODE_CALLING_BOIDS:
			mode = MODE_CONTROLLING_BOIDS;
			cond_callingBoidsLeft = true;
			cond_callingBoidsRight = true;
			break;
		case MODE_CONTROLLING_BOIDS:
			if(!cond_callingBoidsLeft && !cond_callingBoidsRight){
				setMode(MODE_WAITING);
			}
			if(cond_callingBoidsLeft){
				sim.addArrousalMagnetSphere("leftCaller", 0, lh.x, lh.y, lh.z, 0, 50, 500, 5f, 4f);
			}else{
				sim.removeMagnet("leftCaller");
			}
			if(cond_callingBoidsRight){
				sim.addArrousalMagnetSphere("rightCaller", 0, rh.x, rh.y, rh.z, 0, 50, 500,	5f, 4f);
			}else{
				sim.removeMagnet("rightCaller");
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
				/**
				if(_message.getAddress().equals("/skeleton/refscale"))
					refscale(	new Vector3f(((Float)(_message.getArguments()[0])).floatValue(),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue()));
				**/
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
		CommunicationHub.getInstance().addListener("/gesture/data", this);
	}

}
