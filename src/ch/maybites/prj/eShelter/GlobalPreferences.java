/*
 * Copyright (C) 2011 Martin Fr�hlich
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

import gestalt.model.ModelLoaderOBJ;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class GlobalPreferences {

	public static final String SIM_ID 					= "SIM_ID";
	public static final String OTHERSIM_ID 				= "OTHERSIM_ID";
	public static final String OSC_LISTENPORT 			= "OSC_LISTENPORT";
	public static final String OSC_SEND_MUSIC_PORT 		= "OSC_SEND_MUSIC_PORT";
	public static final String OSC_SEND_MUSIC_ADDRESS 	= "OSC_SEND_MUSIC_ADDRESS";
	public static final String OSC_SEND_SIM_PORT 		= "OSC_SEND_SIM_PORT";
	public static final String OSC_SEND_SIM_ADDRESS 	= "OSC_SEND_SIM_ADDRESS";

	static private GlobalPreferences _instance;
	private int localOSCID;
	private Properties props;
	
	// methods and attributes for Singleton pattern
	private GlobalPreferences() {
		localOSCID = 0;
		props = new Properties();
	}

	static public GlobalPreferences getInstance() {
		if (_instance == null) {
			synchronized(GlobalPreferences.class) {
				if (_instance == null)
					_instance = new GlobalPreferences();
			}
		}
		return _instance;
	}
	
	private String _dataPath = "";
	/*
	 * returns the absolute path. the specified addPath, which is relative to the 
	 * data folder, is being added to the absolute path of the data folder
	 */

	/**
	 * Sets the actual absolute data path. needs an "/" at the end of the path;
	 * @param path
	 */
	public void setDataPath(String path){
		_dataPath = path;
		try {
			FileInputStream file = new FileInputStream(path + "properties.txt");
			props.load(file);
			file.close();
		} catch (IOException exp) {
			Debugger.getInstance().errorMessage(this.getClass(),
					"No Property File found: " + exp.getMessage());
			;
		}
	}

	/**
	 * 
	 * @param addPath 	the specified addPath, starts without "/", which is relative to the 
	 * data folder, is being added to the absolute path of the data folder
	 * @return	the absolute path
	 */
	public String getAbsDataPath(String addPath){
		return _dataPath + addPath;
	}
	
	public String getAbsResourcePath(String addPath){
		return _dataPath + "resource/" + addPath;
	}

    public InputStream getStream(String filename)
    {
		try {
	        URL url;
			url = new URL("file:///" +_dataPath+filename);
	        return url.openStream();
		} catch (MalformedURLException e) {
	        Debugger.getInstance().fatalMessage(this.getClass(), "### ERROR @getStream / "+ _dataPath + filename + " / " + e.getMessage());
		} catch (IOException e) {
	        Debugger.getInstance().fatalMessage(this.getClass(), "### ERROR @getStream / "+ _dataPath + filename + " / " + e.getMessage());
		}
        return null;
    }

    public String getStringProperty(String key, String _default){
    	if(props.containsKey(key))
    		return props.getProperty(key, _default);
		Debugger.getInstance().errorMessage(this.getClass(),"No Property found for key: " + key + "... returning default value: "+_default);
		return _default;
    }

    public int getIntProperty(String key, int _default){
    	if(props.containsKey(key))
    		return Integer.parseInt(props.getProperty(key));
		Debugger.getInstance().errorMessage(this.getClass(),"No Property found for key: " + key + "... returning default value: "+_default);
		return _default;
    }
    
    public float getfloatProperty(String key, float _default){
    	if(props.containsKey(key))
    		return Float.parseFloat(props.getProperty(key));
		Debugger.getInstance().errorMessage(this.getClass(),"No Property found for key: " + key + "... returning default value: "+_default);
		return _default;
    }
}
