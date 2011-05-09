/*
 * Copyright (C) 2011 Martin Fršhlich
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

import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class GlobalPreferences {

	static private GlobalPreferences _instance;
	private int localOSCID;
	
	// methods and attributes for Singleton pattern
	private GlobalPreferences() {
		localOSCID = 0;
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
			url = new URL("file://" +_dataPath+filename);
	        return url.openStream();
		} catch (MalformedURLException e) {
	        Debugger.getInstance().fatalMessage(this.getClass(), "### ERROR @getStream / "+ _dataPath + filename + " / " + e.getMessage());
		} catch (IOException e) {
	        Debugger.getInstance().fatalMessage(this.getClass(), "### ERROR @getStream / "+ _dataPath + filename + " / " + e.getMessage());
		}
        return null;
    }

    
	public void setLocalOSCID(int sim){
		localOSCID = sim;
	}

	public int getOSC_ID(){
		return localOSCID;
	}
}
