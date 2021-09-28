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
package org.jmeld.fx.ui;

import org.tbee.javafx.scene.layout.MigPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

public class StatusBarFx
  extends MigPane
{
  // Class variables:
  // Instance variables:
  private Label statusLabel;
  private ProgressBar progressBar;
  private ProgressIndicator progressIndicator;

  private StatusBarFx()
  {
    init();
  }

  private void init()
  {
    statusLabel = new Label(" ");
    progressBar = new ProgressBar();
    progressIndicator = new ProgressIndicator();

    add(statusLabel);
    add(progressBar);
    add(progressIndicator);
  }

  public void startProgress()
  {
    progressIndicator.setProgress(-1);
    progressIndicator.setVisible(true);
  }

  public void stopProgress()
  {
    progressIndicator.setVisible(false);
    progressIndicator.setProgress(0);
  }

  public void setState(String format, Object... args)
  {
    statusLabel.setTextFill(null);
    statusLabel.setText(String.format(format, args));
  }

  public void setText(String format, Object... args)
  {
    statusLabel.setTextFill(null);
    setState(format, args);
  }

  public void setAlarm(String format, Object... args)
  {
    statusLabel.setTextFill(javafx.scene.paint.Color.RED);
    setState(format, args);
  }

  public void setProgress(int value, int maximum)
  {
    progressBar.setVisible(true);
    progressBar.setProgress(value / maximum);
  }
}
