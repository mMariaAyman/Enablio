package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.facebook.login.LoginManager

class ContactusVol : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        setTitle("Contact Us")
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dropdown_menu_Profile -> {
                val intent = Intent(this, ProfileVol::class.java)
                startActivity(intent)
                return true
            }
            R.id.dropdown_menu_Suggestions -> {
                val intent = Intent(this, SuggestionsVol::class.java)
                startActivity(intent)
                return true
            }
            R.id.dropdown_menu_Contactus -> {
                val intent = Intent(this, ContactusVol::class.java)
                startActivity(intent)
                return true
            }
            R.id.dropdown_menu_Logout -> {
                LoginManager.getInstance().logOut()
                val intent = Intent(this, LoginVol::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}