package main;

import entities.Entity;

public class GameMath {
	
	public final static double SIN_OF_45 = Math.sin(Math.toRadians(45));

	public static double getDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	public static double getDistance(Entity e1, Entity e2){
		return getDistance(e1.x,e1.y,e2.x,e2.y);
	}
	public static double getAngle(double x1, double y1, double x2, double y2){
		double angle = 0;
		double x = Math.abs(x1 - x2);
		double y = Math.abs(y1 - y2);
		angle = Math.atan2(x, y);
		return Math.toDegrees(angle);
	}
	
}
