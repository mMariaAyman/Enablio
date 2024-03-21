package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.enablio.databinding.ActivityProfileBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values

class ProfileVol : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var rootFBRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("My Profile")
        auth = FirebaseAuth.getInstance()
        rootFBRef = FirebaseDatabase.getInstance().getReference("Volunteer")
        binding.editNameTxt.text.append(auth.currentUser?.displayName.toString())
        binding.userName.text = auth.currentUser?.displayName.toString()
        binding.editEmailTxt.text.append(auth.currentUser?.email.toString())

        binding.saveProfile.setOnClickListener {
            val map = mapOf<String,String>(
                "name" to binding.editNameTxt.text.toString(),
                "gender" to binding.gender.text.toString(),

            )
            rootFBRef.child("2").updateChildren(map).addOnSuccessListener {
                Toast.makeText(this,"Updated Successfully!", Toast.LENGTH_SHORT).show()
                val genderRef = rootFBRef.child("2/gender")
                val nameRef = rootFBRef.child("2/name")
                nameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val nameValue = dataSnapshot.getValue(String::class.java)
                        binding.userName.setText(nameValue)
                        binding.editNameTxt.setText(nameValue)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
                genderRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val genderValue = dataSnapshot.getValue(String::class.java)
                        binding.gender.setText(genderValue)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
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