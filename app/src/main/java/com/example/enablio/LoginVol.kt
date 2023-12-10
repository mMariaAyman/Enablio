package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginVol : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var fb: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        setContentView(binding.root)
        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, HomeVol::class.java)
            startActivity(intent)
        }
        binding.signRedirectText.setOnClickListener {
            val intent = Intent(this, SignupVolunteer::class.java)
            startActivity(intent)
        }
        /* binding.loginBtn.setOnClickListener {
            val email = binding.emailTxt.text.toString()
            val pass = binding.passTxt.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                fb.signInWithEmailLink(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, HomeDis::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_LONG).show()
            }
        }
        binding.signRedirectText.setOnClickListener{
            intent = Intent(this, SignupVolunteer::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/
    }
}