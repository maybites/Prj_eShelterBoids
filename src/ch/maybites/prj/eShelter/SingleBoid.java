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

import processing.core.*;
import ch.maybites.prj.eShelter.Canvas;
import ch.maybites.prj.eShelter.magnet.Magnet;
import ch.maybites.tools.*;
import gestalt.Gestalt;
import ch.maybites.prj.eShelter.BoidsSim.ShaderMaterial;
import gestalt.model.*;
import gestalt.texture.*;
import gestalt.texture.bitmap.IntegerBitmap;
import gestalt.render.bin.AbstractBin;
import gestalt.shape.*;
import gestalt.shape.material.TexturePlugin;

import java.io.*;

import java.util.*;

import javax.media.opengl.GL;

import com.illposed.osc.OSCMessage;

public class SingleBoid {
	// fields
	private static final int MAX_ARROUSALCOUNTER = 200;
	
	public java.util.UUID uuid;
	public int swarmID;
	Bitmap texColor;
	Bitmap refrColor;
	Bitmap reflColor;
	TexturePlugin myTexture;
	TexturePlugin myRefractionTexture;
	TexturePlugin myReflectionTexture;
	
	public PVector pos, vel, acc; // pos, velocity, and acceleration in
											// a vector datatype
	private SwarmParameters swarmProps;
	
	float scale;
	Color color;
	
	float h; // hue
	float flap = 0;
	float t = 0;
	int numberOfSwarms = 4;
	
	float arrousal = 1.0f;
	
	int arrousalCounter = 0;
	
	public boolean isAlive;
	
	int simID;

	// final String MODELNAME = "model/singleE_lowPoly.obj";
	final String MODELNAME = "resource/model/singleE_lowPolyVolume.obj";
	// final String MODELNAME = "model/weirdobject.obj";

	MayRandom random = new MayRandom();

	private AbstractBin myRenderer;
	private ModelData myModelData;
	private Model myModel;
	
	
	SingleBoid(PVector _pos, PVector inVel, int _type) {
		swarmProps = SwarmParameters.getInstance();
		simID = GlobalPreferences.getInstance().getIntProperty(GlobalPreferences.SIM_ID, 1);
		uuid = java.util.UUID.randomUUID();

		setupRenderer();
		
		set(_pos, inVel, _type);
		
		applyToSwarm(_type);

		applyTranslation();
		
		isAlive = true;
	}

	public void set(PVector _pos, PVector inVel, int _type) {
		pos = new PVector(_pos.x, _pos.y, _pos.z);
		vel = new PVector(inVel.x, inVel.y, inVel.z);
	}

	public void setShader(ShaderMaterial shaderMaterial, TexturePlugin myReflectionTexture){
 		
        /* create textures */
		myRefractionTexture = Canvas.getInstance().getPlugin().drawablefactory().texture();
		myRefractionTexture.load(texColor);
        myRefractionTexture.setTextureUnit(GL.GL_TEXTURE1);

        myModel.mesh().material().plugins().clear();
        
        myModel.mesh().material().addPlugin(myRefractionTexture);
        myModel.mesh().material().addPlugin(myReflectionTexture);
        myModel.mesh().material().addPlugin(shaderMaterial);
	}
	
	private void setupRenderer() {
		myModelData = ModelLoaderOBJ.getModelData(GlobalPreferences
					.getInstance().getStream(MODELNAME));
			
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);
		Mesh myModelMesh = Canvas
				.getInstance()
				.getPlugin()
				.drawablefactory()
				.mesh(true, myModelData.vertices, 3, myModelData.vertexColors,
						4, myModelData.texCoordinates, 2, myModelData.normals,
						myModelData.primitive);

		myModel = Canvas.getInstance().getPlugin().drawablefactory()
				.model(myModelData, myModelMesh);

		myTexture = Canvas.getInstance().getPlugin().drawablefactory().texture();
		texColor = IntegerBitmap.getDefaultImageBitmap(1, 1); 
		myTexture.setWrapMode(Canvas.getInstance().getPlugin().TEXTURE_WRAPMODE_REPEAT);
		myModel.mesh().material().addPlugin(myTexture);

		myModel.mesh().material().lit = true;

		/* add model to renderer */
		myRenderer.add(myModel);
	}
	
	public void applyToSwarm(int _swarm){
		swarmID = _swarm;
		if(myRenderer.find(myModel) == -1)
			myRenderer.add(myModel);
		applySwarmCharcteristics();
	}
	
	public void applySwarmCharcteristics(){
		scale = random.create(swarmProps.sc[swarmID] - swarmProps.sc[swarmID] / 2, swarmProps.sc[swarmID]  - swarmProps.sc[swarmID] / 2);
		color = swarmProps.color[swarmID];
		texColor.setPixel(0, 0, color);
		myTexture.load(texColor);
		if(myRefractionTexture != null)
			myRefractionTexture.load(texColor);
	}
	
	public void kill(){
		myRenderer.remove(myModel);
		isAlive = false;
	}
	
	void applyTranslation() {
		myModel.mesh().transform().translation.x = pos.x;
		myModel.mesh().transform().translation.y = pos.y;
		myModel.mesh().transform().translation.z = pos.z;

		myModel.mesh().rotation().y = (float) Math.atan2(-vel.z, vel.x);
		myModel.mesh().rotation().z = (float) Math.asin(vel.y / vel.mag());

		myModel.mesh().scale(swarmProps.sc[this.swarmID], swarmProps.sc[this.swarmID], swarmProps.sc[this.swarmID]);
	}


}
