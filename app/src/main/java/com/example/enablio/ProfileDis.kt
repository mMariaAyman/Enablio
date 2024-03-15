package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.enablio.databinding.ActivityProfileBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ProfileDis : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var rootFBRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("My Profile")
        auth = FirebaseAuth.getInstance()
        binding.editNameTxt.text.append(auth.currentUser?.displayName.toString())
        binding.userName.text = auth.currentUser?.displayName.toString()
        binding.editEmailTxt.text.append(auth.currentUser?.email.toString())

        binding.saveProfile.setOnClickListener {
            binding.editNameTxt.text.toString()
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