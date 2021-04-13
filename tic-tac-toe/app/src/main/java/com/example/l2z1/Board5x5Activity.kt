package com.example.l2z1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.l2z1.databinding.ActivityBoard5x5Binding


class Board5x5Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBoard5x5Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard5x5Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createButtons()
        clearButtons()
    }

    private var playerMove = "X"
    private lateinit var checkTable : Array<Array<Button>>
    private lateinit var checkTableCrosswise : Array<Array<Button>>

    private fun createButtons(){
        checkTable = arrayOf(
                arrayOf(binding.button11, binding.button12, binding.button13, binding.button14, binding.button15),
                arrayOf(binding.button21, binding.button22, binding.button23, binding.button24, binding.button25),
                arrayOf(binding.button31, binding.button32, binding.button33, binding.button34, binding.button35),
                arrayOf(binding.button41, binding.button42, binding.button43, binding.button44, binding.button45),
                arrayOf(binding.button51, binding.button52, binding.button53, binding.button54, binding.button55)
        )
        checkTableCrosswise = arrayOf(
                arrayOf(binding.button41, binding.button32,binding.button23,binding.button14),
                arrayOf(binding.button51, binding.button42, binding.button33, binding.button24, binding.button15),
                arrayOf(binding.button52, binding.button43, binding.button34, binding.button25),
                arrayOf(binding.button21, binding.button32, binding.button43, binding.button54),
                arrayOf(binding.button11, binding.button22, binding.button33, binding.button44, binding.button55),
                arrayOf(binding.button12, binding.button23, binding.button34, binding.button45)
        )
    }

    fun onClickBoardButton(view: View) {
        var button = view as Button
        button.text = playerMove
        if(playerMove=="X") {
            playerMove = "O"
            binding.textViewMove.text = "Ruch O"
        }
        else {
            playerMove = "X"
            binding.textViewMove.text = "Ruch X"
        }
        button.isEnabled = false
        checkWin()

    }

    private fun clearButtons(){
        for(set in checkTable){
            for(button in set){
                button.text = ""
            }
        }
    }

    private fun checkWin(){
        //Horyzontalnie
        for(set in checkTable){
            var lastMove = ""
            var lastCount = 0
            for(button in set){
                if(button.text == "")
                    continue
                if(button.text == lastMove)
                    lastCount++
                else{
                    lastMove = button.text.toString()
                    lastCount = 1
                    continue
                }
                if(lastCount==4)
                    finishMatch(lastMove)
            }
        }
        //Vertykalnie
        for(i in 0..4){
            var lastMove = ""
            var lastCount = 0
            for(ii in 0..4){
                if(checkTable[ii][i].text == "")
                    continue
                if(checkTable[ii][i].text == lastMove)
                    lastCount++
                else{
                    lastMove = checkTable[ii][i].text.toString()
                    lastCount = 1
                    continue
                }
                if(lastCount==4)
                    finishMatch(lastMove)
            }
        }
        //Ukośnie
        for(set in checkTableCrosswise){
            var lastMove = ""
            var lastCount = 0
            for(button in set){
                if(button.text == "")
                    continue
                if(button.text == lastMove)
                    lastCount++
                else{
                    lastMove = button.text.toString()
                    lastCount = 1
                    continue
                }
                if(lastCount==4)
                    finishMatch(lastMove)
            }
        }
    }

    private fun finishMatch(won: String){
        Toast.makeText(this, "Wygrywa " + won, Toast.LENGTH_LONG).show()

        val myintent = Intent()
        myintent.putExtra("data", won)
        setResult(RESULT_OK, myintent)
        finish()
    }

    private fun checkTripleEquals(first: Button, second: Button, third: Button): String{
        if(first.text == second.text && second.text == third.text)
            return first.text.toString()
        return ""
    }
}