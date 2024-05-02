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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        player.populateShop();
        updateAllTextAndImage();

        // click listeners for buying cards
        for (int i = 0; i < 5; i++) {
            cardList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });
        }

        // click listener for next level button
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    private void updateAllTextAndImage() {
        greetingTextView.setText(player.generateGreetings());
        cargoLeftTextView.setText(formatCargoLeft(player.getCargo()));
        List<Card> shopChoices = player.getShopChoices();
        for (int i = 0; i < 5; i++) {
            int imageResource;
            String costString;
            if (shopChoices.size() == 0) {
                imageResource = R.drawable.back;
                costString = "COST: INF";
            } else {
                Card card = shopChoices.get(i);
                imageResource = card.getResource();
                costString = formatCardCost(card);
            }
            cardCostList[i].setText(costString);
            cardList[i].setImageResource(imageResource);
        }
        deckInfoTextView.setText(formatDeckInfo(player.getMainDeck()));
    }

    ////////////////////////////////// Util functions //////////////////////////////////
    private String formatCargoLeft(int playerCargoLeft) {
        final String format = "CARGO LEFT: ";
        return format + playerCargoLeft;
    }

    private String formatDeckInfo(List<Card> mainDeck) {
        LinkedHashMap<String, Integer> cardsTally = new LinkedHashMap<>();
        // assign value 1 to key card.toString if key null else increment value associated with key
        for (Card card : mainDeck) {
            cardsTally.merge(card.toString(), 1, Integer::sum);
        }
        String out = "";
        int i = 0;
        for (Map.Entry<String, Integer> set : cardsTally.entrySet()) {
            out += set.getValue() + " x " + "[" + set.getKey() + "]     ";

            // split into 2 lines
            if (i == Math.floorDiv(cardsTally.size(), 2)) out += "\n";
            i++;
        }
        return out;
    }

    private String formatCardCost(Card card) {
        return "COST: " + card.getCost();
    }

}