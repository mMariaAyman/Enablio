package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginDis : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Login")
        auth = FirebaseAuth.getInstance()
        binding.signRedirectText.setOnClickListener {
           val intent = Intent(this, SignupDisabled::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener {
            val intent = Intent(this,ForgetPassDis::class.java)
            startActivity(intent)
        }
       binding.loginBtn.setOnClickListener {
            val email = binding.emailTxt.text.toString()
            val pass = binding.passTxt.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                    val intent = Intent(this, HomeDis::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "User not Found!", Toast.LENGTH_LONG).show()

                }

            }
            else{
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_LONG).show()
            }
        }
        binding.signRedirectText.setOnClickListener{
            intent = Intent(this, SignupDisabled::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener{
            intent = Intent(this, ForgetPassDis::class.java)
            startActivity(intent)
        }

    }
}