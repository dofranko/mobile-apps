package com.example.lab1zad2java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int playerScoreTotal;
    private int enemyScoreTotal;
    private int playerScoreRound;
    private int enemyScoreRound;
    private final int[][] winsTable = {{0, -1, 1},{1,0,-1},{-1,1,0}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerScoreTotal =0;
        enemyScoreTotal =0;
        playerScoreRound =0;
        enemyScoreRound =0;
        refreshScores();
    }

    private void refreshScores() {
        if(playerScoreRound>2){
            playerScoreTotal++;
            playerScoreRound=0;
            enemyScoreRound=0;
        }
        else if(enemyScoreRound>2){
            enemyScoreTotal++;
            enemyScoreRound=0;
            playerScoreRound=0;
        }
        TextView pst =(TextView)findViewById(R.id.textViewPlayerPoints);
        TextView est = (TextView)findViewById(R.id.textViewPointsEnemy);
        pst.setText(Integer.toString(playerScoreTotal));
        est.setText(Integer.toString(enemyScoreTotal));
    }

    private void refreshCircles(){
        ImageView[] playerCircles = {(ImageView) findViewById(R.id.circleImagePlayer0),
                (ImageView) findViewById(R.id.circleImagePlayer1)};
        ImageView[] enemyCircles = {(ImageView) findViewById(R.id.circleImageEnemy0),
                (ImageView) findViewById(R.id.circleImageEnemy1)};
        for(int i=0; i<2; i++){
            playerCircles[i].setImageResource(R.drawable.circle_grey);
            enemyCircles[i].setImageResource(R.drawable.circle_grey);
        }
        for(int i=0; i<playerScoreRound; i++){
            playerCircles[i].setImageResource(R.drawable.circle_green);
        }
        for(int i=0; i<enemyScoreRound; i++){
            enemyCircles[i].setImageResource(R.drawable.circle_green);
        }
    }

    public void buttonDecisionOnClick(View view){
        //0 - stone, 1 - paper, 2 - scissors
        int enemyDecision = new Random().nextInt(3);
        switch (enemyDecision){
            case 0:
                Toast.makeText(this, "Przeciwnik: Kamień!", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Przeciwnik: Papier!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Przeciwnik: Nożyce!", Toast.LENGTH_SHORT).show();
                break;
        }

        int playerDecision = 0;
        switch(view.getId()){
            case R.id.buttonStone:
                playerDecision = 0;
                break;
            case R.id.buttonPaper:
                playerDecision = 1;
                break;
            case R.id.buttonScissors:
                playerDecision = 2;
                break;
        }
        int result = winsTable[playerDecision][enemyDecision];
        if(result==-1){
            enemyScoreRound++;
        }
        else if (result == 1){
            playerScoreRound++;
        }
        refreshScores();
        refreshCircles();
    }
}