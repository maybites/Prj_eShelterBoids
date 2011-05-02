package ch.maybites.prj.eShelter.magnet;

import ch.maybites.prj.eShelter.Boid;
import processing.core.PVector;

public interface Magnet {

	public void showOutlines(int i);
	public PVector getAttractionForce(Boid _boid);
	
}
