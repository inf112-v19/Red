package inf112.skeleton.app.Player;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import inf112.skeleton.app.Cards.PlayerDeck;
import inf112.skeleton.app.Directions.Direction;
import inf112.skeleton.app.Directions.Position;
import inf112.skeleton.app.GameObjects.PlayerLayerObject;

public class Player {

    public static final int MAX_DAMAGE_TOKENS = 9;
    public static final int MAX_LIFE_TOKENS = 3;

    private Position backup;
    private PlayerLayerObject playerTile;
    private int lifeTokens = MAX_LIFE_TOKENS;
    private int damageTokens = MAX_DAMAGE_TOKENS;
    private int id;
    private PlayerDeck playerDeck;
    private boolean handChosen;


    public Player(TiledMapTileSet tiles, int id) {
        this.id = id;
        this.playerTile = new PlayerLayerObject(tiles, id);
        this.playerDeck = new PlayerDeck();
        this.backup = new Position(id, id);
        handChosen = false;

    }

    public int getId() {
        return id;
    }

    /**
     * Select the 5 first cards form player deck
     */
    public void select5FirstCards() {
        for (int i = 4; i >= 0; i--)
            playerDeck.selectCardForHand(i);
    }

    public Position getPosition() {
        return playerTile.getPosition();
    }

    public Direction getDirection() {
        return playerTile.getDirection();
    }

    public PlayerLayerObject getPlayerTile() {
        return playerTile;
    }

    public void setBackup(Position pos) {
        backup = pos;
    }

    public TiledMapTile getAvatar() {
        return playerTile.getAvatar();
    }

    public PlayerDeck getPlayerDeck() {
        return playerDeck;
    }

    public void setHandChosen(Boolean handChosen){
        this.handChosen = handChosen;
    }
    public boolean getHandChoosen(){
        return handChosen;
    }


}
