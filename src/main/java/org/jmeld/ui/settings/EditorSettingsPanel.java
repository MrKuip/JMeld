package org.jmeld.ui.settings;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jmeld.JMeld;
import org.jmeld.fx.settings.EditorSettingsFx.ToolbarButtonIcon;
import org.jmeld.settings.EditorSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.util.EmptyIcon;
import org.jmeld.ui.util.FontUtil;
import org.jmeld.ui.util.LookAndFeelManager;
import org.jmeld.util.CharsetDetector;
import org.jmeld.util.Ignore;
import org.jmeld.util.conf.ConfigurationListenerIF;
import com.l2fprod.common.swing.JFontChooser;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class EditorSettingsPanel
  extends JPanel
    implements ConfigurationListenerIF
{
  private static JDialog colorDialog;
  private static JColorChooser colorChooser;
  private boolean originalAntiAlias;
  private JCheckBox ignoreWhitespaceAtBeginCheckBox;
  private JCheckBox ignoreWhitespaceInBetweenCheckBox;
  private JCheckBox ignoreWhitespaceAtEndCheckBox;
  private JCheckBox ignoreEOLCheckBox;
  private JCheckBox ignoreBlankLinesCheckBox;
  private JCheckBox ignoreCaseCheckBox;
  private JRadioButton defaultFontButton;
  private JRadioButton customFontButton;
  private JButton fontChooserButton;
  private JComboBox<String> lookAndFeelComboBox;
  private JCheckBox showLineNumbersCheckBox;
  private JCheckBox rightSideReadonlyCheckBox;
  private JCheckBox leftSideReadonlyCheckBox;
  private JCheckBox antiAliasCheckBox;
  private JRadioButton fileEncodingDefaultButton;
  private JRadioButton fileEncodingDetectButton;
  private JRadioButton fileEncodingSpecificButton;
  private JComboBox<String> fileEncodingSpecificComboBox;
  private JSpinner tabSizeSpinner;
  private JComboBox<ToolbarButtonIcon> toolBarIconInButtonComboBox;
  private JCheckBox toolbarIconInButtonCheckBox;
  private JCheckBox toolbarTextInButtonCheckBox;
  private JButton colorAddedButton;
  private JButton colorDeletedButton;
  private JButton colorChangedButton;
  private JButton colorRestoreButton;

  public EditorSettingsPanel()
  {
    setLayout(new MigLayout(new LC().noGrid().fillX()));
    originalAntiAlias = getEditorSettings().isAntialiasEnabled();

    initLayout();
    initActions();
    initConfiguration();

    JMeldSettings.getInstance().addConfigurationListener(this);
  }

  private void initLayout()
  {
    JPanel panel;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    add(SettingsPanel.header1("Editor settings"), new CC().dockNorth().wrap().grow().gapLeft("10"));

    panel = new JPanel(new MigLayout("", "[pref][pref][grow,fill]"));
    add(panel, "west");

    // Creation:
    defaultFontButton = new JRadioButton("Use default font");
    customFontButton = new JRadioButton("Use custom font");
    fontChooserButton = new JButton("Dialog (12)");
    tabSizeSpinner = new JSpinner();
    lookAndFeelComboBox = new JComboBox<>();
    showLineNumbersCheckBox = new JCheckBox("Show line numbers");
    rightSideReadonlyCheckBox = new JCheckBox("Rightside readonly");
    leftSideReadonlyCheckBox = new JCheckBox("Leftside readonly");
    antiAliasCheckBox = new JCheckBox("Antialias on");
    colorAddedButton = new JButton();
    colorDeletedButton = new JButton();
    colorChangedButton = new JButton();
    colorRestoreButton = new JButton("Restore original colors");

    // Layout:
    panel.add(SettingsPanel.header2(new JLabel("Font")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(defaultFontButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(customFontButton, new CC().gapLeft(gap1).split(2));
    panel.add(fontChooserButton, new CC().wrap());
    panel.add(SettingsPanel.header2(new JLabel("Miscelaneous")), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    panel.add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(new JLabel("Tab size"), new CC().gapLeft(gap1).split(3));
    panel.add(tabSizeSpinner, new CC().wrap());
    panel.add(new JLabel("Look and feel"), new CC().gapLeft(gap1).split(3));
    panel.add(lookAndFeelComboBox, new CC().wrap());
    panel.add(showLineNumbersCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(rightSideReadonlyCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(leftSideReadonlyCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(antiAliasCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(SettingsPanel.header2(new JLabel("Colors")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(colorAddedButton, new CC().gapLeft(gap1).split(2));
    panel.add(new JLabel("Lines have been added"), new CC().wrap());
    panel.add(colorDeletedButton, new CC().gapLeft(gap1).split(2));
    panel.add(new JLabel("Lines have been deleted"), new CC().wrap());
    panel.add(colorChangedButton, new CC().gapLeft(gap1).split(2));
    panel.add(new JLabel("Lines have been changed"), new CC().wrap());
    panel.add(colorRestoreButton, new CC().gapLeft(gap1).wrap());

    ignoreWhitespaceAtBeginCheckBox = new JCheckBox("Ignore whitespace at begin of a line");
    ignoreWhitespaceInBetweenCheckBox = new JCheckBox("Ignore whitespace betweene begin and end of a line");
    ignoreWhitespaceAtEndCheckBox = new JCheckBox("Ignore whitespace at the end of a line");
    ignoreEOLCheckBox = new JCheckBox("ignore EOL (End of line markers)");
    ignoreBlankLinesCheckBox = new JCheckBox("Ignore blank lines");
    ignoreCaseCheckBox = new JCheckBox("Ignore case");
    fileEncodingDefaultButton = new JRadioButton("Default encoding on this computer (UTF-8)");
    fileEncodingDetectButton = new JRadioButton("Try to detect encoding");
    fileEncodingSpecificButton = new JRadioButton("Use encoding:");
    fileEncodingSpecificComboBox = new JComboBox<>();
    toolBarIconInButtonComboBox = new JComboBox<>();
    toolbarTextInButtonCheckBox = new JCheckBox("Text in button");
    toolbarIconInButtonCheckBox = new JCheckBox("Icon in button");

    panel = new JPanel(new MigLayout("", "[pref][pref][grow,fill]"));
    add(panel, "west");

    // Layout
    panel.add(SettingsPanel.header2(new JLabel("Ignore")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(ignoreWhitespaceAtBeginCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreWhitespaceInBetweenCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreWhitespaceAtEndCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreEOLCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreBlankLinesCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreCaseCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(SettingsPanel.header2(new JLabel("File encoding")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(fileEncodingDefaultButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(fileEncodingDetectButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(fileEncodingSpecificButton, new CC().gapLeft(gap1).split(2));
    panel.add(fileEncodingSpecificComboBox, new CC().wrap());
    panel.add(SettingsPanel.header2(new JLabel("Toolbar appearance")),
        new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new JSeparator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(toolbarIconInButtonCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(toolBarIconInButtonComboBox, new CC().wrap());
    panel.add(toolbarTextInButtonCheckBox, new CC().gapLeft(gap1).wrap());
  }

  private void initActions()
  {
    ButtonGroup buttonGroup;

    // Initialization
    buttonGroup = new ButtonGroup();
    buttonGroup.add(defaultFontButton);
    buttonGroup.add(customFontButton);

    // ignore:
    ignoreWhitespaceAtBeginCheckBox.addActionListener(getIgnoreWhitespaceAtBeginAction());
    ignoreWhitespaceInBetweenCheckBox.addActionListener(getIgnoreWhitespaceInBetweenAction());
    ignoreWhitespaceAtEndCheckBox.addActionListener(getIgnoreWhitespaceAtEndAction());
    ignoreEOLCheckBox.addActionListener(getIgnoreEOLAction());
    ignoreBlankLinesCheckBox.addActionListener(getIgnoreBlankLinesAction());
    ignoreCaseCheckBox.addActionListener(getIgnoreCaseAction());

    // Miscellaneous:
    leftSideReadonlyCheckBox.addActionListener(getLeftsideReadonlyAction());
    rightSideReadonlyCheckBox.addActionListener(getRightsideReadonlyAction());
    antiAliasCheckBox.addActionListener(getAntialiasAction());
    tabSizeSpinner.addChangeListener(getTabSizeChangeListener());
    showLineNumbersCheckBox.addActionListener(getShowLineNumbersAction());
    lookAndFeelComboBox.setModel(getLookAndFeelModel());
    lookAndFeelComboBox.setSelectedItem(LookAndFeelManager.getInstance().getInstalledLookAndFeelName());
    lookAndFeelComboBox.addActionListener(getLookAndFeelAction());

    // Colors:
    colorAddedButton.addActionListener(getColorAddedAction());
    colorDeletedButton.addActionListener(getColorDeletedAction());
    colorChangedButton.addActionListener(getColorChangedAction());
    colorRestoreButton.addActionListener(getRestoreOriginalColorsAction());

    // Font:
    defaultFontButton.addActionListener(getDefaultFontAction());
    customFontButton.addActionListener(getCustomFontAction());
    fontChooserButton.addActionListener(getFontChooserAction());

    // File encoding:
    fileEncodingDefaultButton
        .setText(fileEncodingDefaultButton.getText() + " (" + CharsetDetector.getInstance().getDefaultCharset() + ")");

    fileEncodingDefaultButton.addActionListener(getDefaultEncodingAction());
    fileEncodingDetectButton.addActionListener(getDetectEncodingAction());
    fileEncodingSpecificButton.addActionListener(getSpecificEncodingAction());
    fileEncodingSpecificComboBox
        .setModel(new DefaultComboBoxModel<>(new Vector<>(CharsetDetector.getInstance().getCharsetNameList())));
    fileEncodingSpecificComboBox.setSelectedItem(getEditorSettings().getSpecificFileEncodingName());
    fileEncodingSpecificComboBox.addActionListener(getSpecificEncodingNameAction());

    // Toolbar appearance:
    toolBarIconInButtonComboBox.setModel(getToolbarButtonIconModel());
    toolBarIconInButtonComboBox.setSelectedItem(getEditorSettings().getToolbarButtonIcon());
    toolBarIconInButtonComboBox.addActionListener(getToolbarButtonIconAction());
    toolbarTextInButtonCheckBox.addActionListener(getToolbarButtonTextEnabledAction());
  }

  private void initConfiguration()
  {
    EditorSettings settings;
    Font font;
    Ignore ignore;

    settings = getEditorSettings();
    ignore = settings.getIgnore();
    colorAddedButton.setIcon(new EmptyIcon(settings.getAddedColor(), 20, 20));
    colorAddedButton.setText("");
    colorDeletedButton.setIcon(new EmptyIcon(settings.getDeletedColor(), 20, 20));
    colorDeletedButton.setText("");
    colorChangedButton.setIcon(new EmptyIcon(settings.getChangedColor(), 20, 20));
    colorChangedButton.setText("");
    showLineNumbersCheckBox.setSelected(settings.getShowLineNumbers());
    ignoreWhitespaceAtBeginCheckBox.setSelected(ignore.getIgnoreWhitespaceAtBegin());
    ignoreWhitespaceInBetweenCheckBox.setSelected(ignore.getIgnoreWhitespaceInBetween());
    ignoreWhitespaceAtEndCheckBox.setSelected(ignore.getIgnoreWhitespaceAtEnd());
    ignoreEOLCheckBox.setSelected(ignore.getIgnoreEOL());
    ignoreBlankLinesCheckBox.setSelected(ignore.getIgnoreBlankLines());
    ignoreCaseCheckBox.setSelected(ignore.getIgnoreCase());
    leftSideReadonlyCheckBox.setSelected(settings.getLeftsideReadonly());
    rightSideReadonlyCheckBox.setSelected(settings.getRightsideReadonly());
    antiAliasCheckBox.setSelected(settings.isAntialiasEnabled());
    if (originalAntiAlias != settings.isAntialiasEnabled())
    {
      antiAliasCheckBox.setText("antialias on (NEED A RESTART)");
    }
    else
    {
      antiAliasCheckBox.setText("antialias on");
    }
    tabSizeSpinner.setValue(settings.getTabSize());
    font = getEditorFont();
    fontChooserButton.setFont(font);
    fontChooserButton.setText(font.getName() + " (" + font.getSize() + ")");
    defaultFontButton.setSelected(!settings.isCustomFontEnabled());
    customFontButton.setSelected(settings.isCustomFontEnabled());

    fileEncodingDefaultButton.setSelected(settings.getDefaultFileEncodingEnabled());
    fileEncodingDetectButton.setSelected(settings.getDetectFileEncodingEnabled());
    fileEncodingSpecificButton.setSelected(settings.getSpecificFileEncodingEnabled());

    toolBarIconInButtonComboBox.setSelectedItem(getEditorSettings().getToolbarButtonIcon());
    toolbarTextInButtonCheckBox.setSelected(getEditorSettings().isToolbarButtonTextEnabled());

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
      getEditorSettings().setLeftsideReadonly(leftSideReadonlyCheckBox.isSelected());
    };
  }

  private ActionListener getRightsideReadonlyAction()
  {
    return (e) -> {
      getEditorSettings().setRightsideReadonly(rightSideReadonlyCheckBox.isSelected());
    };
  }

  private ActionListener getAntialiasAction()
  {
    return (e) -> {
      getEditorSettings().enableAntialias(antiAliasCheckBox.isSelected());
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
      getEditorSettings().setSpecificFileEncodingName((String) fileEncodingSpecificComboBox.getSelectedItem());
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
      getEditorSettings()
          .setToolbarButtonIcon((EditorSettings.ToolbarButtonIcon) toolBarIconInButtonComboBox.getSelectedItem());
      JMeld.getJMeldPanel().addToolBar();
    };
  }

  private ActionListener getToolbarButtonTextEnabledAction()
  {
    return (e) -> {
      getEditorSettings().setToolbarButtonTextEnabled(toolbarTextInButtonCheckBox.isSelected());
      JMeld.getJMeldPanel().addToolBar();
    };
  }

  private ActionListener getDefaultFontAction()
  {
    return (e) -> {
      getEditorSettings().enableCustomFont(!defaultFontButton.isSelected());
    };
  }

  private ActionListener getCustomFontAction()
  {
    return (e) -> {
      getEditorSettings().enableCustomFont(customFontButton.isSelected());
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

  private ComboBoxModel<String> getLookAndFeelModel()
  {
    return new DefaultComboBoxModel<>(new Vector<>(LookAndFeelManager.getInstance().getInstalledLookAndFeels()));
  }

  private ComboBoxModel<ToolbarButtonIcon> getToolbarButtonIconModel()
  {
    return new DefaultComboBoxModel<>(ToolbarButtonIcon.values());
  }

  @Override
  public void configurationChanged()
  {
    initConfiguration();
  }

}
