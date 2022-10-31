package test_project.navi.client.openmap.layer;

import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMLine;
import com.bbn.openmap.omGraphics.OMPoint;
import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.util.Debug;
import com.bbn.openmap.util.PaletteHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import test_project.navi.client.Communication;
import test_project.navi.client.configuration.Config;
import test_project.navi.client.entity.Route;
import test_project.navi.client.entity.Vehicle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class VehicleLayer extends OMGraphicHandlerLayer {

    List<Vehicle> vehicles;

    double movementFactor = 0.1;
    Timer timer;
    int timerDelay = 50;

    public VehicleLayer() {
        getVehiclesFromDB();
    }

    private void getVehiclesFromDB(){
        if(vehicles != null){
            vehicles.clear();
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
            Communication communication = context.getBean("communication",Communication.class);
            List<Route> routes = communication.getAllRoutes();
            vehicles = communication.getAllVehicle();
            vehicles.forEach(vehicle -> {
                int route_index = vehicle.getRouteId()-1;
                if(route_index>=0 && route_index<routes.size()) {
                    vehicle.setRoute(routes.get(route_index));
                }
            });
        context.close();
    }

    public synchronized OMGraphicList prepare() {
        OMGraphicList ret = new OMGraphicList(vehicles.stream().map(Vehicle::getCurrentPoint).collect(Collectors.toList()));
        ret.generate(getProjection());
        return ret;
    }

    public void removed(Container cont) {
        vehicles.clear();
        stopTimer();
    }

    public void reStartSimulation() {

        Projection proj = getProjection();
        if (proj == null) {
            return;
        }
        getVehiclesFromDB();
        // Update the map
        doPrepare();
    }

    public void clearVehicles() {
        vehicles.clear();
        // Update the map
        doPrepare();
        stopTimer();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }

        if (timerButton != null) {
            timerButton.setSelected(false);
        }
    }

    public void resetTimer() {
        stopTimer();

        timer = new Timer();
        timer.scheduleAtFixedRate(new VehicleLayer.ManageGraphicsTask(VehicleLayer.this), 0, timerDelay);

        if (timerButton != null) {
            timerButton.setSelected(true);
        }
    }

    JPanel panel = null;
    JButton startSimButton = null;
    JCheckBox timerButton = null;

    public Component getGUI() {
        if (panel == null) {
            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            panel = new JPanel(gridbag);

            startSimButton = new JButton("Restart Simulation");
            startSimButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    reStartSimulation();
                }
            });

            c.insets = new Insets(5, 5, 5, 2);
            c.anchor = GridBagConstraints.NORTHWEST;
            gridbag.setConstraints(startSimButton, c);
            panel.add(startSimButton);

            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0/* min */, 50/* max */, 10/* initial */);
            java.util.Hashtable dict = new java.util.Hashtable();
            dict.put(new Integer(2), new JLabel(".2"));
            dict.put(new Integer(10), new JLabel("1"));
            dict.put(new Integer(30), new JLabel("3"));
            dict.put(new Integer(50), new JLabel("5"));
            slider.setLabelTable(dict);
            slider.setPaintLabels(true);
            slider.setMajorTickSpacing(2);
            slider.setPaintTicks(true);

            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    JSlider slider2 = (JSlider) ce.getSource();
                    if (slider2.getValueIsAdjusting()) {
                        double interval = ((double) (slider2.getValue()) + .01f) / 100;
                        Debug.output("Speed modifier set to: " + interval*10);
                        movementFactor = interval;
                    }
                }
            });

            JPanel sliderPanel = PaletteHelper.createHorizontalPanel("Speed modifier:");
            sliderPanel.add(slider);

            c.insets = new Insets(0, 5, 5, 5);
            c.anchor = GridBagConstraints.WEST;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            gridbag.setConstraints(sliderPanel, c);
            panel.add(sliderPanel);

            timerButton = new JCheckBox("Run simulation", timer != null);
            timerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    JCheckBox check = (JCheckBox) ae.getSource();
                    if (check.isSelected()) {
                        resetTimer();
                    } else {
                        stopTimer();
                    }
                }
            });

            gridbag.setConstraints(timerButton, c);
            panel.add(timerButton);

        }
        return panel;
    }

    public class ManageGraphicsTask extends TimerTask {

        final VehicleLayer sal;

        public ManageGraphicsTask(VehicleLayer layer) {
            sal = layer;
        }

        @Override
        public void run() {
            Projection proj = sal.getProjection();
            for (Vehicle vehicle : sal.vehicles) {
                OMPoint currentPoint = vehicle.getCurrentPoint();
                OMPoint nextPoint = vehicle.getRoute().getOMPointByIndex(vehicle.getNextRoutePointId()-1);
                double speed = vehicle.getSpeed() * movementFactor;
                while(nextPoint != null && speed > 0) {
                    speed = moveTo(currentPoint, nextPoint, speed);
                    if(speed > 0){
                        vehicle.setNextRoutePointId(vehicle.getNextRoutePointId()+1);
                        nextPoint = vehicle.getRoute().getOMPointByIndex(vehicle.getNextRoutePointId()-1);
                    }
                }
                vehicle.setCurrentPoint(currentPoint);
                if (proj != null) {
                    currentPoint.generate(proj);
                }
            }

            sal.repaint();
        }

        protected double moveTo(OMPoint currentPoint, OMPoint nextPoint, double speed) {
            double current_x = currentPoint.getLat();
            double current_y = currentPoint.getLon();
            double next_x = nextPoint.getLat();
            double next_y = nextPoint.getLon();
            double def_x = next_x - current_x;
            double def_y = next_y - current_y;
            double dist_square = def_x*def_x+def_y*def_y;
            if(dist_square < speed*speed){
                currentPoint.setLat(nextPoint.getLat());
                currentPoint.setLon(nextPoint.getLon());
                return speed - Math.sqrt(dist_square);
            }
            double divider = Math.abs(def_x) + Math.abs(def_y);
            double slope_x = def_x / divider;
            double slope_y = def_y / divider;


            double new_x = current_x+slope_x*speed;
            double new_y = current_y+slope_y*speed;

            currentPoint.setLat(new_x);
            currentPoint.setLon(new_y);
            return 0;
        }
    }
}
