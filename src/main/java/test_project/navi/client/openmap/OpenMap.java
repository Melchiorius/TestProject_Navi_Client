package test_project.navi.client.openmap;


import com.bbn.openmap.Environment;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.PropertyHandler;
import com.bbn.openmap.gui.BasicMapPanel;
import com.bbn.openmap.gui.MapPanel;
import com.bbn.openmap.gui.OpenMapFrame;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class OpenMap {
    private MapPanel mapPanel;
    private static OpenMap _instance;

    public static OpenMap getInstance(){
        if(_instance == null){
            SwingUtilities.invokeLater(() -> {
                try {
                    _instance = new OpenMap(configurePropertyHandler("./openmap.properties"));
                    _instance.showInFrame();
                } catch (IOException ex) {
                    Logger.getLogger(OpenMap.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        return _instance;
    }

    private OpenMap() {
        this((PropertyHandler) null);
    }

    private OpenMap(PropertyHandler propertyHandler) {
        configureMapPanel(propertyHandler);
    }

    private static PropertyHandler configurePropertyHandler(String propertiesFile) throws IOException {
        try {
            return new PropertyHandler.Builder().setPropertiesFile(propertiesFile).build();
        } catch (MalformedURLException murle) {
            Logger.getLogger(OpenMap.class.getName()).log(Level.WARNING, murle.getMessage(), murle);
        } catch (IOException ioe) {
            Logger.getLogger(OpenMap.class.getName()).log(Level.WARNING, ioe.getMessage(), ioe);
        }
        return new PropertyHandler();
    }

    public MapHandler getMapHandler() {
        return mapPanel.getMapHandler();
    }

    private void showInFrame() {
        MapHandler mapHandler = getMapHandler();
        OpenMapFrame omf = (OpenMapFrame) mapHandler.get(OpenMapFrame.class);
        if (omf == null) {
            omf = new OpenMapFrame(Environment.get(Environment.Title));
            PropertyHandler propertyHandler = (PropertyHandler) mapHandler.get(PropertyHandler.class);
            if (propertyHandler != null) {
                omf.setProperties("openmap", propertyHandler.getProperties());
            }
            getMapHandler().add(omf);
        }
        omf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        omf.setVisible(true);
    }

    private void configureMapPanel(PropertyHandler propertyHandler) {
        BasicMapPanel basicMapPanel = new BasicMapPanel(propertyHandler, true);
        basicMapPanel.create();
        mapPanel = basicMapPanel;
    }
}
