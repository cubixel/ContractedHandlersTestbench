package application.view;

import application.controller.BaseController;
import application.controller.PresentationWindowController;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to generate and the views that
 * the user will see. It contains the methods used to generate
 * a 'Controller' to be applied to the supplied Stage for
 * particular FXMLScene. It also generates Controllers for
 * embedded FXML Scenes.
 *
 * @author James Gardner
 * @see    ViewInitialiser
 * @see    BaseController
 */
public class ViewFactory {

  private ViewInitialiser viewInitialiser;
  private static final Logger log = LoggerFactory.getLogger("Client Logger");

  /**
   * Initialises a newly created {@code ViewFactory} object. Needs
   * access to the main client-server connection in order to
   * distribute this to the controllers. This constructor makes
   * creates a new {@code ViewInitialiser} and then calls the
   * other constructor created for testing purposes.
   *
   * @param mainConnection
   *        This is the main connection to the server, established on startup
   *
   */
  public ViewFactory() {
    /* Makes a call to the other constructor that has been created for testing. */
    this.viewInitialiser = new ViewInitialiser();
  }

  /**
   * Creates a PresentationWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showPresentationWindow(Stage stage) {
    BaseController controller =
        new PresentationWindowController(this, "fxml/PresentationWindow.fxml");
    viewInitialiser.initialiseStage(controller, stage);
    log.info("Initialised PresentationWindow");
  }
}
