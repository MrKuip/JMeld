/*
   JMeld is a visual diff and merge tool.
   Copyright (C) 2007  Kees Kuip
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA  02110-1301  USA
 */
package org.jmeld.fx.util;

import javafx.scene.paint.Color;

public class FxColors
{
  public static final Color ADDED = Color.rgb(180, 255, 180);
  public static final Color CHANGED = Color.rgb(160, 200, 255);
  public static final Color CHANGED_LIGHTER = getChangedLighterColor(CHANGED);
  public static final Color DELETED = Color.rgb(255, 160, 180);
  public static final Color DND_SELECTED_NEW = Color.rgb(13, 143, 13);
  public static final Color DND_SELECTED_USED = Color.rgb(238, 214, 128);

  public static Color getChangedLighterColor(Color changedColor)
  {
    Color c;

    c = changedColor;
    c = FxColorUtil.brighter(c);
    c = FxColorUtil.brighter(c);
    c = FxColorUtil.lighter(c);
    c = FxColorUtil.lighter(c);

    return c;
  }

  /**
   * Get a highlighter that will match the current l&f.
   */
  public static Color getTableRowHighLighterColor()
  {
    Color color;

    color = getSelectionColor();
    color = FxColorUtil.setSaturation(color, 0.05f);
    color = FxColorUtil.setBrightness(color, 1.00f);

    return color;
  }

  public static Color getDarkLookAndFeelColor()
  {
    Color color;

    color = getSelectionColor();
    color = FxColorUtil.setBrightness(color, 0.40f);

    return color;
  }

  public static Color getSelectionColor()
  {
    return Color.AZURE;
  }

  public static Color getPanelBackground()
  {
    return Color.GRAY;
  }
}
