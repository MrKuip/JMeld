package org.jmeld.ui.swing.table.util;

import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class JMComboBoxEditor
    extends DefaultCellEditor
{
  public JMComboBoxEditor(Object[] items)
  {
    super(new JComboBox(items));
  }

  public JMComboBoxEditor(List items)
  {
    this(items.toArray());
  }
}
