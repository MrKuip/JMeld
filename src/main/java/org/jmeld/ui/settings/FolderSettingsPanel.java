/*
 * FilterPreferencePanel.java
 *
 * Created on August 22, 2007, 20:10
 */
package org.jmeld.ui.settings;

import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import org.jmeld.settings.FolderSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.util.Icons;
import org.jmeld.util.conf.ConfigurationListenerIF;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author kees
 */
public class FolderSettingsPanel
  extends JPanel
    implements ConfigurationListenerIF
{
  private JComboBox<FolderSettings.FolderView> hierarchyComboBox;
  private JToggleButton onlyLeftButton;
  private JToggleButton leftRightChangedButton;
  private JToggleButton onlyRightButton;
  private JToggleButton leftRightUnChangedButton;

  public FolderSettingsPanel()
  {
    init();

    JMeldSettings.getInstance().addConfigurationListener(this);
  }

  private void init()
  {
    FolderSettings settings;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    setLayout(new MigLayout("", "[pref][pref][grow,fill]"));

    settings = getSettings();

    hierarchyComboBox = new JComboBox<>();
    onlyLeftButton = new JToggleButton();
    leftRightChangedButton = new JToggleButton();
    onlyRightButton = new JToggleButton();
    leftRightUnChangedButton = new JToggleButton();

    hierarchyComboBox.setModel(new DefaultComboBoxModel<>(FolderSettings.FolderView.values()));
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

    add(SettingsPanel.header1("Folder settings"), new CC().dockNorth().wrap().span(3).gapLeft("10"));
    add(SettingsPanel.header2(new JLabel("File filter")), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    add(onlyLeftButton, new CC().gapLeft(gap1).split(2));
    add(new JLabel("Show files that only exist on the left site"), new CC().wrap());
    add(leftRightChangedButton, new CC().gapLeft(gap1).split(2));
    add(new JLabel("Show files that are different"), new CC().wrap());
    add(onlyRightButton, new CC().gapLeft(gap1).split(2));
    add(new JLabel("Show files that only exist on the right site"), new CC().wrap());
    add(leftRightUnChangedButton, new CC().gapLeft(gap1).split(2));
    add(new JLabel("Show files that are equal"), new CC().wrap());
    add(SettingsPanel.header2(new JLabel("Miscellaneous")), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    add(new JLabel("Default hierarchy"), new CC().gapLeft(gap1).split(2));
    add(hierarchyComboBox, new CC().wrap());

  }

  private ActionListener getHierarchyAction()
  {
    return new java.awt.event.ActionListener()
    {
      @Override
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
      @Override
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
      @Override
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
      @Override
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
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        getSettings().setLeftRightUnChanged(leftRightUnChangedButton.isSelected());
      }
    };
  }

  @Override
  public void configurationChanged()
  {
    //initConfiguration();
  }

  private FolderSettings getSettings()
  {
    return JMeldSettings.getInstance().getFolder();
  }
}
