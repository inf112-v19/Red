package inf112.skeleton.app.Player;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import inf112.skeleton.app.Cards.PlayerDeck;
import inf112.skeleton.app.Directions.Direction;
import inf112.skeleton.app.Directions.Position;
import inf112.skeleton.app.Game.ConveyorBelt;
import inf112.skeleton.app.GameObjects.PlayerLayerObject;

public class Player {

    public static final int MAX_HEALTH = 10;
    public static final int MAX_LIFE_TOKENS = 3;

    private Position backup;
    private PlayerLayerObject playerTile;
    private int lifeTokens = MAX_LIFE_TOKENS;
    private int health = MAX_HEALTH;
    private int id;
    private PlayerDeck playerDeck;

    private boolean returnedToBackup;
    private boolean removedFromBoard;
    private boolean active;
    private boolean isDestroyed;
    private boolean isAlive;
    /**
     * Variable to distinguish between human players and bots
     */
    private boolean isAI;
    /**
     * The current conveyor belt the player is standing on.
     * If null then player is not standing on a conveyor belt
     */
    private ConveyorBelt currentConveyorBelt;
    private boolean handChosen;
    private int lastFlagVisited;
    private boolean hitByBoardLaser;

    public Player(int id) {
        this.id = id;
        this.playerTile = new PlayerLayerObject(id);
        this.playerDeck = new PlayerDeck();
        this.backup = new Position(id, id);

        this.handChosen = false;
        this.returnedToBackup = false;
        this.active = true;
        this.isDestroyed = false;
        this.isAlive = true;
        this.isAI = false;

        this.hitByBoardLaser = false;
        this.lastFlagVisited = 0;
    }

    public void setAI() {
        this.isAI = true;
    }

    public boolean isAi() {
        return isAI;
    }

    public int getId() {
        return id;
    }

    /**
     * Select the 5 first cards form player deck
     */
    public void select5FirstCards() {
        playerDeck.selectCardsForHand();
    }

    /**
     * Restores the amount of health to be the max amount of damage tokens -2 and set isDestroyed to false
     */
    public void fix() {
        if (!isAlive)
            return;

        returnedToBackup = false;
        isDestroyed = false;
        health = MAX_HEALTH - 2;
        playerDeck.changedHealth(health);
    }

    /**
     * Increase the health of the player by one.
     * Does not increase if player has max health
     */
    public void incrementHealth() {
        if (health < MAX_HEALTH)
            health++;
        playerDeck.changedHealth(health);
    }

    /**
     * If health is less than 1, then player is destroyed
     * @return true if player has no health
     */
    public boolean lostAllHealth() {
        if (health < 1) {
            isDestroyed = true;
            return true;
        }
        return false;
    }

    public void damagePlayer(int howMuchDamage) {
        if (howMuchDamage < 1)
            throw new IllegalArgumentException("Damage much be greater than 0");
        if (!isAlive || isDestroyed)
            throw new IllegalArgumentException("Player must be alive and not destroyed to take damage");

        health = health - howMuchDamage;
        if(health <= 0) {
            System.out.println("DESTROYED");
            this.destroy();
        }
        playerDeck.changedHealth(health);
    }

    /**
     * Removes one life token, sets isDestroyed to true and sets health = 0
     * If all life tokens are lost then set isAlive to false
     */
    public void destroy() {
        lifeTokens--;
        if (lifeTokens < 1) {
            isDestroyed = true;
            isAlive = false;
            health = 0;
            return;
        }
        health = 0;
        isDestroyed = true;
    }

    public void kill() {
        health = 0;
        lifeTokens = 0;
        isAlive = false;
        isAI = true;
    }

    public boolean hasReturnedToBackup() {
        return returnedToBackup;
    }

    public void returnToBackup() {
        returnedToBackup = true;
    }

    public boolean hasBeenRemoveFromBoard() {
        return removedFromBoard;
    }

    public void removeFromBoard() {
        removedFromBoard = true;
    }


    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isActive() {
        return active;
    }

    public void powerDown() {
        active = false;
        health = MAX_HEALTH;
        playerDeck.changedHealth(health);
    }

    /**
     * A powered down player is now active
     */
    public void activate() {
        active = true;
    }

    public int getLastFlagVisited() {
        return lastFlagVisited;
    }

    public void visitFlag() {
        lastFlagVisited++;
    }

    public Position getPosition() {
        return playerTile.getPosition();
    }

    public void setPosition(Position position) {
        playerTile.setPosition(position);
    }

    public Direction getDirection() {
        return playerTile.getDirection();
    }

    public void setDirection(Direction dir) {
        playerTile.setDirection(dir);
    }

    public PlayerLayerObject getPlayerTile() {
        return playerTile;
    }

    public Position getBackup() {
        return backup;
    }

    public void setBackup(Position pos) {
        backup = pos;
    }

    public TiledMapTile getAvatar() {
        return playerTile.getAvatar();
    }

    public TiledMapTile getDestroyedAvatar() {
        return playerTile.getDestroyedAvatar();
    }

    public TiledMapTile getLaserAvatar() {
        return playerTile.getLaserAvatar();
    }

    public TiledMapTile getBackupAvatar() {
        return playerTile.getBackup().getAvatar();
    }

    public void printStatus() {
        System.out.println(getStatus());
    }

    public String getStatus(){
        String status = "ACTIVE";
        if (!isAlive)
            status = "DEAD";
        else if(isDestroyed)
            status = "DESTROYED";
        else if(!isActive())
            status = "POWER DOWN";

        return String.format("COLOR: %-10s\n" +
                        "HEALTH POINTS: %-2d\n" +
                        "LIFE TOKENS: %s\n" +
                        "FLAG: %s\n" +
                        "STATUS: %s \n\n",
                this.getPlayerTile().getColor(), getHealth(), getLifeTokens(), getLastFlagVisited(), status);
    }

    public ConveyorBelt getCurrentConveyorBelt() {
        return currentConveyorBelt;
    }

    public void setCurrentConveyorBelt(ConveyorBelt conveyorBelt) {
        currentConveyorBelt = conveyorBelt;
    }

    public int getHealth() {
        return health;
    }

    public int getLifeTokens() {
        return lifeTokens;
    }

    public PlayerDeck getPlayerDeck() {
        return playerDeck;
    }


    public void setHandChosen(Boolean handChosen) {
        this.handChosen = handChosen;
    }

    public boolean getHandChosen() {
        return handChosen;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((Player) obj).getId();
    }

    public boolean isHitByBoardLaser() {
        return hitByBoardLaser;
    }

    public void setHitByBoardLaser(boolean b) {
        hitByBoardLaser = b;
    }

}
