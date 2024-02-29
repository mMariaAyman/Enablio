package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityForgetPassBinding

class ForgetPassVol : AppCompatActivity() {
    private lateinit var binding:ActivityForgetPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Forget Password")
        binding = ActivityForgetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.verifyBtn.setOnClickListener {
            val intent = Intent(this, VerificationCodeVol::class.java)
            startActivity(intent)
        }
    }
}