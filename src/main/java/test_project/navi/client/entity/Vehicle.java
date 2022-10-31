package test_project.navi.client.entity;

import com.bbn.openmap.omGraphics.OMPoint;

import java.awt.*;

public class Vehicle {

    private int id;
    private String name;
    private String type;
    private double speed;
    private String owner;
    private String description;
    private String cargo;
    private int routeId;
    private int nextRoutePointId;

    private OMPoint currentPoint;
    private Route route;

    public Vehicle(VehiclePOJO pojo){
        id = pojo.getId();
        name = pojo.getName();
        type = pojo.getType();
        speed = pojo.getSpeed() / 60;
        owner = pojo.getOwner();
        description = pojo.getDescription();
        cargo = pojo.getCargo();
        routeId = pojo.getRouteId();
        nextRoutePointId = pojo.getNextRoutePointId();
        currentPoint = new OMPoint(pojo.getX(),pojo.getY(), 3);
        currentPoint.setFillPaint(Color.red);
    }

    public VehiclePOJO getPOJO(){
        VehiclePOJO pojo = new VehiclePOJO();
        pojo.setId(id);
        pojo.setName(name);
        pojo.setType(type);
        pojo.setSpeed(speed * 60);
        pojo.setOwner(owner);
        pojo.setDescription(description);
        pojo.setCargo(cargo);
        pojo.setRouteId(routeId);
        pojo.setNextRoutePointId(nextRoutePointId);
        pojo.setX(currentPoint.getLat());
        pojo.setY(currentPoint.getLon());
        return pojo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() { return type; }

    public double getSpeed() { return speed; }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getCargo() {
        return cargo;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getNextRoutePointId() { return nextRoutePointId; }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSpeed(double speed) { this.speed = speed; }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setRouteId(int routeId) { this.routeId = routeId; }

    public void setNextRoutePointId(int nextRoutePointId) { this.nextRoutePointId = nextRoutePointId; }


    public double getX() { return currentPoint.getLat(); }

    public double getY() { return currentPoint.getLon(); }

    public void setX(double x) {
        if(currentPoint == null){
            currentPoint = new OMPoint(0f,0f, 3);
            currentPoint.setFillPaint(Color.red);
        }
        currentPoint.setLat(x);
    }

    public void setY(double y) {
        if(currentPoint == null){
            currentPoint = new OMPoint(0f,0f, 3);
            currentPoint.setFillPaint(Color.red);
        }
        currentPoint.setLon(y);
    }

    public OMPoint getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(OMPoint currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
