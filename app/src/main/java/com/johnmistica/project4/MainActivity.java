package com.johnmistica.project4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends Activity {

    private Button startButton;
    private ArrayList<TextView> gridArray;
    private Thread t1;
    private Thread t2;
    private TextView textLog;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) ;
    private final Handler mHandler2 = new Handler(Looper.getMainLooper()) ;
    private volatile boolean turn = false;
    public volatile boolean reset = false;
    private static int moveCount;
    public int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        textLog = (TextView) findViewById(R.id.textLog);

        gridArray = new ArrayList<TextView>(Arrays.asList(
                (TextView)findViewById(R.id.textView1),(TextView)findViewById(R.id.textView2),(TextView)findViewById(R.id.textView3),
                (TextView)findViewById(R.id.textView4),(TextView)findViewById(R.id.textView5),(TextView)findViewById(R.id.textView6),
                (TextView)findViewById(R.id.textView7),(TextView)findViewById(R.id.textView8),(TextView)findViewById(R.id.textView9)));

        startButton.setOnClickListener(v -> {
            if(reset){
                startButton.setText("Start Game");
                textLog.setText("Game Reset");
                Log.i("Gamestate","reset");

                mHandler.removeCallbacksAndMessages(null);
                mHandler2.removeCallbacksAndMessages(null);

                reset = false;
                moveCount = 0;

                for (TextView x : gridArray) {
                    x.setText(" ");
                    x.setTextColor(Color.BLACK);
                }

                try {  Thread.sleep(2000); }
                catch (InterruptedException e) { System.out.println(e) ; }
            }
            else{

                startButton.setText("Reset Game");

                reset = true;
                moveCount = 0;

                for (TextView x : gridArray) {
                    x.setText(" ");
                    x.setTextColor(Color.BLACK);
                }

                t1 = new Thread(new bot1());
                t2 = new Thread(new bot2());

                t1.start();
                t2.start();
            }

        });
    }

    public class bot1 implements Runnable  {

        public void run() {
            int count = 0;
            while(!checkWinCondition() && killSwitch()){
                while(turn == false){
                    if(count == 9){
                        count = 0;
                    }

                    if(gridArray.get(count).getText() == "X" || gridArray.get(count).getText() == "O"){
                        count++;
                        continue;
                    }
                    else{
                        try {  Thread.sleep(3000); }
                        catch (InterruptedException e) { System.out.println(e) ; }
                        int finalCount = count;
                        mHandler.post(new Runnable() {
                            public void run() {
                                if(killSwitch()){
                                    gridArray.get(finalCount).setText("X") ;
                                    if(moveCount < 9){
                                        moveCount++;
                                    }
                                    turn = true;
                                    Log.i("bot1", "Move " + moveCount + " on square " + finalCount);
                                    textLog.setText("bot1: Move " + moveCount + " on square " + finalCount);
                                }
                            }
                        } );

                    }
                    try {  Thread.sleep(1000); }
                    catch (InterruptedException e) { System.out.println(e) ; }
                }
                Log.i("bot1", "still looping");
            }
            Log.i("bot1", "finished");
        }
    }

    public class bot2 implements Runnable  {

        public void run() {
            while(!checkWinCondition() && killSwitch()){
                if(turn == true){
                    Random random=new Random();
                    int rand =random.nextInt(9);

                    if(gridArray.get(rand).getText() == "X" || gridArray.get(rand).getText() == "O"){
                        continue;
                    }
                    else{
                        try {  Thread.sleep(3000); }
                        catch (InterruptedException e) { System.out.println(e) ; }
                        mHandler2.post(new Runnable() {
                            public void run() {
                                if(killSwitch()){
                                    gridArray.get(rand).setText("O") ;
                                    if(moveCount < 9){
                                        moveCount++;
                                    }
                                    turn = false;
                                    Log.i("bot2", "Move " + moveCount + " on square " + rand);
                                    textLog.setText("bot2: Move " + moveCount + " on square " + rand);
                                }
                            }
                        } );

                    }
                    try {  Thread.sleep(1000); }
                    catch (InterruptedException e) { System.out.println(e) ; }
                }
                Log.i("bot2", "still looping");
            }
            Log.i("bot2", "finished");
        }
    }

    public synchronized boolean killSwitch(){
        return reset;
    }

    private boolean checkWinCondition(){

        //checks top left to bottom right
        if(gridArray.get(0).getText() == "X" && gridArray.get(4).getText() == "X" && gridArray.get(8).getText() == "X"){
            gridArray.get(0).setTextColor(Color.GREEN);
            gridArray.get(4).setTextColor(Color.GREEN);
            gridArray.get(8).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(0).getText() == "O" && gridArray.get(4).getText() == "O" && gridArray.get(8).getText() == "O"){
            gridArray.get(0).setTextColor(Color.GREEN);
            gridArray.get(4).setTextColor(Color.GREEN);
            gridArray.get(8).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //checks top right to bottom left
        if(gridArray.get(2).getText() == "X" && gridArray.get(4).getText() == "X" && gridArray.get(6).getText() == "X"){
            gridArray.get(2).setTextColor(Color.GREEN);
            gridArray.get(4).setTextColor(Color.GREEN);
            gridArray.get(6).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(2).getText() == "O" && gridArray.get(4).getText() == "O" && gridArray.get(6).getText() == "O"){
            gridArray.get(2).setTextColor(Color.GREEN);
            gridArray.get(4).setTextColor(Color.GREEN);
            gridArray.get(6).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //middle top to bottom
        if(gridArray.get(1).getText() == "X" && gridArray.get(4).getText() == "X" && gridArray.get(7).getText() == "X"){
            gridArray.get(1).setTextColor(Color.GREEN);
            gridArray.get(2).setTextColor(Color.GREEN);
            gridArray.get(7).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(1).getText() == "O" && gridArray.get(4).getText() == "O" && gridArray.get(7).getText() == "O"){
            gridArray.get(1).setTextColor(Color.GREEN);
            gridArray.get(2).setTextColor(Color.GREEN);
            gridArray.get(7).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //middle left to right
        if(gridArray.get(3).getText() == "X" && gridArray.get(4).getText() == "X" && gridArray.get(5).getText() == "X"){
            gridArray.get(3).setTextColor(Color.GREEN);
            gridArray.get(4).setTextColor(Color.GREEN);
            gridArray.get(5).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(3).getText() == "O" && gridArray.get(4).getText() == "O" && gridArray.get(5).getText() == "O"){
            gridArray.get(3).setTextColor(Color.GREEN);
            gridArray.get(4).setTextColor(Color.GREEN);
            gridArray.get(5).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //top left to top right
        if(gridArray.get(0).getText() == "X" && gridArray.get(1).getText() == "X" && gridArray.get(2).getText() == "X"){
            gridArray.get(0).setTextColor(Color.GREEN);
            gridArray.get(1).setTextColor(Color.GREEN);
            gridArray.get(2).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(0).getText() == "O" && gridArray.get(1).getText() == "O" && gridArray.get(2).getText() == "O"){
            gridArray.get(0).setTextColor(Color.GREEN);
            gridArray.get(1).setTextColor(Color.GREEN);
            gridArray.get(2).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //bottom left to bottom right
        if(gridArray.get(6).getText() == "X" && gridArray.get(7).getText() == "X" && gridArray.get(8).getText() == "X"){
            gridArray.get(6).setTextColor(Color.GREEN);
            gridArray.get(7).setTextColor(Color.GREEN);
            gridArray.get(8).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(6).getText() == "O" && gridArray.get(7).getText() == "O" && gridArray.get(8).getText() == "O"){
            gridArray.get(6).setTextColor(Color.GREEN);
            gridArray.get(7).setTextColor(Color.GREEN);
            gridArray.get(8).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //top right to bottom right
        if(gridArray.get(2).getText() == "X" && gridArray.get(5).getText() == "X" && gridArray.get(8).getText() == "X"){
            gridArray.get(2).setTextColor(Color.GREEN);
            gridArray.get(5).setTextColor(Color.GREEN);
            gridArray.get(8).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(2).getText() == "O" && gridArray.get(5).getText() == "O" && gridArray.get(8).getText() == "O"){
            gridArray.get(2).setTextColor(Color.GREEN);
            gridArray.get(5).setTextColor(Color.GREEN);
            gridArray.get(8).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        //top left to bottom left
        if(gridArray.get(0).getText() == "X" && gridArray.get(3).getText() == "X" && gridArray.get(6).getText() == "X"){
            gridArray.get(0).setTextColor(Color.GREEN);
            gridArray.get(3).setTextColor(Color.GREEN);
            gridArray.get(6).setTextColor(Color.GREEN);
            textLog.setText("bot1 is the winner!");
            return true;
        }
        if(gridArray.get(0).getText() == "O" && gridArray.get(3).getText() == "O" && gridArray.get(6).getText() == "O"){
            gridArray.get(0).setTextColor(Color.GREEN);
            gridArray.get(3).setTextColor(Color.GREEN);
            gridArray.get(6).setTextColor(Color.GREEN);
            textLog.setText("bot2 is the winner!");
            return true;
        }

        if(moveCount == 9){
            return true;
        }

        return false;
    }
}