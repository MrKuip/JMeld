package org.jmeld.fx.ui.settings;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import net.miginfocom.layout.CC;
import org.jmeld.fx.util.FxUtils;
import org.jmeld.settings.FolderSettings;
import org.jmeld.settings.FolderSettings.FolderView;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.ui.util.Icons;
import org.tbee.javafx.scene.layout.MigPane;

public class FolderSettingsPanel
    extends MigPane
    implements SettingsPanelIF
{

  public FolderSettingsPanel()
  {
    super(null,
          "[pref][pref][grow,fill]");

    initConfiguration();
    init();

    JMeldSettings.getInstance().addConfigurationListener(this::configurationChanged);
  }

  @Override
  public String getText()
  {
    return "Folder";
  }

  @Override
  public Node getIcon()
  {
    return FxUtils.getIcon(Icons.FOLDER.getLargeIcon());
  }

  private void init()
  {
    Label label;
    Label text;
    ToggleButton onlyLeftButton;
    ToggleButton leftRightChangedButton;
    ToggleButton onlyRightButton;
    ToggleButton leftRightUnChangedButton;
    ComboBox<FolderView> comboBox;
    Separator separator;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    text = new Label("Folder settings");
    text.setStyle("-fx-font-size:20; ");
    add(text, new CC().dockNorth().wrap().span(3).gapLeft("10"));

    label = new Label("File filter");
    label.setFont(FxUtils.boldFont(label.getFont()));
    add(label, new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    separator = new Separator();
    add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    onlyLeftButton = new ToggleButton();
    onlyLeftButton.setGraphic(FxUtils.getIcon(Icons.ONLY_LEFT));
    onlyLeftButton.setOnAction((ae) -> getSettings().setOnlyLeft(onlyLeftButton.isSelected()));
    onlyLeftButton.setSelected(getSettings().getOnlyLeft());
    label = new Label("Show files that only exist on the left site");
    add(onlyLeftButton, new CC().gapLeft(gap1).split(2));
    add(label, new CC().wrap());

    leftRightChangedButton = new ToggleButton();
    leftRightChangedButton.setGraphic(FxUtils.getIcon(Icons.LEFT_RIGHT_CHANGED));
    leftRightChangedButton.setOnAction((ae) -> getSettings().setLeftRightChanged(leftRightChangedButton.isSelected()));
    leftRightChangedButton.setSelected(getSettings().getLeftRightChanged());
    label = new Label("Show files that are different");
    add(leftRightChangedButton, new CC().gapLeft(gap1).split(2));
    add(label, new CC().wrap());

    onlyRightButton = new ToggleButton();
    onlyRightButton.setGraphic(FxUtils.getIcon(Icons.ONLY_RIGHT));
    onlyRightButton.setOnAction((ae) -> getSettings().setOnlyRight(onlyRightButton.isSelected()));
    onlyRightButton.setSelected(getSettings().getOnlyRight());
    label = new Label("Show files that only exist on the right site");
    add(onlyRightButton, new CC().gapLeft(gap1).split(2));
    add(label, new CC().wrap());

    leftRightUnChangedButton = new ToggleButton();
    leftRightUnChangedButton.setGraphic(FxUtils.getIcon(Icons.LEFT_RIGHT_UNCHANGED));
    leftRightUnChangedButton.setOnAction((ae) -> getSettings().setLeftRightUnChanged(
        leftRightUnChangedButton.isSelected()));
    leftRightUnChangedButton.setSelected(getSettings().getLeftRightUnChanged());
    label = new Label("Show files that are equal");
    add(leftRightUnChangedButton, new CC().gapLeft(gap1).split(2));
    add(label, new CC().wrap());

    label = new Label("Miscellaneous");
    label.setFont(FxUtils.boldFont(label.getFont()));
    add(label, new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    separator = new Separator();
    add(separator, new CC().wrap().gapLeft(gap2).span(2).grow());

    comboBox = new ComboBox<>();
    comboBox.getItems().addAll(FolderSettings.FolderView.values());
    comboBox.getSelectionModel().select(getSettings().getView());
    comboBox.setOnAction((ae) -> getSettings().setView(comboBox.getSelectionModel().getSelectedItem()));
    label = new Label("Default hierarchy");
    add(label, new CC().gapLeft(gap1).split(2));
    add(comboBox, new CC().wrap());
  }

  private void initConfiguration()
  {
  }

  public void configurationChanged()
  {
    initConfiguration();
  }

  private FolderSettings getSettings()
  {
    return JMeldSettings.getInstance().getFolder();
  }
}
