/*
 * SettingsPanel.java
 *
 */
package org.jmeld.ui.settings;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.JMeldPanel;
import org.jmeld.ui.StatusBar;
import org.jmeld.ui.util.Icons;
import org.jmeld.ui.util.ImageUtil;
import org.jmeld.util.conf.ConfigurationListenerIF;
import org.jmeld.util.conf.ConfigurationManager;
import org.jmeld.util.prefs.FileChooserPreference;

/**
 *
 * @author kees
 */
public class SettingsPanel
    extends SettingsPanelForm
    implements ConfigurationListenerIF
{
  private DefaultListModel<Settings> listModel;
  private JMeldPanel mainPanel;

  public SettingsPanel(JMeldPanel mainPanel)
  {
    this.mainPanel = mainPanel;

    init();
    initConfiguration();

    getConfiguration().addConfigurationListener(this);
  }

  private void init()
  {
    settingsPanel.setLayout(new CardLayout());
    for (Settings setting : Settings.values())
    {
      settingsPanel.add(setting.getPanel(),
                        setting.getName());
    }

    initButton(saveButton,
               Icons.SAVE,
               "Save settings");
    saveButton.addActionListener(this::doSaveAction);

    initButton(saveAsButton,
               Icons.SAVE_AS,
               "Save settings to a different file");
    saveAsButton.addActionListener(this::doSaveAsAction);

    initButton(reloadButton,
               Icons.RELOAD,
               "Reload settings from a different file");
    reloadButton.addActionListener(this::doReloadAction);

    fileLabel.setText("");

    listModel = new DefaultListModel<>();
    for (Settings setting : Settings.values())
    {
      listModel.addElement(setting);
    }
    settingItems.setModel(listModel);
    settingItems.setCellRenderer(new SettingCellRenderer());
    settingItems.setSelectedIndex(0);
    settingItems.addListSelectionListener(this::doSettingItemsAction);
  }

  private void initButton(JButton button,
      Icons icon,
      String toolTipText)
  {
    button.setText("");
    button.setToolTipText(toolTipText);
    button.setBorder(BorderFactory.createEmptyBorder(2,
                                                     2,
                                                     2,
                                                     2));
    button.setContentAreaFilled(false);
    button.setIcon(icon.getSmallIcon());
    button.setDisabledIcon(ImageUtil.createTransparentIcon(icon.getSmallIcon()));
    button.setPressedIcon(ImageUtil.createDarkerIcon(icon.getSmallIcon()));
    button.setFocusable(false);
  }

  public void doSaveAction(ActionEvent ae)
  {
    getConfiguration().save();
    StatusBar.getInstance().setText("Configuration saved");
  }

  public void doSaveAsAction(ActionEvent ae)
  {
    JFileChooser chooser;
    int result;
    File file;
    FileChooserPreference pref;
    Window ancestor;

    chooser = new JFileChooser();
    chooser.setApproveButtonText("Save");
    chooser.setDialogTitle("Save settings");
    pref = new FileChooserPreference("SettingsSave",
                                     chooser);

    ancestor = SwingUtilities.getWindowAncestor((Component) ae.getSource());
    result = chooser.showOpenDialog(ancestor);
    if (result == JFileChooser.APPROVE_OPTION)
    {
      pref.save();
      file = chooser.getSelectedFile();
      getConfiguration().setConfigurationFile(file);
      getConfiguration().save();
      StatusBar.getInstance().setText("Configuration saved to " + file);
    }
  }

  public void doReloadAction(ActionEvent e)
  {
    JFileChooser chooser;
    int result;
    File file;
    FileChooserPreference pref;
    Window ancestor;

    chooser = new JFileChooser();
    chooser.setApproveButtonText("Reload");
    chooser.setDialogTitle("Reload settings");
    pref = new FileChooserPreference("SettingsSave",
                                     chooser);

    ancestor = SwingUtilities.getWindowAncestor((Component) e.getSource());
    result = chooser.showOpenDialog(ancestor);
    if (result == JFileChooser.APPROVE_OPTION)
    {
      pref.save();
      file = chooser.getSelectedFile();
      if (!ConfigurationManager.getInstance().reload(file,
                                                     getConfiguration().getClass()))
      {
        StatusBar.getInstance().setAlarm("Failed to reload from " + file);
      }
    }
  }

  public void doSettingItemsAction(ListSelectionEvent event)
  {
    CardLayout layout;
    Settings settings;

    settings = (Settings) settingItems.getSelectedValue();
    layout = (CardLayout) settingsPanel.getLayout();
    layout.show(settingsPanel,
                settings.getName());
  }

  @Override
  public void configurationChanged()
  {
    initConfiguration();
  }

  private void initConfiguration()
  {
    JMeldSettings c;

    c = getConfiguration();

    fileLabel.setText(c.getConfigurationFileName());
    saveButton.setEnabled(c.isChanged());
  }

  @Override
  public boolean checkSave()
  {
    SaveSettingsDialog dialog;

    if (getConfiguration().isChanged())
    {
      dialog = new SaveSettingsDialog(mainPanel);
      dialog.show();
      if (dialog.isOK())
      {
        dialog.doSave();
      }
    }

    return true;
  }

  private JMeldSettings getConfiguration()
  {
    return JMeldSettings.getInstance();
  }
}
