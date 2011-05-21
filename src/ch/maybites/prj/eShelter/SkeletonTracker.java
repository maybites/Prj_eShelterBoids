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

import ch.maybites.tools.MayRandom;

import com.illposed.osc.*;

public class SkeletonTracker implements OSCListener{
	private MessageQueue messageQueue;
	
    private ShaderManager _myShaderManager;
    private ShaderProgram _myShaderProgram;
    TexturePlugin myReflectionTexture;
    TexturePlugin myRefractionTexture;

    Skeleton[] skeletons;
	float h; // for color
		
	public int width, height, depth;
	
	private boolean showOutlines = false;
	
	private AbstractBin myRenderer;
	boolean skeletonActive = true;

	MayRandom random = new MayRandom();

	int size = 10;
	
	float cos;
	float sin;
	
	int currentID;
	
	BoidsSim sim;
	
	Line refLineY;
	
	private int randomSwarmID;
	
	int actionZoneBorderLeft, actionZoneBorderRight, actionZoneBorderBack, actionZoneBorderFront;
		
	SkeletonTracker(BoidsSim _sim) {
		sim = _sim;
			    
		depth = 600;
				
		actionZoneBorderLeft = 600;
		actionZoneBorderRight = -600;
		actionZoneBorderBack =  sim.borderBack;
		actionZoneBorderFront = sim.borderFront;
		
		messageQueue = new MessageQueue();
		
		skeletons = new Skeleton[size];
		for(int i = 0; i < skeletons.length; i++){
			skeletons[i] = new Skeleton(i);
			skeletons[i].setBoundaries(actionZoneBorderFront, actionZoneBorderBack, actionZoneBorderRight, actionZoneBorderLeft);
		}
		
		relposition(new Vector3f(210f, 2330f, -862f));
		refposition(new Vector3f(0f, -700, 200));
		refscale(new Vector3f(0.5f, 0.5f, 0.5f));							
	}
			
	public int getSwarmID(){
		return randomSwarmID;
	}
	
	private void setRandomSwarmID(){
		randomSwarmID = sim.swarmProps.getRandomSwarmID();
	}
	
	private void refscale(Vector3f _scale){
		for(Skeleton skel: skeletons){
			skel.refscale(_scale);
		}
	}
 	
	private void refposition(Vector3f _pos){
		for(Skeleton skel: skeletons){
			skel.refposition(_pos);
		}
	}
	
	private void relposition(Vector3f _pos){
		for(Skeleton skel: skeletons){
			skel.relposition(_pos);
		}
	}
 	
	public boolean currentIsActive(){
		if(skeletonActive){
			for(int i = 0; i < skeletons.length; i++){
				if(skeletons[i].isActive()){
					currentID = i;
					skeletons[i].testForActivity();
					return true;
				}
			}
		}
		setRandomSwarmID();
		return false;
	}
	
	public boolean checkIfInActionZone(Vector3f test){
		if(		test.x < actionZoneBorderLeft &&
				test.x > actionZoneBorderRight &&
				test.z < actionZoneBorderFront &&
				test.z > actionZoneBorderBack)
			return true;
		return false;
	}

	
	public float getCurrentShoulderRef(float _factor){
		return skeletons[currentID].getCurrentShoulderRef(_factor);
	}

	public Vector3f getCurrentHead(){
		return skeletons[currentID].getCurrentHead();
	}

	public Vector3f getCurrentNeck(){
		return skeletons[currentID].getCurrentNeck();
	}
	
	public Vector3f getCurrentTorso(){
		return skeletons[currentID].getCurrentTorso();
	}

	public Vector3f getCurrentRightShoulder(){
		return skeletons[currentID].getCurrentRightShoulder();
	}
	
	public Vector3f getCurrentLeftShoulder(){
		return skeletons[currentID].getCurrentLeftShoulder();
	}

	public Vector3f getCurrentRightHip(){
		return skeletons[currentID].getCurrentRightHip();
	}
	
	public Vector3f getCurrentLeftHip(){
		return skeletons[currentID].getCurrentLeftHip();
	}

	public Vector3f getCurrentRightElbow(){
		return skeletons[currentID].getCurrentRightElbow();
	}
	
	public Vector3f getCurrentLeftElbow(){
		return skeletons[currentID].getCurrentLeftElbow();
	}

	public Vector3f getCurrentRightHand(){
		return skeletons[currentID].getCurrentRightHand();
	}

	public float getCurrentRightHandSpeed(){
		return skeletons[currentID].getCurrentRightHandSpeed();
	}

	public Vector3f getCurrentLeftHand(){
		return skeletons[currentID].getCurrentLeftHand();
	}
	
	public float getCurrentLeftHandSpeed(){
		return skeletons[currentID].getCurrentLeftHandSpeed();
	}

	public Vector3f getCurrentLowerLeftArm(){
		return skeletons[currentID].getCurrentLowerLeftArm();
	}
	
	public Vector3f getCurrentLowerRightArm(){
		return skeletons[currentID].getCurrentLowerRightArm();
	}

	public Vector3f getCurrentUpperLeftArm(){
		return skeletons[currentID].getCurrentUperLeftArm();
	}
	
	public Vector3f getCurrentUpperRightArm(){
		return skeletons[currentID].getCurrentUpperRightArm();
	}

	public void showOutlines(int i){
	}
	
	public void noDisplay(){
	}
	
	void run() {
		executeMessages();
	}
		
	public void acceptMessage(java.util.Date time, OSCMessage _message){
		messageQueue.addMessage((OSCMessage)_message);
	}

	private void executeMessages(){
		OSCMessage _message;
		if(messageQueue.hasMoreMessages()){
			while(messageQueue.hasMoreMessages()){
				_message = messageQueue.removeNextMessage();
				try{
					if(_message.getAddress().equals("/skeleton/data")){
						for(Skeleton skel: skeletons){
							skel.parseSkeletonData(_message);
						}
					}
					else if(_message.getAddress().equals("/skeleton/active")){
						skeletonActive = (((Integer)(_message.getArguments()[0])).intValue() == 1)? true: false;
					}
					else if(_message.getAddress().equals("/skeleton/refscale"))
						refscale(	new Vector3f(((Float)(_message.getArguments()[0])).floatValue(),
								((Float)(_message.getArguments()[1])).floatValue(),
								((Float)(_message.getArguments()[2])).floatValue()));
					else if(_message.getAddress().equals("/skeleton/relposition"))
						relposition(new Vector3f(((Float)(_message.getArguments()[0])).floatValue(),
								((Float)(_message.getArguments()[1])).floatValue(),
								((Float)(_message.getArguments()[2])).floatValue()));
					else if(_message.getAddress().equals("/skeleton/refposition"))
						refposition(new Vector3f(((Float)(_message.getArguments()[0])).floatValue(),
								((Float)(_message.getArguments()[1])).floatValue(),
								((Float)(_message.getArguments()[2])).floatValue()));
				}catch (ArrayIndexOutOfBoundsException e){
					Debugger.getInstance().warningMessage(this.getClass(), "OSC Message in wrong format: "+ _message.getAddress());
				}catch (ClassCastException e){
					Debugger.getInstance().warningMessage(this.getClass(), "OSC Message Data in wrong format: "+ _message.getAddress() + " -> " + e.getMessage());
				}
			}
			for(Skeleton skel: skeletons){
				skel.newFrame();
				skel.testForActivity();
			}
		}
				
	}

	/*
	 * If this class needs to be listening to osc messages, call this message
	 * after instantiation is done.
	 */
	public void addToOSCListener(){
		CommunicationHub.getInstance().addListener("/skeleton/active", this);
		CommunicationHub.getInstance().addListener("/skeleton/data", this);
		CommunicationHub.getInstance().addListener("/skeleton/refscale", this);
		CommunicationHub.getInstance().addListener("/skeleton/relposition", this);
		CommunicationHub.getInstance().addListener("/skeleton/refposition", this);
	}

    public class ShaderMaterial
    implements MaterialPlugin {

	    public void begin(GLContext theRenderContext, Material theParent) {
	        /* enable shader */
	        _myShaderManager.enable(_myShaderProgram);
	
	        /* set uniform variables in shader */
	        _myShaderManager.setUniform(_myShaderProgram, "LightPos", 0.0f, 0.0f, 4.0f);
	        _myShaderManager.setUniform(_myShaderProgram, "BaseColor", 1.0f, 1.0f, 1.0f);
	        _myShaderManager.setUniform(_myShaderProgram, "EnvMap", 0);
	        _myShaderManager.setUniform(_myShaderProgram, "RefractionMap", 1);
	        _myShaderManager.setUniform(_myShaderProgram, "textureWidth", 512.0f);
	        _myShaderManager.setUniform(_myShaderProgram, "textureHeight", 512.0f);
	
	        _myShaderManager.setUniform(_myShaderProgram, "Depth", 0f);
	        _myShaderManager.setUniform(_myShaderProgram, "MixRatio", 0.5f);
	    }
	
	
	    public void end(GLContext theRenderContext, Material theParent) {
	        _myShaderManager.disable();
	    }
	}


}
