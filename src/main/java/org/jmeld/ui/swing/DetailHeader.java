package org.jmeld.ui.swing;

import java.awt.Font;
import javax.swing.JLabel;

public class DetailHeader
  extends JLabel
{
  public DetailHeader()
  {
    this("");
  }

  public DetailHeader(String text)
  {
    setFont(getFont().deriveFont(Font.BOLD));
  }
}
