package application.controller.presentation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * CLASS DESCRIPTION.
 *
 * @author CUBIXEL
 *
 */

public class TimingManager extends Thread {
  private static final Logger log = LoggerFactory.getLogger("Client Logger");

  private long slideStartTime;
  private long currentTime;
  private long timeElapsed;
  private long slideDuration;
  private int slideNumber = 0;
  private TimingNode tempNode;
  private ArrayList<LinkedList<TimingNode>> startTimesList = 
      new ArrayList<LinkedList<TimingNode>>();
  private ArrayList<LinkedList<TimingNode>> endTimesList = new ArrayList<LinkedList<TimingNode>>();
  private LinkedList<TimingNode> startTimes = new LinkedList<TimingNode>();
  private LinkedList<TimingNode> endTimes = new LinkedList<TimingNode>();
  private LinkedList<TimingNode> displayedNodes = new LinkedList<TimingNode>();
  private PresentationObject presentation;
  private TextHandler textHandler;
  private ImageHandler imageHandler;
  private VideoHandler videoHandler;
  private AudioHandler audioHandler;
  private GraphicsHandler graphicsHandler;
  //private GraphicsHandler graphicsHandler;
  //private AudioHandler audioHandler;

  /**
   * METHOD DESCRIPTION.
   */
  public TimingManager(PresentationObject presentation, StackPane pane, TextHandler textHandler, 
      ImageHandler imageHandler, VideoHandler videoHandler, AudioHandler audioHandler,
      GraphicsHandler graphicsHandler) {
    setDaemon(true);
    this.presentation = presentation;
    this.textHandler = textHandler;
    this.imageHandler = imageHandler;
    this.videoHandler = videoHandler;
    this.audioHandler = audioHandler;
    this.graphicsHandler = graphicsHandler;

    List<PresentationSlide> slidesList = presentation.getSlidesList();
    PresentationSlide slide;
    List<Node> elements;
    Node element;
    String elementName;
    NamedNodeMap attributes;
    String tempId;

    for (int slideId = 0; slideId < slidesList.size(); slideId++) {
      slide = presentation.getSlidesList().get(slideId);
      startTimesList.add(new LinkedList<TimingNode>());
      endTimesList.add(new LinkedList<TimingNode>());

      elements = slide.getElementList();
      log.info("Making Slide " + slideId + " with " + elements.size() + " elements.");
      for (int elementId = 0; elementId < elements.size(); elementId++) {
        element = elements.get(elementId);
        elementName = element.getNodeName();
        attributes = element.getAttributes();
        tempId = slideId + ":" + elementId;
        switch (elementName) {
          case "text":
            textHandler.registerText(element, tempId);
            addElement(elementName, slideId, elementId, 
                attributes.getNamedItem("starttime").getNodeValue(), 
                attributes.getNamedItem("endtime").getNodeValue());
            log.info("Text element made at ID " + tempId);
            break; 
          case "line":
            graphicsHandler.registerLine(
                Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                Float.parseFloat(attributes.getNamedItem("xend").getTextContent()),
                Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()),
                Float.parseFloat(attributes.getNamedItem("yend").getTextContent()),
                attributes.getNamedItem("linecolor").getTextContent(),
                tempId);
            addElement(elementName, slideId, elementId, 
                attributes.getNamedItem("starttime").getNodeValue(), 
                attributes.getNamedItem("endtime").getNodeValue());
            log.info("Line element made at ID " + tempId);
            break; 
          case "shape":
            // Check if rectangle or oval
            if (attributes.getNamedItem("type").getTextContent().equals("rectangle")) {
              //Check for shading
              if (element.getChildNodes().getLength() > 0) {
                // Shading version
                NamedNodeMap shading = element.getChildNodes().item(1).getAttributes();
                graphicsHandler.registerRectangle(
                    Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("width").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("height").getTextContent()),
                    tempId,
                    Float.parseFloat(shading.getNamedItem("x1").getTextContent()),
                    Float.parseFloat(shading.getNamedItem("y1").getTextContent()),
                    shading.getNamedItem("color1").getTextContent(),
                    Float.parseFloat(shading.getNamedItem("x2").getTextContent()),
                    Float.parseFloat(shading.getNamedItem("y2").getTextContent()),
                    shading.getNamedItem("color2").getTextContent(),
                    Boolean.parseBoolean(shading.getNamedItem("cyclic").getTextContent()));
              } else {
                // No shading version
                graphicsHandler.registerRectangle(
                    Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("width").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("height").getTextContent()),
                    attributes.getNamedItem("fillcolor").getTextContent(),
                    tempId);
              }
            } else if (attributes.getNamedItem("type").getTextContent().equals("oval")) {
              //Check for shading
              if (element.getChildNodes().getLength() > 0) {
                // Shading version
                NamedNodeMap shading = element.getChildNodes().item(1).getAttributes();
                graphicsHandler.registerOval(
                    Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("width").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("height").getTextContent()),
                    tempId,
                    Float.parseFloat(shading.getNamedItem("x1").getTextContent()),
                    Float.parseFloat(shading.getNamedItem("y1").getTextContent()),
                    shading.getNamedItem("color1").getTextContent(),
                    Float.parseFloat(shading.getNamedItem("x2").getTextContent()),
                    Float.parseFloat(shading.getNamedItem("y2").getTextContent()),
                    shading.getNamedItem("color2").getTextContent(),
                    Boolean.parseBoolean(shading.getNamedItem("cyclic").getTextContent()));
              } else {
                // No shading version
                graphicsHandler.registerOval(
                    Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("width").getTextContent()),
                    Float.parseFloat(attributes.getNamedItem("height").getTextContent()),
                    attributes.getNamedItem("fillcolor").getTextContent(),
                    tempId);
              }
            } else {
              log.error("Shape was not oval or rectangle");
              break;
            }
            addElement(elementName, slideId, elementId, 
                attributes.getNamedItem("starttime").getNodeValue(), 
                attributes.getNamedItem("endtime").getNodeValue());
            log.info("Shape element made at ID " + tempId);
            break;
          case "audio":
            audioHandler.registerAudio(
                attributes.getNamedItem("urlname").getTextContent(),
                Boolean.parseBoolean(attributes.getNamedItem("loop").getTextContent()),
                tempId);
            addElement(elementName, slideId, elementId, 
                attributes.getNamedItem("starttime").getNodeValue());
            log.info("Audio element made at ID " + tempId);
            break; 
          case "image":
            imageHandler.registerImage(attributes.getNamedItem("urlname").getTextContent(), tempId, 
                Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()), 
                Float.parseFloat(attributes.getNamedItem("width").getTextContent()), 
                Float.parseFloat(attributes.getNamedItem("height").getTextContent()));
            addElement(elementName, slideId, elementId, 
                attributes.getNamedItem("starttime").getNodeValue(), 
                attributes.getNamedItem("endtime").getNodeValue());
            log.info("Image element made at ID " + tempId);
            break; 
          case "video":
            videoHandler.registerVideo(attributes.getNamedItem("urlname").getTextContent(), tempId, 
                Float.parseFloat(attributes.getNamedItem("xstart").getTextContent()),
                Float.parseFloat(attributes.getNamedItem("ystart").getTextContent()), 
                Boolean.parseBoolean(attributes.getNamedItem("loop").getTextContent()));
            addElement(elementName, slideId, elementId, 
                attributes.getNamedItem("starttime").getNodeValue());
            log.info("Video element made at ID " + tempId);
            break; 
          default:
            break;
        }
      }
    } //end slides loop
    
  }

  @Override
  public void run() {
    log.info("Starting...");
    Boolean moreToRemove = false;
    setSlide(0);
    log.info("Start times detected: " + startTimes.size());
    log.info("End times detected: " + endTimes.size());
    while (true) {
      currentTime = System.currentTimeMillis();
      if (currentTime - slideStartTime != timeElapsed) {
        timeElapsed = currentTime - slideStartTime;
        moreToRemove = false;
        do {
          if (!startTimes.isEmpty()) {
            if (timeElapsed >= startTimes.getFirst().getTime()) {
              tempNode = startTimes.removeFirst();
              startElement(tempNode);
              displayedNodes.add(tempNode);
              moreToRemove = true;
            } else {
              moreToRemove = false;
            }
          } else {
            moreToRemove = false;
          }

        } while (moreToRemove);

        moreToRemove = false;
        do {
          if (!endTimes.isEmpty()) {
            if (timeElapsed >= endTimes.getFirst().getTime()) {
              tempNode = endTimes.removeFirst();
              endElement(tempNode);
              boolean notRemoved = true;
              int tempIndex = 0;
              while (notRemoved && (tempIndex < displayedNodes.size())) {
                if (displayedNodes.get(tempIndex).getId() == tempNode.getId()) {
                  displayedNodes.remove(tempIndex);
                  notRemoved = false;
                } else {
                  tempIndex = tempIndex + 1;
                }
              }
            } else {
              moreToRemove = false;
            }
          } else {
            moreToRemove = false;
          }
        } while (moreToRemove);

        if (timeElapsed >= slideDuration && slideDuration != -1) {
          log.info("ended slide " + slideNumber + " at " + timeElapsed + " intended " 
              + slideDuration);
          setSlide(slideNumber + 1);
        }
      }
    }
  }

  /**
   * METHOD DESCRIPTION.
   */
  public synchronized void setSlide(int number) {
    this.slideNumber = number % presentation.getTotalSlides();
    clearSlide();
    startTimes = new LinkedList<>(startTimesList.get(this.slideNumber));
    endTimes = new LinkedList<>(endTimesList.get(this.slideNumber));
    

    log.info("Adding Slide Duration");
    PresentationSlide slide = presentation.getSlidesList().get(this.slideNumber);
    addSlideTimer(slide.getDuration());
    log.info("Added slide duration of " + slideDuration);
    slideStartTime = System.currentTimeMillis();
  }

  /**
   * METHOD DESCRIPTION.
   */
  public void addElement(String name, int slideId, int elementId, String startTime, 
      String endTime) {
    int startIndex = 0;
    int endIndex = 0;
    Long startLong = Long.parseLong(startTime);
    Long endLong = Long.parseLong(endTime);
    boolean found = false;
    String id = slideId + ":" + elementId; 
    LinkedList<TimingNode> starts = startTimesList.get(slideId);
    LinkedList<TimingNode> ends = endTimesList.get(slideId);
    while (startIndex < starts.size() && !found) {
      if (starts.get(startIndex).getTime() > startLong) {
        starts.add(startIndex, new TimingNode(id, startLong, name));
        found = true;
      }
      startIndex = startIndex + 1;
    }
    if (!found) {
      starts.add(new TimingNode(id, startLong, name));
    }

    if (!endTime.equals("-1")) {
      found = false;
      while (endIndex < ends.size() && !found) {
        if (ends.get(endIndex).getTime() > endLong) {
          ends.add(endIndex, new TimingNode(id, endLong, name));
          found = true;
        }
        endIndex = endIndex + 1;
      }
      if (!found) {
        ends.add(new TimingNode(id, endLong, name));
      }
    }
  }

  /**
   * METHOD DESCRIPTION.
   */
  public void addElement(String name, int slideId, int elementId, String startTime) {
    int startIndex = 0;
    Long startLong = Long.parseLong(startTime);
    boolean found = false;
    String id = slideId + ":" + elementId; 
    LinkedList<TimingNode> starts = startTimesList.get(slideId);
    while (startIndex < starts.size() && !found) {
      if (starts.get(startIndex).getTime() > startLong) {
        starts.add(startIndex, new TimingNode(id, startLong, name));
        found = true;
      }
      startIndex = startIndex + 1;
    }
    if (!found) {
      starts.add(new TimingNode(id, startLong, name));
    }

  }

  /**
   * METHOD DESCRIPTION.
   */
  public void addSlideTimer(int duration) {
    slideDuration = Long.valueOf(duration);
    slideStartTime = System.currentTimeMillis();
  }

  /**
   * METHOD DESCRIPTION.
   */
  public void clearSlide() {
    displayedNodes.forEach(node -> {
      endElement(node);
      log.warn("Node ID " + node.getId() + " was implicitly removed on slide change;" 
          + " ignore intended removal time");
    });
    startTimes.clear();
    endTimes.clear();
    displayedNodes.clear();
    slideStartTime = -1;
    slideDuration = -1;
  }

  private synchronized void startElement(TimingNode element) {
    String elementName = element.getType();
    switch (elementName) {
      case "text":
        Platform.runLater(() -> {
          textHandler.drawText(element.getId());
        });
        break; 
      case "line":
        Platform.runLater(() -> {
          graphicsHandler.drawGraphic(element.getId());
        });
        break; 
      case "shape":
        Platform.runLater(() -> {
          graphicsHandler.drawGraphic(element.getId());
        });
        break;
      case "audio":
        Platform.runLater(() -> {
          audioHandler.startAudio(element.getId());
        });
        break; 
      case "image":
        Platform.runLater(() -> {
          imageHandler.drawImage(element.getId());
        });
        break; 
      case "video":
        Platform.runLater(() -> {
          videoHandler.startVideo(element.getId());
        });
        break; 
      default:
        break;
    }
    log.info("Started " + element.getType() + " element " + element.getId() 
        + " @ time: " + timeElapsed + " Intended: " + element.getTime());
  }

  private synchronized void endElement(TimingNode element) {
    String elementName = element.getType();
    switch (elementName) {
      case "text":
        Platform.runLater(() -> {
          textHandler.undrawText(element.getId());
        });
        break; 
      case "line":
        Platform.runLater(() -> {
          graphicsHandler.undrawGraphic(element.getId());
        });
        break; 
      case "shape":
        Platform.runLater(() -> {
          graphicsHandler.undrawGraphic(element.getId());
        });
        break;
      case "audio":
        Platform.runLater(() -> {
          audioHandler.stopAudio(element.getId());
        });
        break; 
      case "image":
        Platform.runLater(() -> {
          imageHandler.undrawImage(element.getId());
        });
        break; 
      case "video":
        Platform.runLater(() -> {
          videoHandler.stopVideo(element.getId());
        });
        break; 
      default:
        break;
    }
    log.info("Ended " + element.getType() + " element " + element.getId() 
        + " @ time: " + timeElapsed + " Intended: " + element.getTime());
  }

  public long getSlideStartTime() {
    return slideStartTime;
  }

  public void setSlideStartTime(long slideStartTime) {
    this.slideStartTime = slideStartTime;
  }

  public long getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(long currentTime) {
    this.currentTime = currentTime;
  }

  public long getTimeElapsed() {
    return timeElapsed;
  }

  public void setTimeElapsed(long timeElapsed) {
    this.timeElapsed = timeElapsed;
  }

  public long getSlideDuration() {
    return slideDuration;
  }

  public void setSlideDuration(long slideDuration) {
    this.slideDuration = slideDuration;
  }

  public int getSlideNumber() {
    return slideNumber;
  }

  public void setSlideNumber(int slideNumber) {
    this.slideNumber = slideNumber;
  }

  public TimingNode getTempNode() {
    return tempNode;
  }

  public void setTempNode(TimingNode tempNode) {
    this.tempNode = tempNode;
  }

  public LinkedList<TimingNode> getStartTimes() {
    return startTimes;
  }

  public void setStartTimes(LinkedList<TimingNode> startTimes) {
    this.startTimes = startTimes;
  }

  public LinkedList<TimingNode> getEndTimes() {
    return endTimes;
  }

  public void setEndTimes(LinkedList<TimingNode> endTimes) {
    this.endTimes = endTimes;
  }

  public LinkedList<TimingNode> getDisplayedNodes() {
    return displayedNodes;
  }

  public void setDisplayedNodes(LinkedList<TimingNode> displayedNodes) {
    this.displayedNodes = displayedNodes;
  }

  public PresentationObject getPresentation() {
    return presentation;
  }

  public void setPresentation(PresentationObject presentation) {
    this.presentation = presentation;
  }
}