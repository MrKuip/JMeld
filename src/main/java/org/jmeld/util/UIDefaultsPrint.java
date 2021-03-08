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
package org.jmeld.util;

import java.util.Comparator;
import javax.swing.UIManager;

public class UIDefaultsPrint
{
  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      UIManager.getDefaults().entrySet().stream()
          .sorted(Comparator.comparing(e -> e.getKey() == null ? "" : e.getKey().toString()))
          .forEach(e -> System.out.printf("%-40.40s = %s\n",
                                          e.getKey(),
                                          e.getValue()));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
