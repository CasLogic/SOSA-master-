package edu.gsu.psych.sosa.main;

import java.awt.geom.Point2D.Float;

@SuppressWarnings("serial")
public class SOSAPoint2D extends Float {
	/**
	 * Creates a SOSA point object
	 * @param float x
	 * @param float y
	 */
	public SOSAPoint2D(float x, float y) {
		super(x,y);
	}

	public String toString(){
		return "("+x+","+y+")";		
	}
}
