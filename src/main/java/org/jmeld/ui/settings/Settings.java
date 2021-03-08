/*
 * SettingsPanel2.java
 *
 * Created on January 19, 2007, 8:02 PM
 */
package org.jmeld.ui.settings;

import javax.swing.JPanel;
import org.jmeld.ui.util.Icons;

public enum Settings
{
  Editor("Editor", Icons.EDIT, new EditorSettingsPanel()),
  Filter("Filter", Icons.FILTER, new FilterSettingsPanel()),
  Folder("Folder", Icons.FOLDER, new FolderSettingsPanel());

  // Instance variables:
  private String name;
  private Icons icon;
  private JPanel panel;

  Settings(String name,
      Icons iconName,
      JPanel panel)
  {
    this.name = name;
    this.icon = iconName;
    this.panel = panel;
  }

  String getName()
  {
    return name;
  }

  Icons getIcon()
  {
    return icon;
  }

  JPanel getPanel()
  {
    return panel;
  }

  public String toString()
  {
    return name;
  }
}
