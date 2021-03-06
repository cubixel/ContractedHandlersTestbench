package application.controller.presentation;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Module to display text onto a StackPlane by adding extra TextFlows.
 * Also handles removing text via an ID assigned when drawn.
 *
 * @author Daniel Bishop/Eric Walker
 *
 */
public class TextHandler {

  //private static final Logger log = LoggerFactory.getLogger("Client Logger");

  private StackPane pane;
  private Map<String, TextFlow> texts = new HashMap<>();
  private String dfFont;
  private int dfFontSize;
  private String dfFontColor;

  /**
   * Set up the handler with the target pane and default settings.
   */
  public TextHandler(StackPane targetPane, String dfFont, int dfFontSize, String dfFontColor) {
    this.pane = targetPane;
    this.dfFont = dfFont;
    this.dfFontColor = dfFontColor;
    this.dfFontSize = dfFontSize;
  }

  /**
   * Registers text from an input node, using the provided ID.
   */
  public String registerText(Node node, String id) {

    //Set up defaults
    TextFlow elementText = new TextFlow();
    String fontName = this.dfFont;
    int fontSize = this.dfFontSize;
    Text partialText;
    String fontColor = this.dfFontColor;
    
    // Overwrite defaults
    // If size is specified, use it
    Node sizeNode = node.getAttributes().getNamedItem("fontsize");
    if (sizeNode != null) {
      fontSize = Integer.parseInt(sizeNode.getTextContent());
    }

    // If color is specified, use it
    Node colorNode = node.getAttributes().getNamedItem("fontcolor");
    if (colorNode != null) {
      fontColor = colorNode.getTextContent();
    }

    // If Font is specified, set it to that, else defaults
    Node fontNode = node.getAttributes().getNamedItem("font");
    if (fontNode != null) {
      fontName = fontNode.getTextContent();
    }

    Color fontFill = Color.web(fontColor);
    Font basic = Font.font(fontName, fontSize);
    Font italic = Font.font(fontName, FontPosture.ITALIC, fontSize);
    Font bold = Font.font(fontName, FontWeight.BOLD, fontSize);

    // Add each text element to the TextFlow
    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      // Add plain text
      if (children.item(i).getNodeName().equals("#text")) {
        partialText = new Text(children.item(i).getNodeValue());
        partialText.setFill(fontFill);
        partialText.setFont(basic);
        elementText.getChildren().add(partialText);
      // Add bold text
      } else if (children.item(i).getNodeName().equals("b")) {
        partialText = new Text(children.item(i).getChildNodes().item(0).getNodeValue());
        partialText.setFill(fontFill);
        partialText.setFont(bold);
        elementText.getChildren().add(partialText);
      // Add italic text
      } else if (children.item(i).getNodeName().equals("i")) {
        partialText = new Text(children.item(i).getChildNodes().item(0).getNodeValue());
        partialText.setFill(fontFill);
        partialText.setFont(italic);
        elementText.getChildren().add(partialText);
      }
    }



    float floatX = Float.parseFloat(node.getAttributes().getNamedItem("xpos").getTextContent());
    float floatY = Float.parseFloat(node.getAttributes().getNamedItem("ypos").getTextContent());

    // Calculate pixel values for x, y, w and h
    int x = Math.toIntExact(Math.round((floatX / 100) * pane.getMaxWidth()));
    int y = Math.toIntExact(Math.round((floatY / 100) * pane.getMaxHeight()));

    elementText.setTranslateX(x);
    elementText.setTranslateY(y);

    if (texts.putIfAbsent(id, elementText) == null) {
      return id;
    } else {
      return null;
    }
  }

  /**
   * Draw the text with the provided ID.
   */
  public boolean drawText(String id) {
    if (texts.containsKey(id) && !pane.getChildren().contains(texts.get(id))) {
      pane.getChildren().add(texts.get(id));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Undraws the text with the provided ID.
   */
  public boolean undrawText(String id) {
    if (texts.containsKey(id) && pane.getChildren().contains(texts.get(id))) {
      pane.getChildren().remove(texts.get(id));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Deregister the text with the provided ID.
   */
  public boolean deregisterText(String id) {
    if (texts.containsKey(id)) {
      pane.getChildren().remove(texts.get(id));
      texts.remove(id);
      return true;
    } else {
      return false;
    }
  }
}