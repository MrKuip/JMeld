package org.jmeld.ui.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import org.jmeld.util.ResourceLoader;

public class FontUtil
{
  public static Font defaultTextAreaFont = new JTextArea().getFont();
  private static Map<Integer, Font> m_fontBySizeMap = new HashMap<>();

  public static Font getIconFont(int iconSize)
  {
    Font font;

    font = m_fontBySizeMap.get(iconSize);
    if (font == null)
    {
      String resourceName;

      resourceName = "font/materialdesignicons-webfont.ttf";
      try (InputStream is = ResourceLoader.getResourceAsStream(resourceName))
      {
        font = Font.createFont(Font.TRUETYPE_FONT, is);
        font = font.deriveFont(Font.PLAIN, iconSize);
        System.out.println("size=" + font.getSize());
      } catch (IOException | FontFormatException exp)
      {
        System.out.println("Cannot create font[size=" + iconSize + ", resource=" + resourceName + "]");
      }

      m_fontBySizeMap.put(iconSize, font);
    }

    return font;
  }

}
