package com.example.ydrn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameFragment extends Fragment {
    // UI variables
    private TextView current;
    private TextView target;
    private TextView level;
    private TextView turn;
    private TextView cardsLeft;
    private TextView cargoLeft;
    private RecyclerView cardRecycler;
    private FiveCardAdapter cardAdapter;

    // game variables
    private Player player;

    private final String TAG = "GameFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Player
        player = Player.getInstance();
    }

    // create UI here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        // get reference to UI elements
        current = view.findViewById(R.id.current);
        target = view.findViewById(R.id.target);
        level = view.findViewById(R.id.level);
        turn = view.findViewById(R.id.turn);
        cardsLeft = view.findViewById(R.id.cardsLeft);
        cargoLeft = view.findViewById(R.id.cargoLeft);
        cardRecycler = view.findViewById(R.id.cardRecycler);

        // set up card recycler
        cardRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cardAdapter = new FiveCardAdapter(getActivity(), player.getHand());
        cardRecycler.setAdapter(cardAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");

        // set up all listeners
    }
}