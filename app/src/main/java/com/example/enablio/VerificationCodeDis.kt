package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivityVerificationCodeBinding
import com.google.firebase.auth.FirebaseAuth

class VerificationCodeDis : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationCodeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Verify Code" // Corrected setTitle to title
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val email = intent.getStringExtra("email") ?: "" // Using Elvis operator for safety

        binding.verifyBtn.setOnClickListener {
            val verifyCode = binding.verifycode.text.toString().trim()
            if (verifyCode == "912458") {
                val intent = Intent(this, NewPassDis::class.java)
                intent.putExtra("email", email) // Corrected putExtra key
                startActivity(intent)
            } else {
                Toast.makeText(this, "Wrong Code!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.receiveagain.setOnClickListener {
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
