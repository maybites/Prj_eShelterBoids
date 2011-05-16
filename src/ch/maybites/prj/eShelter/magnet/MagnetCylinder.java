package ch.maybites.prj.eShelter.magnet;

import ch.maybites.prj.eShelter.Boid;
import processing.core.*;

public class MagnetCylinder extends MagnetSphere implements Magnet{

	private PVector pos2d;
	
	public MagnetCylinder(String _id, int _swarmID, PVector _pos, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce) {
		super(_id, _swarmID, _pos, _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
		pos2d = new PVector(pos.x, pos.z);
		super.MODELNAME = "model/magnet/cylinder.obj";
	}

	public void set(int _swarmID, PVector _pos, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce){	
		super.set(_swarmID, _pos, _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
		pos2d = new PVector(pos.x, pos.z);
	}
		
	public PVector getAttractionForce(Boid _boid) {
		if((swarmID >= 0 && (_boid.swarmID == swarmID || swarmID == 0)) || 
				(swarmID < 0 && (_boid.swarmID != (-1 * swarmID)))){
			PVector flatBoid = new PVector(_boid.pos.x, _boid.pos.z);
			float d = PVector.dist(pos2d, flatBoid);
			if (d > innerRadius && d <= outerRadius) {
				PVector steer = new PVector(); // creates vector for steering
				steer.set(PVector.sub(pos2d, flatBoid)); // steering vector points
				steer.normalize();
				// multiply according to the distance and attraction type
				switch (attractionType) {
				case LEVEL_ATTRACTION_LINEAR:
					steer.mult(getLevelAttractionLinear(d));
					break;
				case INNER_ATTRACTION_LINEAR:
					steer.mult(getInnerAttractionLinear(d));
					break;
				case OUTER_ATTRACTION_LINEAR:
					steer.mult(getOuterAttractionLinear(d));
					break;
				}
				return new PVector(steer.x, 0, steer.y);
			}
		}
		return new PVector();
	}

	public void update() {
		myInnerModel.mesh().transform().translation.x = pos.x;
		myInnerModel.mesh().transform().translation.y = pos.y;
		myInnerModel.mesh().transform().translation.z = pos.z;

		myInnerModel.mesh().scale(innerRadius, 300, innerRadius);

		myOuterModel.mesh().transform().translation.x = pos.x;
		myOuterModel.mesh().transform().translation.y = pos.y;
		myOuterModel.mesh().transform().translation.z = pos.z;

		myOuterModel.mesh().scale(outerRadius, 300, outerRadius);
	}

}
