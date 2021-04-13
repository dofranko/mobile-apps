package com.example.lab1zad2java

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab1zad2kotlin.R
import com.example.lab1zad2kotlin.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private var playerScoreTotal = 0
    private var enemyScoreTotal = 0
    private var playerScoreRound = 0
    private var enemyScoreRound = 0
    private val winsTable = arrayOf(intArrayOf(0, -1, 1), intArrayOf(1, 0, -1), intArrayOf(-1, 1, 0))

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_main)
        playerScoreTotal = 0
        enemyScoreTotal = 0
        playerScoreRound = 0
        enemyScoreRound = 0
        refreshScores()
    }

    private fun refreshScores() {
        if (playerScoreRound > 2) {
            playerScoreTotal++
            playerScoreRound = 0
            enemyScoreRound = 0
        } else if (enemyScoreRound > 2) {
            enemyScoreTotal++
            enemyScoreRound = 0
            playerScoreRound = 0
        }
        val pst = findViewById<View>(R.id.textViewPlayerPoints) as TextView
        val est = findViewById<View>(R.id.textViewPointsEnemy) as TextView
        pst.text = Integer.toString(playerScoreTotal)
        est.text = Integer.toString(enemyScoreTotal)
    }

    private fun refreshCircles() {
        val playerCircles = arrayOf(findViewById<View>(R.id.circleImagePlayer0) as ImageView,
                findViewById<View>(R.id.circleImagePlayer1) as ImageView)
        val enemyCircles = arrayOf(findViewById<View>(R.id.circleImageEnemy0) as ImageView,
                findViewById<View>(R.id.circleImageEnemy1) as ImageView)
        for (i in 0..1) {
            playerCircles[i].setImageResource(R.drawable.circle_grey)
            enemyCircles[i].setImageResource(R.drawable.circle_grey)
        }
        for (i in 0 until playerScoreRound) {
            playerCircles[i].setImageResource(R.drawable.circle_green)
        }
        for (i in 0 until enemyScoreRound) {
            enemyCircles[i].setImageResource(R.drawable.circle_green)
        }
    }

    fun buttonDecisionOnClick(view: View) {
        //0 - stone, 1 - paper, 2 - scissors
        val enemyDecision = Random().nextInt(3)
        when (enemyDecision) {
            0 -> Toast.makeText(this, "Przeciwnik: Kamień!", Toast.LENGTH_SHORT).show()
            1 -> Toast.makeText(this, "Przeciwnik: Papier!", Toast.LENGTH_SHORT).show()
            2 -> Toast.makeText(this, "Przeciwnik: Nożyce!", Toast.LENGTH_SHORT).show()
        }
        var playerDecision = 0
        when (view.id) {
            R.id.buttonStone -> playerDecision = 0
            R.id.buttonPaper -> playerDecision = 1
            R.id.buttonScissors -> playerDecision = 2
        }
        val result = winsTable[playerDecision][enemyDecision]
        if (result == -1) {
            enemyScoreRound++
        } else if (result == 1) {
            playerScoreRound++
        }
        refreshScores()
        refreshCircles()
    }
}