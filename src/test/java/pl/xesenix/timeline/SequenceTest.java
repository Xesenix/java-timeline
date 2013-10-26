
package pl.xesenix.timeline;

import static org.junit.Assert.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.xesenix.timeline.ParallelTest.MockupAction;
import pl.xesenix.timeline.actions.Action;
import pl.xesenix.timeline.actions.Sequence;


public class SequenceTest
{

	private Sequence action;


	private float timeStep;


	private MockupAction[] actions;


	private int actionIndex;


	private float sequenceTime;


	private int actionsToExecute;


	private float[] startTimeMap;


	@Before
	public void setUp() throws Exception
	{
		action = new Sequence();
		actionIndex = 0;
		sequenceTime = 0;
		actionsToExecute = 0;
		timeStep = 0.5f;
		startTimeMap = new float[3];

		actions = new MockupAction[] { new MockupAction(0), new MockupAction(1), new MockupAction(2) };

		float time = 0;

		for (MockupAction mockAction : actions)
		{
			action.add((Action) mockAction);

			startTimeMap[actionsToExecute] = time;
			time += mockAction.duration;
			actionsToExecute++;
		}

		action.restart();
	}


	@After
	public void tearDown() throws Exception
	{
	}


	@Test
	public final void testAct()
	{
		while (actionsToExecute > 0)
		{
			sequenceTime += timeStep;
			assertFalse("Sequence action finished before all attached actions end.", action.act(timeStep));
		}

		assertTrue("Sequence action didn`t finished after all mockup actions finished.", action.act(timeStep));

		int i = 0;

		for (MockupAction mockAction : actions)
		{
			assertTrue("Some attached action didn`t finish after main action end.", mockAction.finished);
			assertTrue("Some attached action didn`t started while main action play.", mockAction.started);
			assertEquals("Some attached action started in wrong order.", i++, mockAction.startedIndex);
			assertThat("Some attached action started in wrong time", is(greaterThanOrEqualTo(startTimeMap[i++])));
		}
	}


	private void assertThat(String string)
	{
		// TODO Auto-generated method stub

	}


	class MockupAction implements Action
	{

		public float duration;


		private float time;


		public boolean initialized = false;


		public boolean finished = false;


		public int startedIndex;


		public float startTimeRelativeToParent;


		public boolean started;


		public MockupAction(float duration)
		{
			this.duration = duration;
		}


		public boolean act(float delta)
		{
			if (this.time == this.duration)
			{
				this.started = true;
				this.startedIndex = actionIndex++;
				this.startTimeRelativeToParent = sequenceTime;
			}

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
			this.started = false;
			this.finished = false;

			this.time = this.duration;
		}
	}
}
