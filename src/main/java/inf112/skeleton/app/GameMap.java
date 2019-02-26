package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.GameObjects.Directions.Direction;
import inf112.skeleton.app.GameObjects.Directions.Position;
import inf112.skeleton.app.GameObjects.PlayerTile;

import java.util.ArrayList;
import java.util.Stack;

public class GameMap {

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private Grid grid;
    private final TiledMapTileLayer playerLayer;
    private TiledMapTileSet tiles;
    private final int nPlayers;

    //List of players and list of their cells on the map
    private ArrayList<Player> players;
    private ArrayList<TiledMapTileLayer.Cell> cells;

    //Takes String (filename for tileset), int (number of players)
    public GameMap(String name, int nPlayers) {
        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load(name);
        this.grid = new Grid(map);
        this.tiles = map.getTileSets().getTileSet("testTileset");
        this.players = new ArrayList<>();
        this.nPlayers = nPlayers;
        this.cells = new ArrayList<>();
        this.playerLayer = (TiledMapTileLayer) map.getLayers().get(2);

        initializePlayers();
    }

    private void initializePlayers() {
        //Initializes each player and gives them a unique ID and cell
        //Arrays 'players' and 'cells' have corresponding player/cell indices
        for (int id = 0; id < nPlayers; id++) {
            Player player = new Player(tiles, id);
            TiledMapTileLayer.Cell pCell = new TiledMapTileLayer.Cell();
            cells.add(pCell);
            players.add(player);
        }
        drawPlayers();

    }

    //Draws players
    public void drawPlayers() {
        for (Player player : players) {
            Position pos = player.getPosition();
            TiledMapTileLayer.Cell avatar = cells.get(player.getId()).setTile(player.getAvatar());
            playerLayer.setCell(pos.getX(), pos.getY(), avatar);
        }
    }

    //Moves players one by one
    public void movePlayers() {
        for (Player player : players) {
            Direction dir = player.getMove();
            Position pos = player.getPosition();

            if (canGo(dir, pos)) {
                Position playerPosition = player.getPosition();
                playerLayer.setCell(playerPosition.getX(), playerPosition.getY(), null);
                player.update();
            }
            System.out.println(player.getPosition().getX() + "  " + player.getPosition().getY());
        }
        drawPlayers();
    }

    //Check if valid position
    public boolean canGo(Direction dir, Position pos) {
        return grid.AllowedToMoveInDirection(dir, pos);
    }

    public TiledMapTileLayer getPlayerLayer() {
        return playerLayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
