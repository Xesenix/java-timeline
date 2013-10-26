/**
 * 
 */

package pl.xesenix.timeline;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.xesenix.timeline.actions.Action;
import pl.xesenix.timeline.actions.Parallel;


/**
 * @author Xesenix
 * 
 */
public class ParallelTest
{

	private Parallel action;


	private float longestDuration;


	private float timeStep;


	private MockupAction[] actions;


	private int actionsToExecute;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		action = new Parallel();

		longestDuration = 5;
		timeStep = 0.5f;
		actionsToExecute = 0;

		actions = new MockupAction[] {
			new MockupAction(longestDuration),
			new MockupAction(longestDuration - 1),
			new MockupAction(longestDuration - 2)
		};

		for (MockupAction mockAction : actions)
		{
			action.add((Action) mockAction);
			actionsToExecute++;
		}

		action.restart();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}


	/**
	 * Test method for {@link pl.xesenix.timeline.actions.Parallel#reset()}.
	 */
	@Test
	public void testReset()
	{
		for (MockupAction mockAction : actions)
		{
			assertTrue("Some attached action wasn`t initialized.", mockAction.initialized);
		}
	}


	/**
	 * Test method for {@link pl.xesenix.timeline.actions.Parallel#act(float)}.
	 */
	@Test
	public void testAct()
	{
		while (actionsToExecute > 0)
		{
			assertFalse("Parallel action finished before all attached actions end.", action.act(timeStep));
		}

		assertTrue("Parallel action didn`t finished after all mockup actions finished.", action.act(timeStep));

		for (MockupAction mockAction : actions)
		{
			assertTrue("Some attached action didn`t finish after main action end.", mockAction.finished);
		}
	}


	class MockupAction implements Action
	{

		private float duration;


		private float time;


		public boolean initialized = false;


		public boolean finished = false;


		public MockupAction(float duration)
		{
			this.duration = duration;
		}


		public boolean act(float delta)
		{
			this.time -= delta;

			if (this.time < 0)
			{
				this.finished = true;

				actionsToExecute--;

				return true;
			}

			return false;
		}


		public void restart()
		{
			this.initialized = true;
			this.finished = false;
			this.time = this.duration;
		}
	}

}
