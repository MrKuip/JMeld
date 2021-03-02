package org.jmeld.fx.ui.settings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.jmeld.fx.util.FxUtils;
import org.jmeld.settings.EditorSettings;
import org.jmeld.settings.FolderSettings;
import org.jmeld.settings.FolderSettings.FolderView;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.util.Icons;
import org.tbee.javafx.scene.layout.MigPane;

public class EditorSettingsPanel
    extends MigPane
    implements SettingsPanelIF
{
  public EditorSettingsPanel()
  {
    super(new LC().noGrid());

    initConfiguration();
    init();

    JMeldSettings.getInstance().addConfigurationListener(this::configurationChanged);
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
    Label label;
    Text text;
    ToggleButton toggleButton;
    Button button;
    ComboBox<FolderView> comboBox;
    CheckBox ignoreWhitespaceAtBeginCheckBox;
    CheckBox ignoreWhitespaceInBetweenCheckBox;
    CheckBox ignoreWhitespaceAtEndCheckBox;
    CheckBox ignoreEOLCheckBox;
    CheckBox ignoreBlankLinesCheckBox;
    CheckBox ignoreCaseCheckBox;
    Separator separator;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    text = new Text("Editor settings");
    text.setStyle("-fx-font-size:20; ");
    add(text, new CC().dockNorth().wrap().span(3).gapLeft("10"));

    panel = new MigPane(null,
                        "[pref][pref][grow,fill]");
    add(panel, "west");

    label = new Label("Font");
    label.setFont(FxUtils.boldFont(label.getFont()));
    panel.add(label, new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    separator = new Separator();
    panel.add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    toggleButton = new RadioButton("Use default font");
    toggleButton.setOnAction((ae) -> System.out.println("Pressed:" + ae.getSource()));
    panel.add(toggleButton, new CC().gapLeft(gap1).split(2).wrap());

    toggleButton = new RadioButton("Use custom font");
    toggleButton.setOnAction((ae) -> System.out.println("Pressed:" + ae.getSource()));
    panel.add(toggleButton, new CC().gapLeft(gap1).split(2));
    button = new Button("Dialog (12)");
    panel.add(button, new CC().wrap());

    label = new Label("Miscelaneous");
    label.setFont(FxUtils.boldFont(label.getFont()));
    panel.add(label, new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    separator = new Separator();
    panel.add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    panel.add(new Label("Tab size"), new CC().gapLeft(gap1).split(3));
    panel.add(new Spinner(), new CC().wrap());

    panel.add(new Label("Look and feel"), new CC().gapLeft(gap1).split(3));
    comboBox = new ComboBox<>();
    comboBox.getItems().addAll(FolderSettings.FolderView.values());
    panel.add(comboBox, new CC().wrap());

    panel.add(new CheckBox(), new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Show line numbers"), new CC().wrap());

    panel.add(new CheckBox(), new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Rightside readonly"), new CC().wrap());

    panel.add(new CheckBox(), new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Leftside readonly"), new CC().wrap());

    panel.add(new CheckBox(), new CC().gapLeft(gap1).split(2));
    panel.add(new Label("antialias on"), new CC().wrap());

    panel = new MigPane(null,
                        "[pref][pref][grow,fill]");
    add(panel, "west");

    label = new Label("Ignore");
    label.setFont(FxUtils.boldFont(label.getFont()));
    panel.add(label, new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    separator = new Separator();
    panel.add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    ignoreWhitespaceAtBeginCheckBox = new CheckBox();
    ignoreWhitespaceAtBeginCheckBox.setSelected(getSettings().getIgnore().ignoreWhitespaceAtBegin);
    ignoreWhitespaceAtBeginCheckBox.setOnAction((ae) -> getSettings().setIgnoreWhitespaceAtBegin(
        ignoreWhitespaceAtBeginCheckBox.isSelected()));
    panel.add(ignoreWhitespaceAtBeginCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Ignore whitespace at begin of a line"), new CC().wrap());

    ignoreWhitespaceInBetweenCheckBox = new CheckBox();
    ignoreWhitespaceInBetweenCheckBox.setSelected(getSettings().getIgnore().ignoreWhitespaceInBetween);
    ignoreWhitespaceInBetweenCheckBox.setOnAction((ae) -> getSettings().setIgnoreWhitespaceInBetween(
        ignoreWhitespaceInBetweenCheckBox.isSelected()));
    panel.add(ignoreWhitespaceInBetweenCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Ignore whitespace betweene begin and end of a line"), new CC().wrap());

    ignoreWhitespaceAtEndCheckBox = new CheckBox();
    ignoreWhitespaceAtEndCheckBox.setSelected(getSettings().getIgnore().ignoreWhitespaceAtEnd);
    ignoreWhitespaceAtEndCheckBox.setOnAction((ae) -> getSettings().setIgnoreWhitespaceAtEnd(
        ignoreWhitespaceAtEndCheckBox.isSelected()));
    panel.add(ignoreWhitespaceAtEndCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Ignore whitespace at the end of a line"), new CC().wrap());

    ignoreEOLCheckBox = new CheckBox();
    ignoreEOLCheckBox.setSelected(getSettings().getIgnore().ignoreEOL);
    ignoreEOLCheckBox.setOnAction((ae) -> getSettings().setIgnoreEOL(ignoreEOLCheckBox.isSelected()));
    panel.add(ignoreEOLCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("ignore EOL (End of line markers)"), new CC().wrap());

    ignoreBlankLinesCheckBox = new CheckBox();
    ignoreBlankLinesCheckBox.setSelected(getSettings().getIgnore().ignoreBlankLines);
    ignoreBlankLinesCheckBox.setOnAction((ae) -> getSettings().setIgnoreBlankLines(
        ignoreBlankLinesCheckBox.isSelected()));
    panel.add(ignoreBlankLinesCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Ignore blank lines"), new CC().wrap());

    ignoreCaseCheckBox = new CheckBox();
    ignoreCaseCheckBox.setSelected(getSettings().getIgnore().ignoreCase);
    ignoreCaseCheckBox.setOnAction((ae) -> getSettings().setIgnoreCase(ignoreCaseCheckBox.isSelected()));
    panel.add(ignoreCaseCheckBox, new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Ignore case"), new CC().wrap());

    label = new Label("File encoding");
    label.setFont(FxUtils.boldFont(label.getFont()));
    panel.add(label, new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    separator = new Separator();
    panel.add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    toggleButton = new RadioButton("Default encoding on this computer (UTF-8)");
    toggleButton.setOnAction((ae) -> System.out.println("Pressed:" + ae.getSource()));
    panel.add(toggleButton, new CC().gapLeft(gap1).split(2).wrap());

    toggleButton = new RadioButton("Try to detect encoding");
    toggleButton.setOnAction((ae) -> System.out.println("Pressed:" + ae.getSource()));
    panel.add(toggleButton, new CC().gapLeft(gap1).split(2).wrap());

    toggleButton = new RadioButton("Use encoding:");
    toggleButton.setOnAction((ae) -> System.out.println("Pressed:" + ae.getSource()));
    panel.add(toggleButton, new CC().gapLeft(gap1).split(2));
    comboBox = new ComboBox<>();
    comboBox.getItems().addAll(FolderSettings.FolderView.values());
    panel.add(comboBox, new CC().wrap());

    label = new Label("Toolbar appearance");
    label.setFont(FxUtils.boldFont(label.getFont()));
    panel.add(label, new CC().wrap().gapLeft(gap2).gapTop("10").span(2));
    separator = new Separator();
    panel.add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    toggleButton = new RadioButton("Icon in button:");
    toggleButton.setOnAction((ae) -> System.out.println("Pressed:" + ae.getSource()));
    panel.add(toggleButton, new CC().gapLeft(gap1).split(2));
    comboBox = new ComboBox<>();
    comboBox.getItems().addAll(FolderSettings.FolderView.values());
    panel.add(comboBox, new CC().wrap());

    panel.add(new CheckBox(), new CC().gapLeft(gap1).split(2));
    panel.add(new Label("Text in button"), new CC().wrap());
  }

  private void initConfiguration()
  {
  }

  public void configurationChanged()
  {
    initConfiguration();
  }

  private EditorSettings getSettings()
  {
    return JMeldSettings.getInstance().getEditor();
  }
}
