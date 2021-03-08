package org.jmeld.ui.settings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class SettingCellRenderer
    extends JLabel
    implements ListCellRenderer<Settings>
{
  public SettingCellRenderer()
  {
    setOpaque(true);
    setBackground(Color.white);
    setForeground(Color.black);
    setHorizontalAlignment(JLabel.CENTER);
    setVerticalAlignment(JLabel.CENTER);
    setVerticalTextPosition(JLabel.BOTTOM);
    setHorizontalTextPosition(JLabel.CENTER);
    setBorder(BorderFactory.createEmptyBorder(10,
                                              10,
                                              10,
                                              10));
    setPreferredSize(new Dimension(70,
                                   70));
  }

  public Component getListCellRendererComponent(JList<? extends Settings> list,
      Settings value,
      int index,
      boolean isSelected,
      boolean cellHasFocus)
  {
    Settings settings;

    settings = (Settings) value;

    setText(settings.getName());
    setIcon(settings.getIcon().getLargeIcon());

    if (isSelected)
    {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
    }
    else
    {
      setBackground(Color.white);
      setForeground(Color.black);
    }

    setEnabled(list.isEnabled());
    setFont(list.getFont());

    return this;
  }
}
