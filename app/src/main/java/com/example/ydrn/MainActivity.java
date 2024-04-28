package com.example.ydrn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 5 cards
        FiveCardUI handFragment = (FiveCardUI) getSupportFragmentManager().findFragmentById(R.id.five_card_frag);
        if (handFragment == null) {
            Log.e("onCreate", "cannot find FiveCardUI fragment");
            throw new NullPointerException();
        }
        Player player = new Player();
        ImageView[] cards = new ImageView[] {
                handFragment.getView().findViewById(R.id.card1),
                handFragment.getView().findViewById(R.id.card2),
                handFragment.getView().findViewById(R.id.card3),
                handFragment.getView().findViewById(R.id.card4),
                handFragment.getView().findViewById(R.id.card5)
        };
        bind_listener_and_resource(cards, player);

//        Toast.makeText(MainActivity.this, R.string.app_name, Toast.LENGTH_LONG).show();

        // next turn button
        Button nextTurn = findViewById(R.id.nextTurn);
        nextTurn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                player.nextTurnButton();
                Log.d("MainActivity.nextTurnButton", player.getHand().toString());

                // refresh card images
                for (int i = 0; i < cards.length; i++) {
                    ImageView cardImView = cards[i];
                    Card cardLogic = player.getHand().get(i);
                    cardImView.setImageResource(cardLogic.getResource());
                }

                // update turn and cargo
                TextView turn = findViewById(R.id.turn);
                TextView cargo = findViewById(R.id.cargo);
                TextView deck = findViewById(R.id.deck);
                turn.setText("TURN: "+ player.turn);
                cargo.setText("CARGO: "+player.cargo);
                deck.setText("CARDS LEFT: "+player.cardsLeft());
            }
        });
    }

    /**
     * Binds onClickListeners and ImageResource for the 5 cards
     * @param cards 5 card images in FiveCardUI fragment
     * @param player stores game logic
     */
    private void bind_listener_and_resource(ImageView[] cards, Player player) {
        for (int i = 0; i < cards.length; i++) {
            ImageView cardImView = cards[i];
            Card cardLogic = player.getHand().get(i);

            // bind click listener
            int position = i;
            cardImView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MainActivity.FiveCardUI.card", "card " + position + " clicked");
                    // play card in game logic, card should be unuseable
                    player.playCard(position);
                    Log.d("MainActivity.FiveCardUI.card", "card.useable: "+ player.getHand().get(position).is_useable());

                    // update visuals to back upon clicked
                    cardImView.setImageResource(cardLogic.getBackResource());

                    // update the current number
                    TextView current = findViewById(R.id.current);
                    current.setText(String.valueOf(player.currentNumber));
                }
            });

            // bind card image resource
            cardImView.setImageResource(cardLogic.getResource());
        }
    }
}