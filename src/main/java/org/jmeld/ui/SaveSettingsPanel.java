/*
 * SaveSettingsPanel.java
 *
 * Created on September 14, 2007, 6:32 PM
 */

package org.jmeld.ui;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author kees
 */
public class SaveSettingsPanel
  extends JPanel
{
  public SaveSettingsPanel()
  {
    init();
  }

  private void init()
  {
    JLabel label;
    Font font;

    setLayout(new MigLayout());

    label = new JLabel("Settings have changed");
    font = label.getFont().deriveFont(Font.BOLD);
    label.setFont(font);
    add(label, "wrap");

    label = new JLabel("Would you like to save the settings?");
    label.setFont(font);
    add(label, "wrap");
  }
}
