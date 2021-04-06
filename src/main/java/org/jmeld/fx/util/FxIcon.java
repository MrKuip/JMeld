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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jmeld.fx.util.FxFontUtil.FontMetrics;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

public enum FxIcon
{
  NEW(() -> new IconBuilder("F0224").fill("F0214")),
  DELETE(() -> new IconBuilder("F09E7").fill("F01B4")),
  SAVE(() -> new IconBuilder("F0818").fill("F0193")),
  SAVE_AS(() -> new IconBuilder("F0E28").fill("F0E27")),
  RELOAD(() -> new IconBuilder("F0453")),
  UNDO(() -> new IconBuilder("F054C")),
  REDO(() -> new IconBuilder("F044E")),
  HELP(() -> new IconBuilder("F02D7").fill("F0625")),
  SETTINGS(() -> new IconBuilder("F1064")),
  REFRESH(() -> new IconBuilder("F0450")),
  LEFT(() -> new IconBuilder("F004D")),
  RIGHT(() -> new IconBuilder("F0054")),
  COMPARE(() -> new IconBuilder("F1492")),
  FILE_COMPARE(() -> new IconBuilder("F08AA")),
  EDIT(() -> new IconBuilder("F11E8").fill("F11E7")),
  FILTER(() -> new IconBuilder("F0233").fill("F0232")),
  FOLDER(() -> new IconBuilder("F0256").fill("F024B")),
  EXPAND_ALL(() -> new IconBuilder("F1143").fill("F0334")),
  COLLAPSE_ALL(() -> new IconBuilder("F1142").fill("F1141")),
  ABOUT(() -> new IconBuilder("F02FC").fill("F02FD")),
  ALERT(() -> new IconBuilder("F002A").color(IconColor.RED)),
  CLOSE(() -> new IconBuilder("F0156")),
  TULIP(() -> new IconBuilder("F09F1").color(IconColor.RED)),
  SEARCH_NEXT(() -> new IconBuilder("F0140")),
  SEARCH_PREVIOUS(() -> new IconBuilder("F0143")),
  FILE_EXIST_NOTEQUAL(
      () -> new IconBuilder("F08A1").color(IconColor.DEFAULT_OUTLINE).fill("F0764").color(IconColor.LIGHT_BLUE_FILL)),
  FILE_EXIST_EQUAL(
      () -> new IconBuilder("F08A1").color(IconColor.DEFAULT_OUTLINE).fill("F0764").color(IconColor.WHITE)),
  FILE_NOT_EXIST(() -> new IconBuilder("F08A4").color(IconColor.DEFAULT_OUTLINE).fill("F0764").color(IconColor.WHITE)),
  PANEL_SELECTED(() -> new IconBuilder("F08A1").color(IconColor.DEFAULT_OUTLINE).fill("F0764").color(IconColor.YELLOW));

  public enum IconSize
  {
    VERY_SMALL(12),
    SMALLER(18),
    SMALL(24),
    LARGE(32),
    VERY_LARGE(48);

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
    DEFAULT_OUTLINE(Color.rgb(0, 74, 131)),
    DEFAULT_FILL(Color.WHITE),
    LIGHT_BLUE_FILL(Color.rgb(160, 200, 255)),
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
    RED(Color.RED),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW);

    private Color m_color;

    IconColor(Color color)
    {
      m_color = color;
      System.out.println(name() + " opacity=" + color.getOpacity());
    }

    public Color getColor()
    {
      return m_color;
    }
  }

  private final Supplier<IconBuilder> builder;
  private final static Map<String, Image> m_imageByKey = new HashMap<>();

  private FxIcon(Supplier<IconBuilder> builder)
  {
    this.builder = builder;
  }

  public Image getSmallImage()
  {
    return getImage(IconSize.SMALL);
  }

  public Image getLargeImage()
  {
    return getImage(IconSize.LARGE);
  }

  public Image getVeryLargeImage()
  {
    return getImage(IconSize.VERY_LARGE);
  }

  public Image getImage(IconSize size)
  {
    String key;
    Image result;
    Canvas canvas;

    key = name() + "-" + size.name();
    result = m_imageByKey.get(key);
    if (result == null)
    {
      WritableImage image;
      SnapshotParameters snapshotParameters;

      canvas = builder.get().size(size).build();
      snapshotParameters = new SnapshotParameters();
      snapshotParameters.setFill(Color.TRANSPARENT);
      image = canvas.snapshot(snapshotParameters, null);
      m_imageByKey.put(key, image);
      result = image;
    }

    return result;
  }

  public static class IconBuilder
  {
    private Icon mi_icon;
    private IconSize mi_size;

    IconBuilder(String codepoint)
    {
      mi_icon = new Icon(codepoint);
      mi_icon.color = IconColor.DEFAULT_OUTLINE;
    }

    public IconBuilder size(IconSize size)
    {
      mi_size = size;
      return this;
    }

    public IconBuilder fill(String codepoint)
    {
      mi_icon.fill = new Icon(codepoint);
      mi_icon.fill.color = IconColor.DEFAULT_FILL;
      return this;
    }

    public IconBuilder color(IconColor color)
    {
      getCurrentIcon().setColor(color);
      return this;
    }

    private Icon getCurrentIcon()
    {
      return mi_icon.fill != null ? mi_icon.fill : mi_icon;
    }

    public Canvas build()
    {
      return build(new Canvas(mi_size.getSize(), mi_size.getSize()), 0.0f, 0.0f);
    }

    private Canvas build(Canvas canvas, double x, double y)
    {
      String text;
      GraphicsContext gc;
      Font font;
      FontMetrics fontMetrics;
      Icon icon;
      InnerShadow effect;
      double correction;

      // Correction because we do not have access to real FontMetrics
      correction = 2.0;

      gc = canvas.getGraphicsContext2D();

      /*
       * gc.setFill(Color.YELLOW); gc.fillRect(0, 0, canvas.getWidth(),
       * canvas.getHeight()); gc.setStroke(Color.PURPLE); gc.strokeRect(0, 0,
       * canvas.getWidth(), canvas.getHeight());
       */

      if (mi_icon.fill != null)
      {
        icon = mi_icon.fill;
        text = icon.getText();
        font = FxFontUtil.getIconFont(mi_size.getSize());
        fontMetrics = FxFontUtil.getFontMetrics(font);

        x = (canvas.getWidth() - fontMetrics.computeStringWidth(text)) / 2.0;
        y = (canvas.getHeight() + fontMetrics.getAscent()) / 2 - correction;

        gc.setFont(font);
        gc.setFill(icon.getColor());
        gc.setFontSmoothingType(FontSmoothingType.GRAY);
        gc.fillText(text, x, y);
      }

      icon = mi_icon;
      text = icon.getText();
      font = FxFontUtil.getIconFont(mi_size.getSize());
      fontMetrics = FxFontUtil.getFontMetrics(font);

      x = (canvas.getWidth() - fontMetrics.computeStringWidth(text)) / 2.0;
      y = (canvas.getHeight() + fontMetrics.getAscent()) / 2 - correction;

      // effect = new InnerShadow();
      // effect.setOffsetX(mi_size.getSize() / 16);
      // effect.setOffsetY(effect.getOffsetX());
      // effect.setColor(icon.getColor().brighter().desaturate());

      gc.setFont(font);
      // gc.setEffect(effect);
      gc.setStroke(icon.getColor());
      gc.setFill(icon.getColor());
      gc.setFontSmoothingType(FontSmoothingType.GRAY);
      gc.setLineWidth(1.2);
      gc.setLineJoin(StrokeLineJoin.MITER);
      gc.setMiterLimit(2.0);
      gc.fillText(text, x, y);

      return canvas;
    }

    static class Icon
    {
      private String text;
      private IconColor color = IconColor.DEFAULT_OUTLINE;
      private Icon fill;

      public Icon(String codepoint)
      {
        text = new String(Character.toChars(Integer.parseInt(codepoint, 16)));
      }

      public String getText()
      {
        return text;
      }

      public void setColor(IconColor color)
      {
        this.color = color;
      }

      public Color getColor()
      {
        return color.getColor();
      }
    }
  }
}
