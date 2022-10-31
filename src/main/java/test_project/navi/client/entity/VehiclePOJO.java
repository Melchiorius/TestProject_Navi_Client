package test_project.navi.client.entity;

public class VehiclePOJO {

    private int id;
    private String name;
    private String type;
    private double speed;
    private String owner;
    private String description;
    private String cargo;
    private int routeId;
    private int nextRoutePointId;
    private double x;
    private double y;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

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

    public double getX() { return x; }

    public double getY() { return y; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type){
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

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }


}
