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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public enum Images
{
 NEW("F0224"),
 DELETE("F09E7"),
 SAVE("F0818"),
 UNDO("F054C"),
 REDO("F044E"),
 HELP("F0206"),
 SETTINGS("F1064"),
 REFRESH("F0450"),
 LEFT("F004D"),
 RIGHT("F0054"),
 COMPARE("F1492"),
 ABOUT("F02FC");

  static private Map<String, ImageIcon> m_iconImageMap = new HashMap<>();

  public enum IconSize
  {
   SMALL(20),
   LARGE(32);

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
   DEFAULT(new Color(0, 74, 131)),
   RED(Color.RED);

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

  private final String m_codepoint;
  private final IconColor m_color;

  Images(String codepoint)
  {
    this(codepoint, IconColor.DEFAULT);
  }

  Images(String codepoint, IconColor color)
  {
    m_codepoint = codepoint;
    m_color = color;
  }

  public ImageIcon getSmallIcon()
  {
    return getIcon(m_color, IconSize.SMALL);
  }

  public ImageIcon getLargeIcon()
  {
    return getIcon(m_color, IconSize.LARGE);
  }

  public ImageIcon getIcon(IconColor iconColor, IconSize iconSize)
  {
    JLabel label;
    BufferedImage image;
    Graphics2D g2d;
    int codepoint;
    int size;
    Color color;
    ImageIcon icon;
    String key;

    color = iconColor.getColor();
    size = iconSize.getSize();

    key = name() + "-" + color.getRGB() + "-" + size;
    icon = m_iconImageMap.get(key);
    if (icon != null)
    {
      return icon;
    }

    codepoint = Integer.parseInt(m_codepoint, 16);
    if (codepoint == 0)
    {
      codepoint = 0xfc8c; // a solid filled square to indicate that the icon is not yetdefined.
      color = Color.RED;
    }

    label = new JLabel(new String(Character.toChars(Integer.parseInt(m_codepoint, 16))));

    label.setForeground(color);
    label.setFont(FontUtil.getIconFont(size));
    label.setSize(size, size);

    image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    g2d = image.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    label.print(g2d);
    g2d.dispose();

    icon = new ImageIcon(image);
    m_iconImageMap.put(key, icon);

    return icon;
  }
}
