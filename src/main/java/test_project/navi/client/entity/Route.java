package test_project.navi.client.entity;

import com.bbn.openmap.omGraphics.OMPoint;

import java.util.List;

public class Route {

    private int id;
    private String name;
    private String description;

    private List<RoutePoint> points;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RoutePoint> getPoints() {
        return points;
    }

    public RoutePoint getPointByIndex(int index) {
        if(index>=0 && index<points.size()) {
            return points.get(index);
        }
        return null;
    }

    public OMPoint getOMPointByIndex(int index) {
        if(index>=0 && index<points.size()) {
            return points.get(index).getOMPoint();
        }
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoints(List<RoutePoint> points) {
        this.points = points;
    }
}
