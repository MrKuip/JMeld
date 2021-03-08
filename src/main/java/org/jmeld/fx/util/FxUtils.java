package org.jmeld.fx.util;

import java.awt.Graphics2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.swing.Icon;
import org.jfree.fx.FXGraphics2D;

public class FxUtils
{
  static public Node getWidthSpacer(double width)
  {
    Region region;

    region = new Region();
    region.setPrefWidth(width);

    return region;
  }

  static public Node getSpacer()
  {
    Node spacer;

    spacer = new Region();
    HBox.setHgrow(spacer, Priority.SOMETIMES);

    return spacer;
  }

  static public Font boldFont(Font font)
  {
    return Font.font(font.getFamily(), FontWeight.BOLD, font.getSize());
  }

  static public Node getIcon(Icon icon)
  {
    Canvas canvas;
    Graphics2D g2;

    canvas = new Canvas(icon.getIconWidth(),
                        icon.getIconHeight());
    g2 = new FXGraphics2D(canvas.getGraphicsContext2D());

    icon.paintIcon(null, g2, 0, 0);

    return canvas;
  }

  static public Background getBackgroundColor(Color color)
  {
    return new Background(new BackgroundFill(color,
                                             null,
                                             null));
  }
}
