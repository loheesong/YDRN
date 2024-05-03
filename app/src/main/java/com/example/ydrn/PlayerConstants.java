package com.example.ydrn;

import android.util.Pair;

public class PlayerConstants {
    static final int HAND_SIZE = 5;
    static final int difficulty = 8;
    static final Pair[] STARTER_DECK = {
            new Pair<>("+", 1), new Pair<>("+",1), new Pair<>("+",1),
            new Pair<>("+",2), new Pair<>("+",2),
            new Pair<>("+",3), new Pair<>("+",3),
            new Pair<>("-",1), new Pair<>("-",1), new Pair<>("-",1),
            new Pair<>("-",2), new Pair<>("-",2),
            new Pair<>("-",3), new Pair<>("-",3),
            new Pair<>("*",2), new Pair<>("*",2), new Pair<>("*",3),
            new Pair<>("//",2), new Pair<>("//",2), new Pair<>("//",3),
    };
    static final Card[] ALL_CARDS = {
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
    static final int shopGachaOdds = 100; // must be above 0
    static final int baseRewardCargo = 10;
}