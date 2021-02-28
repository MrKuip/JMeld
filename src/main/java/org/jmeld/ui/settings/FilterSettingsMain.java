package org.jmeld.ui.settings;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.Icon;

import org.jfree.fx.FXGraphics2D;
import org.jmeld.ui.util.Icons;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FilterSettingsMain
    extends Application
{
  public FilterSettingsMain()
  {
  }

  public void start(Stage stage)
  {
    TabPane tabPane;
    Tab tab;
    Scene scene;
    StackPane root;

    tabPane = new TabPane();
    tab = new Tab("Settings");
    tab.setGraphic(getIcon(Icons.SETTINGS.getSmallIcon()));
    tabPane.getTabs().add(tab);

    root = new StackPane();
    root.getChildren().add(tabPane);

    scene = new Scene(root,
                      300,
                      300);

    stage.setTitle("JMeld");
    stage.setScene(scene);
    stage.show();

  }

  private Node getIcon(Icon icon)
  {
    Canvas canvas;
    Graphics2D g2;

    canvas = new Canvas(icon.getIconWidth(),
                        icon.getIconHeight());
    g2 = new FXGraphics2D(canvas.getGraphicsContext2D());

    icon.paintIcon(null,
                   g2,
                   0,
                   0);

    return canvas;
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
