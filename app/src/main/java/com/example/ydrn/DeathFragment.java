package com.example.ydrn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class DeathFragment extends Fragment {
    private Player player;
    private TextView deathLevelTextView;
    private TextView deathMessageTextView;
    private Button tryAgainButton;

    private final String[] deathMessages = new String[] {
            "This is extremely not stonks.",
            "My cabbages!!!",
            "Should we get out and push?\nOh right, we're in space..."
    };

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
        View view = inflater.inflate(R.layout.fragment_death, container, false);

        deathLevelTextView = view.findViewById(R.id.deathLevel);
        deathMessageTextView = view.findViewById(R.id.deathMessage);
        tryAgainButton = view.findViewById(R.id.tryAgain);

        return view;
    }

    // set listeners here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deathLevelTextView.setText(formatDeathLevel(player.getLevel()));
        deathMessageTextView.setText(getRandomDeathMessage());

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.tryAgainButton();
                player.getGameState().setValue(Player.GameState.GAME);
            }
        });

        player.getGameState().observe(getViewLifecycleOwner(), new Observer<Player.GameState>() {
            @Override
            public void onChanged(Player.GameState gameState) {
                if (gameState == Player.GameState.DEATH) {
                    deathLevelTextView.setText(formatDeathLevel(player.getLevel()));
                    deathMessageTextView.setText(getRandomDeathMessage());
                }
            }
        });
    }

    //////////////////////// Util Methods ////////////////////////
    private String formatDeathLevel(int deathLevel) {
        return "YOU LOST ON: " + deathLevel;
    }

    private String getRandomDeathMessage() {
        return deathMessages[new Random().nextInt(deathMessages.length)];
    }
}