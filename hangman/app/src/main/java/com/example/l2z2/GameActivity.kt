package com.example.l2z2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.l2z2.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        wordToGuess = generateWord()
    }

    private lateinit var wordToGuess: String
    private var errors: Int = 0
    private val hangmanImages = arrayOf(
        R.drawable.wisielec, R.drawable.wisielec0, R.drawable.wisielec1, R.drawable.wisielec2,
        R.drawable.wisielec3, R.drawable.wisielec4, R.drawable.wisielec5, R.drawable.wisielec6,
        R.drawable.wisielec7, R.drawable.wisielec8, R.drawable.wisielec9, R.drawable.wisielec10,
        R.drawable.wisielec11
    )

    fun generateWord(): String {
        val words: Array<String> = resources.getStringArray(R.array.words_categories)
        val random = Random.nextInt(words.size)
        var word = words[random]
        var text = ""
        for(letter in word){
            text += if (letter.isLetter()){
                "_"
            } else
                " "
        }
        binding.textViewWord.text = text
        return words[random]
    }

    fun checkLetter(letterGuessed: Char) {
        if (wordToGuess.contains(letterGuessed, ignoreCase = true)) {
            var newText = ""
            var actualText = binding.textViewWord.text.toString()
            for(i in wordToGuess.indices){
                newText += if (!wordToGuess[i].isLetter()){
                    " "
                } else if (wordToGuess[i].toLowerCase() != letterGuessed.toLowerCase()) {
                    actualText[i]
                }
                else {
                    letterGuessed
                }
            }
            binding.textViewWord.text = newText
            if(!binding.textViewWord.text.toString().contains("_")){
                Toast.makeText(this, "WYGRALES", Toast.LENGTH_LONG).show()
                val buttons = arrayOf(binding.buttonA, binding.buttonB, binding.buttonC,
                    binding.buttonD, binding.buttonE, binding.buttonF, binding.buttonG, binding.buttonH,
                    binding.buttonI, binding.buttonJ, binding.buttonK, binding.buttonL, binding.buttonM,
                    binding.buttonN, binding.buttonO, binding.buttonP, binding.buttonQ, binding.buttonR,
                    binding.buttonS, binding.buttonT, binding.buttonU, binding.buttonV, binding.buttonW,
                    binding.buttonX, binding.buttonY, binding.buttonZ)
                for(button in buttons)
                    button.isEnabled = false
            }
        } else {
            errors++;
            binding.imageView.setImageResource(hangmanImages[errors])
            if(errors+1 >= hangmanImages.size){
                binding.textViewWord.text = wordToGuess
                Toast.makeText(this, "PRZEGRALES", Toast.LENGTH_LONG).show()
                val buttons = arrayOf(binding.buttonA, binding.buttonB, binding.buttonC,
                    binding.buttonD, binding.buttonE, binding.buttonF, binding.buttonG, binding.buttonH,
                    binding.buttonI, binding.buttonJ, binding.buttonK, binding.buttonL, binding.buttonM,
                    binding.buttonN, binding.buttonO, binding.buttonP, binding.buttonQ, binding.buttonR,
                    binding.buttonS, binding.buttonT, binding.buttonU, binding.buttonV, binding.buttonW,
                    binding.buttonX, binding.buttonY, binding.buttonZ)
                for(button in buttons)
                    button.isEnabled = false
            }
        }
    }

    fun onClickLetter(view: View) {
        (view as Button).isEnabled = false
        val letter = view.text.toString()[0]
        checkLetter(letter)
    }
}