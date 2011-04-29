package ch.maybites.prj.eShelter.magnet;

import ch.maybites.prj.eShelter.Boid;
import processing.core.PVector;

public interface Magnet {

	public void display();
	public void noDisplay();
	public PVector getAttractionForce(Boid _boid);
	
}
