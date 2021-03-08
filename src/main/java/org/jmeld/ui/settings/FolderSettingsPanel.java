/*
 * FilterPreferencePanel.java
 *
 * Created on August 22, 2007, 20:10
 */
package org.jmeld.ui.settings;

import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import org.jmeld.settings.FolderSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.util.Icons;
import org.jmeld.util.conf.ConfigurationListenerIF;

/**
 *
 * @author kees
 */
public class FolderSettingsPanel
    extends FolderSettingsForm
    implements ConfigurationListenerIF
{
  public FolderSettingsPanel()
  {
    init();

    JMeldSettings.getInstance().addConfigurationListener(this);
  }

  private void init()
  {
    FolderSettings settings;

    settings = getSettings();

    hierarchyComboBox.setModel(new DefaultComboBoxModel(FolderSettings.FolderView.values()));
    hierarchyComboBox.setSelectedItem(getSettings().getView());
    hierarchyComboBox.setFocusable(false);
    hierarchyComboBox.addActionListener(getHierarchyAction());

    onlyLeftButton.setText(null);
    onlyLeftButton.setIcon(Icons.ONLY_LEFT);
    onlyLeftButton.setFocusable(false);
    onlyLeftButton.setSelected(settings.getOnlyLeft());
    onlyLeftButton.addActionListener(getOnlyLeftAction());

    leftRightChangedButton.setText(null);
    leftRightChangedButton.setIcon(Icons.LEFT_RIGHT_CHANGED);
    leftRightChangedButton.setFocusable(false);
    leftRightChangedButton.setSelected(settings.getLeftRightChanged());
    leftRightChangedButton.addActionListener(getLeftRightChangedAction());

    onlyRightButton.setText(null);
    onlyRightButton.setIcon(Icons.ONLY_RIGHT);
    onlyRightButton.setFocusable(false);
    onlyRightButton.setSelected(settings.getOnlyRight());
    onlyRightButton.addActionListener(getOnlyRightAction());

    leftRightUnChangedButton.setText(null);
    leftRightUnChangedButton.setIcon(Icons.LEFT_RIGHT_UNCHANGED);
    leftRightUnChangedButton.setFocusable(false);
    leftRightUnChangedButton.setSelected(settings.getLeftRightUnChanged());
    leftRightUnChangedButton.addActionListener(getLeftRightUnChangedAction());
  }

  private ActionListener getHierarchyAction()
  {
    return new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        getSettings().setView((FolderSettings.FolderView) hierarchyComboBox.getSelectedItem());
      }
    };
  }

  private ActionListener getOnlyLeftAction()
  {
    return new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        getSettings().setOnlyLeft(onlyLeftButton.isSelected());
      }
    };
  }

  private ActionListener getLeftRightChangedAction()
  {
    return new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        getSettings().setLeftRightChanged(leftRightChangedButton.isSelected());
      }
    };
  }

  private ActionListener getOnlyRightAction()
  {
    return new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        getSettings().setOnlyRight(onlyRightButton.isSelected());
      }
    };
  }

  private ActionListener getLeftRightUnChangedAction()
  {
    return new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        getSettings().setLeftRightUnChanged(leftRightUnChangedButton.isSelected());
      }
    };
  }

  public void configurationChanged()
  {
    //initConfiguration();
  }

  private FolderSettings getSettings()
  {
    return JMeldSettings.getInstance().getFolder();
  }
}
