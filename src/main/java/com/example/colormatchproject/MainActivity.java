package com.example.colormatchproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int[] buttonIds = new int[] {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46, R.id.button47, R.id.button48, R.id.button49};
    Context context = this;


    int firstButtonNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startIndexes.clear();

        for(int i = 0; i < buttonIds.length; i++) {
            findViewById(buttonIds[i]).setBackgroundColor(getResources().getColor(R.color.black));
            findViewById(buttonIds[i]).setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        }

        final float len2 = getResources().getDisplayMetrics().densityDpi/6;
        View.OnTouchListener controlGridTouchListener=new View.OnTouchListener() {
            float initX;
            float initY;
            int firstButtonNum;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initX = event.getX();
                        initY = event.getY();
                        firstButtonNum = findStartPoint(event.getX(), event.getY());
                        clearOthers(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        initX -= event.getX(); //now has difference value
                        initY -= event.getY(); //now has difference value


                        Log.d(TAG, event.getX() + ", " + event.getY());
                        Button theButton = findViewById(buttonIds[0]);
                        int [] buttonCoords = new int[2];
                        buttonCoords[0] = theButton.getLeft() * 140;
                        buttonCoords[1] = theButton.getTop()*140;
//                        Log.d(TAG, "button: " + buttonCoords[0] + ", " + buttonCoords[1]);

                        highlightEndPoint(event.getX(), event.getY(), firstButtonNum);
                        endGame();


                        break;
                }
                return true;
            }
        };
        findViewById(R.id.interactionView).setOnTouchListener(controlGridTouchListener);
        highlightStarters();
    }

    private void clearOthers(float x, float y){
        int toClear = 0;
        for(int i = 0; i < buttonIds.length; i++) {
            if(findViewById(buttonIds[i]).getLeft() - x < 0 && findViewById(buttonIds[i]).getLeft() - x > -140 && findViewById(buttonIds[i]).getTop() - y < 0 && findViewById(buttonIds[i]).getTop() - y > -140) {
                toClear = i;
            }
        }
        runClear(toClear);
    }

    private void runClear(int toClear){
        if(startIndexes.contains(toClear)) {
            for (int i = 0; i < buttonIds.length; i++) {
                if (findViewById(buttonIds[toClear]).getBackgroundTintList().equals(findViewById(buttonIds[i]).getBackgroundTintList())) {
                    if (!startIndexes.contains(i)) {
                        findViewById(buttonIds[i]).setBackgroundColor(getResources().getColor(R.color.black));
                        findViewById(buttonIds[i]).setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
                    }
                }
            }
        }
    }

    private void highlightEndPoint(float x, float y, int firstButtonNum){
        int toHighlight = 0;
        for(int i = 0; i < buttonIds.length; i++) {
            if(findViewById(buttonIds[i]).getLeft() - x < 0 && findViewById(buttonIds[i]).getLeft() - x > -140 && findViewById(buttonIds[i]).getTop() - y < 0 && findViewById(buttonIds[i]).getTop() - y > -140) {
//                Log.d(TAG, "true");
                toHighlight = i;
            }
        }
        Button lastButton = findViewById(buttonIds[toHighlight]);
//        Log.d(TAG, "button: " + toHighlight);
//        Log.d(TAG, "button: " + lastButton);
//        lastButton.setBackgroundColor(getResources().getColor(R.color.skyblue));
        highlightPath(firstButtonNum, toHighlight);

    }

    ColorStateList colorID;
    private int findStartPoint(float x, float y){
        colorID = ContextCompat.getColorStateList(this, R.color.black);
        int toHighlight = 0;
        for(int i = 0; i < buttonIds.length; i++) {
            if(findViewById(buttonIds[i]).getLeft() - x < 0 && findViewById(buttonIds[i]).getLeft() - x > -140 && findViewById(buttonIds[i]).getTop() - y < 0 && findViewById(buttonIds[i]).getTop() - y > -140) {
//                Log.d(TAG, "true");
                toHighlight = i;
                colorID = findViewById(buttonIds[i]).getBackgroundTintList();
            }
        }
        return toHighlight;
    }

    private void highlightPath(int firstButtonNum, int lastButtonNum){
        //divide by 7 or mod 7
        int realFirst = firstButtonNum;
        if(firstButtonNum > lastButtonNum) {
            int temp = lastButtonNum;
            lastButtonNum = firstButtonNum;
            firstButtonNum = temp;
        }

        if(firstButtonNum / 7 == lastButtonNum / 7) {
            for(int i = firstButtonNum; i <= lastButtonNum; i++) {
//                Log.d(TAG, "first: " + findViewById(buttonIds[i]).getBackgroundTintList());
//                Log.d(TAG, "second: " + getResources().getColor(R.color.skyblue));
                if(((ColorDrawable)findViewById(buttonIds[i]).getBackground()).getColor() == getResources().getColor(R.color.black) && colorID != ContextCompat.getColorStateList(this, R.color.black)){
                    findViewById(buttonIds[i]).setBackgroundColor(getResources().getColor(R.color.skyblue));
                    findViewById(buttonIds[i]).setBackgroundTintList(colorID);
                    Log.d(TAG, "changed");
                }

                if(!(findViewById(buttonIds[i]).getBackgroundTintList()).equals(findViewById(buttonIds[realFirst]).getBackgroundTintList())){
                    runClear(realFirst);
                    i = lastButtonNum + 1;
                    Log.d(TAG, "clearing: " + colorID.toString());
                }
            }
        }

        else if(firstButtonNum % 7 == lastButtonNum % 7 ) {
            for(int i = firstButtonNum; i <= lastButtonNum; i+=7) {
                if(((ColorDrawable)findViewById(buttonIds[i]).getBackground()).getColor() == getResources().getColor(R.color.black)){
                    findViewById(buttonIds[i]).setBackgroundColor(getResources().getColor(R.color.skyblue));
                    findViewById(buttonIds[i]).setBackgroundTintList(colorID);
                    Log.d(TAG, "changed");
                }
                if(!(findViewById(buttonIds[i]).getBackgroundTintList()).equals(findViewById(buttonIds[realFirst]).getBackgroundTintList())){
                    runClear(realFirst);
                    i = lastButtonNum + 1;
                    Log.d(TAG, "clearing: " + colorID.toString());
                }
            }
        }
    }

    private void highlightStarters(){
        for(int j = 0; j < 2; j++) {
            int numToHighlight = getBlankButton();
            Button buttonToHighlight = (Button) findViewById(buttonIds[numToHighlight]);
            buttonToHighlight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_200));
            buttonToHighlight.setBackgroundColor(getResources().getColor(R.color.purple_200));

        }
        for(int j = 0; j < 2; j++) {
            int numToHighlight = getBlankButton();
            Button buttonToHighlight = (Button) findViewById(buttonIds[numToHighlight]);
            buttonToHighlight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.skyblue));
            buttonToHighlight.setBackgroundColor(getResources().getColor(R.color.skyblue));

        }
        for(int j = 0; j < 2; j++) {
            int numToHighlight = getBlankButton();
            Button buttonToHighlight = (Button) findViewById(buttonIds[numToHighlight]);
            buttonToHighlight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_700));
            buttonToHighlight.setBackgroundColor(getResources().getColor(R.color.teal_700));

        }
        for(int j = 0; j < 2; j++) {
            int numToHighlight = getBlankButton();
            Button buttonToHighlight = (Button) findViewById(buttonIds[numToHighlight]);
            buttonToHighlight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_700));
            buttonToHighlight.setBackgroundColor(getResources().getColor(R.color.purple_700));
        }
        for(int j = 0; j < 2; j++) {
            int numToHighlight = getBlankButton();
            Button buttonToHighlight = (Button) findViewById(buttonIds[numToHighlight]);
            buttonToHighlight.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.grey));
            buttonToHighlight.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

    private ArrayList<Integer> startIndexes = new ArrayList<>();
    private int getBlankButton(){
        int numToHighlight = (int) (Math.random() * 48 + 1);
        Button buttonToHighlight = (Button) findViewById(buttonIds[numToHighlight]);
        if(((ColorDrawable)buttonToHighlight.getBackground()).getColor() == getResources().getColor(R.color.black)){
            startIndexes.add(numToHighlight);
            return numToHighlight;
        }
        else {
            return getBlankButton();
        }
    }

//    TextView textview2 =  (TextView)(findViewById(R.id.textView2));
    private void endGame(){
        boolean canEnd = true;
        for(int i = 0; i < startIndexes.size(); i++) {
            if(!checkAround(startIndexes.get(i))) {
                canEnd = false;
            }
        }

        if(canEnd) {

//            textview2.setText("Game Over!");
            startIndexes.clear();

            for(int i = 0; i < buttonIds.length; i++) {
                findViewById(buttonIds[i]).setBackgroundColor(getResources().getColor(R.color.black));
                findViewById(buttonIds[i]).setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            }
            highlightStarters();

        }
    }

    private boolean checkAround(int buttonNum){
        boolean toReturn = false;
        if(!(buttonNum % 7 == 6)) {
            if (findViewById(buttonIds[buttonNum]).getBackgroundTintList().equals(findViewById(buttonIds[buttonNum + 1]).getBackgroundTintList())) {
                toReturn = true;
            }
        }
        if(!(buttonNum % 7 == 0) || buttonNum == 0) {
            if (findViewById(buttonIds[buttonNum]).getBackgroundTintList().equals(findViewById(buttonIds[buttonNum - 1]).getBackgroundTintList())) {
                toReturn = true;
            }
        }
        if(!(buttonNum > 41)) {
            if (findViewById(buttonIds[buttonNum]).getBackgroundTintList().equals(findViewById(buttonIds[buttonNum + 7]).getBackgroundTintList())) {
                toReturn = true;
            }
        }

        if(!(buttonNum < 7)) {
            if (findViewById(buttonIds[buttonNum]).getBackgroundTintList().equals(findViewById(buttonIds[buttonNum - 7]).getBackgroundTintList())) {
                toReturn = true;
            }
        }
        return toReturn;
    }

    public void refreshBtn(View v){
        startIndexes.clear();
        for(int i = 0; i < buttonIds.length; i++) {
            findViewById(buttonIds[i]).setBackgroundColor(getResources().getColor(R.color.black));
            findViewById(buttonIds[i]).setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        }
        highlightStarters();
//        textview2.setText("");
    }
}