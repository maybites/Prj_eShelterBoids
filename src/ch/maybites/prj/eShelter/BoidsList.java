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
