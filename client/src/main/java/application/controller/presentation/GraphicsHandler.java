package application.controller.presentation;

import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphicsHandler {

  private static final Logger log = LoggerFactory.getLogger("Graphics Logger");

  public GraphicsHandler(StackPane targetPane) {
    log.warn("Constructor not implemented");
  }

  public void registerLine(float startX, float endX, float startY, float endY,
      String lineColor, String id) {
    log.warn("Method not implemented");
  }

  public void registerRectangle(float startX, float startY, float width, float height,
      String lineColor, String id) {
    log.warn("Method not implemented");
  }

  public void registerRectangle(float startX, float startY, float width, float height,
      String id, float shadingX1, float shadingY1, String shadingColor1,
      float shadingX2, float shadingY2, String shadingColor2, boolean shadingCyclic) {
    log.warn("Method not implemented");
  }

  public void registerOval(float startX, float startY, float width, float height,
      String lineColor, String id) {
    log.warn("Method not implemented");
  }

  public void registerOval(float startX, float startY, float width, float height,
      String id, float shadingX1, float shadingY1, String shadingColor1,
      float shadingX2, float shadingY2, String shadingColor2, boolean shadingCyclic) {
    log.warn("Method not implemented");
  }

  public void drawGraphic(String id) {
    log.warn("Method not implemented");
  }

  public void undrawGraphic(String id) {
    log.warn("Method not implemented");
  }

  public void deregisterGraphic(String id) {
    log.warn("Method not implemented");
  }
}
