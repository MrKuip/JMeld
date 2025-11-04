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
package org.jmeld.fx;

import java.util.ArrayList;
import java.util.List;
import org.jmeld.fx.ui.JMeldPaneFx;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.util.ResourceLoader;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import net.miginfocom.layout.LC;

public class JMeldFx
  extends Application
{
  static
  {
    System.setProperty("prism.lcdtext", "false");
    // System.setProperty("prism.text", "t2k");
  }

  @Override
  public void start(Stage stage) throws Exception
  {
    Scene scene;

    setUserAgentStylesheet(JMeldSettings.getInstance().getEditor().getLookAndFeelName());

    scene = new Scene(getNode(), 300, 300);
    scene.getStylesheets().add(ResourceLoader.getResource("jmeld.css").toExternalForm());

    stage.setTitle("JMeld");
    stage.getIcons().add(FxIcon.TULIP.getSmallImage());
    stage.getIcons().add(FxIcon.TULIP.getLargeImage());
    stage.getIcons().add(FxIcon.TULIP.getVeryLargeImage());
    stage.setScene(scene);
    stage.setWidth(1000);
    stage.setHeight(750);
    stage.show();
  }

  Parent getNode()
  {
    if (false)
    {
      MigPane pane;

      pane = new MigPane(new LC().fill());
      /*
      Font.getFamilies().forEach(fam -> {
        System.out.println(fam);
        Stream.of(FontPosture.values()).forEach(fp -> {
          Stream.of(FontWeight.values()).forEach(fw -> {
            Font font = Font.font(fam, fw, fp, 18.0);
            boolean isBold = font.getStyle().contains("Bold");
            boolean isItalic = font.getStyle().contains("Italic");
            Label label = new Label(
                "" + font + " (" + fp + ", " + fw + ") isBold=" + isBold + ", isItalic=" + isItalic);
            label.setFont(font);
            pane.add(label, "wrap");
          });
        });
      });
      */
      Font.getFamilies().forEach(fam -> {
        FxFont fxFont;

        fxFont = new FxFont(fam, 12.0);
        fxFont.getFontPostureList().forEach(fp -> {
          fxFont.getFontWeightList().forEach(fw -> {
            Font font = Font.font(fam, fw, fp, 18.0);
            boolean isBold = font.getStyle().contains("Bold");
            boolean isItalic = font.getStyle().contains("Italic");
            Label label = new Label(
                "" + font + " (" + fp + ", " + fw + ") isBold=" + isBold + ", isItalic=" + isItalic);
            label.setFont(font);
            pane.add(label, "wrap");
          });
        });
      });

      return new ScrollPane(pane);
    }

    return new JMeldPaneFx();
  }

  static class FxFont
  {
    private final String mi_familyName;
    private final Font mi_font;
    private final double mi_size;

    public FxFont(String familyName, double size)
    {
      mi_familyName = familyName;
      mi_size = size;
      mi_font = Font.font(mi_familyName);
    }

    public List<FontPosture> getFontPostureList()
    {
      Font font;
      List<FontPosture> result;

      result = new ArrayList<>();
      for (FontPosture fontPosture : FontPosture.values())
      {
        font = Font.font(mi_familyName, fontPosture, mi_size);
        if (font.getStyle().contains(fontPosture.name()))
        {
          result.add(fontPosture);
        }
      }

      return result;
    }

    public List<FontWeight> getFontWeightList()
    {
      Font font;
      List<FontWeight> result;

      result = new ArrayList<>();
      for (FontWeight fontWeight : FontWeight.values())
      {
        font = Font.font(mi_familyName, fontWeight, mi_size);
        if (font.getStyle().contains(fontWeight.name()))
        {
          result.add(fontWeight);
        }
      }

      return result;
    }
  }

  static public void main(String[] args)
  {
    JMeldFx.launch(args);
  }
}
