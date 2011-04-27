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
	ArrayList<Boid> boids; // will hold the boids in this BoidList
	float h; // for color

	int width, height, depth;

	BoidsList(int _width, int _height, int n, float ih) {
		width = _width;
		height = _height;
		boids = new ArrayList<Boid>();
		h = ih;
		for (int i = 0; i < n; i++)
			boids.add(new Boid(width, height, 600));
	}

	void add() {
		boids.add(new Boid(width, height, 0));
	}

	void addBoid(Boid b) {
		boids.add(b);
	}

	void run(boolean aW) {
		for (int i = 0; i < boids.size(); i++) // iterate through the list of
												// boids
		{
			Boid tempBoid = (Boid) boids.get(i); // create a temporary boid to
													// process and make it the
													// current boid in the list
			tempBoid.h = h;
			tempBoid.avoidWalls = aW;
			tempBoid.run(boids); // tell the temporary boid to execute its run
									// method
		}
	}

	void render(PApplet canvas) {
		for (int i = 0; i < boids.size(); i++) // iterate through the list of
												// boids
		{
			boids.get(i).render(canvas); // create a temporary boid to process
											// and make it the current boid in
											// the list
		}
	}
}
