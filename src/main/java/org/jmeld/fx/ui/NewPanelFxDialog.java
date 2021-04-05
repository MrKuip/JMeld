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

import java.io.File;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.util.prefs.ComboBoxPreference;
import org.jmeld.util.prefs.ComboBoxSelectionPreference;
import org.jmeld.util.prefs.TabPanePreference;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import net.miginfocom.layout.CC;

public class NewPanelFxDialog
{
  // Class variables:
  // File comparison:
  public enum Function
  {
    FILE_COMPARISON("File comparison", () -> new FileComparisonNode()),
    DIRECTORY_COMPARISON("Directory comparison", () -> new DirectoryComparisonNode());

    private final String description;
    private final Supplier<Node> node;

    Function(String description, Supplier<Node> node)
    {
      this.description = description;
      this.node = node;
    }

    public String getDescription()
    {
      return description;
    }

    public Node getNode()
    {
      return node.get();
    }
  }

  public NewPanelFxDialog()
  {
  }

  public void show()
  {
    Dialog<?> dialog;

    dialog = new Dialog<>();
    dialog.setTitle("Choose files");
    dialog.initStyle(StageStyle.UTILITY);
    dialog.getDialogPane().setContent(getTabPane());
    dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonData.OK_DONE));
    dialog.getDialogPane().getButtonTypes().add(new ButtonType("Cancel", ButtonData.CANCEL_CLOSE));

    dialog.showAndWait();
  }

  private TabPane getTabPane()
  {
    TabPane tabPane;

    tabPane = new TabPane();
    Stream.of(Function.values()).forEach(f -> {
      Tab tab;

      tab = new Tab(f.getDescription());
      tab.setContent(f.getNode());
      tabPane.getTabs().add(tab);
    });

    new TabPanePreference("NewPanelTabPane", tabPane);

    return tabPane;
  }

  static class FileComparisonNode
    extends MigPane
  {
    private ComboBox<String> leftFileComboBox;
    private ComboBox<String> rightFileComboBox;
    private ComboBox<String> filterComboBox;

    FileComparisonNode()
    {
      super("fill", "[p][fill, grow][p]", "[p][p][p][fill, grow]");
      init();
    }

    private void init()
    {
      Button leftFileBrowseButton;
      Button rightFileBrowseButton;

      leftFileBrowseButton = new Button("Browse");
      leftFileBrowseButton.setOnAction((e) -> setOnAction(leftFileComboBox));
      leftFileComboBox = new ComboBox<>();
      leftFileComboBox.setEditable(false);
      new ComboBoxPreference("LeftFile", leftFileComboBox);

      rightFileBrowseButton = new Button("Browse");
      rightFileBrowseButton.setOnAction((e) -> setOnAction(rightFileComboBox));
      rightFileComboBox = new ComboBox<>();
      rightFileComboBox.setEditable(false);
      new ComboBoxPreference("RightFile", rightFileComboBox);

      filterComboBox = new ComboBox<>();
      filterComboBox.getItems().addAll(JMeldSettingsFx.getInstance().getFilter().getFilters().stream()
          .map(filter -> filter.getName()).collect(Collectors.toList()));
      new ComboBoxSelectionPreference("Filter", filterComboBox);

      add(new Label("Left"), new CC());
      add(leftFileComboBox, new CC());
      add(leftFileBrowseButton, new CC().wrap());

      add(new Label("Right"), new CC());
      add(rightFileComboBox, new CC());
      add(rightFileBrowseButton, new CC().wrap());

      add(new Label("Filter"), new CC());
      add(filterComboBox, new CC().wrap());
    }

    private void setOnAction(ComboBox<String> comboBox)
    {
      FileChooser fileChooser;
      File selectedFile;

      fileChooser = new FileChooser();
      selectedFile = fileChooser.showOpenDialog(null);
      if (selectedFile != null)
      {
        int index;

        index = comboBox.getItems().indexOf(selectedFile.getAbsolutePath());
        if (index < 0)
        {
          index = 0;
          comboBox.getItems().add(index, selectedFile.getAbsolutePath());
        }
        comboBox.getSelectionModel().select(index);
      }
    }
  }

  static class DirectoryComparisonNode
    extends MigPane
  {
    private ComboBox<String> leftDirectoryComboBox;
    private ComboBox<String> rightDirectoryComboBox;

    DirectoryComparisonNode()
    {
      super("fill", "[p][fill, grow][p]", "[p][p][fill, grow]");
      init();
    }

    private void init()
    {
      Button leftDirectoryBrowseButton;
      Button rightDirectoryBrowseButton;

      leftDirectoryBrowseButton = new Button("Browse");
      leftDirectoryBrowseButton.setOnAction((e) -> setOnAction(leftDirectoryComboBox));
      leftDirectoryComboBox = new ComboBox<>();
      leftDirectoryComboBox.setEditable(false);
      new ComboBoxPreference("LeftDirectory", leftDirectoryComboBox);

      rightDirectoryBrowseButton = new Button("Browse");
      rightDirectoryBrowseButton.setOnAction((e) -> setOnAction(rightDirectoryComboBox));
      rightDirectoryComboBox = new ComboBox<>();
      rightDirectoryComboBox.setEditable(false);
      new ComboBoxPreference("RightDirectory", rightDirectoryComboBox);

      add(new Label("Left"), new CC());
      add(leftDirectoryComboBox, new CC());
      add(leftDirectoryBrowseButton, new CC().wrap());

      add(new Label("Right"), new CC());
      add(rightDirectoryComboBox, new CC());
      add(rightDirectoryBrowseButton, new CC().wrap());
    }

    private void setOnAction(ComboBox<String> comboBox)
    {
      DirectoryChooser fileChooser;
      File selectedDirectory;

      fileChooser = new DirectoryChooser();
      selectedDirectory = fileChooser.showDialog(null);
      if (selectedDirectory != null)
      {
        int index;

        index = comboBox.getItems().indexOf(selectedDirectory.getAbsolutePath());
        if (index < 0)
        {
          index = 0;
          comboBox.getItems().add(index, selectedDirectory.getAbsolutePath());
        }
        comboBox.getSelectionModel().select(index);
      }
    }

  }
}