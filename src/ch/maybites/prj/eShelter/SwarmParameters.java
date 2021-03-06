package ch.maybites.prj.eShelter;

import ch.maybites.tools.*;
import processing.core.*;
import gestalt.shape.*;

public class SwarmParameters {

	float neighborhoodRadius[]; // radius in which it looks for fellow boids
	float maxSpeed[]; // maximum magnitude for the velocity vector
	float maxSteerForce[]; // maximum magnitude of the steering vector
	float repulseRadius[];
	float repulseRadiusSqr[];
	float alignementDamper[];
	float coherenceDamper[];
	float repulseDamper[];
	int incubationSize[];
	float sc[]; // scale factor for the render of the boid
	Color color[];
	
	private int size;
	
	int incubateSwarmType = 0;
	
	int ceoSwarmType;
	
	private MayRandom random = new MayRandom();

	static private SwarmParameters _instance;

	private SwarmParameters() {
		reset();
	}
	
	public int getRandomSwarmID(){
		return (int)random.create(1, size - 1);
	}
	
	public int getIncubationSize(int _swarmID){
		if(_swarmID >= 0 && _swarmID < size){
			return incubationSize[_swarmID];
		}
		return -1;
	}
	
	public int size(){
		return size;
	}
	
	public void reset(){
		size = 6;
		ceoSwarmType = size-1;
			
		neighborhoodRadius = new float[size];
		maxSpeed = new float[size];
		maxSteerForce = new float[size];
		repulseRadius = new float[size];
		repulseRadiusSqr = new float[size];
		alignementDamper = new float[size];
		coherenceDamper = new float[size];
		repulseDamper = new float[size];
		incubationSize = new int[size];
		sc = new float[size];
		color = new Color[size];

		color[0] = new Color(1f, 1f, 1f);
		sc[0] = 8;
		incubationSize[0] = 50;
		neighborhoodRadius[0] = 150;
		maxSpeed[0] = 2f;
		maxSteerForce[0] = .1f;
		alignementDamper[0] = 2.0f;
		coherenceDamper[0] = 3.0f;
		repulseDamper[0] = 1.0f;
		repulseRadius[0] = 15f;
		repulseRadiusSqr[0] = repulseRadius[0] * repulseRadius[0];
		
		color[1] = new Color(1f, 0.5f, 0.231f);
		sc[1] = 20;
		incubationSize[1] = 50;
		neighborhoodRadius[1] = 150;
		maxSpeed[1] = 20f;
		maxSteerForce[1] = .1f;
		alignementDamper[1] = 2.0f;
		coherenceDamper[1] = 3.0f;
		repulseDamper[1] = 1.0f;
		repulseRadius[1] = 15f;
		repulseRadiusSqr[1] = repulseRadius[1] * repulseRadius[1];

		color[2] = new Color(0.756f, 0.22f, 0.5f); //orange
		sc[2] = 25;
		incubationSize[2] = 50;
		neighborhoodRadius[2] = 70;
		maxSpeed[2] = 10f;
		maxSteerForce[2] = .1f;
		alignementDamper[2] = 1.0f;
		coherenceDamper[2] = 3.0f;
		repulseDamper[2] = 1.0f;
		repulseRadius[2] = 12f;
		repulseRadiusSqr[2] = repulseRadius[2] * repulseRadius[2];

		color[3] = new Color(0.9f, 0.5f, 0.5f); //weinrot
		sc[3] = 35;
		incubationSize[3] = 50;
		neighborhoodRadius[3] = 100;
		maxSpeed[3] = 10f;
		maxSteerForce[3] = .2f;
		alignementDamper[3] = 2.0f;
		coherenceDamper[3] = 4.0f;
		repulseDamper[3] = 1.0f;
		repulseRadius[3] = 12f;
		repulseRadiusSqr[3] = repulseRadius[3] * repulseRadius[3];

		color[4] = new Color(0.9f, 0.0f, 0.568f); //violett
		sc[4] = 30;
		incubationSize[4] = 50;
		neighborhoodRadius[4] = 100;
		maxSpeed[4] = 10f;
		maxSteerForce[4] = .05f;
		alignementDamper[4] = 2.0f;
		coherenceDamper[4] = 4.0f;
		repulseDamper[4] = 1.0f;
		repulseRadius[4] = 10f;
		repulseRadiusSqr[4] = repulseRadius[4] * repulseRadius[4];

		//ceo
		color[5] = new Color(1f, 1f, 1f); //blau
		sc[5] = 80;
		incubationSize[5] = 1;
		neighborhoodRadius[5] = 150;
		maxSpeed[5] = 4f;
		maxSteerForce[5] = .1f;
		alignementDamper[5] = 2.0f;
		coherenceDamper[5] = 3.0f;
		repulseDamper[5] = 1.0f;
		repulseRadius[5] = 20f;
		repulseRadiusSqr[5] = repulseRadius[5] * repulseRadius[5];

	}
	
	public void incubateID(int _type){
		if(_type > 0 && _type < size){
			incubateSwarmType = _type;
			color[0] = color[_type];
			sc[0] = sc[_type];
		}
	}
	
	static public SwarmParameters getInstance() {
		if (_instance == null) {
			synchronized(SwarmParameters.class) {
				if (_instance == null)
					_instance = new SwarmParameters();
			}
		}
		return _instance;
	}
	
	public void setPhysics(
			int swarmID,
			float _neighborhoodRadius, 
			float _maxSpeed, 
			float _maxSteerForce, 
			float _alignementDamper, 
			float _coherenceDamper, 
			float _repulseDamper, 
			float _repulseRadius){
		if(swarmID >= 0 && swarmID < size){
			neighborhoodRadius[swarmID] = _neighborhoodRadius;
			maxSpeed[swarmID] = _maxSpeed;
			maxSteerForce[swarmID] = _maxSteerForce;
			alignementDamper[swarmID] = _alignementDamper;
			coherenceDamper[swarmID] = _coherenceDamper;
			repulseDamper[swarmID] = _repulseDamper;
			repulseRadius[swarmID] = _repulseRadius;
			repulseRadiusSqr[swarmID] = repulseRadius[swarmID] * repulseRadius[swarmID];
		}
	}
	
	public void setAppearance(
			int swarmID,
			float _sc, 
			Color _color, 
			int _incubationSize){
		if(swarmID >= 0 && swarmID < size){
			sc[swarmID] = random.create(_sc, _sc);
			color[swarmID] = _color;
			incubationSize[swarmID] = _incubationSize;
		}
	}
	
}
