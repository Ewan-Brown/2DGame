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
		double x = x1 - x2;
		double y = y1 - y2;
		angle = Math.atan2(x, y);
		return angle;
	}
	public static boolean doCollide(Entity e1, Entity e2){
		double dist = getDistance(e1,e2);
		double deltaX = Math.abs(e1.x - e2.x);
		double deltaY = Math.abs(e1.y - e2.y);
		if(deltaX < (e1.width + e2.width) / 2 && deltaY < (e1.height + e2.height) / 2){
			return true;
		}
		return false;
	}
	
}
