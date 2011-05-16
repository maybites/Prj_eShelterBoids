package ch.maybites.prj.eShelter.magnet;

import ch.maybites.prj.eShelter.Boid;
import processing.core.*;

public class ArrousalMagnetSphere extends MagnetSphere implements Magnet{

	float arrousal;
	
	public ArrousalMagnetSphere(String _id, int _swarmID, PVector _pos, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce, float _arrousal) {
		super(_id, _swarmID, _pos, _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
		arrousal = _arrousal;
	}

	public void set(int _swarmID, PVector _pos, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce, float _arrousal) {
		super.set(_swarmID, _pos, _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
		arrousal = _arrousal;
	}

	public PVector getAttractionForce(Boid _boid) {
		if((swarmID >= 0 && (_boid.swarmID == swarmID || swarmID == 0)) || 
				(swarmID < 0 && (_boid.swarmID != (-1 * swarmID)))){
			float d = PVector.dist(pos, _boid.pos);
			if (d > innerRadius && d <= outerRadius) {
				PVector steer = new PVector(); // creates vector for steering
				steer.set(PVector.sub(pos, _boid.pos)); // steering vector points
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
				_boid.setArrousal(arrousal);
				return steer;
			}
		}
		return new PVector();
	}
}
