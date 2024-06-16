package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivityForgetPassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForgetPassVol : AppCompatActivity() {
    private lateinit var binding:ActivityForgetPassBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Forget Password")
        binding = ActivityForgetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference.child("Volunteer")
        binding.verifyBtn.setOnClickListener {
            val email = binding.emailTxt.text.toString().trim()
            auth.isSignInWithEmailLink(email)
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            } else {
                checkIfEmailExistsAndSendResetEmail(email)
            }

        }

    }
    private fun checkIfEmailExistsAndSendResetEmail(email: String) {
        val query = myRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Email exists in the database
                    sendResetPasswordEmail(email)
                } else {
                    // Email does not exist in the database
                    println("Email does not exist: $email")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database error: ${error.message}")
            }
        })
    }

    private fun sendResetPasswordEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginVol::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error sending verification email", Toast.LENGTH_SHORT).show()
                }
            }

    }
}