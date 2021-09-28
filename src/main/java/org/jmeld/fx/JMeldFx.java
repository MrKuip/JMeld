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

import org.jmeld.fx.settings.JMeldSettingsFx;
import org.jmeld.fx.ui.JMeldPaneFx;
import org.jmeld.fx.util.FxIcon;
import org.jmeld.util.ResourceLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    setUserAgentStylesheet(JMeldSettingsFx.getInstance().getEditor().getLookAndFeelName());

    scene = new Scene(new JMeldPaneFx(), 300, 300);
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

  static public void main(String[] args)
  {
    JMeldFx.launch(args);
  }
}
