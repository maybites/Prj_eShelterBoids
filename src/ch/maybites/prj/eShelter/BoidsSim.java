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
import gestalt.render.bin.AbstractBin;

import java.util.*;

import ch.maybites.prj.eShelter.magnet.*;
import processing.core.*;

import com.illposed.osc.*;

public class BoidsSim implements OSCListener{
	private ArrayList<Boid> boids; // will hold the boids in this BoidList
	private ArrayList<Magnet> magnets; // will hold the magnets in this BoidList
	private MessageQueue messageQueue;
	
	float h; // for color
	
	PVector pos;
	
	private int oscID;

	public int width, height, depth;
	
	private int maxSize;

	private AbstractBin myRenderer;
	private Cube myInnerModel;
	
	int borderLeft, borderRight, borderTop, borderBottom, borderFront, borderBack;

	BoidsSim(int _width, int _height, int n, float ih) {
		width = _width * 2;
		height = _height * 2;
		depth = 600;
		pos = new PVector();
		pos.set(0, 0, 0);
		
		borderLeft = width / 2;
		borderRight = - width/ 2;
		borderTop = height / 2;
		borderBottom = - height / 2;
		borderFront = -depth / 2;
		borderBack = depth / 2;
		
		maxSize = n;
		boids = new ArrayList<Boid>();
		magnets = new ArrayList<Magnet>();
		messageQueue = new MessageQueue();
		h = ih;
		addBoids(maxSize, 0);
		
		setupRenderer();
		
		oscID = GlobalPreferences.getInstance().getOSC_ID();
		
		//magnets.add(new MagnetSphere(new PVector(0, 0, 700), MagnetSphere.INNER_ATTRACTION_LINEAR, 80, 220, 1.0f));
		magnets.add(new MagnetCylinder(new PVector(width / 2, 0, 600), MagnetCylinder.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		//magnets.add(new MagnetCylinder(new PVector(width / 4, 0, 400), MagnetCylinder.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		//magnets.add(new MagnetCylinder(new PVector(-width / 2, 0, 400), MagnetCylinder.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		//magnets.add(new MagnetCylinder(new PVector(-width / 4, 0, 500), MagnetCylinder.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		//magnets.add(new MagnetCylinder(new PVector(0, 0, 500), MagnetCylinder.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		//magnets.add(new EnergyField(new PVector(200, 0, 0), new PVector(200, 50, 50), new PVector(-3, 0, 0)));
		//magnets.add(new EnergyField(new PVector(-200, 0, 0), new PVector(50, 200, 50), new PVector(0, -3, 0)));
	}

	private void setupRenderer() {
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);

		myInnerModel = Canvas.getInstance().getPlugin().drawablefactory().cube();

		// TexturePlugin myTexture =
		// Canvas.getInstance().getPlugin().drawablefactory().texture();
		// myTexture.load(Bitmaps.getBitmap(Resource.getStream("demo/common/styrofoamplates.png")));
		// myTexture.setWrapMode(Canvas.getInstance().getPlugin().TEXTURE_WRAPMODE_CLAMP);
		// myModel.mesh().material().addPlugin(myTexture);

		myInnerModel.material().wireframe = true;

		myInnerModel.scale(width, height, depth);
		myInnerModel.position().set(pos.x, pos.y, pos.z);

	}
	
	public void showOutlines(int i){
		if(i == 1)
			myRenderer.add(myInnerModel);
		else
			myRenderer.remove(myInnerModel);
		
		for (int j = 0; j < magnets.size(); j++){
			magnets.get(j).showOutlines(i);
		}
	}
	
	public void noDisplay(){
		myRenderer.remove(myInnerModel);
	}

	void addBoids(int _number, int _type) {
		for (int i = 0; i < _number; i++){
			boids.add(new Boid(new PVector(0, 0, 0), _type));
		}
	}

	void addBoid(int _type, float posX, float posY, float posZ, float velX, float velY, float velZ, float _radius, float _maxVel, float _maxAcc) {
		boids.add(new Boid(new PVector(posX, posY, posZ), new PVector(velX, velY, velZ), _radius, _maxVel, _maxAcc, _type));
	}

	void addBoid(Boid b) {
		boids.add(b);
	}

	void addMagnet(Magnet m) {
		magnets.add(m);
	}

	void removeAllMagnets(){
		for (int j = magnets.size() - 1; j >= 0; j--){
			magnets.remove(j).delete();
		}
	}
	
	void removeAllBoids(){
		for (int j = boids.size() - 1; j >= 1; j--){
			boids.remove(j).delete();
		}
	}
	
	void typeRandomizeAllBoids(){
		for (int j = boids.size() - 1; j >= 1; j--){
			boids.get(j).applyRandomType();
		}
	}
	
	void addMagnetCylinder(String _id, float _posX, float _posY, float _posZ, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce){
		magnets.add(new MagnetCylinder(new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce));
	}
	
	void addMagnetSphere(String _id, float _posX, float _posY, float _posZ, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce){
		magnets.add(new MagnetSphere(new PVector(_posX, _posY, _posZ), _attractionType, _innerRadius, _outerRadius, _maxAttractionForce));
	}

	void addEnergyField(String _id, float _posX, float _posY, float _posZ, float _sizeX, float _sizeY, float _sizeZ, float _dirX, float _dirY, float _dirZ){
		magnets.add(new EnergyField(_id, new PVector(-_posX, _posY, _posZ), new PVector(_sizeX, _sizeY, _sizeZ), new PVector(_dirX, _dirY, _dirZ)));
	}

	void run(boolean aW) {
		// iterate through the list of boids
		for (int i = 0; i < boids.size(); i++){
			// create a temporary boid
			Boid tempBoid = (Boid) boids.get(i);
			for (int j = 0; j < boids.size(); j++){//  and iterate through the rest of the boids
				tempBoid.calcFlock(boids.get(j));
			}
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
		if (boid.pos.z > borderBack)
			boid.pos.z = borderFront;
		if (boid.pos.z < borderFront){
			sendWarpOSCMessage(boid);
			boid.pos.z = borderBack;
		}
	}
	
	void sendWarpOSCMessage(Boid b){
		CommunicationHub.getInstance().sendWarpOSCMessage(b);		
	}
	
	void sendMirrorOSCMessage(Boid b){
		CommunicationHub.getInstance().sendMirrorOSCMessage(b);		
	}

	void changeBoidParams(float _neighborhoodRadius, float _maxSpeed, float _maxSteerForce) {
		// iterate through the list of boids
		for (int i = 0; i < boids.size(); i++){
			Boid tempBoid = boids.get(i); 
			tempBoid.neighborhoodRadius = _neighborhoodRadius;
			tempBoid.maxSpeed = _maxSpeed;
			tempBoid.maxSteerForce = _maxSteerForce;
		}
	}

	void render(PApplet canvas) {
		// iterate through the list of boids
		for (int i = 0; i < boids.size(); i++){
			Boid tempBoid = boids.get(i); 
			tempBoid.calcFlockAcceleration();
			for (int j = 0; j < magnets.size(); j++){
				tempBoid.calcForceAcceleration(magnets.get(j));
			}
			tempBoid.applyAcceleration();
			checkBounds(tempBoid);
			tempBoid.applyTranslation();
			tempBoid.calcReset();
			
			executeMessages();
		}
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
	 *  /simulation/manager/boid/params <(float)neighborhoodRadius> <(float)maxSpeed> <(float)<maxSteerForce> 
	 */
	private void executeMessages(){
		OSCMessage _message;
		while(messageQueue.hasMoreMessages()){
			_message = messageQueue.removeNextMessage();
			try{
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/magnet/remove/all"))
					removeAllMagnets();
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/boid/randomize/all"))
					typeRandomizeAllBoids();
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/boid/remove/all"))
					removeAllBoids();
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/boid/addswarm"))
					addBoids(((Integer)(_message.getArguments()[0])).intValue(),
							((Integer)(_message.getArguments()[1])).intValue());
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/showoutlines"))
					showOutlines(((Integer)(_message.getArguments()[0])).intValue());
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/boid/params"))
					changeBoidParams(
							((Float)(_message.getArguments()[0])).floatValue(),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue());
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/boid/add"))
					addBoid(
							((Integer)(_message.getArguments()[0])).intValue(),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue(),
							((Float)(_message.getArguments()[8])).floatValue(),
							((Float)(_message.getArguments()[9])).floatValue());
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/magnet/add/energyfield"))
					addEnergyField(
							(String)(_message.getArguments()[0]),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Float)(_message.getArguments()[4])).floatValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue(),
							((Float)(_message.getArguments()[8])).floatValue(),
							((Float)(_message.getArguments()[9])).floatValue());
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/magnet/add/cylinder"))
					addMagnetCylinder(
							((String)(_message.getArguments()[0])),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Integer)(_message.getArguments()[4])).intValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue());
				if(_message.getAddress().equals("/simulation"+oscID+"/manager/magnet/add/sphere"))
					addMagnetSphere(
							((String)(_message.getArguments()[0])),
							((Float)(_message.getArguments()[1])).floatValue(),
							((Float)(_message.getArguments()[2])).floatValue(),
							((Float)(_message.getArguments()[3])).floatValue(),
							((Integer)(_message.getArguments()[4])).intValue(),
							((Float)(_message.getArguments()[5])).floatValue(),
							((Float)(_message.getArguments()[6])).floatValue(),
							((Float)(_message.getArguments()[7])).floatValue());
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
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/boid/randomize/all", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/boid/remove/all", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/boid/params", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/showoutlines", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/magnet/remove/all", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/magnet/add/cylinder", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/magnet/add/sphere", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/magnet/add/energyfield", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/boid/add", this);
		CommunicationHub.getInstance().addListener("/simulation"+oscID+"/manager/boid/addswarm", this);
	}
}
