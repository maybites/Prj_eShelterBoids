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
import gestalt.model.ModelData;
import gestalt.model.ModelLoaderOBJ;
import gestalt.render.bin.AbstractBin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import javax.media.opengl.GL;

import ch.maybites.prj.eShelter.magnet.*;
import ch.maybites.tools.MayRandom;
import processing.core.*;

import com.illposed.osc.*;

public class BoidsSim implements OSCListener{
	private ArrayList<Boid> boids; // will hold the boids in this BoidList
	private ArrayList<Magnet> magnets; // will hold the magnets in this BoidList
	
	private ArrayList<Boid> incubator; // will hold the incubating boids
	
	private MessageQueue messageQueue;
	
    private ShaderManager _myShaderManager;
    private ShaderProgram _myShaderProgram;
    TexturePlugin myReflectionTexture;
    TexturePlugin myRefractionTexture;

	public SwarmParameters swarmProps;
	int[] swarmIDCounter;
	
	float h; // for color
	
	PVector pos;
	
	private int simID;
	private int otherSimID;

	public int width, height, depth;
	
	private boolean showOutlines = false;
	
	private int maxSize;

	private AbstractBin myRenderer;
	private Cube myInnerModel;

	MayRandom random = new MayRandom();

	int borderLeft, borderRight, borderTop, borderBottom, borderFront, borderBack;

	float warpThickness = 100;
	float warpStrength = .4f;
	float backReflThickness = 100;
	float backReflStrength = .5f;
	float topReflThickness = 100;
	float topReflStrength = -1f;
	float bottomReflThickness = 100;
	float bottomReflStrength = 1f;

	BoidsSim(int _width, int _height, int n, float ih) {
		swarmProps = SwarmParameters.getInstance();

		width = _width * 2;
		height = _height * 2;
		depth = 800;
		pos = new PVector();
		pos.set(0, 0, 0);
		
		borderLeft = width / 2;
		borderRight = - width/ 2;
		borderTop = height / 2;
		borderBottom = - height / 2;
		borderFront = depth / 2;
		borderBack = -depth / 2;
		
		maxSize = n;

		magnets = new ArrayList<Magnet>();
		messageQueue = new MessageQueue();
		h = ih;
		setupRenderer();
		
		boids = new ArrayList<Boid>();
		for (int i = 0; i < maxSize; i++){
			int randomType = swarmProps.getRandomSwarmID();
			Boid newBoid = new Boid(new PVector(0, 0, 0), new PVector(random.create(-1, 1), random.create(-1, 1), random.create(-1, 1)), randomType);
			newBoid.setShader(new ShaderMaterial(), myReflectionTexture);
			//if(i > maxSize/2)
			//	newBoid.kill();
			boids.add(newBoid);
		}
				
		simID = GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.SIM_ID, 1);
		otherSimID = GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.OTHERSIM_ID, 1);
	
		this.addMagnetCylinder("ceo", 1, 0, 0, 0, 2, 300, 500, 2f);
		setSystemEnergyFields(100, .4f, 100, .5f, 100, -1f, 100, 1f);
	}

	private void setSystemEnergyFields(
			float _warpThickness, 
			float _warpStrength, 
			float _backReflThickness, 
			float _backReflStrength, 
			float _topReflThickness, 
			float _topReflStrength,
			float _bottomReflThickness,
			float _bottomReflStrength){
		 warpThickness = _warpThickness;
		 warpStrength = _warpStrength;
		 backReflThickness = _backReflThickness;
		 backReflStrength = _backReflStrength;
		 topReflThickness = _topReflThickness;
		 topReflStrength = _topReflStrength;
		 bottomReflThickness = _bottomReflThickness;
		 bottomReflStrength = _bottomReflStrength;
		//warpfield 
//		addEnergyField("System_warp", 0, 0f, 0f, borderFront - warpThickness/2, width, height, warpThickness, 0f, 0f, warpStrength);
		//back reflectionfield
//		addEnergyField("System_backLeft", 0, width / 4, 0f, borderBack + backReflThickness/2, width / 2, height, backReflThickness, -backReflStrength * 3, 0f, backReflStrength);
//		addEnergyField("System_backRight", 0, -width / 4, 0f, borderBack + backReflThickness/2, width / 2, height, backReflThickness, backReflStrength * 3, 0f, backReflStrength);
		//top reflectionfield
//		addEnergyField("System_top", 2, 0f, borderTop - topReflThickness/2, 0, width, topReflThickness, depth, 0f, 0f, topReflStrength);
		//bottom reflectionfield
//		addEnergyField("System_bottom", 2, 0f, borderBottom + bottomReflThickness/2, 0, width, bottomReflThickness, depth, 0f, 0f, bottomReflStrength);
	}

	
	public void incubate(PVector _pos, int _size, int _type){
		swarmProps.incubateID(_type);
		if(incubator == null){
			incubator = new ArrayList<Boid>();
		} 
		addIncubatingBoids(_pos, _size - incubator.size(), 0);
		for(Boid b: incubator){
			if(b != null)
				b.applySwarmCharcteristics();
		}
		//Debugger.getInstance().infoMessage(this.getClass(), "incubate boids - size: " + incubator.size());
		
	}
	
	public void releaseIncubator(){
		for(Boid b: incubator){
			if(b != null)
				b.applyToSwarm(swarmProps.incubateSwarmType);
		}
		incubator.clear();
	}
	
	private void addIncubatingBoids(PVector _pos, int _number, int _type){
		if(_number > 0){
			for(int i = 0; i < _number; i++){
				incubator.add(addBoid(_type, _pos, new PVector(random.create(-1, 1), random.create(-1, 1), random.create(-1, 1))));
			}			
		} else if(_number < 0){
			for(int i = -_number -1; i >= 0; i--){
				removeBoid(incubator.remove(i));
			}			
		}		
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

		myInnerModel = Canvas.getInstance().getPlugin().drawablefactory().cube();

		myInnerModel.material().wireframe = true;

		myInnerModel.scale(width, height, depth);
		myInnerModel.position().set(pos.x, pos.y, pos.z);

	}
	
	public void showOutlines(int i){
		if(i == 1){
			myRenderer.add(myInnerModel);
			showOutlines = true;
		}
		else{
			myRenderer.remove(myInnerModel);
			showOutlines = false;
		}
		
		for (int j = 0; j < magnets.size(); j++){
			magnets.get(j).showOutlines(i);
		}
	}
	
	public void noDisplay(){
		myRenderer.remove(myInnerModel);
	}

	Boid addBoid(int _type, float posX, float posY, float posZ, float velX, float velY, float velZ) {
		return addBoid(_type, new PVector(posX, posY, posZ), new PVector(velX, velY, velZ));
	}
	
	Boid addBoid(int _type, PVector _pos, PVector _vel) {
		for(Boid b:boids){
			if(!b.isAlive){
				b.set(_pos, _vel, _type);
				return b;
			}
		}
		return null;
	}

	void removeAllBoids(){
		for(Boid b:boids){
			b.kill();
		}
	}
	
	void removeBoid(Boid b){
		if(b != null)
			b.kill();
	}

	void addMagnet(Magnet m) {
		magnets.add(m);
	}

	void removeAllMagnets(){
		for (int j = magnets.size() - 1; j >= 0; j--){
			if(!magnets.get(j).isSystemsID())
				magnets.remove(j).delete();
		}
	}
	
	void removeMagnet(String _id){
		for (int j = magnets.size() - 1; j >= 0; j--){
			if(magnets.get(j).isID(_id))
				magnets.remove(j).delete();
		}
	}
		
	void typeRandomizeAllBoids(){
		for (int j = boids.size() - 1; j >= 1; j--){
			boids.get(j).applyRandomSwarm();
		}
	}
	
	void addMagnetCylinder(String _id, int _swarmID, float _posX, float _posY, float _posZ, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce){
		boolean exists = false;
		for(Magnet mag: magnets){
			if(mag.isID(_id)){
				if(mag.getClass().getName().equals("ch.maybites.prj.eShelter.magnet.MagnetCylinder")){
					exists = true;
					MagnetCylinder temp = (MagnetCylinder) mag;
					temp.set(_swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
				}
			}
		}
		if(!exists){
			MagnetCylinder temp = new MagnetCylinder(_id, _swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
			magnets.add(temp); 
			if(showOutlines)
				temp.showOutlines(1);
		}
	}
	
	void addMagnetSphere(String _id, int _swarmID, float _posX, float _posY, float _posZ, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce){
		boolean exists = false;
		for(Magnet mag: magnets){
			if(mag.isID(_id)){
				if(mag.getClass().getName().equals("ch.maybites.prj.eShelter.magnet.MagnetSphere")){
					exists = true;
					MagnetSphere temp = (MagnetSphere) mag;
					temp.set(_swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
				}
			}
		}
		if(!exists){
			MagnetSphere temp = new MagnetSphere(_id, _swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
			magnets.add(temp); 
			if(showOutlines)
				temp.showOutlines(1);
		}
	}

	void addArrousalMagnetSphere(String _id, int _swarmID, float _posX, float _posY, float _posZ, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce, float _arrousal){
		boolean exists = false;
		for(Magnet mag: magnets){
			if(mag.isID(_id)){
				if(mag.getClass().getName().equals("ch.maybites.prj.eShelter.magnet.ArrousalMagnetSphere")){
					exists = true;
					ArrousalMagnetSphere temp = (ArrousalMagnetSphere) mag;
					temp.set(_swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce, _arrousal);
				}
			}
		}
		if(!exists){
			ArrousalMagnetSphere temp = new ArrousalMagnetSphere(_id, _swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce, _arrousal);
			magnets.add(temp); 
			if(showOutlines)
				temp.showOutlines(1);
		}
	}
	
	void addArrousalXclusivMagnetSphere(String _id, int _swarmID, float _posX, float _posY, float _posZ, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce, float _arrousal){
		boolean exists = false;
		for(Magnet mag: magnets){
			if(mag.isID(_id)){
				if(mag.getClass().getName().equals("ch.maybites.prj.eShelter.magnet.ArrousalXclusivMagnetSphere")){
					exists = true;
					ArrousalXclusivMagnetSphere temp = (ArrousalXclusivMagnetSphere) mag;
					temp.set(_swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce, _arrousal);
				}
			}
		}
		if(!exists){
			ArrousalXclusivMagnetSphere temp = new ArrousalXclusivMagnetSphere(_id, _swarmID, new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce, _arrousal);
			magnets.add(temp); 
			if(showOutlines)
				temp.showOutlines(1);
		}
	}

	void addEnergyField(String _id, int _swarmID, float _posX, float _posY, float _posZ, float _sizeX, float _sizeY, float _sizeZ, float _dirX, float _dirY, float _dirZ){
		boolean exists = false;
		for(Magnet mag: magnets){
			if(mag.isID(_id)){
				if(mag.getClass().getName().equals("ch.maybites.prj.eShelter.magnet.EnergyField")){
					exists = true;
					EnergyField temp = (EnergyField) mag;
					temp.set(_swarmID, new PVector(-_posX, _posY, _posZ), new PVector(_sizeX, _sizeY, _sizeZ), new PVector(_dirX, _dirY, _dirZ));
				}
			}
		}
		if(!exists){
			EnergyField temp = new EnergyField(_id, _swarmID, new PVector(-_posX, _posY, _posZ), new PVector(_sizeX, _sizeY, _sizeZ), new PVector(_dirX, _dirY, _dirZ));
			magnets.add(temp); 
			if(showOutlines)
				temp.showOutlines(1);
		}
	}

	void checkBounds(Boid boid) {
		if (boid.pos.x > borderLeft){
			if(boid.vel.x > 0){
				sendMirrorOSCMessage(boid);
				boid.vel.x *= -2.;
			}
			//boid.pos.x = borderRight;
		}
		if (boid.pos.x < borderRight){
			if(boid.vel.x < 0){
				sendMirrorOSCMessage(boid);
				boid.vel.x *= -2.;
			}
			//boid.pos.x = borderLeft;
		}
		if (boid.pos.y > borderTop){
			if(boid.vel.y > 0)
				boid.vel.y *= -2.;
			//boid.pos.y = borderBottom;
		}
		if (boid.pos.y < borderBottom){
			if(boid.vel.y < 0)
				boid.vel.y *= -2.;
			//boid.pos.y = borderTop;
		}
		if (boid.pos.z < borderBack){
			boid.pos.z = borderFront;
			//sendWarpOSCMessage(boid);
			//boid.delete();
		}
		if (boid.pos.z > borderFront){
			boid.pos.z = borderBack;
		}
	}
	
	void sendWarpOSCMessage(Boid b){
		CommunicationHub.getInstance().sendWarpOSCMessage(b, this.simID, this.otherSimID);		
	}
	
	void sendMirrorOSCMessage(Boid b){
		CommunicationHub.getInstance().sendMirrorOSCMessage(b, this.simID);		
	}

	public void resetSwarmPhysics(){
		swarmProps.reset();
	}
	
	public void changeSwarmPhysics(
			int swarmID,
			float _neighborhoodRadius, 
			float _maxSpeed, 
			float _maxSteerForce, 
			float _alignementDamper, 
			float _coherenceDamper, 
			float _repulseDamper, 
			float _repulseRadius){
		swarmProps.setPhysics(
				swarmID,
				_neighborhoodRadius, 
				_maxSpeed, 
				_maxSteerForce, 
				_alignementDamper, 
				_coherenceDamper,
				_repulseDamper,
				_repulseRadius);
	}

	private void killRandomBoids(int _couter){
		if(_couter > maxSize - maxSize/10){
			int swarmIDToKill = 0;
			int maxCounter = swarmIDCounter[swarmIDToKill];
			for(int i = 1; i < swarmIDCounter.length; i++){
				if(maxCounter <  swarmIDCounter[i]){
					maxCounter = swarmIDCounter[i];
					swarmIDToKill = i;
				}
			}

			for(Boid b:boids){
				if(b.isAlive && b.swarmID == swarmIDToKill){
					b.kill();
					return;
				}
			}
		}
	}
	
	void run(boolean aW) {
		// iterate through the list of boids
		for (Boid tempBoid: boids)		// iterate through the all of the boids
			if(tempBoid.isAlive)
			for (Boid tempBoid2: boids)//  and iterate through the rest of the boids
				if(tempBoid2.isAlive && !tempBoid2.equals(tempBoid))
					tempBoid2.calcFlock(tempBoid);
	}

	void render(PApplet canvas) {
		// iterate through the list of boids
		swarmIDCounter = new int[this.swarmProps.size()];
		int counter = 0;
		for (Boid tempBoid: boids){
			if(tempBoid.isAlive){
				swarmIDCounter[tempBoid.swarmID]++;
				counter++;
				tempBoid.calcFlockAcceleration();
				for (int j = 0; j < magnets.size(); j++){
					tempBoid.calcForceAcceleration(magnets.get(j));
				}
				tempBoid.scatter();
				tempBoid.applyAcceleration();
				checkBounds(tempBoid);
				tempBoid.applyTranslation();
				tempBoid.calcReset();
			}
		}
		killRandomBoids(counter);
		for (int j = 0; j < magnets.size(); j++){
			magnets.get(j).update();
		}
		executeMessages();
	}
	
	public void acceptMessage(java.util.Date time, OSCMessage _message){
		messageQueue.addMessage((OSCMessage)_message);
	}
	
	/**
	 *  /simulation/manager/boid/add <type(int)> <posX(float)> <posY(float)> <posZ(float) 
	 *											<velX(float)> <velY(float)> <velZ(float)> 
	 *											<radius(float)> <maxVel(float)> <maxAcc(float)> 
	 */

	/**
	 *  /simulation/manager/magnet/add/cylinder  <(String)id> 
	 *  	<(float)posX> <(float)posY>, <(float)_posZ>, 
	 *  	<(int)attractionType>, <(float)innerRadius>
	 *		<(float)outerRadius> <(float)maxAttractionForce> 
	 */
	
	/**
	 *  /simulation/manager/magnet/add/sphere  <(String)id> 
	 *  	<(float)posX> <(float)posY>, <(float)_posZ>, 
	 *  	<(int)attractionType>, <(float)innerRadius>
	 *		<(float)outerRadius> <(float)maxAttractionForce> 
	 */
	
	/**
	 *  /simulation/manager/magnet/remove/all 
	 *  /simulation"+oscID+"/manager/boid/addswarm <(int)number> <(int)type>
	 */
	
	/**
	 * /simulation/manager/magnet/system (float)_warpThickness (float)_warpStrength (float)_backReflThickness (float)_backReflStrength 
	 *		(float)_topReflThickness (float)_topReflStrength (float)_bottomReflThickness (float)_bottomReflStrength
	 */
	
	/**
	 *  /simulation/manager/boid/params <(float)neighborhoodRadius> <(float)maxSpeed> <(float)<maxSteerForce> 
	 */
	private void executeMessages(){
		OSCMessage _message;
		while(messageQueue.hasMoreMessages()){
			_message = messageQueue.removeNextMessage();
			try{
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/physics/reset"))
					resetSwarmPhysics();
				if(_message.getAddress().equals("/simulation"+simID+"/manager/magnet/remove/all"))
					removeAllMagnets();
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/randomize/all"))
					typeRandomizeAllBoids();
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/remove/all"))
					removeAllBoids();
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/addswarm")){
//					addBoids(((Integer)(_message.getArguments()[0])).intValue(),
//							((Integer)(_message.getArguments()[1])).intValue());
				}
				if(_message.getAddress().equals("/simulation"+simID+"/manager/showoutlines"))
					showOutlines(((Integer)(_message.getArguments()[0])).intValue());
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/physics"))
					changeSwarmPhysics(
							((Integer)(_message.getArguments()[0])).intValue(),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue());
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/add"))
					addBoid(
							((Integer)(_message.getArguments()[0])).intValue(),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue());
				if(_message.getAddress().equals("/simulation"+simID+"/manager/boid/warp")){
					addBoid(
							((Integer)(_message.getArguments()[0])).intValue(),
							((Float)(_message.getArguments()[1])).floatValue() * -1,
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue() - this.warpThickness - 20, //sets the new boid clear within the simbox
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue()* -1);
				}
				if(_message.getAddress().equals("/simulation"+simID+"/manager/magnet/remove/name"))
					removeMagnet((String)(_message.getArguments()[0]));
				if(_message.getAddress().equals("/simulation"+simID+"/manager/magnet/add/energyfield"))
					addEnergyField(
							(String)(_message.getArguments()[0]),
							((Integer)(_message.getArguments()[1])).intValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue(),
							((Float)(_message.getArguments()[8])).floatValue(),
							((Float)(_message.getArguments()[9])).floatValue(),
							((Float)(_message.getArguments()[10])).floatValue());
				if(_message.getAddress().equals("/simulation"+simID+"/manager/magnet/system"))
					setSystemEnergyFields(
							((Float)(_message.getArguments()[0])).floatValue(),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue());
				if(_message.getAddress().equals("/simulation"+simID+"/manager/magnet/add/cylinder"))
					addMagnetCylinder(
							((String)(_message.getArguments()[0])),
							((Integer)(_message.getArguments()[1])).intValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Integer)(_message.getArguments()[5])).intValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue(),
							((Float)(_message.getArguments()[8])).floatValue());
				if(_message.getAddress().equals("/simulation"+simID+"/manager/magnet/add/sphere"))
					addMagnetSphere(
							((String)(_message.getArguments()[0])),
							((Integer)(_message.getArguments()[1])).intValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Integer)(_message.getArguments()[5])).intValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue(),
							((Float)(_message.getArguments()[8])).floatValue());
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
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/warp", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/randomize/all", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/remove/all", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/physics", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/physics/reset", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/showoutlines", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/magnet/system", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/magnet/remove/name", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/magnet/remove/all", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/magnet/add/cylinder", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/magnet/add/sphere", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/magnet/add/energyfield", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/add", this);
		CommunicationHub.getInstance().addListener("/simulation"+simID+"/manager/boid/addswarm", this);
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
