package application;

import application.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main entry for the Client side. It extends the
 * JavaFX Application class and the start() method is the
 * main entry point into the JavaFX application.
 *
 * @author James Gardner
 * @see    Logger
 * @see    MainConnection
 * @see    ViewFactory
 */
public class Launcher extends Application {

  /* Logger used by Client. Prints to both the console and to a file 'logFile.log' saved
   * under resources/logs. All classes in Client should create a Logger of the same name. */
  private static final Logger log = LoggerFactory.getLogger("Client Logger");

  @Override
  public void start(Stage stage) throws Exception {
    log.info("Starting...");
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.showPresentationWindow(stage);
  }

  public static void main(String[] args) {
    /* This method launches the JavaFX Runtime and the JavaFX Application. */
    launch(args);
  }
}
