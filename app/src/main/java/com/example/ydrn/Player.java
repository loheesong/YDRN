package com.example.ydrn;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private List<Card> mainDeck;
    private List<Card> tempDeck;
    private List<Card> hand;
    private final int HAND_SIZE = 5;

    private int cargo;
    private int currentNumber;
    private int targetNumber;
    private int turn;
    private int level;
    private GameState gameState;
    private TurnState turnState;

    enum TurnState {
        CONTINUE, LAST, WIN, DEAD
    }

    enum GameState {
        GAME, SHOP, DEATH
    }

    private final String[] STARTER_DECK = {
            "+1", "+1", "+1", "+2", "+2", "+3", "+3",
            "-1", "-1", "-1", "-2", "-2", "-3", "-3",
            "*2", "*2", "*3",
            "//2", "//2", "//3",
    };

    private final String TAG = "Player";
    private static Player instance;

    // Singleton Design Pattern, only 1 instance of Player across whole app
    private Player() {
        mainDeck = new ArrayList<>();
        tempDeck = new ArrayList<>();
        hand = new ArrayList<>();

        // starting level values
        cargo = 10;
        currentNumber = 0;
        targetNumber = 10;

        loadStarterDeck();
        resetDeck();
        drawHand();

        gameState = GameState.GAME;
        turnState = TurnState.CONTINUE;
        turn = 1;
        level = 1;

        // shop variables

    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private void loadStarterDeck() {
        for (String card : STARTER_DECK) {
            String operator = card.substring(0,card.length()-1);
            int value = Character.getNumericValue(card.charAt(card.length()-1));
            Card new_card = new Card(operator, value);
            mainDeck.add(new_card);
        }
    }

    private void loadTempDeck() {
        Log.i(TAG, "loadTempDeck: ");
        tempDeck.clear();
        for (Card c : mainDeck) {
            tempDeck.add(c.createCopy());
        }
    }

    public void resetDeck() {
        loadTempDeck();
        Collections.shuffle(tempDeck);
        hand.clear();

        turn = 1;
        turnState = TurnState.CONTINUE;
    }

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

    //////////////// GETTERS AND SETTERS ////////////////

    public List<Card> getTempDeck() {
        return tempDeck;
    }

    public List<Card> getHand() {
        return hand;
    }
}
