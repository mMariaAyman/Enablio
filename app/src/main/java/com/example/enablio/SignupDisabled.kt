package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivitySignupDisabledBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupDisabled : AppCompatActivity() {
    private lateinit var binding: ActivitySignupDisabledBinding
    private lateinit var rootFBRef: DatabaseReference
    var childrenCount: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupDisabledBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "SignUp"
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rootFBRef = FirebaseDatabase.getInstance().reference.child("Disabled")
        var flag = 5
        binding.dsignBtn.setOnClickListener {
            val name = binding.dnameTxt.text.toString()
            var email = binding.demailTxt.text.toString()
            val password = binding.dpassTxt.text.toString()
            val conPassword = binding.dconPassTxt.text.toString()
            val disability = if(binding.blind.isChecked){
                    "Blind"
            } else if(binding.deaf.isChecked){
                    "Deaf"
            } else{
                    "Dumb"
            }
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && conPassword.isNotEmpty()) {
                if (password == conPassword) {
                    if (password.length >= 8) {
                        rootFBRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                flag = 1
                                if (dataSnapshot.exists()) {
                                    childrenCount = dataSnapshot.childrenCount
                                    dataSnapshot.children.forEach { snapshot ->
                                        val storedEmail = snapshot.child("email").getValue(String::class.java)
                                        if (storedEmail == email) {
                                            flag = 0 // Set flag to 0 if a match is found
                                            return@forEach
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle the error if the operation is canceled or fails
                            }
                        })
                        if (flag == 1) {
                            val disabled = Disabled_data(name, email, password, disability, "", "")
                            rootFBRef.child((childrenCount + 1).toString()).setValue(disabled)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Disabled Added Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, HomeDis::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Adding Failed", Toast.LENGTH_SHORT).show()
                                }
                        }else if(flag==0){
                            Toast.makeText(this, "This Email is registered already", Toast.LENGTH_SHORT).show()
                            binding.demailTxt.text.clear()
                            email = ""

                        }

                    }else{
                        Toast.makeText(this, "Password should at least contain 8 characters", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Passwords should be the same", Toast.LENGTH_SHORT)
                        .show()
                    binding.dpassTxt.text.clear()
                    binding.dconPassTxt.text.clear()
                }

            }else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.google.setOnClickListener{

        }
        binding.facebook.setOnClickListener {
        }
        binding.linkedin.setOnClickListener {
        }
    }
}