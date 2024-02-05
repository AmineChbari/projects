package pandemic.player.Action;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;

public class NoneActionTest {
	
	private NoneAction action;

	@Before
	public void init() {
		this.action = new NoneAction(new Board("Maps/MapInit.json"));
	}
	
	@Test
	public void doSomethingTest() {
		assertEquals(1, this.action.doSomething());
	}
	
	@Test
	public void toStringTest() {
		assertEquals("NoneAction", this.action.toString());
	}
	
	
	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.Action.NoneActionTest.class);
	}
}
