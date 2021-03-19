package org.jmeld.fx.ui.settings;

import static org.jmeld.fx.util.FxCss.header1;
import static org.jmeld.fx.util.FxCss.header2;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import net.miginfocom.layout.CC;
import org.jmeld.fx.settings.FolderSettingsFx;
import org.jmeld.fx.settings.FolderSettingsFx.FolderView;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.fx.util.FxUtils;
import org.tbee.javafx.scene.layout.MigPane;

public class FolderSettingsPane
    extends MigPane
    implements SettingsPaneIF
{

  public FolderSettingsPane()
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
  public Image getImage()
  {
    return FxIcon.FOLDER.getLargeImage();
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
    onlyLeftButton.setGraphic(FxUtils.createImageNode(FxIcon.FILE_EXIST_NOTEQUAL.getSmallImage(), FxIcon.FILE_NOT_EXIST
        .getSmallImage()));
    leftRightChangedButton.setGraphic(FxUtils.createImageNode(FxIcon.FILE_EXIST_NOTEQUAL.getSmallImage(),
        FxIcon.FILE_EXIST_NOTEQUAL.getSmallImage()));
    onlyRightButton.setGraphic(FxUtils.createImageNode(FxIcon.FILE_NOT_EXIST.getSmallImage(),
        FxIcon.FILE_EXIST_NOTEQUAL.getSmallImage()));
    leftRightUnChangedButton.setGraphic(FxUtils.createImageNode(FxIcon.FILE_EXIST_EQUAL.getSmallImage(),
        FxIcon.FILE_EXIST_EQUAL.getSmallImage()));
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
