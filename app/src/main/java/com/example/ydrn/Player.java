package com.example.ydrn;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {
    //// Game variables ////
    private List<Card> mainDeck;
    private List<Card> tempDeck;
    private List<Card> hand;
    private final int HAND_SIZE = 5;

    private int cargo;
    private MutableLiveData<Integer> currentNumber;
    private int targetNumber;
    private int turn;
    private int level;
    private MutableLiveData<GameState> gameState;
    private TurnState turnState;

    enum TurnState {
        CONTINUE, LAST, WIN, DEAD
    }

    enum GameState {
        START, GAME, SHOP, DEATH
    }

    private final Pair[] STARTER_DECK = {
            new Pair<>("+", 1), new Pair<>("+",1), new Pair<>("+",1),
            new Pair<>("+",2), new Pair<>("+",2),
            new Pair<>("+",3), new Pair<>("+",3),
            new Pair<>("-",1), new Pair<>("-",1), new Pair<>("-",1),
            new Pair<>("-",2), new Pair<>("-",2),
            new Pair<>("-",3), new Pair<>("-",3),
            new Pair<>("*",2), new Pair<>("*",2), new Pair<>("*",3),
            new Pair<>("//",2), new Pair<>("//",2), new Pair<>("//",3),
    };

    //// Shop variables ////
    private List<Card> shopChoices;
    private final int shopSize = 5;
    private final int gachaOdds = 100;
    private final int baseRewardCargo = 10;
    private final Card[] ALL_CARDS = {
            new Card("+",1), new Card("+",2), new Card("+",3),
            new Card("+",4), new Card("+",5), new Card("+",6),
            new Card("+",7), new Card("+",8), new Card("+",9),
            new Card("-",1), new Card("-",2), new Card("-",3),
            new Card("-",4), new Card("-",5), new Card("-",6),
            new Card("-",7), new Card("-",8), new Card("-",9),
            new Card("*",2), new Card("*",3), new Card("*",4), new Card("*",5),
            new Card("//",2), new Card("//",3), new Card("//",4), new Card("//",5),
            new Card("**",2), new Card("**",3),
            new Card("%",10), new Card("%",50), new Card("%",100)
    };

    // Util variables
    private final String TAG = "Player";
    private static Player instance;

    // Singleton Design Pattern, only 1 instance of Player across whole app
    private Player() {
        mainDeck = new ArrayList<>();
        tempDeck = new ArrayList<>();
        hand = new ArrayList<>();
        currentNumber = new MutableLiveData<>();
        gameState = new MutableLiveData<>();

        gameState.setValue(GameState.START);

        // starting level values
        setStartingValues();

        // shop variables
        shopChoices = new ArrayList<>();
    }

    private void setStartingValues() {
        // starting level values
        cargo = 10;
        currentNumber.setValue(0);
        targetNumber = 10;

        turnState = TurnState.CONTINUE;
        turn = 1;
        level = 1;

        mainDeck.clear();
        tempDeck.clear();
        loadStarterDeck();
        resetDeck();
        drawHand();
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private void loadStarterDeck() {
        for (Pair card : STARTER_DECK) {
            String operator = (String) card.first;
            int value = (int) card.second;
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

    private void resetDeck() {
        loadTempDeck();
        Collections.shuffle(tempDeck);
        hand.clear();

        turn = 1;
        turnState = TurnState.CONTINUE;
    }

    private void drawHand() {
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
            currentNumber.setValue(card.use(currentNumber.getValue()));
        }
    }

    /**
     * called when next turn button is pressed
     */
    public void nextTurnButton() {
        endTurn();
        drawHand();
        if (turnState == TurnState.WIN) {
            gameState.setValue(GameState.SHOP);
        } else if (turnState == TurnState.DEAD) {
            gameState.setValue((GameState.DEATH));
        }
    }

    /**
     * Call end of turn actions. if last turn, draw remaining
     */
    private void endTurn() {
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

    //checks game state at the end of the turn. uses 'win', 'last', 'dead', 'continue'
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
        return currentNumber.getValue() == targetNumber;
    }

    public boolean isDead() {
        return tempDeck.size() == 0;
    }

    //Updates cargo, used at the end of turn. Lowest cargo is 0
    private void updateCargo(int number) {
        cargo += number;
        if (cargo < 0) {
            cargo = 0;
        }
    }

    //////////////// SHOP METHODS ////////////////
    // generate shop choices
    public void populateShop() {
        if (cargo == 0) {
            for (int i = 0; i < 5; i++) {
                Card card = new Card("+", 0); // dummy card
                card.setCardUnuseable();
                shopChoices.add(card);
            }
            return;
        }

        // find possible card choices cost < cargo
        List<Card> possibleChoices = new ArrayList<>();
        for (Card card : ALL_CARDS) {
            if (card.getCost() <= cargo) {
                possibleChoices.add(card);
            }
        }

        // make lower cost cards more likely to be drawn
        List<Integer> drawOdds = new ArrayList<>();
        for (Card card : possibleChoices) {
            drawOdds.add(Math.min(cargo - card.getCost() + 1, 5));
        }

        // fill shopChoices
        Random random = new Random();
        for (int i = 0; i < shopSize; i++) {
            int choiceIndex = weightedShopChoice(drawOdds, random);
            Card card = possibleChoices.get(choiceIndex);
            shopChoices.add(card.createCopy()); // copy card to shop choices
        }
        Log.d(TAG, "populateShop: " + shopChoices);
    }

    private int weightedShopChoice(List<Integer> weights, Random random) {
        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight); // some int from 0 to totalWeight
        int index = 0;
        int weightSum = 0;
        for (int weight : weights) {
            weightSum += weight;
            if (randomValue < weightSum) {
                return index;
            }
            index++;
        }
        // This should never be reached if weights are properly provided
        Log.d(TAG, "Weights list is empty or invalid.");
        return 0;
    }

    // called when player buys card in shop, prevents buying unusable or too expensive cards
    public String buyCard(int cardIndex) {
        Card cardChoice = shopChoices.get(cardIndex);
        if (!cardChoice.is_useable()) {
            return "Cant buy this card!";
        }
        if (cardChoice.getCost() > cargo) {
            return "Too expensive!";
        }
        // card can be bought
        mainDeck.add(cardChoice.createCopy());
        cardChoice.setCardUnuseable();
        updateCargo(-1*cardChoice.getCost());
        return "";
    }

    // called when next level pressed
    public void nextLevelButton() {
        shopChoices.clear(); // remove all shop choices
        updateTargetHeading();
        level += 1;
        resetDeck();
        drawHand();

        int gacha = new Random().nextInt(gachaOdds) == 0 ? 50 : 0;
        updateCargo(baseRewardCargo + gacha);

        gameState.setValue(GameState.GAME); // will trigger change fragment
        Log.d(TAG, "nextLevelButton: " + level);
    }

    private void updateTargetHeading() {
        // takes into account current level and current target heading
        // for now do this
        targetNumber += 10;
    }

    //////////////// DEATH METHODS ////////////////
    public void tryAgainButton() {
        setStartingValues();
    }

    //////////////// GETTERS AND SETTERS ////////////////

    public List<Card> getTempDeck() {
        return tempDeck;
    }

    public List<Card> getHand() {
        return hand;
    }

    public MutableLiveData<Integer> getCurrentNumber() {
        return currentNumber;
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public int getLevel() {
        return level;
    }

    public int getTurn() {
        return turn;
    }

    public int getCargo() {
        return cargo;
    }

    public int getCardsLeft() {
        return tempDeck.size();
    }

    public MutableLiveData<GameState> getGameState() {
        return gameState;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }

    public List<Card> getShopChoices() {
        return shopChoices;
    }

    public List<Card> getMainDeck() {
        return mainDeck;
    }
}
