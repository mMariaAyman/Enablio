package com.example.enablio

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContextCompat
import com.example.enablio.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.events.EventHandler
import com.google.firebase.storage.FirebaseStorage
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.components.audiovideo.ZegoBaseAudioVideoForegroundView
import com.zegocloud.uikit.components.audiovideo.ZegoForegroundViewProvider
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.internal.ZegoAudioVideoForegroundView
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUIKitUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import com.zegocloud.uikit.service.defines.ZegoUserUpdateListener
import com.zegocloud.uikit.service.express.EventHandlerList
import im.zego.zegoexpress.entity.ZegoUser
import java.io.File



class LoginDis : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Login")
        auth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference.child("Disabled")
        binding.signRedirectText.setOnClickListener {
           val intent = Intent(this, SignupDisabled::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener {
            val intent = Intent(this,ForgetPassDis::class.java)
            startActivity(intent)
        }
       binding.loginBtn.setOnClickListener {
            val email = binding.emailTxt.text.toString().trim()
            val pass = binding.passTxt.text.toString().trim()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                    myRef.child(auth.currentUser?.uid.toString()).child("password").setValue(pass)
                    myRef.child(auth.currentUser?.uid.toString()).child("isOnline").setValue(true)
                    val intent = Intent(this, HomeDis::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Wrong Email or Password!", Toast.LENGTH_LONG).show()
                }

            }
            else{
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_LONG).show()
            }
        }
        binding.signRedirectText.setOnClickListener{
            intent = Intent(this, SignupDisabled::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener{
            intent = Intent(this, ForgetPassDis::class.java)
            startActivity(intent)
        }

    }



}