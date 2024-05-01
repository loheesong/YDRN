package com.example.ydrn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // game variables
    private GameState gameState;

    // UI variables
    private Fragment gameFragment;
    private Fragment startFragment;
    private FragmentManager fragmentManager;

    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init game variables
        gameState = GameState.GAME;

        // init fragments
        gameFragment = new GameFragment();
        startFragment = new StartFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, gameFragment);
        fragmentTransaction.commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Hide all existing fragments
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            fragmentTransaction.hide(f);
        }

        // Show or add the new fragment
        Log.d(TAG, "showFragment: "+ fragment.isAdded());
//        if (fragment.isAdded()) {
//            fragmentTransaction.show(fragment);
//        } else {
//            fragmentTransaction.add(R.id.frame_layout, fragment);
//            Log.d(TAG, "Theoretically, you should not see this message.");
//        }

        // We are not using fragmentTransaction.replace in order to maintain fragment state
        fragmentTransaction.commit();
    }
}