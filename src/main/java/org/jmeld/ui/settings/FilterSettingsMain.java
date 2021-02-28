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
    int codepoint;
    String text;
    FontMetrics fm;
    double xString;
    double yString;

    canvas = new Canvas(icon.getIconWidth(),
                        icon.getIconHeight());
    g2 = new FXGraphics2D(canvas.getGraphicsContext2D());

    icon.paintIcon(null,
                   g2,
                   0,
                   0);
    /*
    
    codepoint = Integer.parseInt("F1064",
                                 16);
    text = new String(Character.toChars(codepoint));
    
    g2.setClip(0,
               0,
               icon.getIconWidth(),
               icon.getIconHeight());
    
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2.setColor(Color.RED);
    g2.setFont(FontUtil.getIconFont(Icons.IconSize.SMALL.getSize()));
    
    fm = g2.getFontMetrics();
    
    xString = (0 + (icon.getIconWidth() - fm.stringWidth(text)) / 2.0);
    yString = (0 + ((icon.getIconHeight() - fm.getHeight()) / 2.0) + fm.getAscent());
    
    g2.drawString(text,
                  (float) xString,
                  (float) yString);
                  */

    return canvas;
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
