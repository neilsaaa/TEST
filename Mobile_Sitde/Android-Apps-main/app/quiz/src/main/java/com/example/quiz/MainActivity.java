package com.example.quiz;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quiz.AppDatabase;
import com.example.quiz.sqlite.SQLiteActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, scoreTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    private List<Question> questions = new ArrayList<>();
    private ArrayList<String> selected = new ArrayList<>();
    private int currentIndex = 0;
    private Button currentButton;
    private Button backButton, nextButton, retryButton;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<String> correctAnswers = new ArrayList<>();

    LinearLayout resultLayout;

    LinearLayout testLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionTextView = findViewById(R.id.questionTextView);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        optionButton4 = findViewById(R.id.optionButton4);
        resultLayout = findViewById(R.id.resultLayout);
        testLayout = findViewById(R.id.testLayout);
        scoreTextView = findViewById(R.id.scoreTextView);



        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> {
            currentIndex = 0;
            selected.clear();
            for (int i = 0; i < questions.size(); i++) {
                selected.add("No Answer");
                buttons.get(i).setSelected(false);
                buttons.get(i).setBackground(ContextCompat.getDrawable(this, R.drawable.button_states));
            }
            currentButton = buttons.get(currentIndex);
            showQuestion(questions.get(currentIndex));

            currentButton.setSelected(true);
            testLayout.setVisibility(View.VISIBLE);
            resultLayout.setVisibility(View.GONE);

        });
        GridLayout questionsGridLayout = findViewById(R.id.questionsGridLayout);
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Finish Quiz")
                    .setMessage("Are you sure you want to finish?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        displayResult();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });





        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        new Thread(() -> {
            questions.addAll(db.questionDao().getAll());
            runOnUiThread(() -> {
                for (int i = 0; i < questions.size(); i++) {
                    selected.add("No Answer");
                    Button button = new Button(MainActivity.this);
                    button.setText(String.valueOf(i + 1));
                    button.setTextColor(Color.WHITE);
                    final int questionIndex = i;
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);


                    button.setLayoutParams(params);
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_states));



                    button.setOnClickListener(v -> {

                        if (currentButton != null) {
                            currentButton.setSelected(false);

                        }

                        button.setSelected(true);

                        // Keep track of the current selected button
                        currentButton = button;

                        currentIndex = questionIndex;
                        showQuestion(questions.get(currentIndex));



                    });
                    buttons.add(button); // Add this line

                    questionsGridLayout.addView(button);
                    correctAnswers.add(questions.get(i).answer);


                }
                currentButton = buttons.get(currentIndex);

                if (!questions.isEmpty()) {
                    showQuestion(questions.get(0));
                    currentButton.setSelected(true);
                }


                });
        }).start();



        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            currentButton.setSelected(false);
            currentIndex = currentIndex + 1;
            showQuestion(questions.get(currentIndex));
            currentButton = buttons.get(currentIndex);
            currentButton.setSelected(true);
        });
        backButton.setOnClickListener(v -> {
            currentButton.setSelected(false);
            currentIndex = currentIndex -  1;
            showQuestion(questions.get(currentIndex));
            currentButton = buttons.get(currentIndex);
            currentButton.setSelected(true);
        });

        View.OnClickListener optionListener = view -> {
            Button b = (Button) view;
            String answer = b.getText().toString();
            selected.set(currentIndex, answer);

            Button selectedOption = (Button) view;

            optionButton1.setBackgroundColor(Color.parseColor("#344C7C"));
            optionButton2.setBackgroundColor(Color.parseColor("#344C7C"));
            optionButton3.setBackgroundColor(Color.parseColor("#344C7C"));
            optionButton4.setBackgroundColor(Color.parseColor("#344C7C"));

            selectedOption.setBackgroundColor(Color.parseColor("#6a5be2"));
            buttons.get(currentIndex).setBackgroundResource(R.drawable.button_selected);




        };
        optionButton1.setOnClickListener(optionListener);
        optionButton2.setOnClickListener(optionListener);
        optionButton3.setOnClickListener(optionListener);
        optionButton4.setOnClickListener(optionListener);

    }

    private void showQuestion(Question question) {
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
        optionButton1.setBackgroundColor(Color.parseColor("#344C7C"));
        optionButton2.setBackgroundColor(Color.parseColor("#344C7C"));
        optionButton3.setBackgroundColor(Color.parseColor("#344C7C"));
        optionButton4.setBackgroundColor(Color.parseColor("#344C7C"));

        for (int i = 0; i < buttons.size(); i++) {
            if(selected.get(i).equals("No Answer")){
                buttons.get(i).setBackgroundResource(R.drawable.button_states);
            }else{
                buttons.get(i).setBackgroundResource(R.drawable.button_answered_state);
            }


        }
        if (selected.get(currentIndex) != null) {
            if (optionButton1.getText().toString().equals(selected.get(currentIndex))) {
                optionButton1.setBackgroundColor(Color.parseColor("#6a5be2"));
            } else if (optionButton2.getText().toString().equals(selected.get(currentIndex))) {
                optionButton2.setBackgroundColor(Color.parseColor("#6a5be2"));
            } else if (optionButton3.getText().toString().equals(  selected.get(currentIndex))) {
                optionButton3.setBackgroundColor(Color.parseColor("#6a5be2"));
            } else if (optionButton4.getText().toString().equals(selected.get(currentIndex))) {
                optionButton4.setBackgroundColor(Color.parseColor("#6a5be2"));
            }
        }



        if (currentIndex > 0) {
            backButton.setVisibility(View.VISIBLE);
        }else{
            backButton.setVisibility(View.GONE);
        }
        if (currentIndex == questions.size() - 1) {
            nextButton.setVisibility(View.GONE);
        }else{
            nextButton.setVisibility(View.VISIBLE);
        }

        buttons.get(currentIndex).setBackgroundResource(R.drawable.button_selected);
}

int checkAnswer() {
        int score = 0;

    for (int i = 0; i < correctAnswers.size(); i++) {
        if (selected.get(i).equals(correctAnswers.get(i))) {
            score++;
        }
    }

    return score;


}


        public void displayResult() {
            scoreTextView.setText("Quiz Finished! Your score: " + checkAnswer() + "/" + questions.size());
//            optionButton1.setVisibility(View.GONE);
//            optionButton2.setVisibility(View.GONE);
//            optionButton3.setVisibility(View.GONE);
//            optionButton4.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);

            testLayout.setVisibility(View.GONE);
            resultLayout.setVisibility(View.VISIBLE);

            ScrollView resultScrollView = findViewById(R.id.resultScrollView);
            LinearLayout resultLayout = new LinearLayout(this);
            resultLayout.setOrientation(LinearLayout.VERTICAL);
            resultScrollView.removeAllViews(); // Clear old results
            resultScrollView.addView(resultLayout); // Add LinearLayout to ScrollView

            for (int i = 0; i < questions.size(); i++) {
                TextView questionView = new TextView(this);
                questionView.setText("Question " + (i + 1) + ": " + questions.get(i).content);
                resultLayout.addView(questionView);
                TextView userAnswerView = new TextView(this);
                String userAnswer = selected.get(i);
                userAnswerView.setText("Your answer: " + userAnswer);
                resultLayout.addView(userAnswerView); // Add userAnswerView to resultLayout first

                String correctAnswer = correctAnswers.get(i);
                if (userAnswer.equals(correctAnswer)) {
                    questionView.setTextColor(Color.GREEN);
                } else {
                    questionView.setTextColor(Color.RED);
                    TextView correctAnswerView = new TextView(this);
                    correctAnswerView.setText("Correct answer: " + correctAnswer);
                    resultLayout.addView(correctAnswerView); // Add correctAnswerView to resultLayout after userAnswerView
                }
            }


            resultScrollView.setVisibility(View.VISIBLE); // Show the result layout


        }
}