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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jmeld.util.ResourceLoader;

public class ImageUtil
{
  public static synchronized Icon getSmallIcon(String iconName)
  {
    return getIcon("16x16/" + iconName + "-16");
  }

  public static synchronized Icon getIcon(String iconName)
  {
    URL url;

    iconName = "images/" + iconName + ".png";

    url = ResourceLoader.getResource(iconName);
    if (url == null)
    {
      return null;
    }

    return new ImageIcon(url);
  }

  public static Icon createDarkerIcon(Icon icon)
  {
    return createDarkerIcon(icon,
                            -0.10f);
  }

  /** Create a x% Transparent icon */
  public static Icon createDarkerIcon(Icon icon,
      float percentage)
  {
    return createIcon(icon,
                      new BrightnessFilter(percentage));
  }

  /** Create a 20% Transparent icon */
  public static Icon createTransparentIcon(Icon icon)
  {
    return createTransparentIcon(icon,
                                 20);
  }

  /** Create a x% Transparent icon */
  public static Icon createTransparentIcon(Icon icon,
      int percentage)
  {
    return createIcon(icon,
                      new TransparentFilter(percentage));
  }

  /** Create a new icon which is filtered by some ImageFilter */
  private static synchronized Icon createIcon(Icon icon,
      ImageFilter filter)
  {
    ImageProducer ip;
    Image image;
    MediaTracker tracker;

    if (icon == null)
    {
      return null;
    }

    ip = new FilteredImageSource(ImageUtil.createImageIcon(icon).getImage().getSource(),
                                 filter);
    image = Toolkit.getDefaultToolkit().createImage(ip);

    tracker = new MediaTracker(new JPanel());
    tracker.addImage(image,
                     1);
    try
    {
      tracker.waitForID(1);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      return null;
    }

    return new ImageIcon(image);
  }

  public static ImageIcon createImageIcon(Icon icon)
  {
    JLabel label;
    BufferedImage image;
    Graphics2D g2d;

    label = new JLabel(icon);
    label.setSize(icon.getIconWidth(),
                  icon.getIconHeight());

    image = new BufferedImage(icon.getIconWidth(),
                              icon.getIconHeight(),
                              BufferedImage.TYPE_INT_ARGB);
    g2d = image.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                         RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                         RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    label.print(g2d);
    g2d.dispose();

    return new ImageIcon(image);
  }
}
