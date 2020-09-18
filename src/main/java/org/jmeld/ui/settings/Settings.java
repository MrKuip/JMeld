/*
 * SettingsPanel2.java
 *
 * Created on January 19, 2007, 8:02 PM
 */
package org.jmeld.ui.settings;

import javax.swing.JPanel;

import org.jmeld.ui.util.Images;

public enum Settings
{
 Editor("Editor", Images.EDIT, new EditorSettingsPanel()),
 Filter("Filter", Images.FILTER, new FilterSettingsPanel()),
 Folder("Folder", Images.FOLDER, new FolderSettingsPanel());

  // Instance variables:
  private String name;
  private Images icon;
  private JPanel panel;

  Settings(String name, Images iconName, JPanel panel)
  {
    this.name = name;
    this.icon = iconName;
    this.panel = panel;
  }

  String getName()
  {
    return name;
  }

  Images getIcon()
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
