package com.example.enablio

import android.app.Activity
import android.content.ContentValues
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.set
import com.example.enablio.databinding.ActivitySignupDisabledBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Arrays

class SignupDisabled : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignClient:GoogleSignInClient
    private lateinit var binding: ActivitySignupDisabledBinding
    private lateinit var rootFBRef: DatabaseReference
    private lateinit var callbackManager: CallbackManager

    var childrenCount: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupDisabledBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "SignUp"
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rootFBRef = FirebaseDatabase.getInstance().reference.child("Disabled")
        //Facebook:
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Log.d(ContentValues.TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(ContentValues.TAG, "facebook:onError", error)
                }
            },
        )
        binding.facebook.setOnClickListener{
            val accessToken = AccessToken.getCurrentAccessToken()
            if(accessToken!=null && accessToken.isExpired==false) {
                val intent = Intent(this, HomeDis::class.java)
                startActivity(intent)
            }else
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
        }



        auth = FirebaseAuth.getInstance()
        binding.dsignBtn.setOnClickListener {
            val name = binding.dnameTxt.text.toString()
            val email = binding.demailTxt.text.toString()
            val pass = binding.dpassTxt.text.toString()
            val conPass = binding.dconPassTxt.text.toString()
            val disability =
                if (binding.deaf.isChecked) {
                    binding.deaf.text.toString()
                }
                else if(binding.dumb.isChecked){
                    binding.dumb.text.toString()
                }
                else{
                    binding.blind.text.toString()
                }
            if(name.isEmpty()||email.isEmpty()||pass.isEmpty()||conPass.isEmpty()){
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
            else if(pass!=conPass){
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }else if(pass.length<8){
                Toast.makeText(this, "Passwords should be 8 characters at least!", Toast.LENGTH_SHORT).show()
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Invalid Email format!", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = Disabled_data(name, email, pass, disability, "", "")
                        rootFBRef.child(it.result.user?.uid.toString()).setValue(user)
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
                    }
                    else{
                        Toast.makeText(this, "This email is already signed up...Please login instead!", Toast.LENGTH_SHORT).show()
                    }
                }
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

        binding.linkedin.setOnClickListener {
        }
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(ContentValues.TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this, user?.email.toString(), Toast.LENGTH_LONG).show()
                    binding.demailTxt.text.clear()
                    binding.dnameTxt.text.clear()
                    binding.demailTxt.text.append(user?.email.toString())
                    binding.dnameTxt.text.append(user?.displayName.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode==Activity.RESULT_OK){
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
                binding.demailTxt.text.clear()
                binding.dnameTxt.text.clear()
                binding.demailTxt.text.append(account.email.toString())
                binding.dnameTxt.text.append(account.displayName.toString())
            }
            else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    }
}