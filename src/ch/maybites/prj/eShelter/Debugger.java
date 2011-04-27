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

public class Debugger {

	private static final int LEVEL_VERBOSE = 0;
	private static final int LEVEL_DEBUG = 1;
	private static final int LEVEL_INFO = 2;
	private static final int LEVEL_WARNING = 3;
	private static final int LEVEL_ERROR = 4;
	private static final int LEVEL_FATAL = 5;
	
	private static final String[] LEVEL_MESG = {	"Verbose: ",
													"Debug: ",
													"Info: ",
													"Warning: ",
													"Error: ",
													"Fatal: "};
	
	// methods and attributes for Singleton pattern
	private Debugger(int l) {
		myLevel = l;
		_showClassNames = true;
	}

	private int myLevel;
	private boolean _showClassNames;
	
	static private Debugger _instance;

	static public void showClassNames(){
		
	}
	
	static public void setLevelToInfo(){
		_instance.myLevel = LEVEL_INFO;
	}

	static public void setLevelToVerbose(){
		_instance.myLevel = LEVEL_VERBOSE;
	}
	
	static public void setLevelToDebug(){
		_instance.myLevel = LEVEL_DEBUG;
	}
	
	static public void setLevelToWarning(){
		_instance.myLevel = LEVEL_WARNING;
	}
	
	static public void setLevelToError(){
		_instance.myLevel = LEVEL_ERROR;
	}
	
	static public void setLevelToFatal(){
		_instance.myLevel = LEVEL_FATAL;
	}
	
	static public Debugger getInstance() {
		if (_instance == null) {
			synchronized(GlobalPreferences.class) {
				if (_instance == null)
					_instance = new Debugger(LEVEL_DEBUG);
			}
		}
		return _instance;
	}
	
	
	private void message(Class o, String message, int level){
		System.out.print(LEVEL_MESG[level]);
		if(o != null && _showClassNames)
			System.out.print("from " + o.getName() + ": ");
		System.out.println(message);
	}

	private void messageErr(Class o, String message, int level){
		System.err.print(LEVEL_MESG[level]);
		if(o != null && _showClassNames)
			System.err.print("from " + o.getName() + ": ");
		System.err.println(message);
	}

	public void infoMessage(Class o, String message){
		if(myLevel <= LEVEL_INFO)
			message(o, message, LEVEL_INFO);
	}

	public void verboseMessage(Class o, String message){
		if(myLevel <= LEVEL_VERBOSE)
			message(o, message, LEVEL_VERBOSE);
	}

	public void debugMessage(Class o, String message){
		if(myLevel <= LEVEL_DEBUG)
			message(o, message, LEVEL_DEBUG);
	}

	public void warningMessage(Class o, String message){
		if(myLevel <= LEVEL_WARNING)
			message(o, message, LEVEL_WARNING);
	}

	public void errorMessage(Class o, String message){
		if(myLevel <= LEVEL_ERROR)
			messageErr(o, message, LEVEL_ERROR);
	}

	public void fatalMessage(Class o, String message){
		if(myLevel <= LEVEL_FATAL)
			messageErr(o, message, LEVEL_FATAL);
	}

}
