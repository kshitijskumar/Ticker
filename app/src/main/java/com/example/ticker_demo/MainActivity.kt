package com.example.ticker_demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ticker_demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTime.setOnClickListener {
            val timeSelected = binding.timePicker.getCurrentlySelectedTime()
            Toast.makeText(this, timeSelected, Toast.LENGTH_SHORT).show()
        }

        binding.btnSet.setOnClickListener {
            binding.timePicker.setInitialSelectedTime("10:40 Am")
        }

    }
}