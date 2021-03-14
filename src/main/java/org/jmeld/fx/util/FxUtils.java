package org.jmeld.fx.util;

import java.util.stream.Stream;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

  static public Background getBackgroundColor(Color color)
  {
    return new Background(new BackgroundFill(color,
                                             null,
                                             null));
  }
  
  static public Node createImageNode(Image... images)
  {
	if(images == null)
	{
	  return new ImageView();
	}
	
	if(images.length == 1)
	{
	  return new ImageView(images[0]);
	}

    return new HBox(Stream.of(images).map(image -> new ImageView(image)).toArray(ImageView[]::new));
  }
}
