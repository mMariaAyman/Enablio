package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.enablio.databinding.ActivityHomeDisBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class HomeDis : AppCompatActivity() {
    private lateinit var binding:ActivityHomeDisBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Home")

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