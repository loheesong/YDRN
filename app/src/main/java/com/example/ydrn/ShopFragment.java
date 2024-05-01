package com.example.ydrn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShopFragment extends Fragment {
    private ImageView[] cardList;
    private TextView[] cardCostList;
    private TextView greetingTextView;
    private TextView deckInfoTextView;
    private TextView cargoLeftTextView;
    private Button nextLevelButton;
    private Player player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Player singleton
        player = Player.getInstance();
    }

    // init UI here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        greetingTextView = view.findViewById(R.id.greetings);
        cargoLeftTextView = view.findViewById(R.id.cargoLeft);
        cardList = new ImageView[]{
            view.findViewById(R.id.card1),
            view.findViewById(R.id.card2),
            view.findViewById(R.id.card3),
            view.findViewById(R.id.card4),
            view.findViewById(R.id.card5),
        };
        cardCostList = new TextView[]{
            view.findViewById(R.id.cardCost1),
            view.findViewById(R.id.cardCost2),
            view.findViewById(R.id.cardCost3),
            view.findViewById(R.id.cardCost4),
            view.findViewById(R.id.cardCost5),
        };
        deckInfoTextView = view.findViewById(R.id.deckInfo);
        nextLevelButton = view.findViewById(R.id.nextLevel);

        return view;
    }

    // set listeners here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bind all text to text view

    }

    private void updateAllText() {
        greetingTextView.setText(String.valueOf("Greetings"));
        cargoLeftTextView.setText(formatCargoLeft(player.getCargo()));
    }

    ////////////////////////////////// Util functions //////////////////////////////////
    private String formatCargoLeft(int playerCargoLeft) {
        final String format = "CARGO LEFT: ";
        return format + playerCargoLeft;
    }

    // TODO
    private String formatDeckInfo() {
        return "";
    }

    private String formatCardCost(Card card) {
        return "";
    }


}