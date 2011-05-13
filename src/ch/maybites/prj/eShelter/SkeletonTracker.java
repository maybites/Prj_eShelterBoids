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

public class SkeletonTracker implements OSCListener{
	private MessageQueue messageQueue;
	
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
	
	public int width, height, depth;
	
	private boolean showOutlines = false;
	
	private AbstractBin myRenderer;
	
	int size = 10;
	
	float cos;
	float sin;
	
	int currentID;
	
	BoidsSim sim;
	
	Line refLineY;
		
	SkeletonTracker(BoidsSim _sim) {
		sim = _sim;
		
		
	    _mySNeck = new Line[size];
	    _mySLeftShoulder = new Line[size];
	    _mySUpperLeftArm = new Line[size];
	    _mySLowerLeftArm = new Line[size];
	    _mySUpperLeftTorso = new Line[size];
	    _mySLowerLeftTorso = new Line[size];
	    _mySUpperLeftLeg = new Line[size];
	    _mySLowerLeftLeg = new Line[size];
	    _mySRightShoulder = new Line[size];
	    _mySUpperRightArm = new Line[size];
	    _mySLowerRightArm = new Line[size];
	    _mySUpperRightTorso = new Line[size];
	    _mySLowerRightTorso = new Line[size];
	    _mySUpperRightLeg = new Line[size];
	    _mySLowerRightLeg = new Line[size];
	    _mySHip = new Line[size];
	    _drawable = new boolean[size];
	    
		depth = 600;
				
		messageQueue = new MessageQueue();
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

        Color[] color = new Color[size];
        
        refLineY = Canvas.getInstance().getPlugin().drawablefactory().line();
        refLineY.points = new Vector3f[2];
        refLineY.colors = new Color[2];
        for (int i = 0; i < refLineY.points.length; i++) {
        	refLineY.colors[i] = new Color(1f, 1f, 1f);
        }
    	refLineY.points[0] = new Vector3f(0f, 100f, 0f);
    	refLineY.points[1] = new Vector3f(0f, -100f, 0f);
    	myRenderer.add(refLineY);
        
        for(int i = 0; i < size; i++){
        	color[i] = new Color(1f, 1f, 1f);
        	
    	    _mySNeck[i] = createLine(i, color[i]);
    	    _mySLeftShoulder[i] = createLine(i, color[i]);
    	    _mySUpperLeftArm[i] = createLine(i, color[i]);
    	    _mySLowerLeftArm[i] = createLine(i, color[i]);
    	    _mySUpperLeftTorso[i] = createLine(i, color[i]);
    	    _mySLowerLeftTorso[i] = createLine(i, color[i]);
    	    _mySUpperLeftLeg[i] = createLine(i, color[i]);
    	    _mySLowerLeftLeg[i] = createLine(i, color[i]);
    	    _mySRightShoulder[i] = createLine(i, color[i]);
    	    _mySUpperRightArm[i] = createLine(i, color[i]);
    	    _mySLowerRightArm[i] = createLine(i, color[i]);
    	    _mySUpperRightTorso[i] = createLine(i, color[i]);
    	    _mySLowerRightTorso[i] = createLine(i, color[i]);
    	    _mySUpperRightLeg[i] = createLine(i, color[i]);
    	    _mySLowerRightLeg[i] = createLine(i, color[i]);
    	    _mySHip[i] = createLine(i, color[i]);
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
        //_myLine.setStipplePattern("1100110011001100");

        /* add line to renderer */
        return newLine;
	}

	
	private void setCurrentID(int _id){
		if(_mySUpperLeftTorso[_id].points[0].length() != 0){
			currentID = _id;
		}
	}
	
	private void setDrawable(int _id){
//		Debugger.getInstance().infoMessage(this.getClass(), "torso: "+_mySUpperLeftTorso[_id].points[0].z);
		if(_mySUpperLeftTorso[_id].points[0].length() != 0 &&
				_mySUpperLeftTorso[_id].points[0].z < sim.borderBack && 
				_mySUpperLeftTorso[_id].points[0].z > sim.borderFront){
			_drawable[_id] = true;
			currentID = _id;
		}else{
			myRenderer.remove(_mySNeck[_id]);
			myRenderer.remove(_mySLeftShoulder[_id]);
			myRenderer.remove(_mySUpperLeftArm[_id]);
			myRenderer.remove(_mySLowerLeftArm[_id]);
			myRenderer.remove(_mySUpperLeftTorso[_id]);
			myRenderer.remove(_mySLowerLeftTorso[_id]);
			myRenderer.remove(_mySUpperLeftLeg[_id]);
			myRenderer.remove(_mySLowerLeftLeg[_id]);
			myRenderer.remove(_mySRightShoulder[_id]);
			myRenderer.remove(_mySUpperRightArm[_id]);
			myRenderer.remove(_mySLowerRightArm[_id]);
			myRenderer.remove(_mySUpperRightTorso[_id]);
			myRenderer.remove(_mySLowerRightTorso[_id]);
			myRenderer.remove(_mySUpperRightLeg[_id]);
			myRenderer.remove(_mySLowerRightLeg[_id]);
			myRenderer.remove(_mySHip[_id]);
			_drawable[_id] = false;
		}
	}
	
	private void refscale(Vector3f _scale){
		refscale = _scale;
	}
 	
	private void refposition(Vector3f _pos){
		refpos = _pos;
	}
	
	private void relposition(Vector3f _pos){
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

	public boolean currentIsActive(){
		return _drawable[currentID];
	}
	
	public float getCurrentShoulderRef(float _factor){
		return _mySRightShoulder[currentID].points[1].distance(_mySLeftShoulder[currentID].points[1]) * _factor;
	}

	public Vector3f getCurrentHead(){
		return new Vector3f(_mySNeck[currentID].points[0]);
	}

	public Vector3f getCurrentNeck(){
		return new Vector3f(_mySNeck[currentID].points[1]);
	}
	
	public Vector3f getCurrentTorso(){
		return new Vector3f(_mySUpperLeftTorso[currentID].points[0]);
	}

	public Vector3f getCurrentRightShoulder(){
		return new Vector3f(_mySUpperRightArm[currentID].points[0]);
	}
	
	public Vector3f getCurrentLeftShoulder(){
		return new Vector3f(_mySUpperLeftArm[currentID].points[0]);
	}

	public Vector3f getCurrentRightElbow(){
		return new Vector3f(_mySUpperRightArm[currentID].points[1]);
	}
	
	public Vector3f getCurrentLeftElbow(){
		return new Vector3f(_mySUpperLeftArm[currentID].points[1]);
	}

	public Vector3f getCurrentRightHand(){
		return new Vector3f(_mySLowerRightArm[currentID].points[1]);
	}
	
	public Vector3f getCurrentLeftHand(){
		return new Vector3f(_mySLowerLeftArm[currentID].points[1]);
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

	void run() {
		executeMessages();
	}
		
	public void acceptMessage(java.util.Date time, OSCMessage _message){
		messageQueue.addMessage((OSCMessage)_message);
	}

	private void executeMessages(){
		OSCMessage _message;
		while(messageQueue.hasMoreMessages()){
			_message = messageQueue.removeNextMessage();
			try{
				if(_message.getAddress().equals("/skeleton/data")){
					int _id = ((Integer)(_message.getArguments()[0])).intValue();
					//Debugger.getInstance().infoMessage(this.getClass(),
					//		"got osc message length: " +_message.getArguments().length);
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
					
					setLineVertices(_mySNeck[_id],headConfidence, head, neckConfidence, neck);
					setLineVertices(_mySRightShoulder[_id], neckConfidence, neck, rightShoulderConfidence, rightShoulder);
					setLineVertices(_mySUpperRightArm[_id], rightShoulderConfidence, rightShoulder, rightElbowConfidence, rightElbow);
					setLineVertices(_mySLowerRightArm[_id], rightElbowConfidence, rightElbow, rightHandConfidence, rightHand);
					setLineVertices(_mySLeftShoulder[_id], neckConfidence, neck, leftShoulderConfidence, leftShoulder);
					setLineVertices(_mySUpperLeftArm[_id], leftShoulderConfidence, leftShoulder, leftElbowConfidence, leftElbow);
					setLineVertices(_mySLowerLeftArm[_id], leftElbowConfidence, leftElbow, leftHandConfidence, leftHand);
					setLineVertices(_mySUpperLeftTorso[_id], torsoConfidence, torso, leftShoulderConfidence, leftShoulder);
					setLineVertices(_mySLowerLeftTorso[_id], torsoConfidence, torso, leftHipConfidence, leftHip);
					setLineVertices(_mySUpperRightTorso[_id] , torsoConfidence, torso, rightShoulderConfidence, rightShoulder);
					setLineVertices(_mySLowerRightTorso[_id], torsoConfidence, torso, rightHipConfidence, rightHip);
					setLineVertices(_mySUpperLeftLeg[_id], leftHipConfidence, leftHip, leftKneeConfidence, leftKnee);
					setLineVertices(_mySLowerLeftLeg[_id], leftKneeConfidence, leftKnee, leftFootConfidence, leftFoot);
					setLineVertices(_mySUpperRightLeg[_id] , rightHipConfidence, rightHip, rightKneeConfidence, rightKnee);
					setLineVertices(_mySLowerRightLeg[_id] , rightKneeConfidence, rightKnee, rightFootConfidence, rightFoot);
					setLineVertices(_mySHip[_id] , leftHipConfidence, leftHip, rightHipConfidence, rightHip);
					
					setDrawable(_id); //after all the transformations!!!
					//setCurrentID(_id);
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
				
	}

	/*
	 * If this class needs to be listening to osc messages, call this message
	 * after instantiation is done.
	 */
	public void addToOSCListener(){
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
