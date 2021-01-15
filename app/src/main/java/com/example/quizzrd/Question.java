package com.example.quizzrd;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String EASY_D = "Easy";
    public static final String MEDIUM_D = "Medium";
    public static final String HARD_D = "Hard";

    private int id;
    private String question;
    private String opt1;
    private String opt2;
    private String opt3;
    private int answerNr;
    private String difficulty;
    private int categoryID;

    public Question(){}

    public Question(String question, String opt1,
                    String opt2, String opt3, int answerNr,
                    String difficulty, int categoryID) {
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.answerNr=answerNr;
        this.difficulty = difficulty;
        this.categoryID = categoryID;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        opt1 = in.readString();
        opt2 = in.readString();
        opt3 = in.readString();
        answerNr = in.readInt();
        difficulty = in.readString();
        categoryID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(opt1);
        dest.writeString(opt2);
        dest.writeString(opt3);
        dest.writeInt(answerNr);
        dest.writeString(difficulty);
        dest.writeInt(categoryID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }
    public int getanswerNr(){
        return answerNr;
    }
    public void setAnswerNr(int answerNr){
        this.answerNr = answerNr;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public static String[] getAllDifficulties(){
        return new String[]{
                EASY_D,
                MEDIUM_D,
                HARD_D
        };
    }
}
