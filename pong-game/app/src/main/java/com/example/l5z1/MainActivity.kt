package com.example.l5z1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setTop()
    }

    fun onClickStartGame(view :View){
        val gameIntent = Intent(this, GameActivity::class.java)
        startActivityForResult(gameIntent, 1)
    }

    fun setTop(){
        var topResults : List<ResultEntity> = emptyList()
        val t = thread {
            topResults = GameDatabase.getInstance(this).resultDao().getTop()
        }
        t.join()
        val textView = findViewById<TextView>(R.id.textViewTopResults)
        var resultsString : String = "Last Results:"

        for (result in topResults){
            resultsString += "\nGracz " + result.PlayerScore + " : " + result.EnemyScore + " Przeciwnik"
        }
        textView.text = resultsString
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setTop()
    }
}