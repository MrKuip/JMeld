package org.jmeld.fx.ui.settings;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public interface SettingsPanelIF
{
  public String getText();

  public Node getIcon();

  default public Pane getContent()
  {
    return (Pane) this;
  }
}
