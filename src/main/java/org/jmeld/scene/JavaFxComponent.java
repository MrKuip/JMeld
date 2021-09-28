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
package org.jmeld.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * Override this component to allow custom painting modelled after swing.
 * 
 * @author kees
 */
public abstract class JavaFxComponent
  extends Pane
{
  private final Canvas canvas;

  public JavaFxComponent()
  {
    canvas = new Canvas(getWidth(), getHeight());

    getChildren().add(canvas);
    widthProperty().addListener(e -> canvas.setWidth(getWidth()));
    heightProperty().addListener(e -> canvas.setHeight(getHeight()));
  }

  @Override
  protected void layoutChildren()
  {
    super.layoutChildren();

    if (isVisible())
    {
      paintComponent(canvas.getGraphicsContext2D());
    }
  }

  protected abstract void paintComponent(GraphicsContext gc);
}
