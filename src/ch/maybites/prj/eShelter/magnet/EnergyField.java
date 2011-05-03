package ch.maybites.prj.eShelter.magnet;

import ch.maybites.prj.eShelter.Boid;
import ch.maybites.prj.eShelter.Canvas;

import gestalt.Gestalt;
import gestalt.shape.*;
import gestalt.render.bin.AbstractBin;
import processing.core.*;

public class EnergyField implements Magnet{
	
	Color color = new Color(0, 0, 255);
	String id;

	PVector pos;
	PVector size;
	PVector direction;

	private AbstractBin myRenderer;
	private Cube myInnerModel;

	int borderLeft, borderRight, borderTop, borderBottom, borderFront, borderBack;

	public EnergyField(PVector _pos, PVector _size, PVector _direction) {
		pos = _pos;
		size = _size;
		direction = _direction;

		borderLeft = (int) (pos.x + size.x / 2);;
		borderRight = (int) (pos.x - size.x / 2);
		borderTop = (int) (pos.y + size.y / 2);
		borderBottom = (int) (pos.y - size.y / 2);
		borderFront = (int) (pos.z - size.z / 2);
		borderBack = (int) (pos.z + size.z / 2);

		setupRenderer();
		id = "default";
	}

	public EnergyField(String _id, PVector _pos, PVector _size, PVector _direction) {
		this(_pos, _size, _direction);
		id = _id;
	}
	
	private void setupRenderer() {
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);
		myInnerModel = Canvas.getInstance().getPlugin().drawablefactory().cube();

		myInnerModel.material().wireframe = true;
		myInnerModel.material().color = color;

		myInnerModel.position().set(pos.x, pos.y, pos.z);
		myInnerModel.scale().set(size.x, size.y, size.z);

	}

	public boolean isID(String _id){
		return (_id.equals(id))? true: false;
	}
	
	public void delete(){
		myRenderer.remove(myInnerModel);
	}
		
	public void showOutlines(int i){
		if(i == 1){
			myRenderer.add(myInnerModel);
		}else{
			myRenderer.remove(myInnerModel);
		}
	}
		
	public PVector getAttractionForce(Boid _boid) {
		if (_boid.pos.x < borderLeft && _boid.pos.x > borderRight){
			if (_boid.pos.y < borderTop && _boid.pos.y > borderBottom){
				if (_boid.pos.z < borderBack && _boid.pos.z > borderFront){
					return direction;
				}
			}
		}
		return new PVector();
	}
}
