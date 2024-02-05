package pandemic.player;
import static org.junit.Assert.*;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
//import pandemic.city.Card.PlayerCard;
import pandemic.disease.Disease;


public class ExpertTest {
	private Player p;
	@Before 
	public void init() {
		this.p = new Expert(new City("city", Disease.NOIR), "name", new Board("Maps/MapInit.json")) ;
	}
	
	@Test
	public void initPlayerTest() {
		assertEquals("city", this.p.getCity().getName());
		assertEquals("name", this.p.getName());
	}
	
	@Test
	public void checkToString() {
		assertEquals("Expert", this.p.toString());
	}
	
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.player.ExpertTest.class);
	}
}

