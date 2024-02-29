package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityVerificationCodeBinding

class VerificationCodeVol : AppCompatActivity() {
    private lateinit var binding:ActivityVerificationCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Verify Code")
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.verifyBtn.setOnClickListener {
            val intent = Intent(this, NewPassDis::class.java)
            startActivity(intent)
        }
    }
}