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

public class Skeleton{
	
    private ShaderManager _myShaderManager;
    private ShaderProgram _myShaderProgram;
    TexturePlugin myReflectionTexture;
    TexturePlugin myRefractionTexture;

    public Line[] _mySNeck;
    public Line[] _mySLeftShoulder;
    public Line[] _mySUpperLeftArm;
    public Line[] _mySLowerLeftArm;
    public Line[] _mySUpperLeftTorso;
    public Line[] _mySLowerLeftTorso;
    public Line[] _mySUpperLeftLeg;
    public Line[] _mySLowerLeftLeg;
    public Line[] _mySRightShoulder;
    public Line[] _mySUpperRightArm;
    public Line[] _mySLowerRightArm;
    public Line[] _mySUpperRightTorso;
    public Line[] _mySLowerRightTorso;
    public Line[] _mySUpperRightLeg;
    public Line[] _mySLowerRightLeg;
    public Line[] _mySHip;
    public boolean[] _drawable;

	float h; // for color
	
	Vector3f relpos;
	Vector3f refpos;
	Vector3f refscale;
	Vector3f refrotate;

	boolean hasBeenUpdated = false;
	int frameDrops = 0;

	public int width, height, depth;
	
	private boolean isActive = false;
	
	private AbstractBin myRenderer;
		
	float cos;
	float sin;
	
	int storageSize = 10;
	int currentStorageID;
	
	int myID;
	float torsoBoundaryFront, torsoBoundaryBack, torsoBoundaryRight, torsoBoundaryLeft;
	
	Skeleton(int _id) {
		
	    _mySNeck = new Line[storageSize];
	    _mySLeftShoulder = new Line[storageSize];
	    _mySUpperLeftArm = new Line[storageSize];
	    _mySLowerLeftArm = new Line[storageSize];
	    _mySUpperLeftTorso = new Line[storageSize];
	    _mySLowerLeftTorso = new Line[storageSize];
	    _mySUpperLeftLeg = new Line[storageSize];
	    _mySLowerLeftLeg = new Line[storageSize];
	    _mySRightShoulder = new Line[storageSize];
	    _mySUpperRightArm = new Line[storageSize];
	    _mySLowerRightArm = new Line[storageSize];
	    _mySUpperRightTorso = new Line[storageSize];
	    _mySLowerRightTorso = new Line[storageSize];
	    _mySUpperRightLeg = new Line[storageSize];
	    _mySLowerRightLeg = new Line[storageSize];
	    _mySHip = new Line[storageSize];
	    _drawable = new boolean[storageSize];
	    
	    currentStorageID = 0;
		depth = 600;
		
		myID = _id;
				
		setupRenderer();
		
		relposition(new Vector3f(210f, 2330f, -862f));
		refposition(new Vector3f(0f, -700, 200));
		refscale(new Vector3f(0.5f, 0.5f, 0.5f));
		
		cos = (float)Math.cos(-1.3f);
		sin = (float)Math.sin(-1.3f);
						
	}

	private void setupRenderer() {
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);

        /* create shadermanager and a shaderprogram */
        _myShaderManager = Canvas.getInstance().getPlugin().drawablefactory().extensions().shadermanager();
        _myShaderProgram = _myShaderManager.createShaderProgram();
        _myShaderManager.attachVertexShader(_myShaderProgram,GlobalPreferences.getInstance().getStream("shader/RefractionReflectionShader.vs"));
        _myShaderManager.attachFragmentShader(_myShaderProgram, GlobalPreferences.getInstance().getStream("shader/RefractionReflectionShader.fs"));
        Canvas.getInstance().getPlugin().bin(Gestalt.BIN_FRAME_SETUP).add(_myShaderManager);

        myReflectionTexture = Canvas.getInstance().getPlugin().drawablefactory().texture();
        myReflectionTexture.load(Bitmaps.getBitmap(GlobalPreferences.getInstance().getAbsDataPath("shader/images/sky-reflection.png")));
        myReflectionTexture.setTextureUnit(GL.GL_TEXTURE0);

        Color color = new Color(1f, 1f, 1f);
                
        for(int i = 0; i < storageSize; i++){
    	    _mySNeck[i] = createLine(i, color);
    	    _mySLeftShoulder[i] = createLine(i, color);
    	    _mySUpperLeftArm[i] = createLine(i, color);
    	    _mySLowerLeftArm[i] = createLine(i, color);
    	    _mySUpperLeftTorso[i] = createLine(i, color);
    	    _mySLowerLeftTorso[i] = createLine(i, color);
    	    _mySUpperLeftLeg[i] = createLine(i, color);
    	    _mySLowerLeftLeg[i] = createLine(i, color);
    	    _mySRightShoulder[i] = createLine(i, color);
    	    _mySUpperRightArm[i] = createLine(i, color);
    	    _mySLowerRightArm[i] = createLine(i, color);
    	    _mySUpperRightTorso[i] = createLine(i, color);
    	    _mySLowerRightTorso[i] = createLine(i, color);
    	    _mySUpperRightLeg[i] = createLine(i, color);
    	    _mySLowerRightLeg[i] = createLine(i, color);
    	    _mySHip[i] = createLine(i, color);
    	    _drawable[i] = false;
        }
	}
	
	private Line createLine(int _id, Color _color){
		Line newLine = Canvas.getInstance().getPlugin().drawablefactory().line();
		newLine.points = new Vector3f[2];
		newLine.colors = new Color[2];
        for (int i = 0; i < newLine.points.length; i++) {
        	newLine.points[i] = new Vector3f(0f, 0f, 0f);
        	newLine.colors[i] = _color;
        }

        newLine.scale().scale(1f);
        newLine.position().set(0f, 0f, 0f);
        
        newLine.linewidth = 5;
        newLine.smooth = true;
        newLine.stipple = false;

        return newLine;
	}
		
	public void setBoundaries(float front, float back, float right, float left){
		torsoBoundaryFront = front;
		torsoBoundaryBack = back;
		torsoBoundaryRight = right;
		torsoBoundaryLeft = left;
	}
	
	private boolean checkIfDrawable(){
		Vector3f torso = getCurrentTorso();
		if(		torso.length() != 0 &&
				torso.x < torsoBoundaryLeft &&
				torso.x > torsoBoundaryRight &&
				torso.z > torsoBoundaryFront &&
				torso.z < torsoBoundaryBack)
			return true;
		return false;
	}
	
	private void removeCurrentFrameFromRenderer(){
		myRenderer.remove(_mySNeck[currentStorageID]);
		myRenderer.remove(_mySLeftShoulder[currentStorageID]);
		myRenderer.remove(_mySUpperLeftArm[currentStorageID]);
		myRenderer.remove(_mySLowerLeftArm[currentStorageID]);
		myRenderer.remove(_mySUpperLeftTorso[currentStorageID]);
		myRenderer.remove(_mySLowerLeftTorso[currentStorageID]);
		myRenderer.remove(_mySUpperLeftLeg[currentStorageID]);
		myRenderer.remove(_mySLowerLeftLeg[currentStorageID]);
		myRenderer.remove(_mySRightShoulder[currentStorageID]);
		myRenderer.remove(_mySUpperRightArm[currentStorageID]);
		myRenderer.remove(_mySLowerRightArm[currentStorageID]);
		myRenderer.remove(_mySUpperRightTorso[currentStorageID]);
		myRenderer.remove(_mySLowerRightTorso[currentStorageID]);
		myRenderer.remove(_mySUpperRightLeg[currentStorageID]);
		myRenderer.remove(_mySLowerRightLeg[currentStorageID]);
		myRenderer.remove(_mySHip[currentStorageID]);
	}
	
	public void refscale(Vector3f _scale){
		refscale = _scale;
	}
 	
	public void refposition(Vector3f _pos){
		refpos = _pos;
	}
	
	public void relposition(Vector3f _pos){
		relpos = _pos;
	}
 	
	private void setLineVertices(Line _line, float confidence1, Vector3f _pos1, float confidence2, Vector3f _pos2){
		Vector3f pos1 = new Vector3f(_pos1);
		Vector3f pos2 = new Vector3f(_pos2);
		pos1.sub(relpos);
		pos2.sub(relpos);
		pos1.set(pos1.x, pos1.y * cos - pos1.z * sin,  pos1.y * sin + pos1.z * cos);
		pos2.set(pos2.x, pos2.y * cos - pos2.z * sin,  pos2.y * sin + pos2.z * cos);
		pos1.scale(refscale);
		pos2.scale(refscale);
		pos1.add(refpos);
		pos2.add(refpos);
		
		_line.points[0].set(pos1.x, pos1.y, pos1.z);
		_line.points[1].set(pos2.x, pos2.y, pos2.z);

		if(confidence1 > 0.5f && confidence2 > 0.5f){
	        if(myRenderer.find(_line) == -1){
	        	myRenderer.add(_line);
	        }
		}else{
			myRenderer.remove(_line);
		}
	}

	public void testForActivity(){
		if(frameDrops < storageSize)
			if(checkIfDrawable())
				isActive = true;
		else
			isActive = false;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public float getCurrentShoulderRef(float _factor){
		return _mySRightShoulder[currentStorageID].points[1].distance(_mySLeftShoulder[currentStorageID].points[1]) * _factor;
	}

	public Vector3f getCurrentHead(){
		return new Vector3f(_mySNeck[currentStorageID].points[0]);
	}

	public Vector3f getCurrentNeck(){
		return new Vector3f(_mySNeck[currentStorageID].points[1]);
	}
	
	public Vector3f getCurrentTorso(){
		return new Vector3f(_mySUpperLeftTorso[currentStorageID].points[0]);
	}
	
	private Vector3f getPreviousTorso(){
		return new Vector3f(_mySUpperLeftTorso[getPreviousStorageID()].points[0]);
	}

	public Vector3f getCurrentRightShoulder(){
		return new Vector3f(_mySUpperRightArm[currentStorageID].points[0]);
	}
	
	public Vector3f getCurrentLeftShoulder(){
		return new Vector3f(_mySUpperLeftArm[currentStorageID].points[0]);
	}

	public Vector3f getCurrentRightElbow(){
		return new Vector3f(_mySUpperRightArm[currentStorageID].points[1]);
	}
	
	public Vector3f getCurrentLeftElbow(){
		return new Vector3f(_mySUpperLeftArm[currentStorageID].points[1]);
	}

	public Vector3f getCurrentRightHand(){
		return new Vector3f(_mySLowerRightArm[currentStorageID].points[1]);
	}
	
	public float getCurrentRightHandSpeed(){
		Vector3f speed = new Vector3f(_mySLowerRightArm[currentStorageID].points[1]);
		speed.sub(_mySLowerRightArm[getPreviousStorageID()].points[1]);
		return speed.length();
	}
	
	public Vector3f getCurrentLeftHand(){
		return new Vector3f(_mySLowerLeftArm[currentStorageID].points[1]);
	}
	
	public float getCurrentLeftHandSpeed(){
		Vector3f speed = new Vector3f(_mySLowerLeftArm[currentStorageID].points[1]);
		speed.sub(_mySLowerLeftArm[getPreviousStorageID()].points[1]);
		return speed.length();
	}

	public Vector3f getCurrentLowerLeftArm(){
		Vector3f send = getCurrentLeftHand();
		send.sub(getCurrentLeftElbow());
		return send;
	}
	
	public Vector3f getCurrentLowerRightArm(){
		Vector3f send = getCurrentRightHand();
		send.sub(getCurrentRightElbow());
		return send;
	}

	public Vector3f getCurrentUperLeftArm(){
		Vector3f send = getCurrentLeftElbow();
		send.sub(getCurrentLeftShoulder());
		return send;
	}
	
	public Vector3f getCurrentUpperRightArm(){
		Vector3f send = getCurrentRightElbow();
		send.sub(getCurrentRightShoulder());
		return send;
	}

	public void showOutlines(int i){
	}
	
	public void noDisplay(){
	}
	
	
	private void setNextStorageID(){
		currentStorageID = (currentStorageID + 1) % storageSize; 
	}

	private int getNextStorageID(){
		return (currentStorageID + 1) % storageSize; 
	}

	private int getPreviousStorageID(){
		return (storageSize + currentStorageID - 1) % storageSize; 
	}

	public void parseSkeletonData(OSCMessage _message){
		if(_message.getAddress().equals("/skeleton/data")){
			int _id = ((Integer)(_message.getArguments()[0])).intValue();
			if(_id == myID){
				removeCurrentFrameFromRenderer(); // first remove the current frame from the renderer
				setNextStorageID();
				float headConfidence = ((Float)(_message.getArguments()[1])).floatValue();
				Vector3f head = new Vector3f(
						((Float)(_message.getArguments()[2])).floatValue(), 
						((Float)(_message.getArguments()[3])).floatValue(),
						((Float)(_message.getArguments()[4])).floatValue());
				float neckConfidence = ((Float)(_message.getArguments()[5])).floatValue();
				Vector3f neck = new Vector3f(
						((Float)(_message.getArguments()[6])).floatValue(), 
						((Float)(_message.getArguments()[7])).floatValue(),
						((Float)(_message.getArguments()[8])).floatValue());
				float leftShoulderConfidence = ((Float)(_message.getArguments()[9])).floatValue();
				Vector3f leftShoulder = new Vector3f(
						((Float)(_message.getArguments()[10])).floatValue(), 
						((Float)(_message.getArguments()[11])).floatValue(),
						((Float)(_message.getArguments()[12])).floatValue());
				float leftElbowConfidence = ((Float)(_message.getArguments()[13])).floatValue();
				Vector3f leftElbow = new Vector3f(
						((Float)(_message.getArguments()[14])).floatValue(), 
						((Float)(_message.getArguments()[15])).floatValue(),
						((Float)(_message.getArguments()[16])).floatValue());
				float leftHandConfidence = ((Float)(_message.getArguments()[17])).floatValue();
				Vector3f leftHand = new Vector3f(
						((Float)(_message.getArguments()[18])).floatValue(), 
						((Float)(_message.getArguments()[19])).floatValue(),
						((Float)(_message.getArguments()[20])).floatValue());
				float rightShoulderConfidence = ((Float)(_message.getArguments()[21])).floatValue();
				Vector3f rightShoulder = new Vector3f(
						((Float)(_message.getArguments()[22])).floatValue(), 
						((Float)(_message.getArguments()[23])).floatValue(),
						((Float)(_message.getArguments()[24])).floatValue());
				float rightElbowConfidence = ((Float)(_message.getArguments()[25])).floatValue();
				Vector3f rightElbow = new Vector3f(
						((Float)(_message.getArguments()[26])).floatValue(), 
						((Float)(_message.getArguments()[27])).floatValue(),
						((Float)(_message.getArguments()[28])).floatValue());
				float rightHandConfidence = ((Float)(_message.getArguments()[29])).floatValue();
				Vector3f rightHand = new Vector3f(
						((Float)(_message.getArguments()[30])).floatValue(), 
						((Float)(_message.getArguments()[31])).floatValue(),
						((Float)(_message.getArguments()[32])).floatValue());
				float torsoConfidence = ((Float)(_message.getArguments()[33])).floatValue();
				Vector3f torso = new Vector3f(
						((Float)(_message.getArguments()[34])).floatValue(), 
						((Float)(_message.getArguments()[35])).floatValue(),
						((Float)(_message.getArguments()[36])).floatValue());
				float leftHipConfidence = ((Float)(_message.getArguments()[37])).floatValue();
				Vector3f leftHip = new Vector3f(
						((Float)(_message.getArguments()[38])).floatValue(), 
						((Float)(_message.getArguments()[39])).floatValue(),
						((Float)(_message.getArguments()[40])).floatValue());
				float leftKneeConfidence = ((Float)(_message.getArguments()[41])).floatValue();
				Vector3f leftKnee = new Vector3f(
						((Float)(_message.getArguments()[42])).floatValue(), 
						((Float)(_message.getArguments()[43])).floatValue(),
						((Float)(_message.getArguments()[44])).floatValue());
				float leftFootConfidence = ((Float)(_message.getArguments()[45])).floatValue();
				Vector3f leftFoot = new Vector3f(
						((Float)(_message.getArguments()[46])).floatValue(), 
						((Float)(_message.getArguments()[47])).floatValue(),
						((Float)(_message.getArguments()[48])).floatValue());
				float rightHipConfidence = ((Float)(_message.getArguments()[49])).floatValue();
				Vector3f rightHip = new Vector3f(
						((Float)(_message.getArguments()[50])).floatValue(), 
						((Float)(_message.getArguments()[51])).floatValue(),
						((Float)(_message.getArguments()[52])).floatValue());
				float rightKneeConfidence = ((Float)(_message.getArguments()[53])).floatValue();
				Vector3f rightKnee = new Vector3f(
						((Float)(_message.getArguments()[54])).floatValue(), 
						((Float)(_message.getArguments()[55])).floatValue(),
						((Float)(_message.getArguments()[56])).floatValue());
				float rightFootConfidence = ((Float)(_message.getArguments()[57])).floatValue();
				Vector3f rightFoot = new Vector3f(
						((Float)(_message.getArguments()[58])).floatValue(), 
						((Float)(_message.getArguments()[59])).floatValue(),
						((Float)(_message.getArguments()[60])).floatValue());
				
				setLineVertices(_mySNeck[currentStorageID],headConfidence, head, neckConfidence, neck);
				setLineVertices(_mySRightShoulder[currentStorageID], neckConfidence, neck, rightShoulderConfidence, rightShoulder);
				setLineVertices(_mySUpperRightArm[currentStorageID], rightShoulderConfidence, rightShoulder, rightElbowConfidence, rightElbow);
				setLineVertices(_mySLowerRightArm[currentStorageID], rightElbowConfidence, rightElbow, rightHandConfidence, rightHand);
				setLineVertices(_mySLeftShoulder[currentStorageID], neckConfidence, neck, leftShoulderConfidence, leftShoulder);
				setLineVertices(_mySUpperLeftArm[currentStorageID], leftShoulderConfidence, leftShoulder, leftElbowConfidence, leftElbow);
				setLineVertices(_mySLowerLeftArm[currentStorageID], leftElbowConfidence, leftElbow, leftHandConfidence, leftHand);
				setLineVertices(_mySUpperLeftTorso[currentStorageID], torsoConfidence, torso, leftShoulderConfidence, leftShoulder);
				setLineVertices(_mySLowerLeftTorso[currentStorageID], torsoConfidence, torso, leftHipConfidence, leftHip);
				setLineVertices(_mySUpperRightTorso[currentStorageID] , torsoConfidence, torso, rightShoulderConfidence, rightShoulder);
				setLineVertices(_mySLowerRightTorso[currentStorageID], torsoConfidence, torso, rightHipConfidence, rightHip);
				setLineVertices(_mySUpperLeftLeg[currentStorageID], leftHipConfidence, leftHip, leftKneeConfidence, leftKnee);
				setLineVertices(_mySLowerLeftLeg[currentStorageID], leftKneeConfidence, leftKnee, leftFootConfidence, leftFoot);
				setLineVertices(_mySUpperRightLeg[currentStorageID] , rightHipConfidence, rightHip, rightKneeConfidence, rightKnee);
				setLineVertices(_mySLowerRightLeg[currentStorageID] , rightKneeConfidence, rightKnee, rightFootConfidence, rightFoot);
				setLineVertices(_mySHip[currentStorageID] , leftHipConfidence, leftHip, rightHipConfidence, rightHip);
				
				if(!checkIfDrawable()) //after all the transformations!!!
					removeCurrentFrameFromRenderer();
				
				hasBeenUpdated = true;
			}
		}
	}
	
	/**
	 * is beeing called each time a set of new tracking frames arrive from the tracking software
	 */
	public void newFrame() {
		if(hasBeenUpdated && getCurrentTorso().distance(getPreviousTorso()) != 0)
			frameDrops = 0;
		else
			frameDrops++;
		hasBeenUpdated = false;
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
