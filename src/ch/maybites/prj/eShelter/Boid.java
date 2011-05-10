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
	private SwarmParameters swarmProps;
	
	float scale;
	Color color;
	
	float h; // hue
	float flap = 0;
	float t = 0;
	int numberOfSwarms = 4;
	
	public boolean isAlive = true;
	
	int simID;

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
				random.create(1, -1)), 0);
	}

	Boid(PVector _pos, int _type) {
		init(_pos, new PVector(random.create(-1, 1), 
				random.create(-1, 1),
				random.create(1, -1)), _type);
	}
	
	Boid(PVector _pos, PVector inVel, int _type) {
		init(_pos, inVel, _type);
	}

	private void init(PVector _pos, PVector inVel, int _type) {
		pos = new PVector(_pos.x, _pos.y, _pos.z);
		vel = new PVector(inVel.x, inVel.y, inVel.z);
		acc = new PVector(0, 0, 0);
		swarmProps = SwarmParameters.getInstance();
		
		myRepulse = new PVector();
		
		simID = GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.SIM_ID, 1);
		
		uuid = java.util.UUID.randomUUID();

		applySwarmCharcteristics();
		
		setupRenderer();

		applyToSwarm(_type);

		calcReset();
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
		setColor();
		// myTexture.load(Bitmaps.getBitmap(Resource.getStream("demo/common/styrofoamplates.png")));
		myTexture.setWrapMode(Canvas.getInstance().getPlugin().TEXTURE_WRAPMODE_REPEAT);
		myModel.mesh().material().addPlugin(myTexture);

		myModel.mesh().material().lit = true;

		/* add model to renderer */
		myRenderer.add(myModel);
	}
	
	private void setColor(){
		texColor.setPixel(0, 0, color);
		myTexture.load(texColor);
	}
	
	public void delete(){
		myRenderer.remove(myModel);
		isAlive = false;
	}
	
	public void applyRandomSwarm(){
		applyToSwarm((int)random.create(0, swarmProps.size));
	}

	public void applyToSwarm(int _swarm){
		swarmID = _swarm;
		applySwarmCharcteristics();
		setColor();
	}
	
	private void applySwarmCharcteristics(){
		scale = random.create(swarmProps.sc[swarmID] - swarmProps.sc[swarmID] / 2, swarmProps.sc[swarmID] +  - swarmProps.sc[swarmID] / 2);
		color = swarmProps.color[swarmID];
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
		if (d > 1 && d <= swarmProps.neighborhoodRadius[swarmID]) {
			// separation
			float repulseForce = d * d / swarmProps.repulseRadiusSqr[swarmID];
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
				if(d < swarmProps.repulseRadius[swarmID])
					sendCollisionOSCMessage(b);
			}

			mySepSum.add(myRepulse);

			flockcounter++;
		}
	}
	
	void sendCollisionOSCMessage(Boid b){
		CommunicationHub.getInstance().sendCollisionOSCMessage(this, simID);		
	}
	
	void calcFlockAcceleration() {
		if (flockcounter > 0) {
			myAliSum.div((float) flockcounter);
			myAliSum.limit(swarmProps.maxSteerForce[swarmID]);
			myCohSum.div((float) flockcounter);
		}
		mySteer = PVector.sub(myCohSum, pos);
		mySteer.limit(swarmProps.maxSteerForce[swarmID]);

		acc.add(PVector.mult(myAliSum, swarmProps.alignementDamper[swarmID]));
		acc.add(PVector.mult(mySteer, swarmProps.coherenceDamper[swarmID]));
		acc.add(PVector.mult(mySepSum, swarmProps.repulseDamper[swarmID]));
	}
	
	void calcForceAcceleration(Magnet _magnet){
		acc.add(PVector.mult(_magnet.getAttractionForce(this), 1));
	}
	
	void applyAcceleration() {
		vel.add(acc); // add acceleration to velocity
		vel.limit(swarmProps.maxSpeed[swarmID]); // make sure the velocity vector magnitude does not
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

		myModel.mesh().scale(swarmProps.sc[this.swarmID], swarmProps.sc[this.swarmID], swarmProps.sc[this.swarmID]);
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
			steer.limit(swarmProps.maxSteerForce[swarmID]); // limits the steering force to
										// maxSteerForce
		} else {
			PVector targetOffset = PVector.sub(target, pos);
			float distance = targetOffset.mag();
			float rampedSpeed = swarmProps.maxSpeed[swarmID] * (distance / 100);
			float clippedSpeed = Calc.min(rampedSpeed, swarmProps.maxSpeed[swarmID]);
			PVector desiredVelocity = PVector.mult(targetOffset,
					(clippedSpeed / distance));
			steer.set(PVector.sub(desiredVelocity, vel));
		}
		return steer;
	}

}
