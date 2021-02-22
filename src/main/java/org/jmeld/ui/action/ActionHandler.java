package org.jmeld.ui.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ActionHandler
{
  private Map<Actions.Action, MeldAction> actions = new HashMap<Actions.Action, MeldAction>();

  public ActionHandler()
  {
  }

  public MeldAction get(Actions.Action a)
  {
    return actions.get(a);
  }

  /*
  public MeldAction createAction(Object object,
      Actions.Action a)
  {
    MeldAction action;

    action = new MeldAction(this,
                            object,
                            a.getName(), null, null);
    actions.put(a,
                action);

    checkActions();

    return action;
  }
  */
  
  public MeldAction createAction(Actions.Action a,
      Consumer<ActionEvent> doAction)
  {
	  return createAction(a, doAction, null);
  }
  
  public MeldAction createAction(
      Actions.Action a,
      Consumer<ActionEvent> doAction,
      BooleanSupplier enabler)
  {
    MeldAction action;

    action = new MeldAction(this,
                            a.getName(),
                            doAction, 
                            enabler);
    actions.put(a,
                action);

    checkActions();

    return action;
  }


  public void checkActions()
  {
    boolean actionEnabled;
    boolean someActionChanged;

    do
    {
      someActionChanged = false;
      for (MeldAction action : actions.values())
      {
        actionEnabled = action.isActionEnabled();
        if (actionEnabled != action.isEnabled())
        {
          action.setEnabled(actionEnabled);

          // Some actions depend on other actions!
          someActionChanged = true;
        }
      }
    }
    while (someActionChanged);
  }
}
