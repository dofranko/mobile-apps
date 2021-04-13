package com.example.l2z1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.l2z1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun onClickStart3x3(view: View) {
        var board = Intent(this, Board3x3Activity::class.java)
        startActivityForResult(board, 33)
    }

    fun onClickStart5x5(view: View) {
        var board = Intent(this, Board5x5Activity::class.java)
        startActivityForResult(board, 55)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = data?.getStringExtra("data")
        if(result.isNullOrBlank())
            binding.textViewLastResult.text = ""
        else if(requestCode==33)
            binding.textViewLastResult.text = "Wygrany ostatniej rozgrywki(3x3):  " +  result.toString()
        else if (requestCode==55){
            binding.textViewLastResult.text = "Wygrany ostatniej rozgrywki(5x5):  " +  result.toString()
        }
    }
}