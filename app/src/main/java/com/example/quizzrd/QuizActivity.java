package com.example.quizzrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTER_IN_MILLIS = 30000;

    private static final String SCORE_ROTATE = "scoreRotate";
    private static final String QUESTION_COUNT_ROTATE = "questionCountRotate";
    private static final String MILLIS_ROTATE = "millisRotate";
    private static final String ANSWERED_ROTATE = "answeredRotate";
    private static final String QUESTION_LIST_ROTATE = "questionListRotate";
    private static final String JOKER_ROTATE = "jokerRotate";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewDifficulty;
    private TextView textViewJoker;
    private TextView textViewTimer;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirm;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionTotal;
    private Question currQuestion;

    private String joker = new String(Character.toChars(0x1F0CF));
    private int numberOfJokers = 3;

    private ColorStateList defaultRbColor;
    private ColorStateList defaultCdColor;

    private CountDownTimer counter;
    private long timeLeftMillis;

    private int score;
    private boolean answered;
    private long pressBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        textViewJoker = findViewById(R.id.text_view_joker);
        textViewTimer = findViewById(R.id.text_view_timer);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirm = findViewById(R.id.button_confirm);

        defaultRbColor = rb1.getTextColors();
        defaultCdColor = textViewTimer.getTextColors();

        Intent intent = getIntent();
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID,0);
        String category = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);

        textViewCategory.setText("Category: " + category);
        textViewDifficulty.setText("Difficulty: " + difficulty);

        if(savedInstanceState == null) {
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID,difficulty);
            questionTotal = questionList.size();
            Collections.shuffle(questionList);

            displayNextQuestion();
        }else {
            questionList = savedInstanceState.getParcelableArrayList(QUESTION_LIST_ROTATE);
            questionTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(QUESTION_COUNT_ROTATE);
            currQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(SCORE_ROTATE);
            timeLeftMillis = savedInstanceState.getLong(MILLIS_ROTATE);
            answered = savedInstanceState.getBoolean(ANSWERED_ROTATE);
            numberOfJokers = savedInstanceState.getInt(JOKER_ROTATE);

            if(!answered){
                startCounting();
            }
            else {
                updateCounterText();
                displaySolution();
            }
        }

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() ||rb3.isChecked()){
                        checkChosenAnswer();
                    }
                    else{
                        Toast.makeText(QuizActivity.this,"Please select an answer",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    displayNextQuestion();
                }
            }
        });
    }

     private void displayNextQuestion(){
        rb1.setTextColor(defaultRbColor);
        rb2.setTextColor(defaultRbColor);
        rb3.setTextColor(defaultRbColor);
        rbGroup.clearCheck();

        if(questionCounter < questionTotal && numberOfJokers >= 0){
            timeLeftMillis = COUNTER_IN_MILLIS;
            startCounting();

            currQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currQuestion.getQuestion());
            rb1.setText(currQuestion.getOpt1());
            rb2.setText(currQuestion.getOpt2());
            rb3.setText(currQuestion.getOpt3());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + " out of "+ questionTotal);
            answered = false;
            buttonConfirm.setText("Confirm");

            switch (numberOfJokers){
                case 3:
                    textViewJoker.setText("Joker: " + joker + " " + joker + " " + joker);
                    break;
                case 2:
                    textViewJoker.setText("Joker: " + joker + " " + joker);
                    break;
                case 1:
                    textViewJoker.setText("Joker: " + joker);
                    break;
                case 0:
                    textViewJoker.setText("Joker: /");
                    break;
            }
        }
        else{
            finishQuiz();
        }
    }

    private void startCounting(){
        counter = new CountDownTimer(timeLeftMillis,1000) {
            @Override//vsako sekundo klicemo onTick
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateCounterText();
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                updateCounterText();
                checkChosenAnswer();
            }
        }.start();
    }

    private void updateCounterText() {
        int minute = (int)(timeLeftMillis / 1000) / 60;
        int second = (int)(timeLeftMillis / 1000) % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d", minute, second);
        textViewTimer.setText(time);

        if(timeLeftMillis < 10000){
            textViewTimer.setTextColor(Color.RED);
        }
        else {
            textViewTimer.setTextColor(defaultCdColor);
        }
    }

    private void checkChosenAnswer(){
        answered = true;
        counter.cancel();
        RadioButton selectedButton = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(selectedButton) + 1;

        if(answerNr == currQuestion.getanswerNr()){
            score+=100;
            textViewScore.setText("Score: " + score);
        }
        else {
            numberOfJokers -= 1;
        }
        displaySolution();
    }

    private void displaySolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currQuestion.getanswerNr()){
            case 1: rb1.setTextColor(Color.GREEN);
                    textViewQuestion.setText("1. answer is correct");
                    break;
            case 2: rb2.setTextColor(Color.GREEN);
                    textViewQuestion.setText("2. answer is correct");
                    break;
            case 3: rb3.setTextColor(Color.GREEN);
                    textViewQuestion.setText("3. answer is correct");
                    break;
        }

        if(questionCounter < questionTotal && numberOfJokers >= 0){
            buttonConfirm.setText("Next question");
        }
        else {
            buttonConfirm.setText("Finish");
        }
    }

    private void finishQuiz(){
        Intent resutIntent = new Intent();
        resutIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resutIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(pressBack + 2000 > System.currentTimeMillis()){
            finish();
        }
        else {
            Toast.makeText(this, "Press back again to finish (answers will not save)", Toast.LENGTH_SHORT).show();
        }
        pressBack = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(counter != null)
            counter.cancel();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_ROTATE, score);
        outState.putInt(QUESTION_COUNT_ROTATE, questionCounter);
        outState.putLong(MILLIS_ROTATE, timeLeftMillis);
        outState.putBoolean(ANSWERED_ROTATE, answered);
        outState.putParcelableArrayList(QUESTION_LIST_ROTATE, questionList);
        outState.putInt(JOKER_ROTATE, numberOfJokers);
    }
}