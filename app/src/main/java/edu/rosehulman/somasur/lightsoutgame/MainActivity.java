package edu.rosehulman.somasur.lightsoutgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LightsOutGame mGame = new LightsOutGame();
    private TextView mGameStateTextView;
    private Button mLightsOutButtons[];
    private String mstateString;
    private int mNumberOfClicks = 0;
    private int mNumberOfButtons = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGameStateTextView = findViewById(R.id.message_text);
        if (savedInstanceState != null) {
            char buttonTextArray[] = savedInstanceState.getString("GameState").toCharArray();
            int buttonValues[] = new int[savedInstanceState.getString("GameState").length()];
            for (int i = 0; i < buttonValues.length; i++) {
                buttonValues[i] = Character.digit(buttonTextArray[i], 10);
            }
            mGame.setAllValues(buttonValues);
            int mNumberOfClicks = savedInstanceState.getInt("ClickCount");
            mGame.setNumPresses(mNumberOfClicks);
            if (mNumberOfClicks == 0) {
                mGameStateTextView.setText(getResources().getString(R.string.message_start));
            } else if (mGame.checkForWin()) {
                mGameStateTextView.setText(getResources().getString(R.string.you_win));
                mLightsOutButtons = new Button[7];
                for (int row = 0; row < mNumberOfButtons; row++) {
                    int id = getResources().getIdentifier("button" + row, "id", getPackageName());
                    mLightsOutButtons[row] = findViewById(id);
                    mLightsOutButtons[row].setEnabled(false);
                }
            } else {
                mGameStateTextView.setText(getResources().getQuantityString(R.plurals.message_format, mNumberOfClicks, mNumberOfClicks));
            }
        }

        Button newGameButton;

        newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);

        mLightsOutButtons = new Button[7];
        for (int row = 0; row < mLightsOutButtons.length; row++) {
            int id = getResources().getIdentifier("button" + row, "id", getPackageName());
            mLightsOutButtons[row] = findViewById(id);
            mLightsOutButtons[row].setOnClickListener(this);
            mLightsOutButtons[row].setText(mGame.myString(mGame.getValueAtIndex(row)));
        }
        mstateString = mGame.toString();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_game_button) {
            mGame = new LightsOutGame(7);
            mGameStateTextView.setText(getResources().getString(R.string.message_start));
            for (int row = 0; row < mLightsOutButtons.length; row++) {
                mLightsOutButtons[row].setEnabled(true);
            }
        }
        for (int row = 0; row < mLightsOutButtons.length; row++) {
            if (v.getId() == mLightsOutButtons[row].getId()) {
                if (!mGame.pressedButtonAtIndex(row)) {
                    mNumberOfClicks = mGame.getNumPresses();
                    mGameStateTextView.setText(getResources().getQuantityString(R.plurals.message_format, mNumberOfClicks, mNumberOfClicks));
                } else {
                    mGameStateTextView.setText(getResources().getString(R.string.you_win));
                }
            }
        }

        for (int row = 0; row < mLightsOutButtons.length; row++) {
            mLightsOutButtons[row].setText(mGame.myString(mGame.getValueAtIndex(row)));
            mstateString = mGame.toString();
        }

        if (mGameStateTextView.getText() == getResources().getString(R.string.you_win)) {
            for (int row = 0; row < mLightsOutButtons.length; row++) {
                mLightsOutButtons[row].setEnabled(false);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("GameState", mstateString);
        state.putInt("ClickCount", mNumberOfClicks);
    }

}
