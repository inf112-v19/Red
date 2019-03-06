package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.GameObjects.*;
import inf112.skeleton.app.GameObjects.Directions.Direction;
import inf112.skeleton.app.GameObjects.Directions.Position;

import java.util.ArrayList;

public class Grid {

    private int width, height;
    private ArrayList gameLogicGrid[][];
    private TiledMapTileLayer groundLayer;
    private TiledMapTileLayer specialLayer;
    private TiledMapTileLayer playerLayer;
    private ArrayList<PlayerLayerObject> listOfPlayerTilesToMove;
    private final int groundIndex;
    private final int specialIndex;
    private final int playerIndex;

    public Grid(TiledMap map) {
        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");

        gameLogicGrid = new ArrayList[width][height];

        groundLayer = (TiledMapTileLayer) map.getLayers().get(0);
        specialLayer = (TiledMapTileLayer) map.getLayers().get(1);
        playerLayer = (TiledMapTileLayer) map.getLayers().get(2);

        listOfPlayerTilesToMove = new ArrayList<PlayerLayerObject>();
        fillGridWithArrayListsAndGameObjects();

        groundIndex = 0;
        specialIndex = 1;
        playerIndex = 2;
    }


    private void fillGridWithArrayListsAndGameObjects() {
        int id;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //ArrayLists
                gameLogicGrid[x][y] = new ArrayList<GameObject>();

                //Game objects on ground layer
                id = groundLayer.getCell(x, y).getTile().getId();
                gameLogicGrid[x][y].add(new GroundLayerObject(id));

                //fiks slik at man adder riktige special objekter
                gameLogicGrid[x][y].add(new NothingSpecial());


                gameLogicGrid[x][y].add(new NotAPlayer());
                //Game objects on special layer **(idè)**
                //id = specialLayer.getCell(x, y).getTile().getId();
                //gameLogicGrid[x][y].add(new SpecialObject(id));
            }
        }
    }
    public void setPlayerPosition(PlayerLayerObject playerLayerObject){
        gameLogicGrid[playerLayerObject.getPosition().getX()][playerLayerObject.getPosition().getY()].remove(playerIndex);
        gameLogicGrid[playerLayerObject.getPosition().getX()][playerLayerObject.getPosition().getY()].add(playerLayerObject);
    }
    public void removePlayerPosition(Position position){
        gameLogicGrid[position.getX()][position.getY()].remove(playerIndex);
        gameLogicGrid[position.getX()][position.getY()].add(new NotAPlayer());
    }

    /**
     * Skjekker om det er lov å flytte i Direction fra Position, og legger spillern til i en liste over spillere som skal flyttes.
     * Dersom spilleren som skal flyttes koliderer med en annen spiller skjekker man også om denne spilleren kan flytte seg i samme reting,
     * dersom den kan legges denne også til listen over spillere som skal flyttes, hvis ikke tømmes listen, for da har ingen lov å flytte
     * @param dir
     * @param pos
     */
    public void AllowedToMoveInDirection(Direction dir, Position pos){

        GameObject groundLayerObject = (GameObject) gameLogicGrid[pos.getX()][pos.getY()].get(groundIndex);
        if(groundLayerObject.canGo(dir)){
            listOfPlayerTilesToMove.add((PlayerLayerObject)gameLogicGrid[pos.getX()][pos.getY()].get(playerIndex));
            Position positionInDir;
            switch (dir){
                case NORTH:
                    positionInDir = pos.North();
                    if(gameLogicGrid[positionInDir.getX()][positionInDir.getY()].get(playerIndex) instanceof PlayerLayerObject){
                        AllowedToMoveInDirection(Direction.NORTH,positionInDir);
                    }
                    break;
                case EAST:
                    positionInDir = pos.East();
                    if(gameLogicGrid[positionInDir.getX()][positionInDir.getY()].get(playerIndex) instanceof PlayerLayerObject){
                        AllowedToMoveInDirection(Direction.EAST,positionInDir);
                    }
                    break;
                case WEST:
                    positionInDir = pos.West();
                    if(gameLogicGrid[positionInDir.getX()][positionInDir.getY()].get(playerIndex) instanceof PlayerLayerObject){
                        AllowedToMoveInDirection(Direction.WEST,positionInDir);
                    }
                    break;
                case SOUTH:
                    positionInDir = pos.South();
                    if(gameLogicGrid[positionInDir.getX()][positionInDir.getY()].get(playerIndex) instanceof PlayerLayerObject){
                        AllowedToMoveInDirection(Direction.SOUTH,positionInDir);
                    }
                    break;
            }
        }
        else {
            listOfPlayerTilesToMove.clear();
            return;
        }


    }

    /**
     *
     * @param direction
     * @param position
     * @return en liste over spillere som skal flyttes, dersom den er tom har ingen spillere lov å flytte
     */
    public ArrayList<PlayerLayerObject> numberOfPlayersToMove(Direction direction, Position position){
        listOfPlayerTilesToMove.clear();
        AllowedToMoveInDirection(direction,position);

        return listOfPlayerTilesToMove;
    }

}
