package com.example.retrofitexample.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.retrofitexample.R;

public class PalendromeActivity extends AppCompatActivity {

    private EditText wordEditext;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palendrome);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Palindrome");
        actionBar.setHomeButtonEnabled(true);

        wordEditext = findViewById(R.id.wordEditText);
        textView = findViewById(R.id.labelWord);

        wordEditext.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        textView.setVisibility(View.VISIBLE);
                        if(isPalindrome(s.toString()))
                            textView.setText(s.toString() + " is a palindrome");
                        else
                            textView.setText(s.toString()  + " is not a palindrome");

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );



    }

    private boolean isPalindrome(String word){

        StringBuilder sb = new StringBuilder(word);
        String reversedWord = new String(sb.reverse());

        if(word.toLowerCase().equals(reversedWord.toLowerCase()))
            return true;
        else
            return false;
    }


}
