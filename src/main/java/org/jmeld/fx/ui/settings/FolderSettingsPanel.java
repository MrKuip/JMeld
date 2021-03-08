package org.jmeld.fx.ui.settings;

import static org.jmeld.fx.util.FxCss.header1;
import static org.jmeld.fx.util.FxCss.header2;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import net.miginfocom.layout.CC;
import org.jmeld.fx.settings.FolderSettingsFx;
import org.jmeld.fx.settings.FolderSettingsFx.FolderView;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxUtils;
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

    JMeldSettingsFx.getInstance().addConfigurationListener(this::configurationChanged);
  }

  @Override
  public String getText()
  {
    return "Folder";
  }

  @Override
  public Node getIcon()
  {
    return FxUtils.getIcon(Icons.FOLDER.getSmallerIcon());
  }

  private void init()
  {
    ToggleButton onlyLeftButton;
    ToggleButton leftRightChangedButton;
    ToggleButton onlyRightButton;
    ToggleButton leftRightUnChangedButton;
    ComboBox<FolderView> viewComboBox;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    // Creation
    onlyLeftButton = new ToggleButton();
    leftRightChangedButton = new ToggleButton();
    onlyRightButton = new ToggleButton();
    leftRightUnChangedButton = new ToggleButton();
    viewComboBox = new ComboBox<>();

    // Layout
    add(header1(new Label("Folder settings")), new CC().dockNorth().wrap().span(3).gapLeft("10"));
    add(header2(new Label("File filter")), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    add(onlyLeftButton, new CC().gapLeft(gap1).split(2));
    add(new Label("Show files that only exist on the left site"), new CC().wrap());
    add(leftRightChangedButton, new CC().gapLeft(gap1).split(2));
    add(new Label("Show files that are different"), new CC().wrap());
    add(onlyRightButton, new CC().gapLeft(gap1).split(2));
    add(new Label("Show files that only exist on the right site"), new CC().wrap());
    add(leftRightUnChangedButton, new CC().gapLeft(gap1).split(2));
    add(new Label("Show files that are equal"), new CC().wrap());
    add(header2(new Label("Miscellaneous")), new CC().wrap().gapLeft(gap2).gapTop("20").span(2));
    add(new Separator(), new CC().wrap().gapLeft(gap2).span(2).grow());
    add(new Label("Default hierarchy"), new CC().gapLeft(gap1).split(2));
    add(viewComboBox, new CC().wrap());

    // Initialization
    onlyLeftButton.setGraphic(FxUtils.getIcon(Icons.ONLY_LEFT));
    leftRightChangedButton.setGraphic(FxUtils.getIcon(Icons.LEFT_RIGHT_CHANGED));
    onlyRightButton.setGraphic(FxUtils.getIcon(Icons.ONLY_RIGHT));
    leftRightUnChangedButton.setGraphic(FxUtils.getIcon(Icons.LEFT_RIGHT_UNCHANGED));
    viewComboBox.getItems().addAll(FolderSettingsFx.FolderView.values());

    // Binding
    onlyLeftButton.selectedProperty().bindBidirectional(getSettings().onlyLeftProperty);
    leftRightChangedButton.selectedProperty().bindBidirectional(getSettings().leftRightChangedProperty);
    onlyRightButton.selectedProperty().bindBidirectional(getSettings().onlyRightProperty);
    leftRightUnChangedButton.selectedProperty().bindBidirectional(getSettings().leftRightUnChangedProperty);
    viewComboBox.valueProperty().bindBidirectional(getSettings().viewProperty);
  }

  private void initConfiguration()
  {
  }

  public void configurationChanged()
  {
    initConfiguration();
  }

  private FolderSettingsFx getSettings()
  {
    return JMeldSettingsFx.getInstance().getFolder();
  }
}
