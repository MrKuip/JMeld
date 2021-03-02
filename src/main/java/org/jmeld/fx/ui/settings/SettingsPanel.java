package org.jmeld.fx.ui.settings;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class SettingsPanel
    extends MigPane
{

  public SettingsPanel()
  {
    super(new LC().noGrid());

    init();
  }

  private void init()
  {
    StackPane contentPanel;
    ListView<ListItem> listPanel;
    MigPane toolbarPanel;

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
    listPanel.setPrefHeight(80);
    listPanel.setPrefWidth(80);

    toolbarPanel = new MigPane();

    add(toolbarPanel, new CC().dockNorth().wrap());
    add(listPanel, new CC().dockWest().wrap().gapTop("20").gapLeft("10").gapBottom("20").width("pref!").height("100%"));
    add(contentPanel, new CC().dockWest().wrap().gapTop("20").gapLeft("10"));
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
    private Pane content;

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

    public Pane getContent()
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
      setTextAlignment(TextAlignment.CENTER);
      setGraphic(item.getImage());
    }
  }
}
