package org.jmeld.fx.ui.settings;

import javafx.scene.Node;
import javafx.scene.image.Image;

public interface SettingsPanelIF
{
  public String getText();

  public Image getImage();

  default public Node getContent()
  {
    return (Node) this;
  }
}
