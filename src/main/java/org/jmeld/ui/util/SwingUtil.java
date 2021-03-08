package org.jmeld.ui.util;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.jmeld.ui.action.MeldAction;

public class SwingUtil
{
  private SwingUtil()
  {
  }

  public static void installKey(JComponent component,
      String key,
      MeldAction action)
  {
    InputMap inputMap;
    ActionMap actionMap;
    KeyStroke stroke;

    stroke = KeyStroke.getKeyStroke(key);

    inputMap = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    if (inputMap.get(stroke) != action.getName())
    {
      inputMap.put(stroke,
                   action.getName());
    }

    actionMap = component.getActionMap();
    if (actionMap.get(action.getName()) != action)
    {
      actionMap.put(action.getName(),
                    action);
    }
  }

  public static void deInstallKey(JComponent component,
      String key,
      MeldAction action)
  {
    InputMap inputMap;
    ActionMap actionMap;
    KeyStroke stroke;

    stroke = KeyStroke.getKeyStroke(key);
    inputMap = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    inputMap.remove(stroke);

    // Do not deinstall the action because I don't know how many other
    //   inputmap residents will call the action.
  }
}
