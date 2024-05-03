package com.example.ydrn;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Card {
    // cardAssets used for rendering card
    public static final Map<String, Integer> cardAssets;
    static {
        cardAssets = new HashMap<>();

        cardAssets.put("+1", R.drawable.plus1); cardAssets.put("+2", R.drawable.plus2); cardAssets.put("+3", R.drawable.plus3);
        cardAssets.put("+4", R.drawable.plus4); cardAssets.put("+5", R.drawable.plus5); cardAssets.put("+6", R.drawable.plus6);
        cardAssets.put("+7", R.drawable.plus7); cardAssets.put("+8", R.drawable.plus8); cardAssets.put("+9", R.drawable.plus9);

        cardAssets.put("-1", R.drawable.minus1); cardAssets.put("-2", R.drawable.minus2); cardAssets.put("-3", R.drawable.minus3);
        cardAssets.put("-4", R.drawable.minus4); cardAssets.put("-5", R.drawable.minus5); cardAssets.put("-6", R.drawable.minus6);
        cardAssets.put("-7", R.drawable.minus7); cardAssets.put("-8", R.drawable.minus8); cardAssets.put("-9", R.drawable.minus9);

        cardAssets.put("*2", R.drawable.times2); cardAssets.put("*3", R.drawable.times3); cardAssets.put("*4", R.drawable.times4); cardAssets.put("*5", R.drawable.times5);

        cardAssets.put("//2", R.drawable.divide2); cardAssets.put("//3", R.drawable.divide3); cardAssets.put("//4", R.drawable.divide4); cardAssets.put("//5", R.drawable.divide5);

        cardAssets.put("%10", R.drawable.mod10); cardAssets.put("%50", R.drawable.mod50); cardAssets.put("%100", R.drawable.mod100);

        cardAssets.put("**2", R.drawable.power2); cardAssets.put("**3", R.drawable.power3);

        cardAssets.put("back", R.drawable.back);
    }
    public static final Map<String, BiFunction<Integer, Integer, Integer>> operationsTable;
    static {
        operationsTable = new HashMap<>();
        operationsTable.put("+", (currentValue, cardValue) -> currentValue + cardValue);
        operationsTable.put("-", (currentValue, cardValue) -> currentValue - cardValue);
        operationsTable.put("*", (currentValue, cardValue) -> currentValue * cardValue);
        operationsTable.put("//", (currentValue, cardValue) -> Math.floorDiv(currentValue, cardValue));
        operationsTable.put("%", (currentValue, cardValue) -> currentValue % cardValue);
        operationsTable.put("**", (currentValue, cardValue) -> (int) Math.pow(currentValue, cardValue));
    }

    public static final Map<String, Integer> costTiers;
    static {
        costTiers = new HashMap<>();
        costTiers.put("+", 1);
        costTiers.put("-", 1);
        costTiers.put("*", 2);
        costTiers.put("//", 2);
        costTiers.put("**", 3);
    }

    private String operator;
    private int value;
    private BiFunction<Integer, Integer, Integer> function;
    private boolean useable;
    private int cost;

    Card(String operator, int value) {
        this.operator = operator;
        this.value = value;
        this.useable = true;

        if (operator.equals("%")) {
            switch (value) {
                case 10:
                    this.cost = 5;
                    break;
                case 50:
                    this.cost = 8;
                    break;
                case 100:
                    this.cost = 10;
                    break;
                default:
                    this.cost = 100;
            }
        } else {
            this.cost = value * costTiers.get(operator);
        }
        this.function = operationsTable.get(operator);
    }

    @NonNull
    @Override
    public String toString() {
        return operator + value;
    }

    /**
     *
     * @param currentValue current number
     * @return number after this card is applied
     */
    public int use(int currentValue) {
        setCardUnuseable();
        return function.apply(currentValue, this.value);
    }

    public Card createCopy() {
        return new Card(this.operator, this.value);
    }

    public boolean is_useable() {
        return this.useable;
    }

    public void setCardUnuseable() {
        this.useable = false;
    }

    //////////////////////// GETTER AND SETTER ////////////////////////
    public Integer getResource() {
        Log.d("getResource", operator + value);
        return cardAssets.get(operator + value);
    }

    public Integer getBackResource() {
        return cardAssets.get("back");
    }

    public int getCost() {
        return cost;
    }
}