package pandemic.game.listchooser.util;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;
import java.io.InputStream;


public class InputTest {

    @Test(expected = IOException.class)
    public void testReadIntInvalidInput() throws IOException {
        String invalidInput = "not an int";
        System.setIn(new ByteArrayInputStream(invalidInput.getBytes()));
        Input.readInt();
    }

    @Test(expected = IOException.class)
    public void testReadIntEmptyInput() throws IOException {
        String emptyInput = "";
        System.setIn(new ByteArrayInputStream(emptyInput.getBytes()));
        Input.readInt();
    }

    @Test(expected = IOException.class)
    public void testReadIntWhitespaceInput() throws IOException {
        String whitespaceInput = "    ";
        System.setIn(new ByteArrayInputStream(whitespaceInput.getBytes()));
        Input.readInt();
    }

    
}