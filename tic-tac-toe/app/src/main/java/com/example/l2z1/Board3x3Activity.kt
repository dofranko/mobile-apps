package com.example.l2z1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.l2z1.R
import com.example.l2z1.databinding.ActivityBoard3x3Binding
import android.widget.Button
import android.widget.Toast


class Board3x3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBoard3x3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard3x3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        clearButtons()
    }

    private var playerMove = "X"

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
        binding.button11.text = ""
        binding.button12.text = ""
        binding.button13.text = ""
        binding.button21.text = ""
        binding.button22.text = ""
        binding.button23.text = ""
        binding.button31.text = ""
        binding.button32.text = ""
        binding.button33.text = ""
    }

    private fun checkWin(){
        val checkTable = arrayOf(
            arrayOf(binding.button11, binding.button12, binding.button13),
            arrayOf(binding.button21, binding.button22, binding.button23),
            arrayOf(binding.button31, binding.button32, binding.button33),
            arrayOf(binding.button11, binding.button21, binding.button31),
            arrayOf(binding.button12, binding.button22, binding.button32),
            arrayOf(binding.button13, binding.button23, binding.button33),
            arrayOf(binding.button11, binding.button22, binding.button33),
            arrayOf(binding.button31, binding.button22, binding.button13)
        )
        for(set in checkTable){
            val result = checkTripleEquals(set[0], set[1], set[2])
            if(result != "") {
                Toast.makeText(this, "Wygrywa " + result, Toast.LENGTH_LONG).show()

                val myintent = Intent()
                myintent.putExtra("data", result)
                setResult(RESULT_OK, myintent)
                finish()
            }

        }
    }

    private fun checkTripleEquals(first: Button, second: Button, third: Button): String{
        if(first.text == second.text && second.text == third.text)
            return first.text.toString()
        return ""
    }
}