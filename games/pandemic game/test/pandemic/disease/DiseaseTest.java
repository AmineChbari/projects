package pandemic.disease;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DiseaseTest {

	protected Disease d;

	@Before
	public void init() {
		this.d = Disease.JAUNE;
	}

	@Test
	public void cureChangesFalseToTrue() {
		d.setCured(false);
		d.cure();
		assertTrue(d.isCured());
	}

	@Test
	public void erradicateChangesFalseToTrue() {
		assertFalse(d.isErradicated());
		d.erradicate();
		assertTrue(d.isErradicated());
	}
	
	@Test
	public void testGetStatus() {
		Disease.NOIR.setCured(false);
		Disease.NOIR.setErradicated(false);
		assertEquals(Disease.NOIR.getStatus(), Disease.NOIR + " is not cured and not erradicated");
	}

	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(pandemic.disease.DiseaseTest.class);
	}
}
