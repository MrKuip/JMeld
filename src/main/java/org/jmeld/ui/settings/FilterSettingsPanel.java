/*
 * FilterPreferencePanel.java
 *
 * Created on January 10, 2007, 6:31 PM
 */
package org.jmeld.ui.settings;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jmeld.settings.FilterSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.settings.util.Filter;
import org.jmeld.settings.util.FilterRule;
import org.jmeld.ui.swing.table.JMTable;
import org.jmeld.ui.swing.table.JMTableModel;
import org.jmeld.ui.swing.table.util.JMComboBoxEditor;
import org.jmeld.ui.swing.table.util.JMComboBoxRenderer;
import org.jmeld.util.conf.ConfigurationListenerIF;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author kees
 */
public class FilterSettingsPanel
  extends JPanel
    implements ConfigurationListenerIF
{
  private JMTableModel filterTableModel;
  private JMTableModel filterRuleTableModel;
  private JMTable filterTable;
  private JMTable filterRuleTable;
  private JLabel filterNameLabel;
  private JButton filterNewButton;
  private JButton filterDeleteButton;
  private JButton filterRuleNewButton;
  private JButton filterRuleDeleteButton;

  public FilterSettingsPanel()
  {
    setLayout(new MigLayout(new LC().fill(), new AC().fill().grow(), new AC().fill().grow()));

    initConfiguration();
    init();

    JMeldSettings.getInstance().addConfigurationListener(this);
  }

  private void init()
  {
    ListSelectionModel selectionModel;
    JPanel panel;
    String gap2;

    gap2 = "10";

    filterTable = new JMTable();
    filterRuleTable = new JMTable();
    filterNameLabel = new JLabel();
    filterNameLabel.setFont(filterNameLabel.getFont().deriveFont(Font.BOLD));
    filterNewButton = new JButton("New");
    filterDeleteButton = new JButton("Delete");
    filterRuleNewButton = new JButton("New");
    filterRuleDeleteButton = new JButton("Delete");

    panel = new JPanel(new MigLayout(new LC(), new AC().index(0).grow().fill().index(1).fill()));

    add(SettingsPanel.header1("Filter settings"), new CC().dockNorth().wrap().span().gapLeft("10"));
    add(panel, new CC().grow());

    panel.add(SettingsPanel.header2(new JLabel("Filters:")), new CC().wrap().gapLeft(gap2).gapTop("10").span());
    panel.add(new JScrollPane(filterTable), new CC().gapLeft(gap2).spanY(3).grow());
    panel.add(filterNewButton, new CC().wrap());
    panel.add(filterDeleteButton, new CC().wrap());
    panel.add(new JPanel(), new CC().wrap());

    panel.add(filterNameLabel, new CC().wrap().gapLeft(gap2).gapTop("10").span());
    panel.add(new JScrollPane(filterRuleTable), new CC().gapLeft(gap2).spanY(3).grow());
    panel.add(filterRuleNewButton, new CC().wrap());
    panel.add(filterRuleDeleteButton, new CC().wrap());
    // Do not wrap last component! otherwise a gap is added
    panel.add(new JPanel(), new CC());

    filterTableModel = getFilterTableModel();
    filterTable.setModel(filterTableModel);

    filterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    selectionModel = filterTable.getSelectionModel();
    selectionModel.addListSelectionListener(getFilterSelectionAction());

    filterRuleTableModel = getFilterRuleTableModel(0);
    filterRuleTable.setModel(filterRuleTableModel);
    filterRuleTable.setDefaultEditor(Filter.class, new JMComboBoxEditor(getFilters()));
    filterRuleTable.setDefaultRenderer(Filter.class, new JMComboBoxRenderer(getFilters()));
    filterRuleTable.setDefaultEditor(FilterRule.Rule.class, new JMComboBoxEditor(FilterRule.Rule.values()));
    filterRuleTable.setDefaultRenderer(FilterRule.Rule.class, new JMComboBoxRenderer(FilterRule.Rule.values()));
    filterRuleTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    filterNewButton.addActionListener(getNewFilterAction());
    filterDeleteButton.addActionListener(getDeleteFilterAction());
    filterRuleNewButton.addActionListener(getNewFilterRuleAction());
    filterRuleDeleteButton.addActionListener(getDeleteFilterRuleAction());

    if (filterTable.getRowCount() > 0)
    {
      filterTable.addRowSelectionInterval(0, 0);
    }
  }

  private JMTableModel getFilterTableModel()
  {
    return new FilterTableModel();
  }

  private JMTableModel getFilterRuleTableModel(int filterIndex)
  {
    return new FilterRuleTableModel(filterIndex);
  }

  private ListSelectionListener getFilterSelectionAction()
  {
    return new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        int rowIndex;
        Object value;

        if (e.getValueIsAdjusting())
        {
          return;
        }

        rowIndex = filterTable.getSelectedRow();
        value = filterTableModel.getValueAt(rowIndex, 0);

        filterNameLabel.setText("Filterrules for: " + value.toString());
        filterRuleTableModel = getFilterRuleTableModel(rowIndex);
        filterRuleTable.setModel(filterRuleTableModel);
        filterRuleTable.doLayout();
      }
    };
  }

  private ActionListener getNewFilterAction()
  {
    return (e) -> {
      getFilterSettings().addFilter(new Filter("Untitled"));
      filterTableModel.fireTableDataChanged();
    };
  }

  private ActionListener getDeleteFilterAction()
  {
    return (e) -> {
      getFilterSettings().removeFilter(getSelectedFilter());
      filterTableModel.fireTableDataChanged();
    };
  }

  private ActionListener getNewFilterRuleAction()
  {
    return (e) -> {
      Filter filter;
      FilterRule newRule;
      FilterRule selectedFilterRule;

      filter = getSelectedFilter();
      if (filter == null)
      {
        return;
      }

      newRule = new FilterRule("Untitled", FilterRule.Rule.excludes, "", true);

      selectedFilterRule = getSelectedFilterRule();
      if (selectedFilterRule != null)
      {
        newRule.setDescription(selectedFilterRule.getDescription());
        newRule.setRule(selectedFilterRule.getRule());
        filter.insertRule(selectedFilterRule, newRule);
      }
      else
      {
        filter.addRule(newRule);
      }

      filterRuleTableModel.fireTableDataChanged();
    };
  }

  private ActionListener getDeleteFilterRuleAction()
  {
    return (e) -> {
      Filter filter;
      FilterRule rule;

      filter = getSelectedFilter();
      if (filter == null)
      {
        return;
      }

      rule = getSelectedFilterRule();
      if (rule == null)
      {
        return;
      }

      filter.removeRule(rule);
      filterRuleTableModel.fireTableDataChanged();
    };
  }

  @Override
  public void configurationChanged()
  {
    initConfiguration();
  }

  private void initConfiguration()
  {
  }

  private class FilterTableModel
    extends JMTableModel
  {
    Column nameColumn;

    FilterTableModel()
    {
      nameColumn = addColumn("name", null, "Name", String.class, -1, true);
    }

    @Override
    public int getRowCount()
    {
      return getFilterSettings().getFilters().size();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, Column column)
    {
      Filter filter;

      filter = getFilter(rowIndex);
      if (filter != null)
      {
        if (column == nameColumn)
        {
          filter.setName((String) value);
        }
      }
    }

    @Override
    public Object getValueAt(int rowIndex, Column column)
    {
      Filter filter;

      filter = getFilter(rowIndex);
      if (filter != null)
      {
        if (column == nameColumn)
        {
          return filter.getName();
        }
      }

      return "";
    }

    private Filter getFilter(int rowIndex)
    {
      return getFilters().get(rowIndex);
    }
  }

  private class FilterRuleTableModel
    extends JMTableModel
  {
    private int filterIndex;
    private Column activeColumn;
    private Column descriptionColumn;
    private Column ruleColumn;
    private Column patternColumn;

    public FilterRuleTableModel(int filterIndex)
    {
      this.filterIndex = filterIndex;

      init();
    }

    private void init()
    {
      activeColumn = addColumn("active", null, "Active", Boolean.class, 5, true);
      descriptionColumn = addColumn("description", null, "Description", String.class, 15, true);
      ruleColumn = addColumn("rule", null, "Rule", FilterRule.Rule.class, 10, true);
      patternColumn = addColumn("pattern", null, "Pattern", String.class, -1, true);
    }

    @Override
    public int getRowCount()
    {
      return getRules(filterIndex).size();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, Column column)
    {
      FilterRule rule;

      rule = getRule(rowIndex);
      if (rule != null)
      {
        if (column == activeColumn)
        {
          rule.setActive((Boolean) value);
        }

        if (column == descriptionColumn)
        {
          rule.setDescription((String) value);
        }

        if (column == ruleColumn)
        {
          rule.setRule((FilterRule.Rule) value);
          fireTableCellUpdated(rowIndex, column.getColumnIndex());
        }

        if (column == patternColumn)
        {
          if (value instanceof Filter)
          {
            value = ((Filter) value).getName();
          }

          rule.setPattern((String) value);
        }
      }
    }

    @Override
    public Object getValueAt(int rowIndex, Column column)
    {
      FilterRule rule;

      rule = getRule(rowIndex);
      if (rule != null)
      {
        if (column == activeColumn)
        {
          return rule.isActive();
        }

        if (column == descriptionColumn)
        {
          return rule.getDescription();
        }

        if (column == ruleColumn)
        {
          return rule.getRule();
        }

        if (column == patternColumn)
        {
          if (rule.getRule() == FilterRule.Rule.importFilter)
          {
            return getFilterSettings().getFilter(rule.getPattern());
          }

          return rule.getPattern();
        }
      }

      return "??";
    }

    @Override
    public Class getColumnClass(int rowIndex, Column column)
    {
      FilterRule rule;

      if (column == patternColumn)
      {
        rule = getRule(rowIndex);
        if (rule != null && rule.getRule() == FilterRule.Rule.importFilter)
        {
          return Filter.class;
        }
      }

      return null;
    }

    private FilterRule getRule(int rowIndex)
    {
      return getRules(filterIndex).get(rowIndex);
    }
  }

  private Filter getSelectedFilter()
  {
    int rowIndex;

    rowIndex = filterTable.getSelectedRow();
    if (rowIndex < 0 || rowIndex > getFilters().size())
    {
      return null;
    }

    return getFilters().get(rowIndex);
  }

  private FilterRule getSelectedFilterRule()
  {
    Filter filter;
    int rowIndex;

    filter = getSelectedFilter();
    if (filter == null)
    {
      return null;
    }

    rowIndex = filterRuleTable.getSelectedRow();
    if (rowIndex < 0 || rowIndex > filter.getRules().size())
    {
      return null;
    }

    return filter.getRules().get(rowIndex);
  }

  private List<FilterRule> getRules(int filterIndex)
  {
    int size;

    size = getFilters().size();
    if (filterIndex < 0 || filterIndex >= size)
    {
      return Collections.emptyList();
    }

    return getFilters().get(filterIndex).getRules();
  }

  private List<Filter> getFilters()
  {
    return getFilterSettings().getFilters();
  }

  private FilterSettings getFilterSettings()
  {
    return JMeldSettings.getInstance().getFilter();
  }
}
