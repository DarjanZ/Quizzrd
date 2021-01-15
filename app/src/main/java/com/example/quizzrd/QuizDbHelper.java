package com.example.quizzrd;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.quizzrd.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Quizzrd.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;
    private SQLiteDatabase tmp_db;

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context){
        if(instance == null){
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoryTable.TABLE_NAME + " ( " +
                CategoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoryTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE "+
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoryTable.TABLE_NAME + "(" + CategoryTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategorytable();
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CategoryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategorytable(){
        Category c0 = new Category("General");
        addCategory(c0);
        Category c1 = new Category("Programming");
        addCategory(c1);
    }

    private void addCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(CategoryTable.COLUMN_NAME, category.getName());
        db.insert(CategoryTable.TABLE_NAME,null,cv);
    }

    private void fillQuestionTable(){
        Question q1 = new Question("One loop inside the body of another loop is called",
                "Loop in loop","Nested","Double loops",2,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("What is FIFO ?",
                "First in First out","First in Few out","Few in First out",1,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("A Syntax Error is ?",
                "An error due to user error",
                "An error you will never find",
                "An error caused by language rules being broken.",3,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("What command do you use to output data to the screen? ",
                "Cout>>","Cout","Output>>",1,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("A do while and a while loop are the same ?",
                "False","True","Sometimes",2,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("A short sections of code written to complete a task.",
                "Buffer","Array","Function",3,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("What dose this equation mean ? a != t",
                "A and t are equal","A is not equal to t","T is add to a ",2,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("Which data structure uses LIFO ?",
                "Array","Int","Stacks",3,Question.EASY_D,Category.PROGRAMMING);
        addQuestion(q1);

        q1 = new Question("Choose the function that is most appropriate for reading in a multi-word string ?",
                "strnset()","scanf()","gets()",3,Question.MEDIUM_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("The correct order of mathematical operators in mathematics and computer programming",
                "Addition, Subtraction, Multiplication, Division","Division, Multiplication, Addition, Subtraction","Multiplication, Addition, Division, Subtraction",
                2,Question.MEDIUM_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("Which of the following variable cannot be used by switch-case statement ?",
                "Double","float","int",2,Question.MEDIUM_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("In C, what are the various types of real data type (floating point data type) ?",
                "Float, long double","long double, short int","float, double, long double",3,Question.MEDIUM_D,Category.PROGRAMMING);
        addQuestion(q1);

        q1 = new Question("Which of the following is/are advantages of packages ?",
                "Packages avoid name clashes",
                "We can have hidden classes that are used by the packages, but not visible outside.","All of the above",3,Question.HARD_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("Which one of the following is correct?",
                "An applet is not a small program","An applet can be run on its own","Applets are embedded in another applications",3,Question.HARD_D,Category.PROGRAMMING);
        addQuestion(q1);
        q1 = new Question("What is the use of final keyword in Java?",
                "When a method is final, it can not be overridden","When a variable is final, it can be assigned value only once.",
                "All of the above",3,Question.HARD_D,Category.PROGRAMMING);
        addQuestion(q1);

        q1 = new Question(" How many rings appear on the Olympic flag?",
                "5","3",
                "7",1,Question.EASY_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question(" What is the atomic sign for Helium on the periodic table?",
                "Hf","H2O",
                "He",3,Question.EASY_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question(" What is the general name for a group of wolves?",
                "Herd","Pack",
                "School",2,Question.EASY_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("Frodo Baggins is from which mystical land?",
                "Middle Earth","Narnia",
                "Mount Olympus",1,Question.EASY_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("Blood is filtered by which pair of organs?",
                "Kidneys","Lungs",
                "Ovaries",1,Question.MEDIUM_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("Who developed the theory of relativity?",
                "Tesla","Einstein",
                "Newton",2,Question.MEDIUM_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("Which travel faster",
                "Light waves","Sound waves",
                "Micro waves",1,Question.MEDIUM_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("What is the capital city of New Zealand?",
                "Walles","Wellington",
                "Werington",2,Question.MEDIUM_D,Category.GENERAL);
        addQuestion(q1);

        q1 = new Question("Triton is a moon of which planet?",
                "Jupiter","Neptune",
                "Saturn",2,Question.HARD_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("In which organ of the body is the cerebrum found?",
                "Brain","Liver",
                "Lungs",1,Question.HARD_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("In what sport could you 'hit a six' or 'bowl a leg spinner'?",
                "Badminton","Football",
                "Cricket",3,Question.HARD_D,Category.GENERAL);
        addQuestion(q1);
        q1 = new Question("In which country is a 'gamelan orchestra' most commonly found?",
                "India","Indonesia",
                "Iraq",2,Question.HARD_D,Category.GENERAL);
        addQuestion(q1);

    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOpt1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOpt2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOpt3());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getanswerNr());
        cv.put(QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME,null, cv);
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoryTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoryTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoryTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String [] selectionArguments = new String[]{String.valueOf(categoryID),difficulty};
        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArguments,
                null,
                null,
                null
        );

        //String[] selectionArgs = new String[]{difficulty};
        //Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME + " WHERE " + QuestionTable.COLUMN_DIFFICULTY + " = ?",
        //        selectionArgs);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOpt1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOpt2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOpt3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
