package pandemic.game.listchooser;

import static org.junit.Assert.*;

import org.junit.Test;



	import static org.junit.Assert.assertEquals;
	import static org.junit.Assert.assertNull;

	import java.io.ByteArrayInputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.List;

	import org.junit.After;
	import org.junit.Before;


	public class InteractiveListChooserTest {
		
		private InputStream stdin;
		
		@Before
		public void setUp() {
			// sets up the input stream to test user input
			String data = "2\n";
			stdin = System.in;
			System.setIn(new ByteArrayInputStream(data.getBytes()));
		}
		
		@After
		public void tearDown() {
			// restores the standard input stream
			System.setIn(stdin);
		}
		
	
		
		@Test
		public void testChooseInputValidation() throws IOException {
			InteractiveListChooser<String> ilc = new InteractiveListChooser<String>();
			
			// entering an invalid choice should repeat the prompt
			String data = "invalid\n2\n";
			System.setIn(new ByteArrayInputStream(data.getBytes()));
			List<String> multiItemList = Arrays.asList("item1", "item2", "item3");
			assertEquals("item2", ilc.choose("Choose an element:", multiItemList));
		}
	}
