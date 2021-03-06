package inf112.skeleton.app.Game;

import com.badlogic.gdx.maps.tiled.*;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Cards.ProgramCardDeck;
import inf112.skeleton.app.Cards.ProgramType;
import inf112.skeleton.app.Directions.*;
import inf112.skeleton.app.GameObjects.PlayerLayerObject;
import inf112.skeleton.app.Player.Player;
import inf112.skeleton.app.Round.Phase;
import inf112.skeleton.app.Round.Round;


import java.util.ArrayList;

public class GameMap {

    private boolean isMultiplayer;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private Grid grid;

    private final TiledMapTileLayer playerLayer;
    private final TiledMapTileLayer backupLayer1;
    private final TiledMapTileLayer backupLayer2;
    private final TiledMapTileLayer backupLayer3;
    private final TiledMapTileLayer backupLayer4;
    private final TiledMapTileLayer laserLayer;

    private TiledMapTileSet tiles;
    private final int nPlayers;
    private IStartingPosition startingPositions;
    private ProgramCardDeck programCardDeck;
    private ArrayList<PlayerLayerObject> playerTiles;
    private ArrayList<Player> players;
    private Player winner;
    private Laser laser;

    private Round round;
    private EndOfPhaseActions endOfPhaseActions;
    private EndOfRoundActions endOfRoundActions;
    private boolean startRound;

    private boolean cardsDealt;

    public GameMap(String filename, int nPlayers, boolean isMultiplayer) {
        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load(filename);
        this.grid = new Grid(map);
        this.tiles = map.getTileSets().getTileSet("testTileset");
        this.players = new ArrayList<>();
        this.nPlayers = nPlayers;
        getStartingPositions(filename);
        this.isMultiplayer = isMultiplayer;
        this.laser = new Laser(this, grid, map);
        this.programCardDeck = ProgramCardDeck.getProgramCardDeckSingleton();
        this.playerLayer = (TiledMapTileLayer) map.getLayers().get(8);
        this.backupLayer1 = (TiledMapTileLayer) map.getLayers().get(4);
        this.backupLayer2 = (TiledMapTileLayer) map.getLayers().get(5);
        this.backupLayer3 = (TiledMapTileLayer) map.getLayers().get(6);
        this.backupLayer4 = (TiledMapTileLayer) map.getLayers().get(7);
        this.laserLayer = (TiledMapTileLayer) map.getLayers().get(9);
        this.playerTiles = new ArrayList<>();

        initializePlayers();
        this.cardsDealt = true;

        this.round = new Round();
        this.endOfPhaseActions = new EndOfPhaseActions(this, grid, map);
        this.endOfRoundActions = new EndOfRoundActions(this);
    }

    public void getStartingPositions(String filename) {
        if (filename.equals("assets/testMap.tmx"))
            this.startingPositions = new TestStartingPositions(grid.getWidth(), grid.getHeight());
        else if (filename.equals("assets/riskyExchange.tmx"))
            this.startingPositions = new StartingPositionsRiskyExchange(grid.getWidth());
        else
            this.startingPositions = new StartingPositions(grid.getWidth());
    }

    public void setStartRound(boolean startRound) {
        this.startRound = startRound;
    }

    public boolean getStartRound() {
        return startRound;
    }

    /**
     * Creates all players and gives out cards
     */

    private void initializePlayers() {
        //Initializes each player and gives them a unique ID
        for (int id = 0; id < nPlayers; id++) {
            initializePlayer(id);
        }
        // Give out cards to players
        dealCards();
        programCardDeck.giveOutCardsToAllPlayers(players);
        drawPlayers();
    }

    public void initializePlayer(int id) {
        Player player = new Player(id);
        players.add(player);
        Position startingPosition = startingPositions.getStartingPosition(player.getId());
        player.setPosition(startingPosition);
        player.setBackup(startingPosition);
        PlayerLayerObject playerTile = player.getPlayerTile();
        playerTile.setSprite(tiles);
        grid.setPlayerPosition(playerTile);
        setBackup(player);
        if (!isMultiplayer && player.getId() > 0)
            player.setAI();
    }

    public void dealCards() {
        getDeck().giveOutCardsToAllPlayers(players);
    }

    public void drawPlayers() {
        for (Player player : players) {
            if (player.isAlive())
                drawPlayer(player);
        }
    }

    public void drawPlayer(Player player) {
        Position pos = player.getPosition();
        laserLayer.setCell(pos.getX(), pos.getY(), null);
        TiledMapTileLayer.Cell avatar = new TiledMapTileLayer.Cell();
        // If player is destroyed then draw a grey avatar
        if (player.isDestroyed())
            avatar.setTile(player.getDestroyedAvatar());
        else
            avatar.setTile(player.getAvatar());

        playerLayer.setCell(pos.getX(), pos.getY(), avatar);
    }

    public void drawBackup(Player player) {
        int playerId = player.getId();
        Position pos = player.getBackup();
        int x = pos.getX();
        int y = pos.getY();

        TiledMapTileLayer.Cell avatar = new TiledMapTileLayer.Cell();
        avatar.setTile(player.getBackupAvatar());

        switch (playerId) {
            case 0:
                backupLayer1.setCell(x, y, avatar);
                break;
            case 1:
                backupLayer2.setCell(x, y, avatar);
                break;
            case 2:
                backupLayer3.setCell(x, y, avatar);
                break;
            case 3:
                backupLayer4.setCell(x, y, avatar);
                break;
        }
    }

    /**
     * Removes the given player's backup point and sets it to the player's current position
     * @param player
     */
    public void setBackup(Player player) {
        Position previousBackupPosition = player.getBackup();
        int x = previousBackupPosition.getX();
        int y = previousBackupPosition.getY();
        grid.removeBackupPosition(previousBackupPosition, player.getId());
        switch (player.getId()) {
            case 0:
                backupLayer1.setCell(x, y, null);
                break;
            case 1:
                backupLayer2.setCell(x, y, null);
                break;
            case 2:
                backupLayer3.setCell(x, y, null);
                break;
            case 3:
                backupLayer4.setCell(x, y, null);
                break;
        }
        grid.setBackupPosition(player.getPlayerTile());
        Position currentPosition = player.getPosition();
        player.setBackup(currentPosition);
        drawBackup(player);
    }

    public void getHandsFromServer(ArrayList<ProgramCard> listOfMovesFromServer, int id) {
        players.get(id).getPlayerDeck().setPlayerHand(listOfMovesFromServer);
        System.out.println("Hand size: " + players.get(id).getPlayerDeck().handSize() + ". ID: " + id);
        players.get(id).setHandChosen(true);
    }

    /**
     * Gives cards to all players.
     * Tell the Round object what moves to perform
     */
    public void addPlayerHandToNewRound() {
        for (Player player : players) {
            if (player.getPlayerDeck().handSize() < 5) {
                giveOutCardsToPlayer(player);
            }
        }
        if (!round.allPhasesAddedToRound()) {
            round = new Round();
            cardsDealt = false;
            int amountOfPhases = 5;
            for (int i = 0; i < amountOfPhases; i++) {
                ArrayList<ProgramCard> cardsToAddInPhaseI = new ArrayList<>();
                for (Player player : players) {
                    if (player.isActive() && player.isAlive()) {
                        // If the players hand is empty then give out 9 new cards and select 5 cards for hand
                        // Temporary solution. Card selection system is coming.
                        if (player.getPlayerDeck().handIsEmpty()) {
                            giveOutCardsToPlayer(player);
                        }
                        ProgramCard tempCard = player.getPlayerDeck().getCardFromHand();
                        tempCard.setPlayerThatPlayedTheCard(player.getId());
                        cardsToAddInPhaseI.add(tempCard);
                    }
                }
                round.addPhases(new Phase(cardsToAddInPhaseI));
            }
        }
    }

    public void setAllPlayerHandsChosen(boolean handsChosen) {
        for (Player player : players) {
            player.setHandChosen(handsChosen);
        }
    }

    public void movePlayer(int playerId, ProgramCard card) {
        Player player = players.get(playerId);
        // A player which is destroyed or dead cannot move
        if (player.isDestroyed() || !player.isAlive()) {
            return;
        }
        ProgramType programType = card.getProgramType();
        Direction playerDir = player.getDirection();
        Direction moveDir = playerDir;
        if (programType.isMoveCard()) {
            if (programType == ProgramType.BACKWARD)
                moveDir = rotate(ProgramType.UTURN, playerDir);

            Position newPos = player.getPosition();
            if (canGo(moveDir, newPos))
                movePlayerTilesInList(moveDir);

            if (programType == ProgramType.BACKWARD)
                player.getPlayerTile().setDirection(playerDir);
        }
        //If rotation card
        else {
            Direction rotated = rotate(card.getProgramType(), playerDir);
            player.getPlayerTile().setDirection(rotated);
        }
        drawPlayers();
    }

    /**
     * Sets players position to backup and draws players
     * @param player
     */
    public void returnToBackup(Player player) {
        playerLayer.setCell(player.getPosition().getX(), player.getPosition().getY(), null);
        grid.removePlayerPosition(player.getPosition());
        Position backup = player.getBackup();

        if (existsPlayerOnBackup(player)) {
            movePlayerToNearestField(player);
        } else {
            player.setPosition(backup);
            grid.setPlayerPosition(player.getPlayerTile());
        }
        player.returnToBackup();
        drawPlayers();
    }

    /**
     * Checks if there is a player already at the given player's backup
     * @param player
     * @return true if there is another player on the backup
     */
    public boolean existsPlayerOnBackup(Player player) {
        Position backup = player.getBackup();
        TiledMapTileLayer.Cell currentPlayerLayerCell = playerLayer.getCell(backup.getX(), backup.getY());
        if (currentPlayerLayerCell == null)
            return false;
        return true;
    }

    /**
     * If someone is standing on the backup of a player when he is due to return,
     * then return player to a position adjacent to the backup
     * @param player
     */
    public void movePlayerToNearestField(Player player) {
        Position backup = player.getBackup();
        if (canGo(Direction.NORTH, backup) && !grid.isHole(backup.north())) {
            Position newPosition = backup.north();
            player.setPosition(newPosition);
            grid.setPlayerPosition(player.getPlayerTile());
        } else if (canGo(Direction.EAST, backup) && !grid.isHole(backup.east())) {
            Position newPosition = backup.east();
            player.setPosition(newPosition);
            grid.setPlayerPosition(player.getPlayerTile());
        } else if (canGo(Direction.SOUTH, backup) && !grid.isHole(backup.south())) {
            Position newPosition = backup.south();
            player.setPosition(newPosition);
            grid.setPlayerPosition(player.getPlayerTile());
        } else if (canGo(Direction.WEST, backup) && !grid.isHole(backup.west())) {
            Position newPosition = backup.west();
            player.setPosition(newPosition);
            grid.setPlayerPosition(player.getPlayerTile());
        }
    }

    /**
     * Checks if player is in a state where he/she has to return to backup
     * @param player
     * @return
     */
    public boolean hasToReturnToBackup(Player player) {
        if (steppedOnHole(player))
            return true;
        if (player.hasBeenRemoveFromBoard() || player.hasReturnedToBackup())
            return false;
        if (player.lostAllHealth())
            return true;
        return false;
    }

    /**
     * Checks if player is stepping on a hole. If so, destroy player
     * @param player
     * @return true if player is in hole
     */
    public boolean steppedOnHole(Player player) {
        Position currentPosition = player.getPosition();
        if (grid.isHole(currentPosition)) {
            player.destroy();
            return true;
        }
        return false;
    }

    /**
     * Performs one move from the phase
     */
    public void performNextMovement() {
        endOfPhaseActions.getLaser().cleanLasers();
        returnDestroyedPlayersToBackup();

        if (round.allPhasesAddedToRound()) {
            if (!round.isCompleted()) {
                if (round.getCurrentPhase().getPhaseComplete()) {
                    endOfPhaseChecks();
                    System.out.println("Phase " + (round.getCurrentPhaseNumber() + 1) + " er ferdig");
                    round.nextPhase();
                } else {
                    ProgramCard currentCard = round.getNextMovementCard();
                    if (currentCard == null)
                        return;
                    int playerId = currentCard.getPlayerThatPlayedTheCard();
                    movePlayer(currentCard.getPlayerThatPlayedTheCard(), currentCard);
                    endOfPhaseActions.getLaser().checkBoardLasers(playerId);
                }
            } else {
                endOfRoundChecks();
                if (!cardsDealt) {
                    dealCards();
                    cardsDealt = true;
                    setAllPlayerHandsChosen(false);
                }
            }
        }
    }

    public void endOfPhaseChecks() {
        endOfPhaseActions.performAllChecks();
    }

    public void endOfRoundChecks() {
        endOfRoundActions.performAllChecks();
        drawPlayers();
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        winner = player;
    }

    public void returnDestroyedPlayersToBackup() {
        for (Player player : players) {
            if (hasToReturnToBackup(player)) {
                returnToBackup(player);
            }
        }
    }

    public void giveOutCardsToPlayer(Player player) {
        programCardDeck.giveOutCardsToPlayer(player);
    }

    /**
     * Select 5 first cards in playerDeck for all bots
     */
    public void selectCardsForBots() {
        for (Player player : players) {
            if (player.isAi() && player.getPlayerDeck().handIsEmpty()) {
                player.select5FirstCards();
                player.setHandChosen(true);
            }
        }
    }

    public ProgramCardDeck getDeck() {
        return programCardDeck;
    }

    /**
     * Flytter alle spillere som koliderer i direction og oppdaterer grid
     * @param direction
     */
    public void movePlayerTilesInList(Direction direction) {
        int numberOfPlayersToMove = playerTiles.size() - 1;

        //Flytter siste spiller først for å ikke ødelegge grid.set og grid.remove logikken
        for (int i = numberOfPlayersToMove; i >= 0; i--) {

            PlayerLayerObject playerLayerObject = playerTiles.get(i);
            Position playerPosition = playerLayerObject.getPosition();

            grid.removePlayerPosition(playerPosition);
            playerLayer.setCell(playerPosition.getX(), playerPosition.getY(), null);

            playerLayerObject.moveTileInDirection(direction);
            grid.setPlayerPosition(playerLayerObject);
        }
    }

    /**
     * Check if valid position
     * @param dir
     * @param pos
     * @return true if valid position
     */
    public boolean canGo(Direction dir, Position pos) {
        playerTiles.clear();
        playerTiles = grid.numberOfPlayersToMove(dir, pos);

        if (playerTiles.size() > 0)
            return true;

        return false;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public TiledMap getMap() {
        return map;
    }

    public ArrayList<PlayerLayerObject> getPlayerTiles() {
        return playerTiles;
    }

    /**
     * @param rotate
     * @param currentDir
     * @return the given direction rotated by 90 degrees clockwise or counter clockwise given its parameter
     */
    public Direction rotate(ProgramType rotate, Direction currentDir) {
        switch (rotate) {
            case ROTATELEFT:
                return Direction.rotate(currentDir, -1);
            case ROTATERIGHT:
                return Direction.rotate(currentDir, 1);
            case UTURN:
                return Direction.rotate(Direction.rotate(currentDir, 1), 1);
        }
        return currentDir;
    }

    /**
     * Checks if every player is ready to start the next round
     * @return true if everyone is ready for next round
     */
    public boolean isReadyForRound() {
        for (Player player : players) {
            if (player.getPlayerDeck().handIsEmpty() && player.isAlive() && player.isActive())
                return false;
        }
        return true;
    }

    public boolean getCardsDealt() {
        return cardsDealt;
    }

    public void setCardsDealt(boolean cardsDealt) {
        this.cardsDealt = cardsDealt;
    }

    public Laser getLaser() {
        return laser;
    }

}