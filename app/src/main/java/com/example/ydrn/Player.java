package com.example.ydrn;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    // constants
    private final int START_TARGET = 10;
    private final int HAND_SIZE = 5;
    private final int START_CARGO = 10;
    private final String[] STARTER_DECK = {
            "+1", "+1", "+1", "+2", "+2", "+3", "+3", "-1", "-1", "-1", "-2", "-2", "-3", "-3",
            "*2", "*2", "*3", "//2", "//2", "//3"
    };
    private final int DEFAULT_SHOP_SIZE = 5;

    // target setting variables
    public int currentNumber;
    public int targetNumber;
    public int cargo;
    public int difficulty;

    // card related variables
    private final List<Card> hand;
    public List<Card> getHand() {
        return hand;
    }
    private final int handSize;
    private final List<Card> mainDeck;
    private final List<Card> tempDeck;
    public int cardsLeft() {
        return tempDeck.size();
    }

    // enum constants are public, static and final
    enum GameState {
        GAME, SHOP, DEATH
    }

    public GameState gameState;

    enum TurnState {
        CONTINUE, LAST, WIN, DEAD
    }

    public TurnState turnState;
    public int level;
    public int turn;

    // shop variables
    public List<Card> shopChoice;
    private int shopSize;

    Player() {
        // game variables
        this.currentNumber = 0;
        this.targetNumber = START_TARGET;
        this.cargo = START_CARGO;
        this.difficulty = 8;

        this.handSize = HAND_SIZE;
        this.mainDeck = new ArrayList<>();
        this.tempDeck = new ArrayList<>();
        this.hand = new ArrayList<>();
        loadStartingDeck();
        resetDeck();
        drawHand();

        this.gameState = GameState.GAME;
        this.turnState = TurnState.CONTINUE;
        this.turn = 1;
        this.level = 1;

        // shop variables
        this.shopChoice = new ArrayList<>();
        this.shopSize = DEFAULT_SHOP_SIZE;
    }

    /**
     * fill main deck with pre defined starter cards
     */
    private void loadStartingDeck() {
        for (String card : STARTER_DECK) {
            String operator = card.substring(0,card.length()-1);
            Log.d("Player.loadStartingDeck", "Card: "+card);
            int value = Character.getNumericValue(card.charAt(card.length()-1));
            Card new_card = new Card(operator, value);
            mainDeck.add(new_card);
            Log.d("Player.loadStartingDeck", new_card.toString());
        }
    }

    /**
     * Make a copy of main deck and copy to temp deck
     */
    private void loadTempDeck() {
        this.tempDeck.clear();
        for (Card c : mainDeck) {
            tempDeck.add(c.createCopy());
        }
//        Log.d("Player.loadTempDeck",);
    }

    /**
     * Reset deck, empty hand, to run at start of every level
     */
    public void resetDeck() {
        loadTempDeck();
        Collections.shuffle(tempDeck);
        hand.clear();

        turn = 1;
        turnState = TurnState.CONTINUE;
    }

    /**
     * draw 5 cards to hand
     */
    public void drawHand() {
        for (int i = 0; i < HAND_SIZE; i++) {
            drawCard();
        }
    }

    /**
     * Checks if temp deck is empty before drawing a card. Will quit immediately if temp deck empty
     */
    public void drawCard() {
        if (tempDeck.size() == 0) {
            return;
        }
        Card card = tempDeck.remove(tempDeck.size() - 1);
        Log.d("Player.drawCard", card.toString());
        hand.add(card);
    }

    /**
     * find card in hand, use card, update current number
     */
    public void playCard(int position) {
        Card card = hand.get(position);
        if (card.is_useable()) {
            currentNumber = card.use(currentNumber);
        }
    }

    /**
     * called when next turn button is pressed
     */
    public void nextTurnButton() {
        endTurn();
        drawHand();
    }
    /**
     * Call end of turn actions. if last turn, draw remaining
     */
    public void endTurn() {
        // put useable cards back into deck
        for (Card card: hand) {
            if (card.is_useable()) {
                tempDeck.add(0, card);
            }
        }
        hand.clear();
        updateTurnState();
        if (turnState != TurnState.WIN) {
            updateCargo(-1);
            turn += 1;
        }
    }

    /**
     * checks game state at the end of the turn. uses 'win', 'last', 'dead', 'continue'
     */
    private void updateTurnState() {
        if (isWin()) {
            turnState = TurnState.WIN;
        }
        else if (isDead() || turnState == TurnState.LAST) {
            turnState = TurnState.DEAD;
        }
        else {
            turnState = TurnState.CONTINUE;
        }
    }

    public boolean isWin() {
        return currentNumber == targetNumber;
    }

    public boolean isDead() {
        return hand.size() == 0;
    }

    /**
     * Updates cargo, used at the end of turn. Lowest cargo is 0
     * @param number number of cargo to update
     */
    private void updateCargo(int number) {
        cargo += number;
        if (cargo < 0) {
            cargo = 0;
        }
    }
}
