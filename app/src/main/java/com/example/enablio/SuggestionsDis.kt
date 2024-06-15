package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.enablio.databinding.ActivityLoginBinding
import com.example.enablio.databinding.ActivitySuggestionsBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SuggestionsDis : AppCompatActivity() {
    private lateinit var binding: ActivitySuggestionsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Suggestion & Complains")
        binding = ActivitySuggestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myRef = FirebaseDatabase.getInstance().reference.child("Suggestion")
        auth = FirebaseAuth.getInstance()
        binding.send.setOnClickListener{
            val subtxt = binding.subjectTxt
            val sub = subtxt.text.toString()
            val msgtxt = binding.messageTxt
            val msg = msgtxt.text.toString()
            if (msg.isNotEmpty()){
                val sug = Suggestion_data(sub, msg)
                myRef.child(auth.currentUser?.uid.toString()).push().setValue(sug)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added Successfully!", Toast.LENGTH_SHORT).show()
                        subtxt.text.clear()
                        msgtxt.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Added Failed!", Toast.LENGTH_SHORT).show()

                    }

            }else{
                Toast.makeText(this, "All fields are requierd",Toast.LENGTH_LONG).show()
            }
        }


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dropdown_menu_Profile -> {
                val intent = Intent(this, ProfileDis::class.java)
                startActivity(intent)
                return true
            }
            R.id.dropdown_menu_Suggestions -> {
                val intent = Intent(this, SuggestionsDis::class.java)
                startActivity(intent)
                return true
            }
            R.id.dropdown_menu_Contactus -> {
                val intent = Intent(this, ContactusDis::class.java)
                startActivity(intent)
                return true
            }
            R.id.dropdown_menu_Logout -> {
                LoginManager.getInstance().logOut()
                val intent = Intent(this, LoginDis::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}