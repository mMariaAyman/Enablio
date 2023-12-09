package com.example.enablio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityHomeDisBinding

class HomeDis : AppCompatActivity() {
    private lateinit var binding:ActivityHomeDisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}