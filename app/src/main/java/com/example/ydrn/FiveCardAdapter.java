package com.example.ydrn;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FiveCardAdapter extends RecyclerView.Adapter<FiveCardAdapter.FiveCardViewHolder> {
    private Context context;
    private List<Card> cardList;
    private final String TAG = "FiveCardAdapter";

    public FiveCardAdapter(Context context, List<Card> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    public static class FiveCardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        public FiveCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardRef);
        }
    }

    @NonNull
    @Override
    public FiveCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_model, parent, false);
        return new FiveCardViewHolder(view);
    }

    // bind data for each cardView in FiveCardAdapter
    @Override
    public void onBindViewHolder(@NonNull FiveCardViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");

        Card card = cardList.get(position);
        holder.cardImage.setImageResource(card.getResource());
        holder.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


}
