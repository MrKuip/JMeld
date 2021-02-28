package org.jmeld.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jmeld.ui.util.Icons.IconSize;

public class IconComposer
    implements Icon
{

  private Icon m_icon;
  private List<IconData> m_iconDataList;
  private Color m_background;
  private int m_minHeight;
  private int m_minWidth;
  private int m_height;
  private int m_width;

  public enum Location
  {
    CENTER,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP,
    LEFT,
    RIGHT,
    BOTTOM
  }

  public IconComposer(Icon icon)
  {
    m_icon = icon;
    m_width = icon.getIconWidth();
    m_height = icon.getIconHeight();
  }

  public void setBackground(Color background)
  {
    m_background = background;
  }

  public void setMinHeight(int minHeight)
  {
    m_minHeight = minHeight;
  }

  public void setMinWidth(int minWidth)
  {
    m_minWidth = minWidth;
  }

  public IconComposer decorate(Location location,
      Icon icon)
  {
    int x;
    int y;
    int iconWidth;
    int iconHeight;
    IconData iconData;

    if (icon == null)
    {
      return this;
    }

    iconWidth = icon.getIconWidth();
    iconHeight = icon.getIconHeight();

    switch (location)
    {
      case CENTER:
        x = m_icon.getIconWidth() / 2 - iconWidth / 2;
        y = m_icon.getIconHeight() / 2 - iconHeight / 2;
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      case TOP_LEFT:
        x = 0;
        y = 0;
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      case TOP_RIGHT:
        x = m_icon.getIconWidth() - iconWidth;
        y = 0;
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      case BOTTOM_LEFT:
        x = 0;
        y = m_icon.getIconHeight() - iconHeight;
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      default:
      case BOTTOM_RIGHT:
        x = m_icon.getIconWidth() - iconWidth;
        y = m_icon.getIconHeight() - iconHeight;
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      case RIGHT:
        x = m_icon.getIconWidth();
        y = m_icon.getIconHeight() / 2 - iconHeight / 2;
        m_width = getIconWidth() + iconWidth;
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      case LEFT:
        x = -iconWidth;
        y = m_icon.getIconHeight() / 2 - iconHeight / 2;
        m_width = getIconWidth() + iconWidth;
        m_height = Math.max(getIconHeight(),
                            iconHeight);
        break;
      case TOP:
        x = m_icon.getIconWidth() / 2 - iconWidth / 2;
        y = -iconHeight;
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = getIconHeight() + iconHeight;
        break;
      case BOTTOM:
        x = m_icon.getIconWidth() / 2 - iconWidth / 2;
        y = m_icon.getIconHeight();
        m_width = Math.max(getIconWidth(),
                           iconWidth);
        m_height = getIconHeight() + iconHeight;
        break;
    }

    iconData = new IconData(x,
                            y,
                            icon);
    if (m_iconDataList == null)
    {
      m_iconDataList = new ArrayList<IconData>();
    }
    m_iconDataList.add(iconData);

    return this;
  }

  @Override
  public void paintIcon(Component c,
      Graphics g,
      final int x,
      final int y)
  {
    int width;
    int height;
    int x1;
    int y1;
    int lowest_x;
    int lowest_y;
    Graphics2D g2;

    g2 = (Graphics2D) g;

    g.translate(x,
                y);

    lowest_x = 0;
    lowest_y = 0;
    if (m_iconDataList != null)
    {
      for (IconData iconData : m_iconDataList)
      {
        lowest_x = iconData.mi_x < lowest_x ? iconData.mi_x : lowest_x;
        lowest_y = iconData.mi_y < lowest_y ? iconData.mi_y : lowest_y;
      }
    }

    if (m_background != null)
    {
      g2.setColor(m_background);
      g2.fillRect(0,
                  0,
                  getIconWidth(),
                  getIconHeight());
    }

    // Center the icon.
    width = m_icon.getIconWidth();
    x1 = -lowest_x;
    if (width < m_minWidth)
    {
      x1 += (m_minWidth - width) / 2;
    }

    height = m_icon.getIconHeight();
    y1 = -lowest_y;
    if (height < m_minHeight)
    {
      y1 += (m_minHeight - height) / 2;
    }

    m_icon.paintIcon(c,
                     g,
                     x1,
                     y1);
    if (m_iconDataList != null)
    {
      for (IconData iconData : m_iconDataList)
      {
        iconData.mi_icon.paintIcon(c,
                                   g,
                                   iconData.mi_x - lowest_x,
                                   iconData.mi_y - lowest_y);
      }
    }
  }

  @Override
  public int getIconWidth()
  {
    return m_width;
  }

  @Override
  public int getIconHeight()
  {
    return m_height;
  }

  class IconData
  {
    int mi_x;
    int mi_y;
    Icon mi_icon;

    IconData(int x,
        int y,
        Icon icon)
    {
      mi_x = x;
      mi_y = y;
      mi_icon = icon;
    }
  }

  public static void main(String[] args)
  {
    JPanel panel;
    JLabel label;
    IconComposer icon;
    IconComposer icon2;
    JFrame frame;

    panel = new JPanel();
    panel.setLayout(new FlowLayout());

    icon = new IconComposer(Icons.NEW.getIcon(IconSize.LARGE));
    // blender.setBackground(Color.orange);
    label = new JLabel(icon);
    panel.add(label);

    frame = new JFrame("Test");
    frame.setContentPane(panel);
    // frame.setSize(new Dimension(100, 100));
    frame.pack();
    frame.setVisible(true);
  }
}
