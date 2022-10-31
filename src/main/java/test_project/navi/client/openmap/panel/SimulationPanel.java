package test_project.navi.client.openmap.panel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;

import com.bbn.openmap.I18n;
import com.bbn.openmap.Layer;
import com.bbn.openmap.LayerHandler;
import com.bbn.openmap.event.LayerEvent;
import com.bbn.openmap.event.LayerListener;
import com.bbn.openmap.event.ZoomSupport;
import com.bbn.openmap.gui.OMToolComponent;
import com.bbn.openmap.proj.Projection;
import test_project.navi.client.openmap.layer.VehicleLayer;


/**
 * The ScaleTextPanel is a JPanel holding a JTextField that controls
 * and responds to the scale setting of a MapBean's projection. It is
 * also a Tool, so it can be added to the ToolPanel.
 */
public class SimulationPanel extends OMToolComponent implements Serializable,
        ActionListener, LayerListener {

    public final static String defaultSimulationPanelKey = "SimulationPanel";
    public transient final static String setScaleCmd = "setScale";
    protected transient JButton simulationButton = null;
    protected transient LayerHandler layerHandler = null;
    protected Projection projection;
    protected VehicleLayer simulationLayer = null;

    public SimulationPanel() {
        super();
        setKey(defaultSimulationPanelKey);
            simulationButton = new JButton("Simulation");
            simulationButton.setBorderPainted(false);
            simulationButton.setToolTipText(i18n.get(SimulationPanel.class, "simulationButton", I18n.TOOLTIP, "Simulation"));
            simulationButton.setMargin(new Insets(0, 0, 0, 0));
            simulationButton.addActionListener(this);
            add(simulationButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(simulationLayer != null) {
            simulationLayer.showPalette();
        }
        else{
            System.out.println("Oooops!");
        }
    }

    @Override
    public void setLayers(LayerEvent layerEvent) {
        Layer[] layers = layerEvent.getLayers();
        for(Layer l : layers){
            if(l instanceof VehicleLayer){
                simulationLayer = (VehicleLayer)l;
            }
        }
    }
}