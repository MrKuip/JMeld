package org.jmeld.fx.ui.settings;

import static org.jmeld.fx.util.FxCss.header1;
import static org.jmeld.fx.util.FxCss.header2;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jmeld.fx.ui.FxFontChooser;
import org.jmeld.fx.util.FxBindings;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.settings.EditorSettings;
import org.jmeld.settings.EditorSettings.ToolbarButtonIcon;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.util.CharsetDetector;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class EditorSettingsPane
  extends MigPane
    implements SettingsPaneIF
{
  public EditorSettingsPane()
  {
    super(new LC().noGrid());

    init();
  }

  @Override
  public String getText()
  {
    return "Editor";
  }

  @Override
  public Image getImage()
  {
    return FxIcon.EDIT.getLargeImage();
  }

  private void init()
  {
    MigPane panel;
    EditorSettings settings;
    CheckBox ignoreWhitespaceAtBeginCheckBox;
    CheckBox ignoreWhitespaceInBetweenCheckBox;
    CheckBox ignoreWhitespaceAtEndCheckBox;
    CheckBox ignoreEOLCheckBox;
    CheckBox ignoreBlankLinesCheckBox;
    CheckBox ignoreCaseCheckBox;
    RadioButton defaultFontButton;
    RadioButton customFontButton;
    Button fontChooserButton;
    ComboBox<String> lookAndFeelComboBox;
    CheckBox showLineNumbersCheckbox;
    CheckBox rightSideReadonlyCheckbox;
    CheckBox leftSideReadonlyCheckbox;
    CheckBox antiAliasCheckbox;
    RadioButton fileEncodingDefaultButton;
    RadioButton fileEncodingDetectButton;
    RadioButton fileEncodingSpecificButton;
    ComboBox<String> fileEncodingSpecificComboBox;
    ComboBox<ToolbarButtonIcon> toolBarIconInButtonComboBox;
    TextField tabSizeTextField;
    CheckBox toolbarIconInButtonCheckBox;
    CheckBox toolbarTextInButtonCheckBox;
    ToggleGroup toggleGroup;
    ColorPicker colorAddedColorPicker;
    ColorPicker colorDeletedColorPicker;
    ColorPicker colorChangedColorPicker;
    Button colorRestoreButton;
    TextFormatter<Number> formatter;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    panel = new MigPane("", "[pref][pref][grow,fill]");
    settings = getSettings();

    add(header1(new Text("Editor settings")), new CC().dockNorth().wrap().span(3).gapLeft("10"));
    add(panel, "west");

    // Creation:
    defaultFontButton = new RadioButton("Use default font");
    customFontButton = new RadioButton("Use custom font");
    fontChooserButton = new Button("Dialog (12)");
    tabSizeTextField = new TextField();
    lookAndFeelComboBox = new ComboBox<>();
    showLineNumbersCheckbox = new CheckBox("Show line numbers");
    rightSideReadonlyCheckbox = new CheckBox("Rightside readonly");
    leftSideReadonlyCheckbox = new CheckBox("Leftside readonly");
    antiAliasCheckbox = new CheckBox("Antialias on");
    colorAddedColorPicker = new ColorPicker();
    colorDeletedColorPicker = new ColorPicker();
    colorChangedColorPicker = new ColorPicker();
    colorRestoreButton = new Button("Restore original colors");

    // Layout:
    panel.add(header2(new Label("Font")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(defaultFontButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(customFontButton, new CC().gapLeft(gap1).split(2));
    panel.add(fontChooserButton, new CC().wrap());
    panel.add(header2(new Label("Miscelaneous")), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(new Label("Tab size"), new CC().gapLeft(gap1).split(3));
    panel.add(tabSizeTextField, new CC().wrap());
    panel.add(new Label("Look and feel"), new CC().gapLeft(gap1).split(3));
    panel.add(lookAndFeelComboBox, new CC().wrap());
    panel.add(showLineNumbersCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(rightSideReadonlyCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(leftSideReadonlyCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(antiAliasCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(header2(new Label("Colors")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(colorAddedColorPicker, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Lines have been added"), new CC().wrap());
    panel.add(colorDeletedColorPicker, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Lines have been deleted"), new CC().wrap());
    panel.add(colorChangedColorPicker, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Lines have been changed"), new CC().wrap());
    panel.add(colorRestoreButton, new CC().gapLeft(gap1).wrap());

    // Initialization
    toggleGroup = new ToggleGroup();
    defaultFontButton.setToggleGroup(toggleGroup);
    customFontButton.setToggleGroup(toggleGroup);

    fontChooserButton.setOnAction((ae) -> {
      FxFontChooser dialog;
      Optional<Font> font;

      dialog = new FxFontChooser(new Text().getFont());
      font = dialog.showAndWait();
      if (font.isPresent())
      {
        fontChooserButton.setFont(font.get());
        getSettings().setFont(FxBindings.toAwtFont(font.get()));
      }
    });
    colorRestoreButton.setOnAction((ae) -> getSettings().restoreColors());
    lookAndFeelComboBox.getItems().setAll(getLookAndFeelList());

    // Binding
    FxBindings.bindBidirectional(defaultFontButton.selectedProperty(), settings::isDefaultFontEnabled,
        settings::setDefaultFontEnabled);
    FxBindings.bindBidirectional(customFontButton.selectedProperty(), settings::isCustomFontEnabled,
        settings::setCustomFontEnabled);
    FxBindings.bindBidirectional(lookAndFeelComboBox.valueProperty(), settings::getLookAndFeelName,
        settings::setLookAndFeelName);
    FxBindings.bindBidirectional(showLineNumbersCheckbox.selectedProperty(), settings::getShowLineNumbers,
        settings::setShowLineNumbers);
    FxBindings.bindBidirectional(rightSideReadonlyCheckbox.selectedProperty(), settings::getRightsideReadonly,
        settings::setRightsideReadonly);
    FxBindings.bindBidirectional(leftSideReadonlyCheckbox.selectedProperty(), settings::getLeftsideReadonly,
        settings::setLeftsideReadonly);
    FxBindings.bindBidirectional(antiAliasCheckbox.selectedProperty(), settings::isAntialiasEnabled,
        settings::setAntialiasEnabled);
    formatter = new TextFormatter<>(new NumberStringConverter("#,###"));
    FxBindings.bindBidirectional(formatter.valueProperty(), settings::getTabSize,
        (Number n) -> settings.setTabSize(n.intValue()));
    tabSizeTextField.setTextFormatter(formatter);
    FxBindings.bindBidirectional(colorAddedColorPicker.valueProperty(), settings::getAddedColor,
        settings::setAddedColor, FxBindings::toAwtColor, FxBindings::toFxColor);
    FxBindings.bindBidirectional(colorDeletedColorPicker.valueProperty(), settings::getDeletedColor,
        settings::setDeletedColor, FxBindings::toAwtColor, FxBindings::toFxColor);
    FxBindings.bindBidirectional(colorChangedColorPicker.valueProperty(), settings::getChangedColor,
        settings::setChangedColor, FxBindings::toAwtColor, FxBindings::toFxColor);

    panel = new MigPane(null, "[pref][pref][grow,fill]");
    add(panel, "west");

    // Creation
    ignoreWhitespaceAtBeginCheckBox = new CheckBox("Ignore whitespace at begin of a line");
    ignoreWhitespaceInBetweenCheckBox = new CheckBox("Ignore whitespace betweene begin and end of a line");
    ignoreWhitespaceAtEndCheckBox = new CheckBox("Ignore whitespace at the end of a line");
    ignoreEOLCheckBox = new CheckBox("ignore EOL (End of line markers)");
    ignoreBlankLinesCheckBox = new CheckBox("Ignore blank lines");
    ignoreCaseCheckBox = new CheckBox("Ignore case");
    fileEncodingDefaultButton = new RadioButton("Default encoding on this computer (UTF-8)");
    fileEncodingDetectButton = new RadioButton("Try to detect encoding");
    fileEncodingSpecificButton = new RadioButton("Use encoding:");
    fileEncodingSpecificComboBox = new ComboBox<>();
    toolBarIconInButtonComboBox = new ComboBox<>();
    toolbarTextInButtonCheckBox = new CheckBox("Text in button");
    toolbarIconInButtonCheckBox = new CheckBox("Icon in button");

    // Binding

    // Layout
    panel.add(header2(new Label("Ignore")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(ignoreWhitespaceAtBeginCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreWhitespaceInBetweenCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreWhitespaceAtEndCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreEOLCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreBlankLinesCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreCaseCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(header2(new Label("File encoding")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(fileEncodingDefaultButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(fileEncodingDetectButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(fileEncodingSpecificButton, new CC().gapLeft(gap1).split(2));
    panel.add(fileEncodingSpecificComboBox, new CC().wrap());
    panel.add(header2(new Label("Toolbar appearance")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(toolbarIconInButtonCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(toolBarIconInButtonComboBox, new CC().wrap());
    panel.add(toolbarTextInButtonCheckBox, new CC().gapLeft(gap1).wrap());

    // Initialization
    fileEncodingSpecificComboBox.getItems().setAll(CharsetDetector.getInstance().getCharsetNameList());
    toolBarIconInButtonComboBox.getItems().setAll(ToolbarButtonIcon.values());
    toggleGroup = new ToggleGroup();
    fileEncodingDefaultButton.setToggleGroup(toggleGroup);
    fileEncodingSpecificButton.setToggleGroup(toggleGroup);
    fileEncodingDetectButton.setToggleGroup(toggleGroup);

    // Binding
    ignoreWhitespaceAtBeginCheckBox.selectedProperty()
        .bindBidirectional(getSettings().getIgnore().ignoreWhitespaceAtBegin);
    ignoreWhitespaceInBetweenCheckBox.selectedProperty()
        .bindBidirectional(getSettings().getIgnore().ignoreWhitespaceInBetween);
    ignoreWhitespaceAtEndCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreWhitespaceAtEnd);
    ignoreEOLCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreEOL);
    ignoreBlankLinesCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreBlankLines);
    ignoreCaseCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreCase);

    FxBindings.bindBidirectional(fileEncodingDefaultButton.selectedProperty(), settings::getDefaultFileEncodingEnabled,
        settings::setDefaultFileEncodingEnabled);
    FxBindings.bindBidirectional(fileEncodingDetectButton.selectedProperty(), settings::getDetectFileEncodingEnabled,
        settings::setDetectFileEncodingEnabled);
    FxBindings.bindBidirectional(fileEncodingSpecificButton.selectedProperty(),
        settings::getSpecificFileEncodingEnabled, settings::setSpecificFileEncodingEnabled);
    FxBindings.bindBidirectional(fileEncodingSpecificComboBox.valueProperty(), settings::getSpecificFileEncodingName,
        settings::setSpecificFileEncodingName);
    FxBindings.bindBidirectional(toolBarIconInButtonComboBox.valueProperty(), settings::getToolbarButtonIcon,
        settings::setToolbarButtonIcon);
    FxBindings.bindBidirectional(toolbarIconInButtonCheckBox.selectedProperty(), settings::isToolbarButtonTextEnabled,
        settings::setToolbarButtonTextEnabled);
    FxBindings.bindBidirectional(toolbarTextInButtonCheckBox.selectedProperty(), settings::isToolbarButtonTextEnabled,
        settings::setToolbarButtonTextEnabled);
  }

  private List<String> getLookAndFeelList()
  {
    return Arrays.asList(Application.STYLESHEET_CASPIAN, Application.STYLESHEET_MODENA);
  }

  private EditorSettings getSettings()
  {
    return JMeldSettings.getInstance().getEditor();
  }

  private void setFont(Font font)
  {
    getSettings().setFont(FxBindings.toAwtFont(font));
  }

  private Font getFont()
  {
    return FxBindings.toFxFont(getSettings().getFont());
  }
}
