package test_project.navi.client.openmap.layer;

import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.policy.BufferedImageRenderPolicy;
import com.bbn.openmap.omGraphics.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import test_project.navi.client.Communication;
import test_project.navi.client.configuration.Config;
import test_project.navi.client.entity.Route;
import test_project.navi.client.entity.RoutePoint;

import java.awt.*;
import java.util.List;

public class RouteLayer
        extends OMGraphicHandlerLayer {

    List<Route> routes;

    public RouteLayer() {
        setName("Routes Layer");
        setProjectionChangePolicy(new com.bbn.openmap.layer.policy.StandardPCPolicy(this, true));
        setRenderPolicy(new BufferedImageRenderPolicy());
    }

    public synchronized OMGraphicList prepare() {
        OMGraphicList list = getList();
        if (list == null) {
            list = init();
        }

        list.generate(getProjection());

        return list;
    }

    private void getRoutesFromDB(){
        if(routes != null){
            routes.clear();
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
            Communication communication = context.getBean("communication",Communication.class);
            routes = communication.getAllRoutes();
        context.close();
    }


    public OMGraphicList init() {

        if(routes == null){
            getRoutesFromDB();
        }
        OMGraphicList omList = new OMGraphicList();

        routes.stream().forEach(route -> {
            OMGraphicList pointList = new OMGraphicList();
            OMPoint previousPoint = null;
            for(RoutePoint point : route.getPoints()){
                OMPoint _point = new OMPoint(point.getX(),point.getY());

                _point.setFillPaint(Color.blue);
                _point.setOval(true);
                pointList.add(_point);
                if(previousPoint != null){
                    OMLine line = new OMLine(previousPoint.getLat(), previousPoint.getLon(), _point.getLat(), _point.getLon(), OMGraphic.LINETYPE_STRAIGHT);
                    line.setStroke(new BasicStroke(1));
                    line.setLinePaint(Color.blue);
                    omList.add(line);

                }
                previousPoint = _point;
            };
            omList.add(pointList);
        });

        return omList;
    }
}
