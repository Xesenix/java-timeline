package pl.xesenix.timeline.actions;

import java.util.Iterator;

public class Sequence extends Parallel
{
	public boolean act(float delta)
	{
		for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext();)
		{
			Action action = iterator.next();
			
			if (!action.act(delta))
			{
				return false;
			}
			
			iterator.remove();
		}
		
		return actions.isEmpty();
	}
}
