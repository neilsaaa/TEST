package com.example.quiz.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.Question;
import com.example.quiz.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SQLiteActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
//    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private Button backButton, nextButton;
    private int score = 0;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        questionTextView = findViewById(R.id.questionTextView);
        databaseManager = new DatabaseManager(this);

        // Load questions from JSON file in assets
        String json = loadJSONFromAsset();
        Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
        List<Question> questions = new Gson().fromJson(json, listType);

        // Save questions to SQLite database
        databaseManager.open();
        for (Question question : questions) {
            databaseManager.addData(question.content, question.options, question.answer);
        }

        // Retrieve questions from SQLite database and display
        List<com.example.quiz.sqlite.Question> questionList = databaseManager.getAllData();

        databaseManager.close();

        showQuestion(questionList.get(currentIndex));



        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            currentIndex = currentIndex + 1;
            showQuestion(questionList.get(currentIndex));


        });
        backButton.setOnClickListener(v -> {
            currentIndex = currentIndex -  1;
            showQuestion(questionList.get(currentIndex));
        });

        View.OnClickListener optionListener = view -> {
            Button b = (Button) view;
            String answer = b.getText().toString();
            if (answer.equals(questionList.get(currentIndex).answer)) {
                score++;
            }
            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                showQuestion(questionList.get(currentIndex));
            } else {
                questionTextView.setText("Quiz Finished! Your score: " + score);
                optionButton1.setVisibility(View.GONE);
                optionButton2.setVisibility(View.GONE);
                optionButton3.setVisibility(View.GONE);
                optionButton4.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        };
        optionButton1.setOnClickListener(optionListener);
        optionButton2.setOnClickListener(optionListener);
        optionButton3.setOnClickListener(optionListener);
        optionButton4.setOnClickListener(optionListener);

    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("question.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void showQuestion(com.example.quiz.sqlite.Question question) {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> options = new Gson().fromJson(question.options, type);
        optionButton1.setVisibility(View.VISIBLE);
        optionButton2.setVisibility(View.VISIBLE);
        optionButton3.setVisibility(View.VISIBLE);
        optionButton4.setVisibility(View.VISIBLE);

        questionTextView.setText(question.content);
        optionButton1.setText(options.get(0));
        optionButton2.setText(options.get(1));
        optionButton3.setText(options.get(2));
        optionButton4.setText(options.get(3));

        if (currentIndex > 0) {
            backButton.setVisibility(View.VISIBLE);
        }else{
            backButton.setVisibility(View.GONE);
        }

    }

}
