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

import java.util.*;
import processing.core.*;

public class BoidsList {
	private ArrayList<Boid> boids; // will hold the boids in this BoidList
	float h; // for color

	public int width, height, depth;
	
	private int maxSize;

	BoidsList(int _width, int _height, int n, float ih) {
		width = _width;
		height = _height;
		depth = 700;
		maxSize = n;
		boids = new ArrayList<Boid>();
		h = ih;
	}

	void init(){
		for (int i = 0; i < maxSize; i++)
			boids.add(new Boid(this));
	}
	
	void add() {
		boids.add(new Boid(this));
	}

	void addBoid(Boid b) {
		boids.add(b);
	}

	void run(boolean aW) {
		// iterate through the list of boids
		for (int i = 0; i < boids.size(); i++){
			// create a temporary boid
			Boid tempBoid = (Boid) boids.get(i);
			for (int j = i + 1; j < boids.size(); j++){//  and iterate through the rest of the boids
				tempBoid.calcSolve(boids.get(j));
			}
		}
	}

	void checkBounds(Boid boid) {
		if (boid.pos.x > width)
			boid.pos.x = -width;
		if (boid.pos.x < -width)
			boid.pos.x = width;
		if (boid.pos.y > height)
			boid.pos.y = -height;
		if (boid.pos.y < -height)
			boid.pos.y = height;
		if (boid.pos.z > 900)
			boid.pos.z = 300;
		if (boid.pos.z < 300)
			boid.pos.z = 900;
	}

	void render(PApplet canvas) {
		// iterate through the list of boids
		for (int i = 0; i < boids.size(); i++){
			boids.get(i).render(canvas); 
		}
	}
}
