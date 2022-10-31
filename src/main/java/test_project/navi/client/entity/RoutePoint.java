package test_project.navi.client.entity;

import com.bbn.openmap.omGraphics.OMPoint;

public class RoutePoint {

    private int id;
    private double x;
    private double y;
    private int routeId;

    private OMPoint omPoint;


    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public OMPoint getOMPoint() {
        if(omPoint == null){
            omPoint = new OMPoint(x,y);
        }
        return omPoint;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(double x) {
        this.x = x;
        if(omPoint == null){
            omPoint = new OMPoint(0f,0f);
        }
        omPoint.setLat(x);
    }

    public void setY(double y) {
        this.y = y;
        if(omPoint == null){
            omPoint = new OMPoint(0f,0f);
        }
        omPoint.setLon(y);
    }

    public void setPoint(double x,double y) {
        this.x = x;
        this.y = y;
        if(omPoint == null){
            omPoint = new OMPoint(0f,0f);
        }
        omPoint.setLat(x);
        omPoint.setLon(y);
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
