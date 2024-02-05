package pandemic.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pandemic.board.Board;
import pandemic.city.City;
import pandemic.disease.Disease;
import pandemic.player.Player;
import pandemic.player.Scientist;
import pandemic.util.GameOverException;

public class GameTest {

    private Game game;
    private Board board;
    private List<Player> players;

    @Before
    public void init() {
        this.board = new Board("Maps/MapInit.json");
        City startingCity = new City("test", Disease.NOIR);
        this.players = new ArrayList<Player>();
        this.players.add(new Scientist(startingCity, "expert", this.board));
        this.game = new Game(board);
    }



    @Test
    public void testGetBoard() {
        assertEquals(this.board, game.getBoard());
    }
}
    