package application.controller;

import application.view.ViewFactory;
import application.view.ViewInitialiser;

/**
 * This abstract Class is the base of all controllers. It provides
 * a common set of methods and fields that all controllers must implement.
 *
 * @author James Gardner
 * @see    ViewInitialiser
 * @see    ViewFactory
 * @see    LoginWindowController
 */
public abstract class BaseController {

  protected ViewFactory viewFactory;
  private String fxmlName;

  /**
   * Constructor that all controllers must use.
   *
   * @param viewFactory
   *        The ViewFactory creates windows that are controlled by the controller.
   *
   * @param fxmlName
   *        The FXML file that describes a window the controller contains the logic for.
   */
  public BaseController(ViewFactory viewFactory, String fxmlName) {
    this.viewFactory = viewFactory;
    this.fxmlName = fxmlName;
  }

  public String getFxmlName() {
    return fxmlName;
  }

  public ViewFactory getViewFactory() {
    return viewFactory;
  }
}
