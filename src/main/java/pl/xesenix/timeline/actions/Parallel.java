
package pl.xesenix.timeline.actions;

import java.util.ArrayList;
import java.util.Iterator;


public class Parallel implements Action
{
	protected ArrayList<Action> actions = null;


	protected ArrayList<Action> actionsToPerform = new ArrayList<Action>();


	public boolean act(float delta)
	{
		for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext();)
		{
			Action action = iterator.next();

			if (action.act(delta))
			{
				iterator.remove();
			}
		}

		return actions.isEmpty();
	}


	public void restart()
	{
		actions = actionsToPerform;

		for (Action action : actions)
		{
			action.restart();
		}
	}


	/**
	 * Adds actions to perform when called after restart has no effect on
	 * current set of performed actions.
	 * 
	 * @param action
	 */
	public void add(Action action)
	{
		if (actions == null)
		{
			actionsToPerform.add(action);
		}
	}
}
