package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginVol : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference.child("Volunteer")
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        setContentView(binding.root)
        setTitle("Login")
        binding.signRedirectText.setOnClickListener {
            val intent = Intent(this, SignupVolunteer::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener {
            val intent = Intent(this,ForgetPassVol::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.emailTxt.text.toString().trim()
            val pass = binding.passTxt.text.toString().trim()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                    myRef.child(auth.currentUser?.uid.toString()).child("password").setValue(pass)
                    val intent = Intent(this, HomeVol::class.java)
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
            intent = Intent(this, SignupVolunteer::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener{
            intent = Intent(this, ForgetPassVol::class.java)
            startActivity(intent)
        }
    }
}