/*
   JMeld is a visual diff and merge tool.
   Copyright (C) 2007  Kees Kuip
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA  02110-1301  USA
 */
package org.jmeld.fx.ui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.ui.NewPanelDialogFx.NewPanelIF;
import org.jmeld.fx.ui.settings.SettingsPane;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.fx.util.FxUtils;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class JMeldPaneFx
  extends MigPane
{
  private TabPane tabPane;
  private Map<String, Tab> openTabs = new HashMap<>();

  public JMeldPaneFx()
  {
    super(new LC().noGrid().fill());

    init();
  }

  private void init2()
  {
    TextArea textArea;
    Path path;
    String text;

    // path = Paths.get("build.gradle");
    path = Paths.get("/projecten/jmeld/PhysicalModelConfiguration.xml");
    try
    {
      text = Files.lines(path).collect(Collectors.joining("\n"));
      textArea = new TextArea(text);
      // add(textArea, new CC().height("100%").width("100%"));
      CodeArea sta;
      VirtualizedScrollPane<CodeArea> sp;

      sta = new CodeArea(text);
      sp = new VirtualizedScrollPane(sta);

      add(sp, new CC().height("100%").width("100%"));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void init()
  {
    ToolBar toolbarPanel;
    Button newButton;
    Button saveButton;
    Button undoButton;
    Button redoButton;
    Button settingsButton;
    Button helpButton;
    Button aboutButton;

    tabPane = new TabPane();

    newButton = new Button("New");
    newButton.setOnAction((ae) -> {
      NewPanelIF newPanel;

      newPanel = new NewPanelDialogFx().getNewPanel();
      if (newPanel != null)
      {
        showTab(TabId.NEW, newPanel.getShortDescription(), () -> newPanel.getNode());
      }
    });
    newButton.setContentDisplay(ContentDisplay.TOP);
    newButton.setGraphic(new ImageView(FxIcon.NEW.getLargeImage()));

    saveButton = new Button("Save");
    saveButton.setContentDisplay(ContentDisplay.TOP);
    saveButton.setAlignment(Pos.CENTER);
    saveButton.setGraphic(new ImageView(FxIcon.SAVE.getLargeImage()));
    saveButton.setOnAction((ae) -> JMeldSettingsFx.getInstance().save());

    undoButton = new Button("Undo");
    undoButton.setContentDisplay(ContentDisplay.TOP);
    undoButton.setAlignment(Pos.CENTER);
    undoButton.setGraphic(new ImageView(FxIcon.UNDO.getLargeImage()));

    redoButton = new Button("Redo");
    redoButton.setContentDisplay(ContentDisplay.TOP);
    redoButton.setAlignment(Pos.CENTER);
    redoButton.setGraphic(new ImageView(FxIcon.REDO.getLargeImage()));

    settingsButton = new Button("Settings");
    settingsButton.setOnAction((ae) -> showTab(TabId.SETTINGS, () -> new SettingsPane()));
    settingsButton.setContentDisplay(ContentDisplay.TOP);
    settingsButton.setAlignment(Pos.CENTER);
    settingsButton.setGraphic(new ImageView(FxIcon.SETTINGS.getLargeImage()));

    helpButton = new Button("Help");
    helpButton.setOnAction((ae) -> showTab(TabId.HELP, () -> new Button("Help")));
    helpButton.setContentDisplay(ContentDisplay.TOP);
    helpButton.setAlignment(Pos.CENTER);
    helpButton.setGraphic(new ImageView(FxIcon.HELP.getLargeImage()));

    aboutButton = new Button("About");
    aboutButton.setOnAction((ae) -> showTab(TabId.ABOUT, () -> new Button("About")));
    aboutButton.setContentDisplay(ContentDisplay.TOP);
    aboutButton.setAlignment(Pos.CENTER);
    aboutButton.setGraphic(new ImageView(FxIcon.ABOUT.getLargeImage()));

    toolbarPanel = new ToolBar();
    toolbarPanel.getItems().addAll(newButton, saveButton, undoButton, redoButton, FxUtils.getSpacer(), settingsButton,
        helpButton, aboutButton);

    add(toolbarPanel, new CC().dockNorth());
    add(new Region(), new CC().dockSouth().wrap().gapTop("20"));
    add(tabPane, new CC().height("100%").width("100%"));
  }

  public enum TabId
  {
    NEW("diff", FxIcon.FOLDER),
    SETTINGS("Settings", FxIcon.SETTINGS),
    HELP("Help", FxIcon.HELP),
    ABOUT("About", FxIcon.ABOUT);

    private final String mi_description;
    private final FxIcon mi_icon;

    TabId(String description, FxIcon icon)
    {
      mi_description = description;
      mi_icon = icon;
    }

    public String getDescription()
    {
      return mi_description;
    }

    public FxIcon getIcon()
    {
      return mi_icon;
    }
  }

  private void showTab(TabId tabId, Supplier<Node> nodeSupplier)
  {
    showTab(tabId, tabId.getDescription(), nodeSupplier);
  }

  public void showTab(TabId tabId, String description, Supplier<Node> nodeSupplier)
  {
    Tab tab;
    String tabKey;

    tabKey = tabId.name() + "-" + description;

    tab = openTabs.get(tabKey);
    if (tab == null)
    {
      Node node;

      node = nodeSupplier.get();
      if (node != null)
      {
        tab = new Tab(description);
        tab.setOnClosed((e) -> openTabs.remove(tabKey));
        tabPane.getTabs().add(tab);
        openTabs.put(tabKey, tab);

        tab.setGraphic(new ImageView(tabId.getIcon().getSmallImage()));
        tab.setContent(node);
      }
    }

    tabPane.getSelectionModel().select(tab);
  }
}
