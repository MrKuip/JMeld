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
package org.jmeld.ui.action;

import java.awt.event.ActionEvent;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.jmeld.ui.util.Icons;
import org.jmeld.ui.util.ImageUtil;

public class MeldAction
    extends AbstractAction
{
  // class variables:
  // backwards compatible with jdk1.5
  public static String LARGE_ICON_KEY = "SwingLargeIconKey";

  // instance variables:
  private ActionHandler actionHandler;
  private Consumer<ActionEvent> doAction;
  private BooleanSupplier enabler;

  MeldAction(ActionHandler actionHandler,
      String name,
      Consumer<ActionEvent> doAction,
      BooleanSupplier enabler)
  {
    super(name);

    this.actionHandler = actionHandler;
    this.doAction = doAction;
    this.enabler = enabler;
  }

  public String getName()
  {
    return (String) getValue(NAME);
  }

  public void setToolTip(String toolTip)
  {
    putValue(SHORT_DESCRIPTION,
             toolTip);
  }

  public void setIcon(Icons icon)
  {
    putValue(SMALL_ICON,
             icon.getSmallIcon());
    putValue(LARGE_ICON_KEY,
             icon.getLargeIcon());
  }

  public Icon getSmallIcon()
  {
    return (Icon) getValue(SMALL_ICON);
  }

  public Icon getLargeIcon()
  {
    return (Icon) getValue(LARGE_ICON_KEY);
  }

  public Icon getTransparentSmallIcon()
  {
    return ImageUtil.createTransparentIcon(getSmallIcon());
  }

  public void actionPerformed(ActionEvent ae)
  {
    if(doAction != null)
    {
      doAction.accept(ae);
      actionHandler.checkActions();
      return;
    }
  }

  public boolean isActionEnabled()
  {
    if(enabler != null)
    {
      return enabler.getAsBoolean();
    }
    
    return true;
  }
}
