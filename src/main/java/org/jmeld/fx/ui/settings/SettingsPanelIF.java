package org.jmeld.fx.ui.settings;

import javafx.scene.Node;

public interface SettingsPanelIF
{
  public String getText();

  public Node getIcon();

  default public Node getContent()
  {
    return (Node) this;
  }
}
