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

public class BoidsList {
	private ArrayList<Boid> boids; // will hold the boids in this BoidList
	private ArrayList<Magnet> magnets; // will hold the magnets in this BoidList
	float h; // for color
	
	PVector pos;

	public int width, height, depth;
	
	private int maxSize;

	private AbstractBin myRenderer;
	private Cube myInnerModel;
	
	int borderLeft, borderRight, borderTop, borderBottom, borderFront, borderBack;

	BoidsList(int _width, int _height, int n, float ih) {
		width = _width;
		height = _height;
		depth = 600;
		pos = new PVector();
		pos.set(0, 0, (depth / 2) + 300);
		
		borderLeft = width;
		borderRight = - width;
		borderTop = height;
		borderBottom = - height;
		borderFront = 300;
		borderBack = 300 + depth;
		
		maxSize = n;
		boids = new ArrayList<Boid>();
		magnets = new ArrayList<Magnet>();
		h = ih;
		for (int i = 0; i < maxSize; i++)
			add();
		
		setupRenderer();
		
		//magnets.add(new MagnetSphere(new PVector(0, 0, 700), MagnetSphere.INNER_ATTRACTION_LINEAR, 80, 220, 1.0f));
		magnets.add(new MagnetCylinder(new PVector(width / 2, 0, 600), MagnetSphere.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		magnets.add(new MagnetCylinder(new PVector(width / 4, 0, 400), MagnetSphere.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		magnets.add(new MagnetCylinder(new PVector(-width / 2, 0, 400), MagnetSphere.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		magnets.add(new MagnetCylinder(new PVector(-width / 4, 0, 500), MagnetSphere.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
		magnets.add(new MagnetCylinder(new PVector(0, 0, 500), MagnetSphere.LEVEL_ATTRACTION_LINEAR, 40, 100, 1.0f));
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

		myInnerModel.scale(width * 2, height * 2, depth);
		myInnerModel.position().x = pos.x;
		myInnerModel.position().y = pos.y;
		myInnerModel.position().z = pos.z;

		myRenderer.add(myInnerModel);
	}
	
	public void display(){
		myRenderer.add(myInnerModel);
	}
	
	public void noDisplay(){
		myRenderer.remove(myInnerModel);
	}

	void add() {
		boids.add(new Boid(new PVector(0, 0, 0)));
	}

	void addBoid(Boid b) {
		boids.add(b);
	}

	void addMagnet(Magnet m) {
		magnets.add(m);
	}

	void run(boolean aW) {
		// iterate through the list of boids
		for (int i = 0; i < boids.size(); i++){
			// create a temporary boid
			Boid tempBoid = (Boid) boids.get(i);
			for (int j = i + 1; j < boids.size(); j++){//  and iterate through the rest of the boids
				tempBoid.calcFlock(boids.get(j));
			}
		}
	}

	void checkBounds(Boid boid) {
		if (boid.pos.x > borderLeft)
			boid.pos.x = borderRight;
		if (boid.pos.x < borderRight)
			boid.pos.x = borderLeft;
		if (boid.pos.y > borderTop)
			boid.pos.y = borderBottom;
		if (boid.pos.y < borderBottom)
			boid.pos.y = borderTop;
		if (boid.pos.z > borderBack)
			boid.pos.z = borderFront;
		if (boid.pos.z < borderFront)
			boid.pos.z = borderBack;
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
		}
	}
}
