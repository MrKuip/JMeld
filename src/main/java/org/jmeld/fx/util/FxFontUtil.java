package org.jmeld.fx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jmeld.util.ResourceLoader;

public class FxFontUtil
{
  public static Font defaultTextAreaFont = new Text().getFont();
  private static Map<Integer, Font> fontBySizeMap = new HashMap<>();
  private static Map<String, FontMetrics> fontMetricMap = new HashMap<>();

  public static Font getIconFont(int iconSize)
  {
    Font font;

    font = fontBySizeMap.get(iconSize);
    if (font == null)
    {
      String resourceName;

      resourceName = "font/materialdesignicons-webfont.ttf";
      try (InputStream is = ResourceLoader.getResourceAsStream(resourceName))
      {
        font = Font.loadFont(is, iconSize);
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
        return null;
      }

      fontBySizeMap.put(iconSize, font);
    }

    return font;
  }

  public static FontMetrics getFontMetrics(Font font)
  {
    FontMetrics fontMetrics;

    fontMetrics = fontMetricMap.get(font.toString());
    if (fontMetrics == null)
    {
      fontMetrics = new FontMetrics(font);
      fontMetricMap.put(font.toString(), fontMetrics);
    }

    return fontMetrics;
  }

  public static class FontMetrics
  {
    final private Text text;
    public final float ascent;
    public final float descent;
    public final float lineHeight;

    private FontMetrics(Font fnt)
    {
      Bounds b;

      text = new Text();
      text.setTextAlignment(TextAlignment.CENTER);
      text.setFont(fnt);

      b = text.getLayoutBounds();

      ascent = (float) -b.getMinY();
      descent = (float) b.getMaxY();
      lineHeight = (float) b.getHeight();
    }

    public float getAscent()
    {
      return ascent;
    }

    public float getDescent()
    {
      return descent;
    }

    public float getLineHeight()
    {
      return lineHeight;
    }

    public float computeStringWidth(String txt)
    {
      text.setText(txt);
      return (float) text.getLayoutBounds().getWidth();
    }
  }

}
