package org.jmeld.fx.ui.settings;

import static org.jmeld.fx.util.FxCss.header1;
import static org.jmeld.fx.util.FxCss.header2;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.jmeld.fx.settings.FilterSettingsFx;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.settings.util.Filter;
import org.jmeld.settings.util.FilterRule;
import org.tbee.javafx.scene.layout.MigPane;

public class FilterSettingsPane
    extends MigPane
    implements SettingsPaneIF
{
  public FilterSettingsPane()
  {
    super(new LC().fill(),
          new AC().fill().grow(),
          new AC().fill().grow());

    init();
  }

  @Override
  public String getText()
  {
    return "Filter";
  }

  @Override
  public Image getImage()
  {
    return FxIcon.FILTER.getLargeImage();
  }

  private void init()
  {
    MigPane panel;
    TableView<Filter> filterTable;
    TableView<FilterRule> filterRuleTable;
    Button filterNewButton;
    Button filterDeleteButton;
    Button filterRuleNewButton;
    Button filterRuleDeleteButton;
    String gap1;
    String gap2;
    TableColumn<Filter, String> nameColumn;
    TableColumn<FilterRule, Boolean> activeColumn;
    TableColumn<FilterRule, String> descriptionColumn;
    TableColumn<FilterRule, FilterRule.Rule> ruleColumn;
    TableColumn<FilterRule, String> patternColumn;

    gap1 = "30";
    gap2 = "10";

    panel = new MigPane(new LC(),
                        new AC().index(0).grow().fill().index(1).fill());

    add(header1(new Text("Filter settings")), new CC().dockNorth().wrap().span().gapLeft("10"));
    add(panel, new CC().grow());

    // Creation:
    filterTable = new TableView<>();
    filterNewButton = new Button("New");
    filterDeleteButton = new Button("Delete");
    filterRuleTable = new TableView<>();
    filterRuleNewButton = new Button("New");
    filterRuleDeleteButton = new Button("Delete");

    // Initialization:
    filterNewButton.setAlignment(Pos.BASELINE_LEFT);
    filterNewButton.setGraphic(new ImageView(FxIcon.NEW.getSmallImage()));
    filterDeleteButton.setAlignment(Pos.BASELINE_LEFT);
    filterDeleteButton.setGraphic(new ImageView(FxIcon.DELETE.getSmallImage()));
    filterRuleNewButton.setAlignment(Pos.BASELINE_LEFT);
    filterRuleNewButton.setGraphic(new ImageView(FxIcon.NEW.getSmallImage()));
    filterRuleDeleteButton.setAlignment(Pos.BASELINE_LEFT);
    filterRuleDeleteButton.setGraphic(new ImageView(FxIcon.DELETE.getSmallImage()));

    nameColumn = new TableColumn<>("Name");
    nameColumn.setMinWidth(200);
    nameColumn.setCellValueFactory(field -> field.getValue().name);
    filterTable.getColumns().add(nameColumn);

    filterNewButton.setOnAction((ae) -> filterTable.itemsProperty().get().add(new Filter("haha")));
    filterDeleteButton.setOnAction((ae) -> filterTable.itemsProperty().get().remove(
        filterTable.getSelectionModel().selectedItemProperty().get()));

    filterRuleTable.setEditable(true);

    activeColumn = new TableColumn<>("Active");
    activeColumn.setCellValueFactory(f -> f.getValue().active);
    activeColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
    filterRuleTable.getColumns().add(activeColumn);

    descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(f -> f.getValue().description);
    descriptionColumn.setMinWidth(200);
    filterRuleTable.getColumns().add(descriptionColumn);
    descriptionColumn = new TableColumn<>("Rule");

    ruleColumn = new TableColumn<>("Rule");
    ruleColumn.setMinWidth(150);
    ruleColumn.setCellValueFactory(f -> f.getValue().rule);
    ruleColumn.setCellFactory(tc -> new ComboBoxTableCell<>(FilterRule.Rule.values()));
    filterRuleTable.getColumns().add(ruleColumn);

    patternColumn = new TableColumn<>("Pattern");
    patternColumn.setMinWidth(200);
    descriptionColumn.setCellValueFactory(f -> f.getValue().pattern);
    filterRuleTable.getColumns().add(patternColumn);

    filterRuleNewButton.setOnAction((ae) -> filterRuleTable.itemsProperty().get().add(new FilterRule()));
    filterRuleDeleteButton.setOnAction((ae) -> filterRuleTable.itemsProperty().get().remove(
        filterRuleTable.getSelectionModel().selectedItemProperty().get()));

    // Layout:
    panel.add(header2(new Label("Filters:")), new CC().wrap().gapLeft(gap2).gapTop("10").span());
    panel.add(filterTable, new CC().gapLeft(gap2).spanY(3).grow());
    panel.add(filterNewButton, new CC().wrap());
    panel.add(filterDeleteButton, new CC().wrap());
    panel.add(new Region(), new CC().wrap());

    panel.add(header2(new Label("Filterrules for:")), new CC().wrap().gapLeft(gap2).gapTop("10").span());
    panel.add(filterRuleTable, new CC().gapLeft(gap2).spanY(3).grow());
    panel.add(filterRuleNewButton, new CC().wrap());
    panel.add(filterRuleDeleteButton, new CC().wrap());
    // Do not wrap last component! otherwise a gap is added
    panel.add(new Region(), new CC());

    // Binding:
    filterTable.itemsProperty().bind(getSettings().filters);

    filterTable.getSelectionModel().selectedItemProperty().addListener((obs, deselectedFilter, selectedFilter) -> {
      if (selectedFilter != null)
      {
        filterRuleTable.itemsProperty().bind(selectedFilter.rules);
      }
    });

  }

  private FilterSettingsFx getSettings()
  {
    return JMeldSettingsFx.getInstance().getFilter();
  }
}
