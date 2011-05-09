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
import ch.maybites.prj.eShelter.Canvas;
import ch.maybites.prj.eShelter.magnet.Magnet;
import ch.maybites.tools.*;
import gestalt.Gestalt;
import ch.maybites.prj.eShelter.BoidsSim.ShaderMaterial;
import gestalt.model.*;
import gestalt.texture.*;
import gestalt.texture.bitmap.IntegerBitmap;
import gestalt.render.bin.AbstractBin;
import gestalt.shape.*;
import gestalt.shape.material.TexturePlugin;

import java.io.*;

import java.util.*;

import javax.media.opengl.GL;

import com.illposed.osc.OSCMessage;

public class Boid {
	// fields
	
	public java.util.UUID uuid;
	public int swarmID;
	Bitmap texColor;
	Bitmap refrColor;
	Bitmap reflColor;
	TexturePlugin myTexture;
	TexturePlugin myRefractionTexture;
	TexturePlugin myReflectionTexture;
	
	public PVector pos, vel, acc; // pos, velocity, and acceleration in
											// a vector datatype
	float neighborhoodRadius; // radius in which it looks for fellow boids
	float maxSpeed; // maximum magnitude for the velocity vector
	float maxSteerForce; // maximum magnitude of the steering vector
	private float repulseDistance;
	private float repulseDistanceSqr;
	float alignementFactor = 2.0f;
	float coherenceFactor = 3.0f;
	float repulseFactor = 1.0f;

	float h; // hue
	float sc = 3; // scale factor for the render of the boid
	float flap = 0;
	float t = 0;
	int numberOfSwarms = 4;

	// final String MODELNAME = "model/singleE_lowPoly.obj";
	final String MODELNAME = "model/singleE_lowPoly1.obj";
	// final String MODELNAME = "model/weirdobject.obj";

	MayRandom random = new MayRandom();

	private AbstractBin myRenderer;
	private ModelData myModelData;
	private Model myModel;
	
	private PVector myCohSum;
	private PVector mySteer;
	private PVector myAliSum;
	private PVector mySepSum;
	private PVector myRepulse;
	int flockcounter;

	// constructors
	Boid(PVector _pos) {
		init(	_pos,
				new PVector(random.create(-1, 1), 
				random.create(-1, 1),
				random.create(1, -1)), 
				100, 2, 0.1f, 0);
		applySwarmCharacteristics();
	}

	Boid(PVector _pos, int _type) {
		init(_pos, new PVector(random.create(-1, 1), 
				random.create(-1, 1),
				random.create(1, -1)), 100, 1, 0.1f, _type);
		applySwarmCharacteristics();
	}
	
	Boid(PVector _pos, PVector inVel, float _radius, float _maxVelocity, float _maxAcceleration, int _type) {
		init(_pos, inVel, _radius, _maxVelocity, _maxAcceleration, _type);
	}

	private void init(PVector _pos, PVector inVel, float _radius, float _maxVelocity, float _maxAcceleration, int _type) {
		pos = new PVector(_pos.x, _pos.y, _pos.z);
		vel = new PVector(inVel.x, inVel.y, inVel.z);
		acc = new PVector(0, 0, 0);
		neighborhoodRadius = _radius;
		maxSpeed = _maxVelocity;
		maxSteerForce = _maxAcceleration; // maximum magnitude of the steering vector
		setRepulseDistance(10);
		
		myRepulse = new PVector();
		
		uuid = java.util.UUID.randomUUID();
		
		setupRenderer();
		calcReset();
		setSwarm(_type);
	}

	public void setRepulseDistance(float _repulseDistance){
		repulseDistance = _repulseDistance;
		repulseDistanceSqr = repulseDistance * repulseDistance;
	}

	public void setShader(ShaderMaterial shaderMaterial, TexturePlugin myReflectionTexture){
 		
        /* create textures */
		myRefractionTexture = Canvas.getInstance().getPlugin().drawablefactory().texture();
		myRefractionTexture.load(texColor);
        myRefractionTexture.setTextureUnit(GL.GL_TEXTURE1);

        myModel.mesh().material().plugins().clear();
        
        myModel.mesh().material().addPlugin(myRefractionTexture);
        myModel.mesh().material().addPlugin(myReflectionTexture);
        myModel.mesh().material().addPlugin(shaderMaterial);
	}
	
	private void setupRenderer() {
		try {
			FileInputStream file = new FileInputStream(GlobalPreferences
					.getInstance().getAbsResourcePath(MODELNAME));
			myModelData = ModelLoaderOBJ.getModelData(file);
			file.close();
		} catch (IOException exp) {
			Debugger.getInstance().errorMessage(this.getClass(),
					"No Model File found: " + exp.getMessage());
			;
		}
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);
		Mesh myModelMesh = Canvas
				.getInstance()
				.getPlugin()
				.drawablefactory()
				.mesh(true, myModelData.vertices, 3, myModelData.vertexColors,
						4, myModelData.texCoordinates, 2, myModelData.normals,
						myModelData.primitive);

		myModel = Canvas.getInstance().getPlugin().drawablefactory()
				.model(myModelData, myModelMesh);

		myTexture = Canvas.getInstance().getPlugin().drawablefactory().texture();
		texColor = IntegerBitmap.getDefaultImageBitmap(1, 1); 
		setColor(new Color(1, 1, 1));
		// myTexture.load(Bitmaps.getBitmap(Resource.getStream("demo/common/styrofoamplates.png")));
		myTexture.setWrapMode(Canvas.getInstance().getPlugin().TEXTURE_WRAPMODE_REPEAT);
		myModel.mesh().material().addPlugin(myTexture);

		myModel.mesh().material().lit = true;

		/* add model to renderer */
		myRenderer.add(myModel);
	}
	
	public void setColor(Color _c){
		texColor.setPixel(0, 0, new Color(_c));
		myTexture.load(texColor);
	}
	
	public void delete(){
		myRenderer.remove(myModel);
	}
	
	public void applyRandomSwarm(){
		setSwarm((int)random.create(0, numberOfSwarms));
		applySwarmCharacteristics();
	}
	
	public void setSwarm(int _swarm){
		swarmID = _swarm;
		Color color = new Color(1f, 1f, 1f);
		switch(swarmID){
		case 0:
			color = new Color(1f, 1f, 1f);
			break;
		case 1:
			color = new Color(1f, 0f, 0f);
			break;
		case 2:
			color = new Color(1f, 0f, 1f);
			break;
		case 3:
			color = new Color(0f, 0f, 1f);
			break;
		}
		setColor(color);
	}
	
	public void applySwarmCharacteristics(){
		switch(swarmID){
		case 0:
			sc = random.create(5, 8);
			neighborhoodRadius = 150;
			maxSpeed = 2f;
			maxSteerForce = .1f;
			alignementFactor = 2.0f;
			coherenceFactor = 3.0f;
			repulseFactor = 1.0f;
			setRepulseDistance(15);
			break;
		case 1:
			sc = random.create(10, 12);
			neighborhoodRadius = 150;
			maxSpeed = 1f;
			maxSteerForce = .1f;
			alignementFactor = 2.0f;
			coherenceFactor = 3.0f;
			repulseFactor = 1.0f;
			setRepulseDistance(20);
			break;
		case 2:
			sc = random.create(2, 3);
			neighborhoodRadius = 70;
			maxSpeed = 5f;
			maxSteerForce = .1f;
			alignementFactor = 1.0f;
			coherenceFactor = 3.0f;
			repulseFactor = 1.0f;
			setRepulseDistance(12);
			break;
		case 3:
			sc = random.create(7, 10);
			neighborhoodRadius = 100;
			maxSpeed = 4f;
			maxSteerForce = .2f;
			alignementFactor = 2.0f;
			coherenceFactor = 4.0f;
			repulseFactor = 1.0f;
			setRepulseDistance(12);
			break;
		case 4:
			sc = random.create(100, 100);
			neighborhoodRadius = 100;
			maxSpeed = 0.1f;
			maxSteerForce = .01f;
			alignementFactor = 2.0f;
			coherenceFactor = 4.0f;
			repulseFactor = 1.0f;
			setRepulseDistance(10);
			break;
		}
		myModel.mesh().scale(sc, sc, sc);
	}
	
	void calcReset() {
		t += .1;
		flap = 10 * (float) Math.sin(t);
		myCohSum = new PVector(0, 0, 0);
		mySteer = new PVector(0, 0, 0);
		myAliSum = new PVector(0, 0, 0);
		mySepSum = new PVector(0, 0, 0);
		flockcounter = 0;
	}

	void calcFlock(Boid b) {
		float d = PVector.dist(pos, b.pos);
		if (d > 1 && d <= neighborhoodRadius) {
			// separation
			float repulseForce = d * d / repulseDistanceSqr;
			myRepulse = PVector.sub(pos, b.pos);
			myRepulse.normalize();
			myRepulse.div(repulseForce);

			if(swarmID == b.swarmID){
				// alignment
				myAliSum.add(b.vel);

				// cohesion
				myCohSum.add(b.pos);
			} else {
				myRepulse.mult(2f); // repulse of an boid of different swarm is stronger.
				if(d < repulseDistance)
					sendCollisionOSCMessage(b);
			}

			mySepSum.add(myRepulse);

			flockcounter++;
		}
	}
	
	void sendCollisionOSCMessage(Boid b){
		CommunicationHub.getInstance().sendCollisionOSCMessage(this);		
	}
	
	void calcFlockAcceleration() {
		if (flockcounter > 0) {
			myAliSum.div((float) flockcounter);
			myAliSum.limit(maxSteerForce);
			myCohSum.div((float) flockcounter);
		}
		mySteer = PVector.sub(myCohSum, pos);
		mySteer.limit(maxSteerForce);

		acc.add(PVector.mult(myAliSum, alignementFactor));
		acc.add(PVector.mult(mySteer, coherenceFactor));
		acc.add(PVector.mult(mySepSum, repulseFactor));
	}
	
	void calcForceAcceleration(Magnet _magnet){
		acc.add(PVector.mult(_magnet.getAttractionForce(this), 1));
	}
	
	void applyAcceleration() {
		vel.add(acc); // add acceleration to velocity
		vel.limit(maxSpeed); // make sure the velocity vector magnitude does not
								// exceed maxSpeed
		pos.add(vel); // add velocity to position
		acc.mult(0); // reset acceleration
	}

	void scatter() {

	}
	
	void applyTranslation() {
		myModel.mesh().transform().translation.x = pos.x;
		myModel.mesh().transform().translation.y = pos.y;
		myModel.mesh().transform().translation.z = pos.z;

		myModel.mesh().rotation().y = (float) Math.atan2(-vel.z, vel.x);
		myModel.mesh().rotation().z = (float) Math.asin(vel.y / vel.mag());

		myModel.mesh().scale(sc, sc, sc);
	}

	// steering. If arrival==true, the boid slows to meet the target. Credit to
	// Craig Reynolds
	PVector steer(PVector target, boolean arrival) {
		PVector steer = new PVector(); // creates vector for steering
		if (!arrival) {
			steer.set(PVector.sub(target, pos)); // steering vector points
													// towards target (switch
													// target and pos for
													// avoiding)
			steer.limit(maxSteerForce); // limits the steering force to
										// maxSteerForce
		} else {
			PVector targetOffset = PVector.sub(target, pos);
			float distance = targetOffset.mag();
			float rampedSpeed = maxSpeed * (distance / 100);
			float clippedSpeed = Calc.min(rampedSpeed, maxSpeed);
			PVector desiredVelocity = PVector.mult(targetOffset,
					(clippedSpeed / distance));
			steer.set(PVector.sub(desiredVelocity, vel));
		}
		return steer;
	}

}
