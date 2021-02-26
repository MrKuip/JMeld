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
package org.jmeld.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import org.jmeld.ui.util.IconComposer.Location;

public enum Icons
{
  NEW("F0224", "F0214"),
  DELETE("F09E7", "F01B4"),
  SAVE("F0818", "F0193"),
  SAVE_AS("F0E28", "F0E27"),
  RELOAD("F0453"),
  UNDO("F054C"),
  REDO("F044E"),
  HELP("F02D7", "F0625"),
  SETTINGS("F1064"),
  REFRESH("F0450"),
  LEFT("F004D"),
  RIGHT("F0054"),
  COMPARE("F1492"),
  EDIT("F11E8", "F11E7"),
  FILTER("F0233", "F0232"),
  FOLDER("F0256", "F024B"),
  EXPAND_ALL("F1143", "F0334"),
  COLLAPSE_ALL("F1142", "F1141"),
  ABOUT("F02FC", "F02FD"),
  ALERT("F002A", IconColor.RED, null, null),
  CLOSE("F0156"),
  SEARCH_NEXT("F0140"),
  SEARCH_PREVIOUS("F0143"),
  FILE_EXIST_NOTEQUAL("F08A1", IconColor.DEFAULT_OUTLINE, "F0764", IconColor.LIGHT_BLUE_FILL),
  FILE_EXIST_EQUAL("F08A1", IconColor.DEFAULT_OUTLINE, "F0764", IconColor.WHITE),
  FILE_NOT_EXIST("F08A4", IconColor.DEFAULT_OUTLINE, "F0764", IconColor.WHITE),
  PANEL_SELECTED("F08A1", IconColor.DEFAULT_OUTLINE, "F0764", IconColor.YELLOW);

  static private Map<String, Icon> m_iconImageMap = new HashMap<>();
  static final public Icon ONLY_RIGHT;
  static final public Icon LEFT_RIGHT_CHANGED;
  static final public Icon ONLY_LEFT;
  static final public Icon LEFT_RIGHT_UNCHANGED;

  static
  {
    IconComposer ic;

    ic = new IconComposer(Icons.FILE_NOT_EXIST.getSmallIcon());
    ic = ic.decorate(Location.RIGHT,
                     Icons.FILE_EXIST_NOTEQUAL.getSmallIcon());
    ONLY_RIGHT = ImageUtil.createImageIcon(ic);

    ic = new IconComposer(Icons.FILE_EXIST_NOTEQUAL.getSmallIcon());
    ic = ic.decorate(Location.RIGHT,
                     Icons.FILE_EXIST_NOTEQUAL.getSmallIcon());
    LEFT_RIGHT_CHANGED = ic;

    ic = new IconComposer(Icons.FILE_EXIST_NOTEQUAL.getSmallIcon());
    ic = ic.decorate(Location.RIGHT,
                     Icons.FILE_NOT_EXIST.getSmallIcon());
    ONLY_LEFT = ic;

    ic = new IconComposer(Icons.FILE_EXIST_EQUAL.getSmallIcon());
    ic = ic.decorate(Location.RIGHT,
                     Icons.FILE_EXIST_EQUAL.getSmallIcon());
    LEFT_RIGHT_UNCHANGED = ic;
  }

  public enum IconSize
  {
    VERY_SMALL(12),
    SMALLER(18),
    SMALL(24),
    LARGE(32),
    VERY_LARGE(64);

    private final int m_size;

    IconSize(int size)
    {
      m_size = size;
    }

    public int getSize()
    {
      return m_size;
    }
  }

  public enum IconColor
  {
    DEFAULT_OUTLINE(new Color(0,
                              74,
                              131)),
    DEFAULT_FILL(Color.WHITE),
    LIGHT_BLUE_FILL(new Color(160,
                              200,
                              255)),
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
    RED(Color.RED),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW);

    private Color m_color;

    IconColor(Color color)
    {
      m_color = color;
    }

    public Color getColor()
    {
      return m_color;
    }
  }

  private final String m_outlineCodepoint;
  private final String m_fillCodepoint;
  private final IconColor m_outlineColor;
  private final IconColor m_fillColor;

  Icons(String outlineCodepoint)
  {
    this(outlineCodepoint,
         IconColor.DEFAULT_OUTLINE,
         null,
         null);
  }

  Icons(String outlineCodepoint,
      String fillCodepoint)
  {
    this(outlineCodepoint,
         IconColor.DEFAULT_OUTLINE,
         fillCodepoint,
         IconColor.DEFAULT_FILL);
  }

  Icons(String outlineCodepoint,
      IconColor outlineColor,
      String fillCodepoint,
      IconColor fillColor)
  {
    m_outlineCodepoint = outlineCodepoint;
    m_outlineColor = outlineColor;
    m_fillCodepoint = fillCodepoint;
    m_fillColor = fillColor;
  }

  public Icon getSmallerIcon()
  {
    return getIcon(IconSize.SMALLER);
  }

  public Icon getSmallIcon()
  {
    return getIcon(IconSize.SMALL);
  }

  public Icon getLargeIcon()
  {
    return getIcon(IconSize.LARGE);
  }

  public Icon getIcon(IconSize iconSize)
  {
    IconComposer icon;

    if (m_fillCodepoint == null)
    {
      return getIcon(m_outlineCodepoint,
                     m_outlineColor,
                     iconSize);
    }
    else
    {
      icon = new IconComposer(getIcon(m_fillCodepoint,
                                      m_fillColor,
                                      iconSize));
      icon.decorate(Location.CENTER,
                    new IconComposer(getIcon(m_outlineCodepoint,
                                             m_outlineColor,
                                             iconSize)));

      return ImageUtil.createImageIcon(icon);
    }
  }

  private Icon getIcon(String codepointString,
      IconColor iconColor,
      IconSize iconSize)
  {
    int size;
    Color color;
    Icon icon;
    String key;

    color = iconColor.getColor();
    size = iconSize.getSize();

    key = name() + "-" + color.getRGB() + "-" + size;
    icon = m_iconImageMap.get(key);
    if (icon != null)
    {
      return icon;
    }

    icon = ImageUtil.createImageIcon(new MyIcon(codepointString,
                                                iconColor,
                                                iconSize));

    m_iconImageMap.put(key,
                       icon);
    return icon;
  }

  public static class MyIcon
      implements Icon
  {
    private String mii_codepointString;
    private IconColor mii_iconColor;
    private IconSize mii_iconSize;

    public MyIcon(String codepointString,
        IconColor iconColor,
        IconSize iconSize)
    {
      mii_codepointString = codepointString;
      mii_iconColor = iconColor;
      mii_iconSize = iconSize;
    }

    @Override
    public int getIconWidth()
    {
      return mii_iconSize.getSize();
    }

    @Override
    public int getIconHeight()
    {
      return mii_iconSize.getSize();
    }

    @Override
    public void paintIcon(Component c,
        Graphics g,
        int x,
        int y)
    {
      int codepoint;
      String text;
      Graphics2D g2d;
      FontMetrics fm;
      double xString;
      double yString;

      codepoint = Integer.parseInt(mii_codepointString,
                                   16);
      text = new String(Character.toChars(codepoint));

      g2d = (Graphics2D) g;
      g2d.setClip(0,
                  0,
                  getIconWidth(),
                  getIconHeight());

      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                           RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                           RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2d.setColor(mii_iconColor.getColor());
      g2d.setFont(FontUtil.getIconFont(getIconHeight()));

      fm = g2d.getFontMetrics();

      xString = (x + (getIconWidth() - fm.stringWidth(text)) / 2.0);
      yString = (y + ((getIconHeight() - fm.getHeight()) / 2.0) + fm.getAscent());

      g2d.drawString(text,
                     (float) xString,
                     (float) yString);
    }
  }
}
