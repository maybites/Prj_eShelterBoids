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


import gestalt.Gestalt;
import gestalt.shape.*;
import gestalt.shape.material.MaterialPlugin;
import gestalt.shape.material.TexturePlugin;
import gestalt.texture.Bitmaps;
import gestalt.candidates.glsl.ShaderManager;
import gestalt.candidates.glsl.ShaderProgram;
import gestalt.context.GLContext;
import gestalt.render.bin.AbstractBin;

import javax.media.opengl.GL;

import mathematik.Vector3f;

import processing.core.*;

import com.illposed.osc.*;

public class LetterBox{
	
    public Line myBox;
		
	private AbstractBin myRenderer;
		
	Color color;
		
	LetterBox() {
		color = new Color(1, 0, 0);
				
		setupRenderer();
								
	}

	private void setupRenderer() {
		myRenderer = Canvas.getInstance().getPlugin().bin(Gestalt.BIN_3D);
                
		myBox = Canvas.getInstance().getPlugin().drawablefactory().line();
		myBox.points = new Vector3f[5];
		myBox.colors = new Color[5];
        for (int i = 0; i < myBox.points.length; i++) {
        	myBox.points[i] = new Vector3f(0f, 0f, 0f);
        	myBox.colors[i] = color;
        }

        myBox.scale().scale(1f);
        myBox.position().set(0f, 0f, 0f);
        
        myBox.linewidth = 5;
        myBox.smooth = true;
        myBox.stipple = false;
	}
	
	public void show(){
		myRenderer.add(myBox);
	}
	
	public void noShow(){
		myRenderer.remove(myBox);
	}
		
	public void setLineVertices(Vector3f _pos, Vector3f _size, int _lineWidth){
		myBox.points[0].set(_pos.x+_size.x, _pos.y+_size.y, _pos.z);
		myBox.points[1].set(_pos.x-_size.x, _pos.y+_size.y, _pos.z);
		myBox.points[2].set(_pos.x-_size.x, _pos.y-_size.y, _pos.z);
		myBox.points[3].set(_pos.x+_size.x, _pos.y-_size.y, _pos.z);
		myBox.points[4].set(_pos.x+_size.x, _pos.y+_size.y, _pos.z);
		 myBox.linewidth = _lineWidth;
	}	

}
