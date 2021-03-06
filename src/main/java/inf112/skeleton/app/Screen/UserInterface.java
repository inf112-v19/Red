package inf112.skeleton.app.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import inf112.skeleton.app.Cards.PlayerDeck;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Cards.ProgramCardDeck;
import inf112.skeleton.app.Directions.Position;
import inf112.skeleton.app.Player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserInterface {

    private ImageButton playButton;
    private ImageButton powerDownButton;

    private Image damageTokenImage;
    private Image flagCheckImage;

    private Image cardSlotsTop;
    private Image cardSlotsMid;
    private Image cardSlotsBottom;
    private Image leftBar;
    private Image rightBar;

    private boolean cardSelection = false;

    Position pos[] = new Position[5];
    ProgramCard chosenCards[];
    ProgramCard lockedCards[];
    HashMap<Image, ProgramCard> cardMap = new HashMap<>();
    HashMap<ProgramCard, Image> imageMap = new HashMap<>();

    private boolean hasPoweredDown = false;

    private Stage stage;
    private float width;
    private Player player;
    private int health;
    private int lifeTokens;
    private int lastFlagVisited;

    public UserInterface(float width, Player player) {
        this.width = width;
        this.player = player;
        this.health = player.getHealth();
        this.lifeTokens = player.getLifeTokens();
        this.lastFlagVisited = player.getLastFlagVisited();

        stage = new Stage();
        initializeSideBars();
        initializePlayButton();
        initializePowerDownButton();
        initializeCardSlots();
    }

    private void initializeLockedCards() {
        ArrayList<ProgramCard> lastHand = new ArrayList<>(player.getPlayerDeck().getHandFromLastRound());
        int lCs = player.getPlayerDeck().numberOfLockedCards;
        lockedCards = new ProgramCard[lCs];
            ProgramCardDeck pcg = ProgramCardDeck.getProgramCardDeckSingleton();
            for (int i = 0; i < lCs; i++) {
                if (lastHand.isEmpty()) {
                    lockedCards[i] = pcg.takeRandomCard();
                } else {
                    lockedCards[i] = lastHand.get(i);
                }
            }
    }

    private void initializeChosenCards() {
        int lC = player.getPlayerDeck().numberOfLockedCards;
        chosenCards = new ProgramCard[5];
        for (int i = 0; i < lC; i++) {
            chosenCards[i] = lockedCards[i];
            placeLockedCards(lockedCards[i], i);
        }
    }

    private void placeLockedCards(ProgramCard card, int i) {
        Sprite picture = new Sprite(new Texture(card.getFilename()));
        final Image cardImage = new Image(new SpriteDrawable(picture));
        float pWidth = (picture.getWidth() / 8) * 0.8f;
        float pHeight = (picture.getHeight() / 8) * 0.8f;
        float cardX = pos[i].getX();
        float cardY = pos[i].getY();
        cardImage.setWidth(pWidth);
        cardImage.setHeight(pHeight);
        cardImage.setPosition(cardX, cardY);
        stage.addActor(cardImage);
    }

    public Player getPlayer() {
        return player;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Checks the number of damageTokens of the player and updates the graphic displaying it
     */
    public void getDamageTokenOfPlayer() {
        if (stage.getActors().contains(damageTokenImage, false))
            stage.getActors().removeValue(damageTokenImage, false);

        int damageTokens = player.getHealth();
        Texture damageTokenTexture;
        switch (damageTokens) {
            case 10:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens10.png");
                break;
            case 9:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens9.png");
                break;
            case 8:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens8.png");
                break;
            case 7:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens7.png");
                break;
            case 6:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens6.png");
                break;
            case 5:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens5.png");
                break;
            case 4:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens4.png");
                break;
            case 3:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens3.png");
                break;
            case 2:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens2.png");
                break;
            case 1:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens1.png");
                break;
            case 0:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens0.png");
                break;
            default:
                damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens0.png");
                break;
        }
        if (!player.isAlive())
            damageTokenTexture = new Texture("assets/userInterface/damageTokens/damageTokens0.png");
        Sprite damageTokenSprite = new Sprite(damageTokenTexture);
        damageTokenImage = new Image(new SpriteDrawable(damageTokenSprite));
        damageTokenImage.setHeight(damageTokenImage.getHeight() / 7 + 14);
        damageTokenImage.setWidth(width / 2);
        damageTokenImage.setPosition(width / 2, 0);
        stage.addActor(damageTokenImage);
    }

    /**
     * Checks the number of lifeTokens of the player and updates the graphic displaying it
     */
    public void getLifeTokenOfPlayer() {
        int lifeTokens = player.getLifeTokens();
        Texture lifeTokenTexture;
        switch (lifeTokens) {
            case 3:
                lifeTokenTexture = new Texture("assets/userInterface/lifeTokens/lifeTokens3.png");
                break;
            case 2:
                lifeTokenTexture = new Texture("assets/userInterface/lifeTokens/lifeTokens2.png");
                break;
            case 1:
                lifeTokenTexture = new Texture("assets/userInterface/lifeTokens/lifeTokens1.png");
                break;
            case 0:
                lifeTokenTexture = new Texture("assets/userInterface/lifeTokens/lifeTokens0.png");
                break;
            default:
                lifeTokenTexture = new Texture("assets/userInterface/lifeTokens/lifeTokens0.png");
                break;
        }
        Sprite lifeTokenSprite = new Sprite(lifeTokenTexture);
        final Image lifeTokenImage = new Image(new SpriteDrawable(lifeTokenSprite));
        lifeTokenImage.setHeight(lifeTokenImage.getHeight() / 8 + 5);
        lifeTokenImage.setWidth(lifeTokenImage.getWidth() / 8);
        lifeTokenImage.setPosition(width - lifeTokenImage.getWidth() - playButton.getWidth(), 43);
        stage.addActor(lifeTokenImage);
    }

    public void getFlagInfo() {
        if (stage.getActors().contains(flagCheckImage, false))
            stage.getActors().removeValue(flagCheckImage, false);

        int lastFlagVisited = player.getLastFlagVisited();
        Texture flagCheckTexture;
        switch (lastFlagVisited) {
            case 3:
                flagCheckTexture = new Texture("assets/userInterface/flagChecks/flagCheck3.png");
                break;
            case 2:
                flagCheckTexture = new Texture("assets/userInterface/flagChecks/flagCheck2.png");
                break;
            case 1:
                flagCheckTexture = new Texture("assets/userInterface/flagChecks/flagCheck1.png");
                break;
            default:
                flagCheckTexture = new Texture("assets/userInterface/flagChecks/flagCheck0.png");
                break;
        }
        Sprite flagCheckSprite = new Sprite(flagCheckTexture);
        flagCheckImage = new Image(new SpriteDrawable(flagCheckSprite));
        flagCheckImage.setHeight(flagCheckImage.getHeight() / 8 + 11);
        flagCheckImage.setWidth(flagCheckImage.getWidth() / 8);
        flagCheckImage.setPosition(width / 2, 43);
        flagCheckImage.toFront();
        stage.addActor(flagCheckImage);
    }

    private void initializeCardSlots() {
        //Creates the slot bars
        Sprite picture = new Sprite(new Texture("assets/hand/hand5v3.png"));
        cardSlotsTop = new Image(new SpriteDrawable(picture));
        cardSlotsTop.setWidth(width / 2);
        cardSlotsTop.setHeight(picture.getHeight() + 25);
        cardSlotsTop.setPosition(424, 292);
        cardSlotsTop.setScale(0.8f);
        cardSlotsTop.setColor(Color.LIGHT_GRAY);

        picture = new Sprite(new Texture("assets/hand/hand5v3.png"));
        cardSlotsMid = new Image(new SpriteDrawable(picture));
        cardSlotsMid.setWidth(width / 2);
        cardSlotsMid.setHeight(picture.getHeight() + 25);
        cardSlotsMid.setPosition(424, 200);
        cardSlotsMid.setScale(0.8f);
        cardSlotsMid.setColor(Color.LIGHT_GRAY);

        picture = new Sprite(new Texture("assets/hand/hand5v3.png"));
        cardSlotsBottom = new Image(new SpriteDrawable(picture));
        cardSlotsBottom.setWidth(width / 2);
        cardSlotsBottom.setHeight(picture.getHeight() + 25);
        cardSlotsBottom.setScale(0.8f);
        cardSlotsBottom.setPosition(424, 108);


    }

    private void initializeSideBars() {
        Sprite picture = new Sprite(new Texture("assets/userInterface/rightSideBar.png"));
        leftBar = new Image(new SpriteDrawable(picture));
        leftBar.setWidth(leftBar.getWidth() * 2);
        leftBar.setHeight(leftBar.getHeight() / 2);
        leftBar.setPosition(width / 2, 0);

        picture = new Sprite(new Texture("assets/userInterface/leftSideBar.png"));
        rightBar = new Image(new SpriteDrawable(picture));
        rightBar.setWidth(rightBar.getWidth() * 2);
        rightBar.setHeight(rightBar.getHeight() / 2);
        rightBar.setPosition(width - rightBar.getWidth(), 0);
    }

    private void initializePlayButton() {
        Sprite picture = new Sprite(new Texture("assets/userInterface/playButton.png"));
        playButton = new ImageButton(new SpriteDrawable(picture));
        playButton.setWidth(playButton.getWidth() / 7);
        playButton.setHeight(playButton.getHeight() / 7 + 4);
        playButton.setPosition(width - playButton.getWidth(), 78);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int count = 0;
                for (int i = 0; i < chosenCards.length; i++)
                    if (chosenCards[i] == null)
                        count++;
                if (count > 0) {
                    System.out.println("Choose " + (count) + " more cards");
                    return;
                }

                if (!player.getHandChosen()) {
                    System.out.println("Cards selected");
                    ArrayList<ProgramCard> list = new ArrayList<>(Arrays.asList(chosenCards));
                    player.getPlayerDeck().setPlayerHand(list);
                    player.setHandChosen(true);
                }
            }
        });
    }

    private void initializePowerDownButton() {
        Sprite picture = new Sprite(new Texture("assets/userInterface/powerDownButton.png"));
        powerDownButton = new ImageButton(new SpriteDrawable(picture));
        powerDownButton.setWidth(powerDownButton.getWidth() / 7);
        powerDownButton.setHeight(powerDownButton.getHeight() / 7 + 4);
        powerDownButton.setPosition(width - powerDownButton.getWidth(), 42);
        powerDownButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (hasChosenCards()) {
                    System.out.println("Cards selected");
                    ArrayList<ProgramCard> list = new ArrayList<ProgramCard>(Arrays.asList(chosenCards));
                    player.getPlayerDeck().setPlayerHand(list);
                    player.setHandChosen(true);

                    System.out.println("PLAYER " + player.getPlayerTile().getColor() + " WILL POWER DOWN NEXT ROUND");
                    hasPoweredDown = true;
                } else
                    System.out.println("Cannot power down before all 5 cards are chosen");
            }
        });
    }

    public void initializeCardSelection() {
        if(!cardSelection)
            return;
        // If player has selected powerdown, then do not distribute new cards. PowerDown
        if (hasPoweredDown || !player.isAlive()) {
            player.powerDown();
            player.setHandChosen(true);
            hasPoweredDown = false;
            return;
        }
        PlayerDeck deck = player.getPlayerDeck();
        for (int i = 0; i < deck.deckSize(); i++) {
            ProgramCard programCard = deck.getCard(i);
            Sprite picture = new Sprite(new Texture(programCard.getFilename()));
            final Image cardImage = new Image(new SpriteDrawable(picture));
            float pWidth = (picture.getWidth() / 8) * 0.8f;
            float pHeight = (picture.getHeight() / 8) * 0.8f;
            float firstCardX = 428;
            float firstCardY = 298;
            cardImage.setWidth(pWidth);
            cardImage.setHeight(pHeight);

            //Place 5 first cards in top row and save position as origin
            if (i < 5) {
                cardImage.setPosition(firstCardX + (i * pWidth), firstCardY);
                cardImage.setOrigin(firstCardX + (i * pWidth), firstCardY);

                //List of coordinates for the bottom slot row for easy access
                pos[i] = new Position((int) (firstCardX + (i * pWidth)), (int) (firstCardY - pHeight * 2 - 26));
            }
            
            //Place remaining 4 cards in row beneath
            else {
                cardImage.setPosition(firstCardX + ((i - 5) * pWidth), firstCardY - pHeight - 13);
                cardImage.setOrigin(firstCardX + ((i - 5) * pWidth), firstCardY - pHeight - 13);
            }

            //Adds dragging functionality to each image
            cardImage.addListener(new DragListener() {
                public void drag(InputEvent event, float x, float y, int pointer) {
                    cardImage.moveBy(x - cardImage.getWidth() / 2, y - cardImage.getHeight() / 2);
                    cardImage.toFront();
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    super.dragStop(event, x, y, pointer);
                    cardListener(cardImage);
                }
            });

            stage.addActor(cardImage);
            cardMap.put(cardImage, programCard);
            imageMap.put(programCard, cardImage);
        }
    }

    private void cardListener(Image cardImage) {
        //Coordinates for card drop zone
        float yLimitTop = cardSlotsBottom.getTop();
        float yLimitBottom = cardSlotsBottom.getTop() - cardSlotsBottom.getHeight();

        //Coordinates for dropped card
        ProgramCard programCard = cardMap.get(cardImage);
        float x = cardImage.getX() + cardImage.getWidth() / 2;
        float y = cardImage.getTop() - cardImage.getHeight() / 2;
        int index = 0;

        //If card is already in the selected list, reset its position
        if (programCard.isMarked()) {
            index = getIndex(programCard);
            if (index != -1)
            chosenCards[index] = null;
        }

        for (int i = player.getPlayerDeck().numberOfLockedCards; i < pos.length; i++) {
            //If inside the zone
            if (x > pos[i].getX() - cardImage.getWidth() / 2 && x < pos[i].getX() + cardImage.getWidth()
                    && y < yLimitTop && y > yLimitBottom) {

                //If overlap, reset card in the selected list when current card comes from deck
                if (chosenCards[i] != null && !programCard.isMarked()) {
                    Image im = imageMap.get(chosenCards[i]);
                    im.setPosition(im.getOriginX(), im.getOriginY());
                    chosenCards[i].setMarked(false);

                    //If overlap and current card comes from selected list, switch places with card already there
                } else if (chosenCards[i] != null && !chosenCards[i].equals(programCard) && programCard.isMarked()) {
                    Image other = imageMap.get(chosenCards[i]);

                    //Save the current cards old position
                    Position oldPos = pos[index];

                    //Set other card to current cards old position
                    other.setPosition(oldPos.getX(), oldPos.getY());
                    chosenCards[index] = cardMap.get(other);
                }

                //Set new card to the new position
                cardImage.setPosition(pos[i].getX(), pos[i].getY());
                programCard.setMarked(true);
                chosenCards[i] = programCard;
                return;
            }
        }
        cardImage.setPosition(cardImage.getOriginX(), cardImage.getOriginY());
        programCard.setMarked(false);
        for (int k = player.getPlayerDeck().numberOfLockedCards; k < chosenCards.length; k++) {
            if (chosenCards[k] != null && chosenCards[k].equals(programCard)) {
                chosenCards[k] = null;
                return;
            }
        }
    }

    private int getIndex(ProgramCard card) {
        for (int i = 0; i < chosenCards.length; i++) {
            if (chosenCards[i] != null && chosenCards[i].equals(card))
                return i;
        }
        return -1;
    }

    public boolean hasChosenCards() {
        for (int i = 0; i < chosenCards.length; i++) {
            if (chosenCards[i] == null)
                return false;
        }
        return true;
    }

    public void prepareNextRound() {
        chosenCards = new ProgramCard[5];
        cardMap.clear();
        imageMap.clear();
        stage.clear();
        stage.addActor(leftBar);
        stage.addActor(rightBar);
        stage.addActor(cardSlotsTop);
        stage.addActor(cardSlotsMid);
        stage.addActor(cardSlotsBottom);
        stage.addActor(playButton);
        stage.addActor(powerDownButton);
        getDamageTokenOfPlayer();
        getLifeTokenOfPlayer();
        getFlagInfo();

        if(!cardSelection)
            return;

        initializeLockedCards();
        initializeChosenCards();
    }

    public int getSavedHealth() {
        return health;
    }

    public void setSavedHealth(int health) {
        this.health = health;
    }

    public int getSavedLifeTokens() {
        return lifeTokens;
    }

    public void setSavedLifeTokens(int lifeTokens) {
        this.lifeTokens = lifeTokens;
    }

    public int getSavedFlag() {
        return lastFlagVisited;
    }

    public void setSavedFlag(int flag) {
        this.lastFlagVisited = flag;
    }

    public void toggleCardSelection (){
        cardSelection = !cardSelection;
        if(cardSelection)
            this.initializeCardSelection();
    }
}
