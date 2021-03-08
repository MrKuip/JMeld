package org.jmeld.fx.ui.settings;

import static org.jmeld.fx.util.FxCss.header1;
import static org.jmeld.fx.util.FxCss.header2;

import org.jmeld.fx.settings.FilterSettingsFx;
import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.util.FxUtils;
import org.jmeld.settings.util.Filter;
import org.jmeld.settings.util.FilterRule;
import org.jmeld.ui.util.Icons;
import org.tbee.javafx.scene.layout.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class FilterSettingsPanel
    extends MigPane
    implements SettingsPanelIF
{
  public FilterSettingsPanel()
  {
    super(new LC().fill());

    init();
  }

  @Override
  public String getText()
  {
    return "Filter";
  }

  @Override
  public Node getIcon()
  {
    return FxUtils.getIcon(Icons.FILTER.getSmallerIcon());
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

    gap1 = "30";
    gap2 = "10";

    panel = new MigPane(new LC(), new AC().index(0).grow().fill().index(1).fill());

    add(header1(new Text("Filter settings")), new CC().dockNorth().wrap().span().gapLeft("10"));
    add(panel, "grow");

    // Creation:
    filterTable = new TableView<>();
    filterNewButton = new Button("New");
    filterDeleteButton = new Button("Delete");
    filterRuleTable = new TableView<>();
    filterRuleNewButton = new Button("New");
    filterRuleDeleteButton = new Button("Delete");

    // Initialization:
    filterNewButton.setAlignment(Pos.BASELINE_LEFT);
    filterNewButton.setGraphic(FxUtils.getIcon(Icons.NEW.getSmallIcon()));
    filterDeleteButton.setAlignment(Pos.BASELINE_LEFT);
    filterDeleteButton.setGraphic(FxUtils.getIcon(Icons.DELETE.getSmallIcon()));
    filterRuleNewButton.setAlignment(Pos.BASELINE_LEFT);
    filterRuleNewButton.setGraphic(FxUtils.getIcon(Icons.NEW.getSmallIcon()));
    filterRuleDeleteButton.setAlignment(Pos.BASELINE_LEFT);
    filterRuleDeleteButton.setGraphic(FxUtils.getIcon(Icons.DELETE.getSmallIcon()));
    
    TableColumn<Filter, String> column1 = new TableColumn<>("Name");
    column1.setCellValueFactory(new PropertyValueFactory<>("name"));
    filterTable.getColumns().add(column1);
   
    TableColumn<FilterRule, String> column2 = new TableColumn<>("Description");
    column2.setCellValueFactory(new PropertyValueFactory<>("description"));
    filterRuleTable.getColumns().add(column2);
    column2 = new TableColumn<>("Pattern");
    column2.setCellValueFactory(new PropertyValueFactory<>("pattern"));
    filterRuleTable.getColumns().add(column2);
    
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
    panel.add(new Region(), new CC().wrap());
    
    // Binding:
    filterTable.itemsProperty().bind(getSettings().filters);
	
    filterTable.getSelectionModel().selectedItemProperty().addListener((obs, deselectedFilter, selectedFilter) -> {
        if (selectedFilter != null) {
          filterRuleTable.itemsProperty().bind(selectedFilter.rules);
        }
    });
    
  }
  
  private void init2()
  {
    MigPane panel;
    TableView filterTable;
    TableView filterRuleTable;
    Button filterNewButton;
    Button filterDeleteButton;
    Button filterRuleNewButton;
    Button filterRuleDeleteButton;
    String gap1;
    String gap2;

    gap1 = "30";
    gap2 = "10";

    panel = new MigPane(new LC().fill());

    add(header1(new Text("Filter settings")), new CC().dockNorth().wrap().span().gapLeft("10"));
    add(panel, new CC().grow());

    // Creation:
    filterTable = new TableView();
    filterNewButton = new Button("New");
    filterDeleteButton = new Button("Delete");
    filterRuleTable = new TableView();
    filterRuleNewButton = new Button("New");
    filterRuleDeleteButton = new Button("Delete");

    // Layout:
    panel.add(header2(new Label("Filters:")), new CC().wrap().gapLeft(gap2).gapTop("10").span());
    panel.add(filterTable, new CC().gapLeft(gap2).spanY(3).height("100%").width("100%"));
    panel.add(filterNewButton, new CC().wrap().minWidth("pref").endGroupX("lala"));
    panel.add(filterDeleteButton, new CC().wrap().minWidth("pref").endGroupX("lala"));
   
    /*
    panel.add(header2(new Label("Filterrules for:")), new CC().wrap().gapLeft(gap2).gapTop("10").span());
    panel.add(filterRuleTable, new CC().gapLeft(gap1).spanY(3).growX().growY());
    panel.add(filterRuleNewButton, new CC().wrap());
    panel.add(filterRuleDeleteButton, new CC().wrap());
    panel.add(new Label(""), new CC().gapLeft(gap1).wrap());
    */

  }

  private FilterSettingsFx getSettings()
  {
    return JMeldSettingsFx.getInstance().getFilter();
  }
}
