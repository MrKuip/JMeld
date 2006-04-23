package org.jmeld.ui.util;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

import javax.swing.*;
import javax.swing.plaf.*;

public class MeldBlue
       extends PlasticTheme
{
  static final ColorUIResource BLUE_MEDIUM_DARKEST;
  static final ColorUIResource BLUE_MEDIUM_MEDIUM;
  static final ColorUIResource BLUE_MEDIUM_LIGHTEST;
  static final ColorUIResource GRAY_MEDIUMDARK;
  static final ColorUIResource GRAY_LIGHT;
  static final ColorUIResource GRAY_LIGHTER;
  static final ColorUIResource YELLOW_FOCUS;

  static
  {
    BLUE_MEDIUM_DARKEST = new ColorUIResource(44, 73, 135);
    BLUE_MEDIUM_MEDIUM = new ColorUIResource(85, 115, 170);
    BLUE_MEDIUM_LIGHTEST = new ColorUIResource(172, 210, 248);
    //GRAY_MEDIUMDARK = new ColorUIResource(110, 110, 110);
    //GRAY_LIGHT = new ColorUIResource(170, 170, 170);
    //GRAY_LIGHTER = new ColorUIResource(220, 220, 220);
    GRAY_MEDIUMDARK = new ColorUIResource(140, 140, 140);
    GRAY_LIGHT = new ColorUIResource(200, 200, 200);
    GRAY_LIGHTER = new ColorUIResource(230, 230, 230);
    YELLOW_FOCUS = new ColorUIResource(255, 223, 63);
  }

  public String getName()
  {
    return "MeldBlue";
  }

  protected ColorUIResource getPrimary1()
  {
    return BLUE_MEDIUM_DARKEST;
  }

  protected ColorUIResource getPrimary2()
  {
    return BLUE_MEDIUM_MEDIUM;
  }

  protected ColorUIResource getPrimary3()
  {
    return BLUE_MEDIUM_LIGHTEST;
  }

  protected ColorUIResource getSecondary1()
  {
    return GRAY_MEDIUMDARK;
  }

  protected ColorUIResource getSecondary2()
  {
    return GRAY_LIGHT;
  }

  protected ColorUIResource getSecondary3()
  {
    return GRAY_LIGHTER;
  }

  public ColorUIResource getMenuItemSelectedBackground()
  {
    return getPrimary2();
  }

  public ColorUIResource getMenuItemSelectedForeground()
  {
    return getWhite();
  }

  public ColorUIResource getMenuSelectedBackground()
  {
    return getSecondary2();
  }

  public ColorUIResource getFocusColor()
  {
    return PlasticLookAndFeel.useHighContrastFocusColors ? YELLOW_FOCUS
                                                         : super.getFocusColor();
  }

  /*
   * TODO: The following two lines are likely an improvement.
   *       However, they require a rewrite of the PlasticInternalFrameTitlePanel.
               public    ColorUIResource getWindowTitleBackground()                 { return getPrimary1(); }
               public    ColorUIResource getWindowTitleForeground()                 { return WHITE;                 }
   */
  public void addCustomEntriesToTable(UIDefaults table)
  {
    super.addCustomEntriesToTable(table);
    Object[] uiDefaults = 
      {
        PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, new Integer(30),
      };

    table.putDefaults(uiDefaults);
  }
}
