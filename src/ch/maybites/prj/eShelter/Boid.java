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
import ch.maybites.tools.*;
import gestalt.Gestalt;
import gestalt.model.Model;
import gestalt.model.ModelData;
import gestalt.model.ModelLoaderOBJ;
import gestalt.render.bin.AbstractBin;
import gestalt.shape.Mesh;
import gestalt.shape.material.TexturePlugin;
import gestalt.texture.Bitmaps;

import java.io.*;

import java.util.*;

public class Boid {
	// fields
	PVector pos, vel, acc, ali, coh, sep; // pos, velocity, and acceleration in
											// a vector datatype
	float neighborhoodRadius; // radius in which it looks for fellow boids
	float maxSpeed = 4; // maximum magnitude for the velocity vector
	float maxSteerForce = .1f; // maximum magnitude of the steering vector
	float h; // hue
	float sc = 3; // scale factor for the render of the boid
	float flap = 0;
	float t = 0;
	boolean avoidWalls = false;

	//final String MODELNAME = "/model/singleE_lowPoly.obj";
	final String MODELNAME = "/model/singleE_lowPolyVolume.obj";
	// final String MODELNAME = "/model/weirdobject.obj";

	MayRandom random = new MayRandom();

	int width, height, depth;

	private AbstractBin myRenderer;
	private ModelData myModelData;
    private Model myModel;

	// constructors
	Boid(int _width, int _height, int _depth) {
		init(_width,
				_height,
				_depth,
				new PVector(random.create(-1, 1), random.create(-1, 1), random
						.create(1, -1)), 100);
	}

	Boid(int _width, int _height, int _depth, PVector inVel, float r) {
		init(_width, _height, _depth, inVel, r);
	}

	private void init(int _width, int _height, int _depth, PVector inVel,
			float r) {
		width = _width;
		height = _height;
		depth = _depth;
		pos = new PVector(width / 2, height / 2, depth);
		//pos = new PVector(width, height, depth);
		vel = new PVector();
		vel.set(inVel);
		acc = new PVector(0, 0);
		neighborhoodRadius = r;
		
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
		Mesh myModelMesh = Canvas.getInstance().getPlugin().drawablefactory().mesh(true, 
				myModelData.vertices,3, 
				myModelData.vertexColors, 4, 
				myModelData.texCoordinates, 2,
				myModelData.normals, 
				myModelData.primitive);

		myModel = Canvas.getInstance().getPlugin().drawablefactory().model(myModelData, myModelMesh);

		//TexturePlugin myTexture = Canvas.getInstance().getPlugin().drawablefactory().texture();
		//myTexture.load(Bitmaps.getBitmap(Resource.getStream("demo/common/styrofoamplates.png")));
		//myTexture.setWrapMode(Canvas.getInstance().getPlugin().TEXTURE_WRAPMODE_CLAMP);
		//myModel.mesh().material().addPlugin(myTexture);
		
		myModel.mesh().material().lit = true;

		/* add model to renderer */
		myRenderer.add(myModel);

	}

	public void calcSector(float size){
		
	}
	
	void run(ArrayList<Boid> bl) {
		t += .1;
		flap = 10 * (float) Math.sin(t);
		// acc.add(steer(new PVector(mouseX,mouseY,300),true));
		// acc.add(new PVector(0,.05,0));
		if (avoidWalls) {
			acc.add(PVector.mult(
					avoid(new PVector(pos.x, height, pos.z), true), 5));
			acc.add(PVector.mult(avoid(new PVector(pos.x, 0, pos.z), true), 5));
			acc.add(PVector.mult(avoid(new PVector(width, pos.y, pos.z), true),
					5));
			acc.add(PVector.mult(avoid(new PVector(0, pos.y, pos.z), true), 5));
			acc.add(PVector
					.mult(avoid(new PVector(pos.x, pos.y, 300), true), 5));
			acc.add(PVector
					.mult(avoid(new PVector(pos.x, pos.y, 900), true), 5));
		}
		newFlock(bl);
	}

	void scatter() {

	}

	// //------------------------------------

	
	void render(PApplet canvas) {
		move();
		checkBounds();
		translation();
	}

	void move() {
		vel.add(acc); // add acceleration to velocity
		vel.limit(maxSpeed); // make sure the velocity vector magnitude does not
								// exceed maxSpeed
		pos.add(vel); // add velocity to position
		acc.mult(0); // reset acceleration
	}

	void checkBounds() {
		if (pos.x > width* 2)
			pos.x = 0;
		if (pos.x < 0)
			pos.x = width * 2;
		if (pos.y > height * 2)
			pos.y = 0;
		if (pos.y < 0)
			pos.y = height * 2;
		if (pos.z > 900)
			pos.z = 300;
		if (pos.z < 300)
			pos.z = 900;
	}

	void translation(){
		myModel.mesh().transform().translation.x = pos.x - width;
		myModel.mesh().transform().translation.y = pos.y - height;
		myModel.mesh().transform().translation.z = pos.z;

		myModel.mesh().rotation().y = (float)Math.atan2(-vel.z,vel.x);
		myModel.mesh().rotation().z = (float)Math.asin(vel.y/vel.mag());

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

	// avoid. If weight == true avoidance vector is larger the closer the boid
	// is to the target
	PVector avoid(PVector target, boolean weight) {
		PVector steer = new PVector(); // creates vector for steering
		steer.set(PVector.sub(pos, target)); // steering vector points away from
												// target
		if (weight)
			steer.mult(1 / Calc.sq(PVector.dist(pos, target)));
		// steer.limit(maxSteerForce); //limits the steering force to
		// maxSteerForce
		return steer;
	}

	
	void newFlock(ArrayList<Boid> boids) {
		PVector cohSum = new PVector(0, 0, 0);
		PVector steer = new PVector(0, 0, 0);
		PVector aliSum = new PVector(0, 0, 0);
		PVector sepSum = new PVector(0, 0, 0);
		PVector repulse;
		int count = 0;
		for (int i = 0; i < boids.size(); i++) {
			Boid b = (Boid) boids.get(i);
			
			float d = PVector.dist(pos, b.pos);
			if (d > 0 && d <= neighborhoodRadius) {
				//alignment
				aliSum.add(b.vel);
				
				//cohesion
				cohSum.add(b.pos);
				
				//separation
				repulse = PVector.sub(pos, b.pos);
				repulse.normalize();
				repulse.div(d);
				sepSum.add(repulse);
				count++;
			}
		}
		if (count > 0) {
			aliSum.div((float) count);
			aliSum.limit(maxSteerForce);
			cohSum.div((float) count);
		}
		steer = PVector.sub(cohSum, pos);
		steer.limit(maxSteerForce);

		
		acc.add(PVector.mult(aliSum, 1));
		acc.add(PVector.mult(steer, 3));
		acc.add(PVector.mult(sepSum, 1));
		//acc.add(PVector.mult(sepSum, 1));
		
	}
	

}
