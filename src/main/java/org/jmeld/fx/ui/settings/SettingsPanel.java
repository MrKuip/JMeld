package org.jmeld.fx.ui.settings;

import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxUtils;
import org.jmeld.ui.util.Icons;
import org.tbee.javafx.scene.layout.MigPane;

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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class SettingsPanel
    extends MigPane
{

  public SettingsPanel()
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

    contentPanel = new StackPane();

    listPanel = new ListView<>();
    listPanel.getItems().add(new ListItem(contentPanel,
                                          new EditorSettingsPanel()));
    listPanel.getItems().add(new ListItem(contentPanel,
                                          new FilterSettingsPanel()));
    listPanel.getItems().add(new ListItem(contentPanel,
                                          new FolderSettingsPanel()));
    listPanel.getSelectionModel().selectedItemProperty().addListener(this::listSelectionChanged);

    listPanel.setCellFactory((l) -> new SettingsCell());
    listPanel.getSelectionModel().selectFirst();
    listPanel.setPrefHeight(100);
    listPanel.setPrefWidth(75);

    toolbarPanel = new ToolBar();

    saveButton = new Button();
    saveButton.setGraphic(FxUtils.getIcon(Icons.SAVE.getSmallIcon()));
    saveButton.setTooltip(new Tooltip("Save settings"));
    saveButton.setOnAction((ae) -> JMeldSettingsFx.getInstance().save());

    settingsLocationLabel = new Label("");
    settingsLocationLabel.setText(JMeldSettingsFx.getInstance().getConfigurationFileName());

    saveAsButton = new Button();
    saveAsButton.setGraphic(FxUtils.getIcon(Icons.SAVE_AS.getSmallIcon()));
    saveAsButton.setTooltip(new Tooltip("Save settings to a different file"));

    reloadButton = new Button();
    reloadButton.setGraphic(FxUtils.getIcon(Icons.RELOAD.getSmallIcon()));
    reloadButton.setTooltip(new Tooltip("Reload settings from a different file"));

    toolbarPanel.getItems().addAll(saveButton, FxUtils.getWidthSpacer(10), settingsLocationLabel, FxUtils.getSpacer(),
        saveAsButton, reloadButton);

    add(toolbarPanel, new CC().dockNorth());
    add(new Region(), new CC().dockSouth().wrap().gapTop("20"));
    add(listPanel, new CC().dockWest().wrap().gapTop("20").gapLeft("10").width("pref!").height("100%"));
    add(contentPanel, new CC().wrap().gapTop("20").gapLeft("20").height("100%").width("100%"));
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
    private Node image;
    private Node content;

    public ListItem(StackPane stackPane,
        SettingsPanelIF panel)
    {
      this.text = panel.getText();
      this.content = panel.getContent();
      this.image = panel.getIcon();

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

    public Node getImage()
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
      setGraphic(item.getImage());
    }
  }
}
