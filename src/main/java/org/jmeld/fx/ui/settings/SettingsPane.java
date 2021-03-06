package org.jmeld.fx.ui.settings;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.fx.util.FxUtils;
import org.tbee.javafx.scene.layout.MigPane;

public class SettingsPane
    extends MigPane
{

  public SettingsPane()
  {
    super(new LC().noGrid().fill());

    init();
  }

  private void init()
  {
    StackPane contentPanel;
    ListView<ListItem> listPanel;
    ToolBar toolbarPanel;
    Button saveButton;
    Button saveAsButton;
    Button reloadButton;
    Label settingsLocationLabel;
    SettingsPaneIF panel;

    contentPanel = new StackPane();

    listPanel = new ListView<>();
    panel = new FilterSettingsPane();
    listPanel.getItems().add(new ListItem(contentPanel,
                                          panel));
    panel = new EditorSettingsPane();
    listPanel.getItems().add(new ListItem(contentPanel,
                                          panel));
    panel = new FolderSettingsPane();
    listPanel.getItems().add(new ListItem(contentPanel,
                                          panel));
    listPanel.getSelectionModel().selectedItemProperty().addListener(this::listSelectionChanged);

    listPanel.setCellFactory((l) -> new SettingsCell());
    listPanel.getSelectionModel().selectFirst();
    //listPanel.setPrefHeight(100);
    listPanel.setPrefWidth(panel.getImage().getWidth() * 2.0);

    toolbarPanel = new ToolBar();

    saveButton = new Button();
    saveButton.setGraphic(new ImageView(FxIcon.SAVE.getSmallImage()));
    saveButton.setTooltip(new Tooltip("Save settings"));
    saveButton.setOnAction((ae) -> JMeldSettingsFx.getInstance().save());

    settingsLocationLabel = new Label("");
    settingsLocationLabel.setText(JMeldSettingsFx.getInstance().getConfigurationFileName());

    saveAsButton = new Button();
    saveAsButton.setGraphic(new ImageView(FxIcon.SAVE_AS.getSmallImage()));
    saveAsButton.setTooltip(new Tooltip("Save settings to a different file"));

    reloadButton = new Button();
    reloadButton.setGraphic(new ImageView(FxIcon.RELOAD.getSmallImage()));
    reloadButton.setTooltip(new Tooltip("Reload settings from a different file"));

    toolbarPanel.getItems().addAll(saveButton, FxUtils.getWidthSpacer(10), settingsLocationLabel, FxUtils.getSpacer(),
        saveAsButton, reloadButton);

    add(toolbarPanel, new CC().dockNorth());
    add(new Region(), new CC().dockSouth().wrap().gapTop("20"));
    add(listPanel, new CC().dockWest().wrap().gapTop("20").gapLeft("10").width("pref!").height("100%"));
    add(contentPanel, new CC().gapTop("20").gapLeft("20").height("100%").width("100%"));
  }

  private void listSelectionChanged(ObservableValue<?> property, ListItem oldValue, ListItem newValue)
  {
    if (oldValue != null)
    {
      oldValue.getContent().setVisible(false);
    }

    if (newValue != null)
    {
      newValue.getContent().setVisible(true);
    }
  }

  static class ListItem
  {
    private String text;
    private Image image;
    private Node content;

    public ListItem(StackPane stackPane,
        SettingsPaneIF panel)
    {
      this.text = panel.getText();
      this.content = panel.getContent();
      this.image = panel.getImage();

      stackPane.getChildren().add(content);
      content.setVisible(false);
    }

    public String getText()
    {
      return text;
    }

    public Node getContent()
    {
      return content;
    }

    public Image getImage()
    {
      return image;
    }
  }

  static class SettingsCell
      extends ListCell<ListItem>
  {
    @Override
    public void updateItem(ListItem item, boolean empty)
    {
      super.updateItem(item, empty);

      if (empty)
      {
        return;
      }

      setText(item.getText());
      setContentDisplay(ContentDisplay.TOP);
      setAlignment(Pos.CENTER);
      setGraphic(new ImageView(item.getImage()));
    }
  }
}
