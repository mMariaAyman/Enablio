package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enablio.databinding.ActivitySignupVolunteerBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupVolunteer : AppCompatActivity() {
    private lateinit var binding:ActivitySignupVolunteerBinding
    private lateinit var rootFBRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("SignUp")
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        rootFBRef= FirebaseDatabase.getInstance().reference
        val name = binding.nameTxt.text.toString()
        val email = binding.emailTxt.text.toString()
        val password = binding.passTxt.text.toString()
        val conPassword = binding.conPassTxt.text.toString()
        var signlanguage = false
        if(binding.yes.isChecked){
            signlanguage= true
        }
        binding.signBtn.setOnClickListener {
            Toast.makeText(this, "${name}, ${email}, ${signlanguage}", Toast.LENGTH_SHORT).show()

        }
        /*
        binding.signBtn.setOnClickListener {
            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && conPassword.isNotEmpty()){
                if(password==conPassword){
                    val vId = rootFBRef.child("Volunteer").push().key
                    val volunteer = Volunteer_data(vId.hashCode(), name, email,password,signlanguage)
                        rootFBRef.child("Volunteer").child(vId.toString()).setValue(volunteer)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Volunteer Added Successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeVol::class.java)
                                startActivity(intent)

                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Adding Failed", Toast.LENGTH_SHORT).show()

                            }

                }else{
                    Toast.makeText(this, "Passwords should be the same", Toast.LENGTH_SHORT).show()
                    binding.passTxt.text.clear()
                    binding.conPassTxt.text.clear()
                }
            } else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

        }*/





    }
}