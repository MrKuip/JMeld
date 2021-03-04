package org.jmeld.fx.ui.settings;

import static org.jmeld.fx.util.FxCss.header1;
import static org.jmeld.fx.util.FxCss.header2;

import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.dialog.FontSelectorDialog;
import org.jmeld.fx.settings.EditorSettingsFx;
import org.jmeld.fx.settings.EditorSettingsFx.ToolbarButtonIcon;
import org.jmeld.fx.settings.FolderSettingsFx;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxUtils;
import org.jmeld.ui.util.Icons;
import org.jmeld.util.CharsetDetector;
import org.tbee.javafx.scene.layout.MigPane;

public class EditorSettingsPanel
    extends MigPane
    implements SettingsPanelIF
{
  public EditorSettingsPanel()
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
  public Node getIcon()
  {
    return FxUtils.getIcon(Icons.EDIT.getLargeIcon());
  }

  private void init()
  {
    MigPane panel;
    Label fontLabel;
    CheckBox ignoreWhitespaceAtBeginCheckBox;
    CheckBox ignoreWhitespaceInBetweenCheckBox;
    CheckBox ignoreWhitespaceAtEndCheckBox;
    CheckBox ignoreEOLCheckBox;
    CheckBox ignoreBlankLinesCheckBox;
    CheckBox ignoreCaseCheckBox;
    RadioButton defaultFontButton;
    RadioButton customFontButton;
    Button fontChooserButton;
    Label miscelaneousLabel;
    ComboBox<FolderSettingsFx.FolderView> folderViewComboBox;
    CheckBox showLineNumbersCheckbox;
    CheckBox rightSideReadonlyCheckbox;
    CheckBox leftSideReadonlyCheckbox;
    CheckBox antiAliasCheckbox;
    Label ignoreLabel;
    Label fileEncodingLabel;
    RadioButton defaultFileEncodingButton;
    RadioButton detectFileEncodingButton;
    RadioButton specificFileEncodingButton;
    ComboBox<String> useEncodingComboBox;
    RadioButton iconInButtonButton;
    ComboBox<ToolbarButtonIcon> toolBarButtonIconComboBox;
    CheckBox textInButtonCheckBox;
    TextField tabSizeTextField;
    CheckBox toolbarButtonIconEnabledCheckBox;
    CheckBox toolbarButtonTextEnabledCheckBox;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    panel = new MigPane(null,
                        "[pref][pref][grow,fill]");

    add(header1(new Text("Editor settings")), new CC().dockNorth().wrap().span(3).gapLeft("10"));
    add(panel, "west");

    // Creation:
    fontLabel = new Label("Font");
    fontLabel.getStyleClass().add("header2");
    defaultFontButton = new RadioButton("Use default font");
    customFontButton = new RadioButton("Use custom font");
    fontChooserButton = new Button("Dialog (12)");
    miscelaneousLabel = new Label("Miscelaneous");
    tabSizeTextField = new TextField();
    folderViewComboBox = new ComboBox<>();
    showLineNumbersCheckbox = new CheckBox("Show line numbers");
    rightSideReadonlyCheckbox = new CheckBox("Rightside readonly");
    leftSideReadonlyCheckbox = new CheckBox("Leftside readonly");
    antiAliasCheckbox = new CheckBox("Antialias on");

    // Layout:
    panel.add(header2(fontLabel), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(defaultFontButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(customFontButton, new CC().gapLeft(gap1).split(2));
    panel.add(fontChooserButton, new CC().wrap());
    panel.add(header2(miscelaneousLabel), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(new Label("Tab size"), new CC().gapLeft(gap1).split(3));
    panel.add(tabSizeTextField, new CC().wrap());
    panel.add(new Label("Look and feel"), new CC().gapLeft(gap1).split(3));
    panel.add(folderViewComboBox, new CC().wrap());
    panel.add(showLineNumbersCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(rightSideReadonlyCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(leftSideReadonlyCheckbox, new CC().gapLeft(gap1).wrap());
    panel.add(antiAliasCheckbox, new CC().gapLeft(gap1).wrap());

    // Binding
    TextFormatter formatter;

    defaultFontButton.selectedProperty().bindBidirectional(getSettings().defaultFontProperty);
    customFontButton.selectedProperty().bindBidirectional(getSettings().customFontProperty);
    showLineNumbersCheckbox.selectedProperty().bindBidirectional(getSettings().showLineNumbersProperty);
    rightSideReadonlyCheckbox.selectedProperty().bindBidirectional(getSettings().rightsideReadonlyProperty);
    leftSideReadonlyCheckbox.selectedProperty().bindBidirectional(getSettings().leftsideReadonlyProperty);
    antiAliasCheckbox.selectedProperty().bindBidirectional(getSettings().antialiasProperty);
    formatter = new TextFormatter<>(new NumberStringConverter("#,###"));
    formatter.valueProperty().bindBidirectional(getSettings().tabSizeProperty);
    tabSizeTextField.setTextFormatter(formatter);

    panel = new MigPane(null,
                        "[pref][pref][grow,fill]");
    add(panel, "west");

    // Creation
    ignoreLabel = new Label("Ignore");
    ignoreWhitespaceAtBeginCheckBox = new CheckBox("Ignore whitespace at begin of a line");
    ignoreWhitespaceInBetweenCheckBox = new CheckBox("Ignore whitespace betweene begin and end of a line");
    ignoreWhitespaceAtEndCheckBox = new CheckBox("Ignore whitespace at the end of a line");
    ignoreEOLCheckBox = new CheckBox("ignore EOL (End of line markers)");
    ignoreBlankLinesCheckBox = new CheckBox("Ignore blank lines");
    ignoreCaseCheckBox = new CheckBox("Ignore case");
    fileEncodingLabel = new Label("File encoding");
    defaultFileEncodingButton = new RadioButton("Default encoding on this computer (UTF-8)");
    detectFileEncodingButton = new RadioButton("Try to detect encoding");
    specificFileEncodingButton = new RadioButton("Use encoding:");
    useEncodingComboBox = new ComboBox<>();
    toolBarButtonIconComboBox = new ComboBox<>();
    toolbarButtonTextEnabledCheckBox = new CheckBox("Text in button");
    toolbarButtonIconEnabledCheckBox = new CheckBox("Icon in button");

    // Binding

    // Layout
    panel.add(header2(ignoreLabel), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(ignoreWhitespaceAtBeginCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreWhitespaceInBetweenCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreWhitespaceAtEndCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreEOLCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreBlankLinesCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(ignoreCaseCheckBox, new CC().gapLeft(gap1).wrap());
    panel.add(header2(fileEncodingLabel), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(defaultFileEncodingButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(detectFileEncodingButton, new CC().gapLeft(gap1).split(2).wrap());
    panel.add(specificFileEncodingButton, new CC().gapLeft(gap1).split(2));
    panel.add(useEncodingComboBox, new CC().wrap());
    panel.add(header2(new Label("Toolbar appearance")), new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    panel.add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    panel.add(toolbarButtonIconEnabledCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(toolBarButtonIconComboBox, new CC().wrap());
    panel.add(toolbarButtonTextEnabledCheckBox, new CC().gapLeft(gap1).wrap());

    // Initialization
    useEncodingComboBox.getItems().setAll(CharsetDetector.getInstance().getCharsetNameList());
    toolBarButtonIconComboBox.getItems().setAll(ToolbarButtonIcon.values());

    // Binding
    ignoreWhitespaceAtBeginCheckBox.selectedProperty().bindBidirectional(
        getSettings().getIgnore().ignoreWhitespaceAtBegin);
    ignoreWhitespaceInBetweenCheckBox.selectedProperty().bindBidirectional(
        getSettings().getIgnore().ignoreWhitespaceInBetween);
    ignoreWhitespaceAtEndCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreWhitespaceAtEnd);
    ignoreEOLCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreEOL);
    ignoreBlankLinesCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreBlankLines);
    ignoreCaseCheckBox.selectedProperty().bindBidirectional(getSettings().getIgnore().ignoreCase);
    defaultFileEncodingButton.selectedProperty().bindBidirectional(getSettings().defaultFileEncodingEnabledProperty);
    detectFileEncodingButton.selectedProperty().bindBidirectional(getSettings().detectFileEncodingEnabledProperty);
    specificFileEncodingButton.selectedProperty().bindBidirectional(getSettings().specificFileEncodingEnabledProperty);
    useEncodingComboBox.valueProperty().bindBidirectional(getSettings().specificFileEncodingNameProperty);
    toolBarButtonIconComboBox.valueProperty().bindBidirectional(getSettings().toolbarButtonIconProperty);
    toolbarButtonIconEnabledCheckBox.selectedProperty().bindBidirectional(
        getSettings().toolbarButtonIconEnabledProperty);
    toolbarButtonTextEnabledCheckBox.selectedProperty().bindBidirectional(
        getSettings().toolbarButtonTextEnabledProperty);

    fontChooserButton.setOnAction((ae) -> {
      FontSelectorDialog dialog;

      dialog = new FontSelectorDialog(null);
      Optional<Font> font = dialog.showAndWait();
      if (font.isPresent())
      {
        getSettings().setFont(font.get());
      }
    });

    folderViewComboBox.getItems().setAll(FolderSettingsFx.FolderView.values());
  }

  private EditorSettingsFx getSettings()
  {
    return JMeldSettingsFx.getInstance().getEditor();
  }
}
