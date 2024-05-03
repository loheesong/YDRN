package com.example.ydrn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // game variables
    private Player player;

    // UI variables
    private Fragment gameFragment;
    private Fragment startFragment;
    private Fragment shopFragment;
    private Fragment deathFragment;
    private FragmentManager fragmentManager;

    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
        // cause null pointer idk why

        // init game variables
        player = Player.getInstance();

        // init fragments
        startFragment = new StartFragment();
        gameFragment = new GameFragment();
        shopFragment = new ShopFragment();
        deathFragment = new DeathFragment();
        fragmentManager = getSupportFragmentManager();

        // 'start' all fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frame_layout, gameFragment);
        fragmentTransaction.hide(gameFragment);
        fragmentTransaction.add(R.id.frame_layout, shopFragment);
        fragmentTransaction.hide(shopFragment);
        fragmentTransaction.add(R.id.frame_layout, deathFragment);
        fragmentTransaction.hide(deathFragment);
        fragmentTransaction.add(R.id.frame_layout, startFragment); // start is first

        fragmentTransaction.commit();

        // GameState observer: this changes the fragments
        player.getGameState().observe(this, new Observer<Player.GameState>() {
            @Override
            public void onChanged(Player.GameState gameState) {
                Log.d(TAG, "gameState onchange" + gameState.toString());
                switch (gameState) {
                    case START:
                        showFragment(startFragment);
                        break;
                    case GAME:
                        showFragment(gameFragment);
                        break;
                    case SHOP:
                        showFragment(shopFragment);
                        break;
                    case DEATH:
                        showFragment(deathFragment);
                        break;
                    default:
                        Log.d(TAG, "gameState default, THIS SHOULD NOT HAPPEN!");
                        showFragment(startFragment);
                        break;
                }
            }
        });
    }

    private void showFragment(Fragment fragment) {
        Log.d(TAG, "showFragment: ");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Hide all existing fragments
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            fragmentTransaction.hide(f);
        }
        // Show or add the new fragment
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.frame_layout, fragment);
            Log.d(TAG, "Theoretically, you should not see this message.");
        }

        fragmentTransaction.commit();
    }
}