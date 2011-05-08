package ch.maybites.prj.eShelter.magnet;

import java.io.FileInputStream;
import java.io.IOException;

import ch.maybites.prj.eShelter.Boid;
import ch.maybites.prj.eShelter.Canvas;
import ch.maybites.prj.eShelter.Debugger;
import ch.maybites.prj.eShelter.GlobalPreferences;

import gestalt.Gestalt;
import gestalt.model.Model;
import gestalt.model.ModelData;
import gestalt.model.ModelLoaderOBJ;
import gestalt.render.bin.AbstractBin;
import gestalt.shape.Color;
import gestalt.shape.Mesh;
import processing.core.*;

public class MagnetCylinder implements Magnet{

	final String MODELNAME = "/model/magnet/cylinder.obj";
	Color color = new Color(255, 0, 255);

	public final static int LEVEL_ATTRACTION_LINEAR = 0;
	public final static int INNER_ATTRACTION_LINEAR = 1;
	public final static int OUTER_ATTRACTION_LINEAR = 2;

	float innerRadius;
	float outerRadius;

	float maxAttractionForce;

	int attractionType;
	String id;

	PVector pos, pos2d;

	private AbstractBin myRenderer;
	private ModelData myModelData;
	private Model myInnerModel;
	private Model myOuterModel;
	
	public MagnetCylinder(PVector _pos, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce) {
		innerRadius = _innerRadius;
		outerRadius = _outerRadius;
		attractionType = _attractionType;
		maxAttractionForce = _maxAttractionForce;
		pos = _pos;
		pos2d = new PVector(pos.x, pos.z);
		setupRenderer();
		id = "default";
	}
	
	public MagnetCylinder(String _id, PVector _pos, int _attractionType, float _innerRadius,
			float _outerRadius, float _maxAttractionForce) {
		this(_pos, _attractionType, _innerRadius, _outerRadius, _maxAttractionForce);
		id = _id;
	}

	public boolean isID(String _id){
		return (_id.equals(id))? true: false;
	}
	
	public void delete(){
		myRenderer.remove(myInnerModel);
		myRenderer.remove(myOuterModel);
	}


	private void setupRenderer() {
		try {
			FileInputStream file = new FileInputStream(GlobalPreferences
					.getInstance().getAbsResourcePath(MODELNAME));
			myModelData = ModelLoaderOBJ.getModelData(file);
			file.close();
		} catch (IOException exp) {
			Debugger.getInstance().errorMessage(this.getClass(),
					"No Model File found: " + exp.getMessage());
			;
		}
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);
		Mesh myInnerModelMesh = Canvas
				.getInstance()
				.getPlugin()
				.drawablefactory()
				.mesh(true, myModelData.vertices, 3, myModelData.vertexColors,
						4, myModelData.texCoordinates, 2, myModelData.normals,
						myModelData.primitive);
		Mesh myOuterModelMesh = Canvas
				.getInstance()
				.getPlugin()
				.drawablefactory()
				.mesh(true, myModelData.vertices, 3, myModelData.vertexColors,
						4, myModelData.texCoordinates, 2, myModelData.normals,
						myModelData.primitive);

		myInnerModel = Canvas.getInstance().getPlugin().drawablefactory()
				.model(myModelData, myInnerModelMesh);
		myOuterModel = Canvas.getInstance().getPlugin().drawablefactory()
				.model(myModelData, myOuterModelMesh);

		// TexturePlugin myTexture =
		// Canvas.getInstance().getPlugin().drawablefactory().texture();
		// myTexture.load(Bitmaps.getBitmap(Resource.getStream("demo/common/styrofoamplates.png")));
		// myTexture.setWrapMode(Canvas.getInstance().getPlugin().TEXTURE_WRAPMODE_CLAMP);
		// myModel.mesh().material().addPlugin(myTexture);

		myInnerModel.mesh().material().lit = true;
		myOuterModel.mesh().material().lit = true;

		myInnerModel.mesh().material().wireframe = true;
		myOuterModel.mesh().material().wireframe = true;
		
		myInnerModel.mesh().material().color = color;
		myOuterModel.mesh().material().color = color;
		
		myInnerModel.mesh().transform().translation.x = pos.x;
		myInnerModel.mesh().transform().translation.y = pos.y;
		myInnerModel.mesh().transform().translation.z = pos.z;

		myInnerModel.mesh().scale(innerRadius, 300, innerRadius);

		myOuterModel.mesh().transform().translation.x = pos.x;
		myOuterModel.mesh().transform().translation.y = pos.y;
		myOuterModel.mesh().transform().translation.z = pos.z;

		myOuterModel.mesh().scale(outerRadius, 300, outerRadius);
		

	}

	public void showOutlines(int i){
		if(i == 1){
			myRenderer.add(myInnerModel);
			myRenderer.add(myOuterModel);
		}else{
			myRenderer.remove(myInnerModel);
			myRenderer.remove(myOuterModel);
		}
	}
		
	public PVector getAttractionForce(Boid _boid) {
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
		return new PVector();
	}

	private float getLevelAttractionLinear(float _d) {
		return maxAttractionForce;
	}

	private float getInnerAttractionLinear(float _d) {
	    return maxAttractionForce * ((outerRadius - _d) / (outerRadius - innerRadius));
	}

	private float getOuterAttractionLinear(float _d) {
	    return maxAttractionForce * ((_d - innerRadius) / (outerRadius - innerRadius));
	}

}