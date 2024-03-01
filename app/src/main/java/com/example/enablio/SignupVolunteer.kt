package com.example.enablio

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.enablio.databinding.ActivitySignupVolunteerBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupVolunteer : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignClient: GoogleSignInClient
    private lateinit var binding: ActivitySignupVolunteerBinding
    private lateinit var rootFBRef: DatabaseReference
    var childrenCount: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "SignUp"
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rootFBRef = FirebaseDatabase.getInstance().reference.child("Volunteer")
        var flag = 5
        binding.signBtn.setOnClickListener {
            val name = binding.nameTxt.text.toString()
            var email = binding.emailTxt.text.toString()
            val password = binding.passTxt.text.toString()
            val conPassword = binding.conPassTxt.text.toString()
            val signLanguage = binding.yes.isChecked
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
                            val volunteer = Volunteer_data(name, email, password, signLanguage, "", "")
                            rootFBRef.child((childrenCount + 1).toString()).setValue(volunteer)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Volunteer Added Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, HomeVol::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Adding Failed", Toast.LENGTH_SHORT).show()
                                }
                        }else if(flag==0){
                            Toast.makeText(this, "This Email is registered already", Toast.LENGTH_SHORT).show()
                            binding.emailTxt.text.clear()
                            email = ""

                        }

                    }else{
                        Toast.makeText(this, "Password should at least contain 8 characters", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Passwords should be the same", Toast.LENGTH_SHORT)
                        .show()
                    binding.passTxt.text.clear()
                    binding.conPassTxt.text.clear()
                }

            }else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(this, gso)
        binding.google.setOnClickListener{
            val signInIntent = googleSignClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.facebook.setOnClickListener {
        }
        binding.linkedin.setOnClickListener {
        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode== Activity.RESULT_OK){
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handelResults(task)
    }
    }
    private fun handelResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result

            if(account!=null){
                updateUI(account)
            }
        }
        else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                binding.emailTxt.text.clear()
                binding.nameTxt.text.clear()
                binding.emailTxt.text.append(account.email.toString())
                binding.nameTxt.text.append(account.displayName.toString())
            }
            else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    }

}