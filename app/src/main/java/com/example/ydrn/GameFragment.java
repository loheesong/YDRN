package com.example.ydrn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment {
    // UI variables
    private TextView currentTextView;
    private TextView targetTextView;
    private TextView levelTextView;
    private TextView turnTextView;
    private TextView cardsLeftTextView;
    private TextView cargoLeftTextView;
    private ImageView[] cardList;
    private Button nextTurnButton;
    private Button concedeButton;

    // game variables
    private Player player;

    private final String TAG = "GameFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Player singleton
        player = Player.getInstance();
    }

    // create UI here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        // get reference to UI elements
        currentTextView = view.findViewById(R.id.current);
        targetTextView = view.findViewById(R.id.target);
        levelTextView = view.findViewById(R.id.level);
        turnTextView = view.findViewById(R.id.turn);
        cardsLeftTextView = view.findViewById(R.id.cardsLeft);
        cargoLeftTextView = view.findViewById(R.id.cargoLeft);
        targetTextView = view.findViewById(R.id.target);
        cardList = new ImageView[] {
                view.findViewById(R.id.card1),
                view.findViewById(R.id.card2),
                view.findViewById(R.id.card3),
                view.findViewById(R.id.card4),
                view.findViewById(R.id.card5)
        };
        nextTurnButton = view.findViewById((R.id.nextTurn));
        concedeButton = view.findViewById(R.id.tryAgain);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");

        // bind image to five card UI
        updateFiveCardsUI();

        // bind text view data
        updateAllText();

        // click listener for five cards
        for (int i = 0; i < 5; i++) {
            ImageView imageView = cardList[i];
            int index = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: "+ index);
                    player.playCard(index);
                    updateFiveCardsUI();
                    if (player.isWin()) {
                        nextTurnButton.setText(R.string.toShop);
                    } else {
                        nextTurnButton.setText(R.string.nextTurn);
                    }
                }
            });
        }

        // observer for current heading
        player.getCurrentNumber().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (player.getCurrentNumber().getValue() != null) {
                    currentTextView.setText(String.valueOf(player.getCurrentNumber().getValue()));
                } else {
                    Log.d(TAG, "CURRENT NUMBER IS NULL");
                }
            }
        });

        // click listener for next turn button
        nextTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.nextTurnButton();
                updateFiveCardsUI();
                updateAllText();
                Log.d(TAG, "nextTurn click "+ player.isWin());
                Log.d(TAG, "nextTurn click "+ player.getTurnState());
                Log.d(TAG, "nextTurn click "+ player.getGameState().getValue());
            }
        });

        // click listener for concede button
        concedeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.getGameState().setValue(Player.GameState.DEATH);
            }
        });

        // prepare ui when changed to this fragment
        player.getGameState().observe(getViewLifecycleOwner(), new Observer<Player.GameState>() {
            @Override
            public void onChanged(Player.GameState gameState) {
                if (gameState == Player.GameState.GAME) {
                    updateAllText();
                    updateFiveCardsUI();
                    nextTurnButton.setText(R.string.nextTurn);
                }
            }
        });
    }

    ////////////////////////////// Util methods //////////////////////////////
    private void updateAllText() {
        levelTextView.setText(formatLevelText(player.getLevel()));
        turnTextView.setText(formatTurnText(player.getTurn()));
        cardsLeftTextView.setText(formatCardsLeft(player.getCardsLeft()));
        cargoLeftTextView.setText(formatCargoLeft(player.getCargo()));
        targetTextView.setText(String.valueOf(player.getTargetNumber()));
    }
    private void updateFiveCardsUI() {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = cardList[i];
            if (i >= player.getHand().size()) {
                return;
            }
            Card card = player.getHand().get(i);
            if (card.is_useable()) {
                imageView.setImageResource(card.getResource());
            } else {
                imageView.setImageResource(card.getBackResource());
            }
        }
    }

    private String formatLevelText(int playerLevel) {
        final String format = "LEVEL: ";
        return format + playerLevel;
    }
    private String formatTurnText(int playerTurn) {
        final String format = "TURN: ";
        return format + playerTurn;
    }
    private String formatCardsLeft(int playerCardLeft) {
        final String format = "CARDS LEFT: ";
        return format + playerCardLeft;
    }
    private String formatCargoLeft(int playerCargoLeft) {
        final String format = "CARGO LEFT: ";
        return format + playerCargoLeft;
    }
}