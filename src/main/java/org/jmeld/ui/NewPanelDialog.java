/*
   JMeld is a visual diff and merge tool.
   Copyright (C) 2007  Kees Kuip
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA  02110-1301  USA
 */
package org.jmeld.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.settings.util.Filter;
import org.jmeld.util.ObjectUtil;
import org.jmeld.util.prefs.DirectoryChooserPreference;
import org.jmeld.util.prefs.FileChooserPreference;
import org.jmeld.util.prefs.JComboBoxPreference;
import org.jmeld.util.prefs.JComboBoxSelectionPreference;
import org.jmeld.util.prefs.TabbedPanePreference;
import net.miginfocom.swing.MigLayout;

public class NewPanelDialog
{
  // Class variables:
  // File comparison:
  public enum Function
  {
    FILE_COMPARISON,
    DIRECTORY_COMPARISON,
    VERSION_CONTROL;
  }

  private static String RIGHT_FILENAME = "RIGHT_FILENAME";
  private static String LEFT_FILENAME = "LEFT_FILENAME";
  private static String previousDirectoryName;

  // Directory comparison:
  private static String RIGHT_DIRECTORY = "RIGHT_DIRECTORY";
  private static String LEFT_DIRECTORY = "LEFT_DIRECTORY";

  // Version control :
  private static String VERSION_CONTROL_DIRECTORY = "VERSION_CONTROL_DIRECTORY";

  // Instance variables:
  private JMeldPanel meldPanel;
  private JTabbedPane tabbedPane;
  private Function function;
  private String leftFileName;
  private String rightFileName;
  private JComboBox<String> leftFileComboBox;
  private JComboBox<String> rightFileComboBox;
  private String leftDirectoryName;
  private String rightDirectoryName;
  private JComboBox<String> leftDirectoryComboBox;
  private JComboBox<String> rightDirectoryComboBox;
  private String versionControlDirectoryName;
  private JComboBox<String> versionControlDirectoryComboBox;
  private JComboBox<Object> filterComboBox;
  private JDialog dialog;

  public NewPanelDialog(JMeldPanel meldPanel)
  {
    this.meldPanel = meldPanel;
  }

  public void show()
  {
    JOptionPane pane;

    pane = new JOptionPane(getChooseFilePanel());
    pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);

    dialog = pane.createDialog(meldPanel, "Choose files");
    dialog.setResizable(true);
    try
    {
      dialog.setVisible(true);

      if (ObjectUtil.equals(pane.getValue(), JOptionPane.OK_OPTION))
      {
        switch (tabbedPane.getSelectedIndex())
        {

          case 0:
            setFunction(Function.FILE_COMPARISON);
            break;

          case 1:
            setFunction(Function.DIRECTORY_COMPARISON);
            break;

          case 2:
            setFunction(Function.VERSION_CONTROL);
            break;
        }
      }
    }
    finally
    {
      // Always dispose a dialog -> otherwise there is a memory leak
      dialog.dispose();
    }
  }

  private void setFunction(Function function)
  {
    this.function = function;
  }

  public Function getFunction()
  {
    return function;
  }

  public String getLeftFileName()
  {
    return leftFileName;
  }

  public String getRightFileName()
  {
    return rightFileName;
  }

  public String getLeftDirectoryName()
  {
    return leftDirectoryName;
  }

  public String getRightDirectoryName()
  {
    return rightDirectoryName;
  }

  public String getVersionControlDirectoryName()
  {
    return versionControlDirectoryName;
  }

  public Filter getFilter()
  {
    if (filterComboBox.getSelectedItem() instanceof Filter)
    {
      return (Filter) filterComboBox.getSelectedItem();
    }

    return null;
  }

  private JComponent getChooseFilePanel()
  {
    JPanel panel;

    tabbedPane = new JTabbedPane();
    tabbedPane.add("File Comparison", getFileComparisonPanel());
    tabbedPane.add("Directory Comparison", getDirectoryComparisonPanel());
    tabbedPane.add("Version control", getVersionControlPanel());

    new TabbedPanePreference("NewPanelTabbedPane", tabbedPane);

    panel = new JPanel(new BorderLayout());
    panel.add(tabbedPane, BorderLayout.CENTER);

    return panel;
  }

  private JComponent getFileComparisonPanel()
  {
    JPanel panel;
    JButton button;

    panel = new JPanel(new MigLayout("", "[pref][grow][pref]", "[][]"));

    button = new JButton("Browse...");
    button.setActionCommand(LEFT_FILENAME);
    button.addActionListener(getFileBrowseAction());
    leftFileComboBox = new JComboBox<>();
    leftFileComboBox.setEditable(false);
    leftFileComboBox.addActionListener(getFileSelectAction());
    new JComboBoxPreference("LeftFile", leftFileComboBox);

    panel.add(new JLabel("Left"), "align right");
    panel.add(leftFileComboBox, "grow");
    panel.add(button, "wrap");

    button = new JButton("Browse...");
    button.setActionCommand(RIGHT_FILENAME);
    button.addActionListener(getFileBrowseAction());
    rightFileComboBox = new JComboBox<>();
    rightFileComboBox.setEditable(false);
    rightFileComboBox.addActionListener(getFileSelectAction());
    new JComboBoxPreference("RightFile", rightFileComboBox);

    panel.add(new JLabel("Right"), "align right");
    panel.add(rightFileComboBox, "grow");
    panel.add(button, "wrap");

    return panel;
  }

  public ActionListener getFileBrowseAction()
  {
    return (e) -> {
      FileChooserPreference pref;
      JFileChooser chooser;
      int result;
      String fileName;
      JComboBox<String> comboBox;
      Window ancestor;

      // Don't allow accidentaly creation or rename of files.
      UIManager.put("FileChooser.readOnly", Boolean.TRUE);
      chooser = new JFileChooser();
      // Reset the readOnly property as it is systemwide.
      UIManager.put("FileChooser.readOnly", Boolean.FALSE);
      chooser.setApproveButtonText("Choose");
      chooser.setDialogTitle("Choose file");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      pref = new FileChooserPreference("Browse-" + e.getActionCommand(), chooser);

      ancestor = SwingUtilities.getWindowAncestor((Component) e.getSource());
      result = chooser.showOpenDialog(ancestor);

      if (result == JFileChooser.APPROVE_OPTION)
      {
        pref.save();

        try
        {
          fileName = chooser.getSelectedFile().getCanonicalPath();

          comboBox = null;
          if (e.getActionCommand().equals(LEFT_FILENAME))
          {
            comboBox = leftFileComboBox;
          }
          else if (e.getActionCommand().equals(RIGHT_FILENAME))
          {
            comboBox = rightFileComboBox;
          }

          if (comboBox != null)
          {
            comboBox.insertItemAt(fileName, 0);
            comboBox.setSelectedIndex(0);
            dialog.pack();
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    };
  }

  public ActionListener getFileSelectAction()
  {
    return (e) -> {
      Object source;

      source = e.getSource();
      if (source == leftFileComboBox)
      {
        leftFileName = (String) leftFileComboBox.getSelectedItem();
      }
      else if (source == rightFileComboBox)
      {
        rightFileName = (String) rightFileComboBox.getSelectedItem();
      }
    };
  }

  private JComponent getDirectoryComparisonPanel()
  {
    JPanel panel;
    JButton button;

    panel = new JPanel(new MigLayout("", "[pref][grow][pref]", "[][]"));

    button = new JButton("Browse...");
    leftDirectoryComboBox = new JComboBox<>();
    leftDirectoryComboBox.setEditable(false);
    leftDirectoryComboBox.addActionListener(getDirectorySelectAction());
    new JComboBoxPreference("LeftDirectory", leftDirectoryComboBox);

    button.setActionCommand(LEFT_DIRECTORY);
    button.addActionListener(getDirectoryBrowseAction());
    panel.add(new JLabel("Left"), "align right");
    panel.add(leftDirectoryComboBox, "grow");
    panel.add(button, "wrap");

    button = new JButton("Browse...");
    button.setActionCommand(RIGHT_DIRECTORY);
    button.addActionListener(getDirectoryBrowseAction());
    rightDirectoryComboBox = new JComboBox<>();
    rightDirectoryComboBox.setEditable(false);
    rightDirectoryComboBox.addActionListener(getDirectorySelectAction());
    new JComboBoxPreference("RightDirectory", rightDirectoryComboBox);
    panel.add(new JLabel("Right"), "align right");
    panel.add(rightDirectoryComboBox, "grow");
    panel.add(button, "wrap");

    filterComboBox = new JComboBox<>(getFilters());
    panel.add(new JLabel("Filter"), "align right");
    panel.add(filterComboBox, "grow, wrap");
    new JComboBoxSelectionPreference("Filter", filterComboBox);

    return panel;
  }

  public ActionListener getDirectoryBrowseAction()
  {
    return (e) -> {
      DirectoryChooserPreference pref;
      JFileChooser chooser;
      int result;
      String fileName;
      JComboBox<String> comboBox;
      Window ancestor;

      // Don't allow accidentaly creation or rename of files.
      UIManager.put("FileChooser.readOnly", Boolean.TRUE);
      chooser = new JFileChooser();
      // Reset the readOnly property as it is systemwide.
      UIManager.put("FileChooser.readOnly", Boolean.FALSE);
      chooser.setApproveButtonText("Choose");
      chooser.setDialogTitle("Choose directory");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      pref = new DirectoryChooserPreference("Browse-" + e.getActionCommand(), chooser, previousDirectoryName);

      ancestor = SwingUtilities.getWindowAncestor((Component) e.getSource());
      result = chooser.showOpenDialog(ancestor);

      if (result == JFileChooser.APPROVE_OPTION)
      {
        pref.save();

        try
        {
          fileName = chooser.getSelectedFile().getCanonicalPath();
          previousDirectoryName = fileName;

          comboBox = null;
          if (e.getActionCommand().equals(LEFT_DIRECTORY))
          {
            comboBox = leftDirectoryComboBox;
          }
          else if (e.getActionCommand().equals(RIGHT_DIRECTORY))
          {
            comboBox = rightDirectoryComboBox;
          }

          if (comboBox != null)
          {
            comboBox.insertItemAt(fileName, 0);
            comboBox.setSelectedIndex(0);
            dialog.pack();
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    };
  }

  public ActionListener getDirectorySelectAction()
  {
    return (e) -> {
      Object source;

      source = e.getSource();
      if (source == leftDirectoryComboBox)
      {
        leftDirectoryName = (String) leftDirectoryComboBox.getSelectedItem();
      }
      else if (source == rightDirectoryComboBox)
      {
        rightDirectoryName = (String) rightDirectoryComboBox.getSelectedItem();
      }
    };
  }

  @SuppressWarnings("unused")
  private JComponent getVersionControlPanel()
  {
    JPanel panel;
    JButton button;

    panel = new JPanel(new MigLayout("", "[pref][grow][pref]", "[][]"));

    button = new JButton("Browse...");
    versionControlDirectoryComboBox = new JComboBox<>();
    versionControlDirectoryComboBox.setEditable(false);
    versionControlDirectoryComboBox.addActionListener(getVersionControlDirectorySelectAction());
    new JComboBoxPreference("VersionControlDirectory", versionControlDirectoryComboBox);

    button.setActionCommand(VERSION_CONTROL_DIRECTORY);
    panel.add(new JLabel("Directory"), "align right");
    panel.add(versionControlDirectoryComboBox, "grow");
    panel.add(button, "wrap");

    return panel;
  }

  public ActionListener getVersionControlDirectoryBrowseAction()
  {
    return (e) -> {
      DirectoryChooserPreference pref;
      JFileChooser chooser;
      int result;
      String fileName;
      JComboBox<String> comboBox;
      Window ancestor;

      // Don't allow accidentaly creation or rename of files.
      UIManager.put("FileChooser.readOnly", Boolean.TRUE);
      chooser = new JFileChooser();
      // Reset the readOnly property as it is systemwide.
      UIManager.put("FileChooser.readOnly", Boolean.FALSE);
      chooser.setApproveButtonText("Choose");
      chooser.setDialogTitle("Choose directory");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      pref = new DirectoryChooserPreference("VersionControlBrowse", chooser, previousDirectoryName);

      ancestor = SwingUtilities.getWindowAncestor((Component) e.getSource());
      result = chooser.showOpenDialog(ancestor);

      if (result == JFileChooser.APPROVE_OPTION)
      {
        pref.save();

        try
        {
          fileName = chooser.getSelectedFile().getCanonicalPath();
          previousDirectoryName = fileName;

          comboBox = versionControlDirectoryComboBox;
          comboBox.insertItemAt(fileName, 0);
          comboBox.setSelectedIndex(0);
          dialog.pack();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    };
  }

  public ActionListener getVersionControlDirectorySelectAction()
  {
    return (e) -> {
      versionControlDirectoryName = (String) versionControlDirectoryComboBox.getSelectedItem();
    };
  }

  private Object[] getFilters()
  {
    List<Object> filters;

    filters = new ArrayList<>();
    filters.add("No filter");
    for (Filter f : JMeldSettings.getInstance().getFilter().getFilters())
    {
      filters.add(f);
    }

    return filters.toArray();
  }
}
