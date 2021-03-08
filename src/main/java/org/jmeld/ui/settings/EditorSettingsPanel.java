/*
 * EditorPreferencePanel.java
 *
 * Created on January 10, 2007, 6:31 PM
 */
package org.jmeld.ui.settings;

import com.l2fprod.common.swing.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jmeld.JMeld;
import org.jmeld.settings.EditorSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.util.EmptyIcon;
import org.jmeld.ui.util.FontUtil;
import org.jmeld.ui.util.LookAndFeelManager;
import org.jmeld.util.CharsetDetector;
import org.jmeld.util.Ignore;
import org.jmeld.util.conf.ConfigurationListenerIF;

/**
 *
 * @author kees
 */
public class EditorSettingsPanel
    extends EditorSettingsForm
    implements ConfigurationListenerIF
{
  private static JDialog colorDialog;
  private static JColorChooser colorChooser;
  private boolean originalAntialias;

  public EditorSettingsPanel()
  {
    originalAntialias = getEditorSettings().isAntialiasEnabled();

    initConfiguration();
    init();

    JMeldSettings.getInstance().addConfigurationListener(this);
  }

  private void init()
  {
    // ignore:
    ignoreWhitespaceAtBeginCheckBox.addActionListener(getIgnoreWhitespaceAtBeginAction());
    ignoreWhitespaceInBetweenCheckBox.addActionListener(getIgnoreWhitespaceInBetweenAction());
    ignoreWhitespaceAtEndCheckBox.addActionListener(getIgnoreWhitespaceAtEndAction());
    ignoreEOLCheckBox.addActionListener(getIgnoreEOLAction());
    ignoreBlankLinesCheckBox.addActionListener(getIgnoreBlankLinesAction());
    ignoreCaseCheckBox.addActionListener(getIgnoreCaseAction());

    // Miscellaneous:
    leftsideReadonlyCheckBox.addActionListener(getLeftsideReadonlyAction());
    rightsideReadonlyCheckBox.addActionListener(getRightsideReadonlyAction());
    antialiasCheckBox.addActionListener(getAntialiasAction());
    tabSizeSpinner.addChangeListener(getTabSizeChangeListener());
    showLineNumbersCheckBox.addActionListener(getShowLineNumbersAction());
    lookAndFeelComboBox.setModel(getLookAndFeelModel());
    lookAndFeelComboBox.setSelectedItem(LookAndFeelManager.getInstance().getInstalledLookAndFeelName());
    lookAndFeelComboBox.addActionListener(getLookAndFeelAction());

    // Colors:
    colorAddedButton.addActionListener(getColorAddedAction());
    colorDeletedButton.addActionListener(getColorDeletedAction());
    colorChangedButton.addActionListener(getColorChangedAction());
    restoreOriginalColorsButton.addActionListener(getRestoreOriginalColorsAction());

    // Font:
    defaultFontRadioButton.addActionListener(getDefaultFontAction());
    customFontRadioButton.addActionListener(getCustomFontAction());
    fontChooserButton.addActionListener(getFontChooserAction());

    // File encoding:
    defaultEncodingRadioButton.setText(defaultEncodingRadioButton.getText() + " (" + CharsetDetector.getInstance()
        .getDefaultCharset() + ")");

    defaultEncodingRadioButton.addActionListener(getDefaultEncodingAction());
    detectEncodingRadioButton.addActionListener(getDetectEncodingAction());
    specificEncodingRadioButton.addActionListener(getSpecificEncodingAction());
    specificEncodingComboBox.setModel(new DefaultComboBoxModel(CharsetDetector.getInstance().getCharsetNameList()
        .toArray()));
    specificEncodingComboBox.setSelectedItem(getEditorSettings().getSpecificFileEncodingName());
    specificEncodingComboBox.addActionListener(getSpecificEncodingNameAction());

    // Toolbar appearance:
    toolbarButtonIconComboBox.setModel(getToolbarButtonIconModel());
    toolbarButtonIconComboBox.setSelectedItem(getEditorSettings().getToolbarButtonIcon());
    toolbarButtonIconComboBox.addActionListener(getToolbarButtonIconAction());
    toolbarButtonTextEnabledCheckBox.addActionListener(getToolbarButtonTextEnabledAction());
  }

  private ChangeListener getTabSizeChangeListener()
  {
    return new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent evt)
      {
        getEditorSettings().setTabSize((Integer) tabSizeSpinner.getValue());
      }
    };
  }

  private ActionListener getColorAddedAction()
  {
    return (e) -> {
      Color color;

      color = chooseColor(getEditorSettings().getAddedColor());
      if (color != null)
      {
        getEditorSettings().setAddedColor(color);
      }
    };
  }

  private ActionListener getColorDeletedAction()
  {
    return (e) -> {
      Color color;

      color = chooseColor(getEditorSettings().getDeletedColor());
      if (color != null)
      {
        getEditorSettings().setDeletedColor(color);
      }
    };
  }

  private ActionListener getColorChangedAction()
  {
    return (e) -> {
      Color color;

      color = chooseColor(getEditorSettings().getChangedColor());
      if (color != null)
      {
        getEditorSettings().setChangedColor(color);
      }
    };
  }

  private ActionListener getShowLineNumbersAction()
  {
    return (e) -> {
      getEditorSettings().setShowLineNumbers(showLineNumbersCheckBox.isSelected());
    };
  }

  private ActionListener getIgnoreWhitespaceAtBeginAction()
  {
    return (e) -> {
      getEditorSettings().setIgnoreWhitespaceAtBegin(ignoreWhitespaceAtBeginCheckBox.isSelected());
    };
  }

  private ActionListener getIgnoreWhitespaceInBetweenAction()
  {
    return (e) -> {
      getEditorSettings().setIgnoreWhitespaceInBetween(ignoreWhitespaceInBetweenCheckBox.isSelected());
    };
  }

  private ActionListener getIgnoreWhitespaceAtEndAction()
  {
    return (e) -> {
      getEditorSettings().setIgnoreWhitespaceAtEnd(ignoreWhitespaceAtEndCheckBox.isSelected());
    };
  }

  private ActionListener getIgnoreEOLAction()
  {
    return (e) -> {
      getEditorSettings().setIgnoreEOL(ignoreEOLCheckBox.isSelected());
    };
  }

  private ActionListener getIgnoreBlankLinesAction()
  {
    return (e) -> {
      getEditorSettings().setIgnoreBlankLines(ignoreBlankLinesCheckBox.isSelected());
    };
  }

  private ActionListener getIgnoreCaseAction()
  {
    return (e) -> {
      getEditorSettings().setIgnoreCase(ignoreCaseCheckBox.isSelected());
    };
  }

  private ActionListener getLeftsideReadonlyAction()
  {
    return (e) -> {
      getEditorSettings().setLeftsideReadonly(leftsideReadonlyCheckBox.isSelected());
    };
  }

  private ActionListener getRightsideReadonlyAction()
  {
    return (e) -> {
      getEditorSettings().setRightsideReadonly(rightsideReadonlyCheckBox.isSelected());
    };
  }

  private ActionListener getAntialiasAction()
  {
    return (e) -> {
      getEditorSettings().enableAntialias(antialiasCheckBox.isSelected());
    };
  }

  private ActionListener getRestoreOriginalColorsAction()
  {
    return (e) -> {
      getEditorSettings().restoreColors();
    };
  }

  private ActionListener getDefaultEncodingAction()
  {
    return (e) -> {
      getEditorSettings().setDefaultFileEncodingEnabled(true);
      getEditorSettings().setDetectFileEncodingEnabled(false);
      getEditorSettings().setSpecificFileEncodingEnabled(false);
    };
  }

  private ActionListener getDetectEncodingAction()
  {
    return (e) -> {
      getEditorSettings().setDefaultFileEncodingEnabled(false);
      getEditorSettings().setDetectFileEncodingEnabled(true);
      getEditorSettings().setSpecificFileEncodingEnabled(false);
    };
  }

  private ActionListener getSpecificEncodingAction()
  {
    return (e) -> {
      getEditorSettings().setDefaultFileEncodingEnabled(false);
      getEditorSettings().setDetectFileEncodingEnabled(false);
      getEditorSettings().setSpecificFileEncodingEnabled(true);
    };
  }

  private ActionListener getSpecificEncodingNameAction()
  {
    return (e) -> {
      getEditorSettings().setSpecificFileEncodingName((String) specificEncodingComboBox.getSelectedItem());
    };
  }

  private ActionListener getLookAndFeelAction()
  {
    return (e) -> {
      getEditorSettings().setLookAndFeelName((String) lookAndFeelComboBox.getSelectedItem());
      LookAndFeelManager.getInstance().install();
    };
  }

  private ActionListener getToolbarButtonIconAction()
  {
    return (e) -> {
      getEditorSettings().setToolbarButtonIcon(
          (EditorSettings.ToolbarButtonIcon) toolbarButtonIconComboBox.getSelectedItem());
      JMeld.getJMeldPanel().addToolBar();
    };
  }

  private ActionListener getToolbarButtonTextEnabledAction()
  {
    return (e) -> {
      getEditorSettings().setToolbarButtonTextEnabled(toolbarButtonTextEnabledCheckBox.isSelected());
      JMeld.getJMeldPanel().addToolBar();
    };
  }

  private ActionListener getDefaultFontAction()
  {
    return (e) -> {
      getEditorSettings().enableCustomFont(!defaultFontRadioButton.isSelected());
    };
  }

  private ActionListener getCustomFontAction()
  {
    return (e) -> {
      getEditorSettings().enableCustomFont(customFontRadioButton.isSelected());
    };
  }

  private ActionListener getFontChooserAction()
  {
    return (e) -> {
      Font font;

      font = chooseFont(getEditorFont());
      if (font != null)
      {
        getEditorSettings().setFont(font);
      }
    };
  }

  private Color chooseColor(Color initialColor)
  {
    // Do not instantiate ColorChooser multiple times because it contains
    //   a memory leak.
    if (colorDialog == null)
    {
      colorChooser = new JColorChooser(initialColor);
      colorDialog = JColorChooser.createDialog(null, "Choose color", true, colorChooser, null, null);
    }

    colorChooser.setColor(initialColor);
    colorDialog.setVisible(true);

    return colorChooser.getColor();
  }

  private Font chooseFont(Font initialFont)
  {
    JFontChooser fontChooser;

    fontChooser = new JFontChooser();
    fontChooser.setSelectedFont(initialFont);

    return fontChooser.showFontDialog(this, "");
  }

  private ComboBoxModel getLookAndFeelModel()
  {
    return new DefaultComboBoxModel(LookAndFeelManager.getInstance().getInstalledLookAndFeels().toArray());
  }

  private ComboBoxModel getToolbarButtonIconModel()
  {
    return new DefaultComboBoxModel(getEditorSettings().getToolbarButtonIcons());
  }

  @Override
  public void configurationChanged()
  {
    initConfiguration();
  }

  private void initConfiguration()
  {
    EditorSettings settings;
    Font font;
    Ignore ignore;

    settings = getEditorSettings();
    ignore = settings.getIgnore();
    colorAddedButton.setIcon(new EmptyIcon(settings.getAddedColor(),
                                           20,
                                           20));
    colorAddedButton.setText("");
    colorDeletedButton.setIcon(new EmptyIcon(settings.getDeletedColor(),
                                             20,
                                             20));
    colorDeletedButton.setText("");
    colorChangedButton.setIcon(new EmptyIcon(settings.getChangedColor(),
                                             20,
                                             20));
    colorChangedButton.setText("");
    showLineNumbersCheckBox.setSelected(settings.getShowLineNumbers());
    ignoreWhitespaceAtBeginCheckBox.setSelected(ignore.getIgnoreWhitespaceAtBegin());
    ignoreWhitespaceInBetweenCheckBox.setSelected(ignore.getIgnoreWhitespaceInBetween());
    ignoreWhitespaceAtEndCheckBox.setSelected(ignore.getIgnoreWhitespaceAtEnd());
    ignoreEOLCheckBox.setSelected(ignore.getIgnoreEOL());
    ignoreBlankLinesCheckBox.setSelected(ignore.getIgnoreBlankLines());
    ignoreCaseCheckBox.setSelected(ignore.getIgnoreCase());
    leftsideReadonlyCheckBox.setSelected(settings.getLeftsideReadonly());
    rightsideReadonlyCheckBox.setSelected(settings.getRightsideReadonly());
    antialiasCheckBox.setSelected(settings.isAntialiasEnabled());
    if (originalAntialias != settings.isAntialiasEnabled())
    {
      antialiasCheckBox.setText("antialias on (NEED A RESTART)");
    }
    else
    {
      antialiasCheckBox.setText("antialias on");
    }
    tabSizeSpinner.setValue(settings.getTabSize());
    font = getEditorFont();
    fontChooserButton.setFont(font);
    fontChooserButton.setText(font.getName() + " (" + font.getSize() + ")");
    defaultFontRadioButton.setSelected(!settings.isCustomFontEnabled());
    customFontRadioButton.setSelected(settings.isCustomFontEnabled());

    defaultEncodingRadioButton.setSelected(settings.getDefaultFileEncodingEnabled());
    detectEncodingRadioButton.setSelected(settings.getDetectFileEncodingEnabled());
    specificEncodingRadioButton.setSelected(settings.getSpecificFileEncodingEnabled());

    toolbarButtonIconComboBox.setSelectedItem(getEditorSettings().getToolbarButtonIcon());
    toolbarButtonTextEnabledCheckBox.setSelected(getEditorSettings().isToolbarButtonTextEnabled());

    revalidate();
  }

  private EditorSettings getEditorSettings()
  {
    return JMeldSettings.getInstance().getEditor();
  }

  private Font getEditorFont()
  {
    Font font;

    font = getEditorSettings().getFont();
    font = font == null ? FontUtil.defaultTextAreaFont : font;

    return font;
  }
}
