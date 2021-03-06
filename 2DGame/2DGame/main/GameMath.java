package main;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


import entities.Entity;

public class GameMath {
	
	public final static double SIN_OF_45 = Math.sin(Math.toRadians(45));

	public static double getDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	public static double getDistance(Point2D p1, Point2D p2){
		return getDistance(p1.getX(),p1.getY(),p2.getX(),p2.getY());
	}
	public static double getDistance(Entity e1, Entity e2){
		return getDistance(e1.getX(),e1.getY(),e2.getX(),e2.getY());
	}
	public static Point2D getIntersect(Line2D l1, Line2D l2){
		double m1 = (l1.getY2() - l1.getY1()) / (l1.getX2() - l1.getX1());
		double m2 = (l2.getY2() - l2.getY1()) / (l2.getX2() - l2.getX1());
		//XXX Stupid infinity!
		if(m1 == Double.NEGATIVE_INFINITY){
			m1 = - 100000;
		}
		if(m1 == Double.POSITIVE_INFINITY){
			m1 = 100000;
		}
		if(m2 == Double.NEGATIVE_INFINITY){
			m2 = -100000;
		}
		if(m2 == Double.POSITIVE_INFINITY){
			m2 = 100000;
		}
		Point2D p1 = l1.getP2();
		Point2D p2 = l2.getP2();
		double b1 = p1.getY() - (m1 * p1.getX());
		double b2 = p2.getY() - (m2 * p2.getX());
		  double x = (b2 - b1) / (m1 - m2);
		    double y = m1 * x + b1;
		    return new Point((int) x, (int) y);
	}
	public static double getAngle(double x1, double y1, double x2, double y2){
		double angle = 0;
		double x = x1 - x2;
		double y = y1 - y2;
		angle = Math.atan2(x, y);
		return angle;
	}
	public static boolean doCollide(Entity e1, Entity e2){
		double diffX = Math.abs(e1.getX() - e2.getX());
		double diffY = Math.abs(e1.getY() - e2.getY());
		if(diffX < (e1.width + e2.width) / 2 && diffY < (e1.height + e2.height) / 2){
			return true;
		}
		return false;
	}
	
}
