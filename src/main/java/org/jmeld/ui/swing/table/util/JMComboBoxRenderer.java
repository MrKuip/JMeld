package org.jmeld.ui.swing.table.util;

import java.awt.Component;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JMComboBoxRenderer
    extends JComboBox
    implements TableCellRenderer
{
  public JMComboBoxRenderer(Object[] items)
  {
    super(items);
  }

  public JMComboBoxRenderer(List items)
  {
    this(items.toArray());
  }

  public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column)
  {
    if (isSelected)
    {
      setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    }
    else
    {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }

    // Select the current value
    setSelectedItem(value);
    return this;
  }
}
