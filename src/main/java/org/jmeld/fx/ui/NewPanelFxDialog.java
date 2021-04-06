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
import org.jmeld.fx.util.FxIcon;
import org.jmeld.util.StringUtil;
import org.jmeld.util.node.JMDiffNode;
import org.jmeld.util.node.JMDiffNodeFactory;
import org.jmeld.util.prefs.ComboBoxPreference;
import org.jmeld.util.prefs.ComboBoxSelectionPreference;
import org.jmeld.util.prefs.TabPanePreference;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import net.miginfocom.layout.CC;

public class NewPanelFxDialog
{
  // Class variables:
  // File comparison:
  public enum Comparison
  {
    FILE_COMPARISON("File comparison", () -> new FileComparisonNode()),
    DIRECTORY_COMPARISON("Directory comparison", () -> new DirectoryComparisonNode());

    private final String description;
    private final Supplier<Node> node;

    Comparison(String description, Supplier<Node> node)
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

  public NewPanelIF getNewPanel()
  {
    Dialog<ButtonType> dialog;
    TabPane tabPane;

    tabPane = getTabPane();

    dialog = new Dialog<>();
    dialog.setTitle("Choose files");
    dialog.setGraphic(new ImageView(FxIcon.FILE_COMPARE.getLargeImage()));
    dialog.setHeaderText("Choose files/directories to compare");
    dialog.initStyle(StageStyle.UTILITY);
    dialog.getDialogPane().setContent(tabPane);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

    if (dialog.showAndWait().get() == ButtonType.OK)
    {
      return (NewPanelIF) tabPane.getSelectionModel().getSelectedItem().getContent();
    }

    return null;
  }

  public interface NewPanelIF
  {
    Node getNode();

    String getShortDescription();
  }

  private TabPane getTabPane()
  {
    TabPane tabPane;

    tabPane = new TabPane();
    Stream.of(Comparison.values()).forEach(f -> {
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
      implements NewPanelIF
  {
    private ComboBox<String> leftFileComboBox;
    private ComboBox<String> rightFileComboBox;

    FileComparisonNode()
    {
      super("fill", "[p][fill, grow][p]", "[p][p][fill, grow]");
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

      add(new Label("Left"), new CC());
      add(leftFileComboBox, new CC());
      add(leftFileBrowseButton, new CC().wrap());

      add(new Label("Right"), new CC());
      add(rightFileComboBox, new CC());
      add(rightFileBrowseButton, new CC().wrap());
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

    private File getLeftFile()
    {
      return new File(leftFileComboBox.getSelectionModel().getSelectedItem());
    }

    private File getRightFile()
    {
      return new File(rightFileComboBox.getSelectionModel().getSelectedItem());
    }

    @Override
    public Node getNode()
    {
      try
      {
        File leftFile;
        File rightFile;
        JMDiffNode diffNode;
        FileDiffPanel fileDiffPanel;

        leftFile = getLeftFile();
        rightFile = getRightFile();

        if (StringUtil.isEmpty(leftFile.getName()))
        {
          showAlert("left filename is empty");
        }

        if (!leftFile.exists())
        {
          showAlert("left filename(" + leftFile.getAbsolutePath() + ") doesn't exist");
        }

        if (StringUtil.isEmpty(rightFile.getName()))
        {
          showAlert("right filename is empty");
        }

        if (!rightFile.exists())
        {
          showAlert("right filename(" + rightFile.getAbsolutePath() + ") doesn't exist");
        }

        diffNode = JMDiffNodeFactory.create(leftFile.getAbsolutePath(), leftFile, rightFile.getAbsolutePath(),
            rightFile);

        diffNode.diff();

        fileDiffPanel = new FileDiffPanel(diffNode);

        return fileDiffPanel;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        showAlert(ex);
      }

      return null;
    }

    public String getShortDescription()
    {
      return getLeftFile().getName() + "-" + getRightFile().getName();
    }

    private void showAlert(Exception ex)
    {
    }

    private void showAlert(String string)
    {
    }
  }

  static class DirectoryComparisonNode
    extends MigPane
      implements NewPanelIF
  {
    private ComboBox<String> leftDirectoryComboBox;
    private ComboBox<String> rightDirectoryComboBox;
    private ComboBox<String> filterComboBox;

    DirectoryComparisonNode()
    {
      super("fill", "[p][fill, grow][p]", "[p][p][p][fill, grow]");
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

      filterComboBox = new ComboBox<>();
      filterComboBox.getItems().addAll(JMeldSettingsFx.getInstance().getFilter().getFilters().stream()
          .map(filter -> filter.getName()).collect(Collectors.toList()));
      new ComboBoxSelectionPreference("Filter", filterComboBox);

      add(new Label("Left"), new CC());
      add(leftDirectoryComboBox, new CC());
      add(leftDirectoryBrowseButton, new CC().wrap());

      add(new Label("Right"), new CC());
      add(rightDirectoryComboBox, new CC());
      add(rightDirectoryBrowseButton, new CC().wrap());

      add(new Label("Filter"), new CC());
      add(filterComboBox, new CC().wrap());
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

    @Override
    public Node getNode()
    {
      return new Button("Show directory diff [filter=" + filterComboBox.getSelectionModel().getSelectedItem() + "] : "
          + leftDirectoryComboBox.getSelectionModel().getSelectedItem() + " -> "
          + rightDirectoryComboBox.getSelectionModel().getSelectedItem());
    }

    private File getLeftDirectory()
    {
      return new File(leftDirectoryComboBox.getSelectionModel().getSelectedItem());
    }

    private File getRightDirectory()
    {
      return new File(rightDirectoryComboBox.getSelectionModel().getSelectedItem());
    }

    public String getShortDescription()
    {
      return getLeftDirectory().getName() + "-" + getRightDirectory().getName();
    }
  }
}