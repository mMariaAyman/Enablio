package com.example.enablio

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.enablio.databinding.ActivityLoginBinding
import com.example.enablio.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var fb:FirebaseAuth
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fb = FirebaseAuth.getInstance()
        binding.signBtn.setOnClickListener {
            val name = binding.nameTxt.text.toString()
            val phone = binding.phoneTxt.text.toString()
            val email = binding.emailTxt.text.toString()
            val pass = binding.passTxt.text.toString()
            val conPass = binding.conPassTxt.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty() &&name.isNotEmpty()
                &&phone.isNotEmpty()&&conPass.isNotEmpty()){
                if(pass==conPass){
                    fb.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                }
                else
                {
                    Toast.makeText(this, "Password doesn't matched!", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_LONG).show()
            }

        }
        binding.loginRedirectText.setOnClickListener{
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
      /*  val btn: Button = findViewById(R.id.signBtn)
        val name:EditText = findViewById(R.id.nameTxt)
        val phone: EditText = findViewById(R.id.phoneTxt)
        val email:EditText = findViewById(R.id.emailTxt)
        val pass: EditText = findViewById(R.id.passTxt)
        val conPass:EditText = findViewById(R.id.conPassTxt)

        btn.setOnClickListener {
            if(name.text.toString()==""||phone.text.toString()==""||email.text.toString()==""
                ||pass.text.toString()==""||conPass.text.toString()==""){
                Toast.makeText(this, "Fill all attributes", Toast.LENGTH_LONG).show()
            }
            else if(pass.text.toString()!=conPass.text.toString()){
                Toast.makeText(this, "Password and Confirm isn't Same", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Registered Successfully..", Toast.LENGTH_LONG).show()
                login()
            }
        }
    }

    private fun login() {
        intent = Intent(this, Login::class.java)
        startActivity(intent)
    }*/
}