package org.jmeld.ui.swing;

import java.awt.Font;
import javax.swing.JLabel;

public class DetailHeader
    extends JLabel
{
  public DetailHeader()
  {
    setFont(getFont().deriveFont(Font.BOLD));
  }
}
