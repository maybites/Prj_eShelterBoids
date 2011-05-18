package ch.maybites.prj.eShelter;

import gestalt.shape.Color;

public class SkeletonColor {

    Color color = new Color(.2f, .2f, .2f);
    Color colorActiveState = new Color(.2f, .2f, .2f);
    Color colorPassiveState = new Color(.5f, .5f, .1f);
    Color colorReActiveState = new Color(.1f, .9f, .9f);
    Color colorOfflineState = new Color(.0f, .0f, .0f);
	final int FRAMES_OFFLINE_TO_REACTIVE = 50;
	final int FRAMES_REACTIVE_TO_PASSIVE = 200;
	final int FRAMES_PASSIVE_TO_ACTIVE = 70;
	final int FRAMES_ACTIVE_TO_PASSIV = 20;
 
	final int OFFLINE = 0;
	final int OFFLINE2REACTIVE = 1;
	final int REACTIVE = 2;
	final int REACTIVE2PASSIVE = 3;
	final int PASSIVE = 4;
	final int PASSIVE2ACTIVE = 5;
	final int ACTIVE = 6;
	final int ACTIVE2PASSIVE = 7;
	
	
	int mode = OFFLINE;
	int counter;
	
	public SkeletonColor(){
		
	}
	
	public Color color(){
		return color;
	}
	
	public boolean isAcvive(){
		if(mode != OFFLINE)
			return true;
		return false;
	}
	
	public void gotoReactive(){
		mode = OFFLINE2REACTIVE;
		counter = 0;
	}
	
	public void gotoActive(){
		mode = PASSIVE2ACTIVE;
		counter = 0;
	}
	
	public void gotoPassive(){
		mode = ACTIVE2PASSIVE;
		counter = 0;
	}
	
	public void gotoOffline(){
		mode = OFFLINE;
		counter = 0;
	}
	
	public void nextFrame(){
		if(mode != OFFLINE){
			counter++;
			switch(mode){
			case OFFLINE2REACTIVE:
				if(counter < FRAMES_OFFLINE_TO_REACTIVE){
					color.r = getMixedColorValue(colorOfflineState.r, colorReActiveState.r, FRAMES_OFFLINE_TO_REACTIVE, counter);
					color.g = getMixedColorValue(colorOfflineState.g, colorReActiveState.g, FRAMES_OFFLINE_TO_REACTIVE, counter);
					color.b = getMixedColorValue(colorOfflineState.b, colorReActiveState.b, FRAMES_OFFLINE_TO_REACTIVE, counter);
				}else{
					mode = REACTIVE2PASSIVE;
					counter = 0;
				}
				break;
			case REACTIVE2PASSIVE:
				if(counter < FRAMES_REACTIVE_TO_PASSIVE){
					color.r = getMixedColorValue(colorReActiveState.r, colorPassiveState.r, FRAMES_REACTIVE_TO_PASSIVE, counter);
					color.g = getMixedColorValue(colorReActiveState.g, colorPassiveState.g, FRAMES_REACTIVE_TO_PASSIVE, counter);
					color.b = getMixedColorValue(colorReActiveState.b, colorPassiveState.b, FRAMES_REACTIVE_TO_PASSIVE, counter);
				}else
					mode = PASSIVE;
				break;
			case PASSIVE2ACTIVE:
				if(counter < FRAMES_PASSIVE_TO_ACTIVE){
					color.r = getMixedColorValue(colorPassiveState.r, colorActiveState.r, FRAMES_PASSIVE_TO_ACTIVE, counter);
					color.g = getMixedColorValue(colorPassiveState.g, colorActiveState.g, FRAMES_PASSIVE_TO_ACTIVE, counter);
					color.b = getMixedColorValue(colorPassiveState.b, colorActiveState.b, FRAMES_PASSIVE_TO_ACTIVE, counter);
				}else
					mode = ACTIVE;
				break;
			case ACTIVE2PASSIVE:
				if(counter < FRAMES_ACTIVE_TO_PASSIV){
					color.r = getMixedColorValue(colorPassiveState.r, colorActiveState.r, FRAMES_ACTIVE_TO_PASSIV, counter);
					color.g = getMixedColorValue(colorPassiveState.g, colorActiveState.g, FRAMES_ACTIVE_TO_PASSIV, counter);
					color.b = getMixedColorValue(colorPassiveState.b, colorActiveState.b, FRAMES_ACTIVE_TO_PASSIV, counter);
				}else
					mode = PASSIVE;
				break;
			}
		}
		
	}
	
	public float getMixedColorValue(float color1, float color2, int max, int value){
		return color1 + (color2 - color1) / max * value;
	}
	
	
}
