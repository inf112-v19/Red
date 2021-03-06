package inf112.skeleton.app.CardTests;

import static junit.framework.TestCase.assertEquals;

import inf112.skeleton.app.Cards.PlayerDeck;
import inf112.skeleton.app.Cards.ProgramType;
import inf112.skeleton.app.Cards.ProgramCard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PlayerDeckTest {

    /**
     * Tests if the selected card for transfer from player deck to hand is too high
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void playerDeckIsTooSmall() {
        PlayerDeck pd = new PlayerDeck();
        pd.setDeck(creatDeckOfNcards(3));
        pd.selectCardsForHand();
    }

    /**
     * Tests if full hand can receive more cards from player deck
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void selectCardFromDeckOverMaxAmountOfCardsOnHand() {
        PlayerDeck pd = new PlayerDeck();
        pd.setDeck(creatDeckOfNcards(9));
        pd.numberOfNewCardsToHand = 10;
        pd.selectCardsForHand();
    }

    /**
     * Tests if one can retrieve card from empty hand
     */
    @Test(expected = NoSuchElementException.class)
    public void getCardFromHandNoCardsLeft() {
        PlayerDeck pd = new PlayerDeck();
        pd.getCardFromHand(2);
    }

    /**
     * Tests if 10 cards can be set to player deck, i.e. more than allowed
     */
    @Test(expected = IllegalArgumentException.class)
    public void setDeckNoMoreThan9CardsInPlayerDeck() {
        PlayerDeck pd = new PlayerDeck();

        int moreThan9 = 10;
        ArrayList<ProgramCard> newDeck = creatDeckOfNcards(moreThan9);

        pd.setDeck(newDeck);
    }

    @Test
    public void deckIsEmptyTest() {
        PlayerDeck pd = new PlayerDeck();
        boolean isEmpty = pd.deckIsEmpty();
        assertEquals(true, isEmpty);
    }

    @Test
    public void handIsEmptyTest() {
        PlayerDeck pd = new PlayerDeck();
        boolean isEmpty = pd.handIsEmpty();
        assertEquals(true, isEmpty);
    }

    @Test
    public void deckIsNotEmpty() {
        PlayerDeck pd = new PlayerDeck();
        pd.setDeck(creatDeckOfNcards(1));
        boolean isNotEmpty = pd.deckIsEmpty();
        assertEquals(false, isNotEmpty);
    }

    @Test
    public void handIsNotEmpty() {
        PlayerDeck pd = new PlayerDeck();
        pd.setDeck(creatDeckOfNcards(5));
        pd.selectCardsForHand();
        boolean isNotEmpty = pd.handIsEmpty();
        assertEquals(false, isNotEmpty);
    }

    /**
     * Tests if player deck is empty after having a deck of 1 card then transfering it
     * to the hand
     */
    @Test
    public void deckIsEmptyAfterHandTransfer() {
        PlayerDeck pd = new PlayerDeck();
        pd.setDeck(creatDeckOfNcards(5));
        pd.selectCardsForHand();
        boolean isNotEmpty = pd.deckIsEmpty();
        assertEquals(true, isNotEmpty);
    }

    @Test
    public void deckSizeTest() {
        PlayerDeck pd = new PlayerDeck();

        for (int i = 0; i < PlayerDeck.MAX_NUMBER_CARDS_IN_DECK; i++) {
            pd.setDeck(creatDeckOfNcards(i));
            assertEquals(i, pd.deckSize());
        }
    }

    @Test
    public void handSizeTest() {
        PlayerDeck pd = new PlayerDeck();
        pd.setDeck(creatDeckOfNcards(9));
        pd.selectCardsForHand();
        int nCardsOnHand = 5;
        assertEquals(nCardsOnHand, pd.handSize());
    }


    public ArrayList<ProgramCard> creatDeckOfNcards(int nCards) {
        ArrayList<ProgramCard> deck = new ArrayList<>();
        for (int i = 0; i < nCards; i++) {
            ProgramCard card = new ProgramCard(ProgramType.MOVE1, 100, "");
            deck.add(card);
        }
        return deck;
    }

}