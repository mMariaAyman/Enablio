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
import androidx.core.view.marginBottom
import com.example.enablio.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.zegocloud.uikit.components.audiovideo.ZegoBaseAudioVideoForegroundView
import com.zegocloud.uikit.components.audiovideo.ZegoForegroundViewProvider
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.internal.ZegoAudioVideoForegroundView
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File


class LoginVol : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference.child("Volunteer")
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        setContentView(binding.root)
        setTitle("Login")
        binding.signRedirectText.setOnClickListener {
            val intent = Intent(this, SignupVolunteer::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener {
            val intent = Intent(this,ForgetPassVol::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.emailTxt.text.toString().trim()
            val pass = binding.passTxt.text.toString().trim()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                    myRef.child(auth.currentUser?.uid.toString()).child("password").setValue(pass)
                    myRef.child(auth.currentUser?.uid.toString()).child("isOnline").setValue(true)
                    val intent = Intent(this, HomeVol::class.java)
                    startActivity(intent)
                    VideoCallServices(auth.currentUser?.uid.toString())
                }.addOnFailureListener {
                    Toast.makeText(this, "User not Found!", Toast.LENGTH_LONG).show()

                }

            }
            else{
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_LONG).show()
            }
        }
        binding.signRedirectText.setOnClickListener{
            intent = Intent(this, SignupVolunteer::class.java)
            startActivity(intent)
        }
        binding.forgetPass.setOnClickListener{
            intent = Intent(this, ForgetPassVol::class.java)
            startActivity(intent)
        }
    }
    private fun VideoCallServices(userID: String) {
        val appID: Long = 2064059270 // your App ID of Zoge Cloud
        val appSign = "8b63cf644f9991ea897cfc113aece1b1943ad771c20f3f1aa0a340e24732bb29" // your App Sign of Zoge Cloud
        val application = application // Android's application context
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
            provider = object : ZegoUIKitPrebuiltCallConfigProvider {
                override fun requireConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
                    val config: ZegoUIKitPrebuiltCallConfig = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
                    // The minimize
                    config.topMenuBarConfig.isVisible = true
                    config.topMenuBarConfig.buttons.add(ZegoMenuBarButtonName.MINIMIZING_BUTTON)
                    config.miniVideoConfig.miniVideoDrawableColor = Color.RED
                    config.miniVideoConfig.miniVideoTextColor = Color.RED
                    config.miniVideoConfig.permissionText = "Request Permission"

                    // the Custom Button
                    config.bottomMenuBarConfig.buttons.add(ZegoMenuBarButtonName.CHAT_BUTTON)
                    config.audioVideoViewConfig.videoViewForegroundViewProvider = object :
                        ZegoForegroundViewProvider {
                        override fun getForegroundView(parent: ViewGroup, uiKitUser: ZegoUIKitUser): ZegoBaseAudioVideoForegroundView {
                            return VolCustomView(parent.context, uiKitUser.userID)
                        }
                    }


                    return config
                }
            }
        }

        val notificationConfig = ZegoNotificationConfig()
        notificationConfig.sound = "zego_uikit_sound_call"
        notificationConfig.channelID = "CallInvitation"
        notificationConfig.channelName = "CallInvitation"

        ZegoUIKitPrebuiltCallInvitationService.init(
            application,
            appID,
            appSign,
            userID,
            userID,
            callInvitationConfig
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallInvitationService.unInit()
    }
}

class VolCustomView(context: Context, userID: String) : ZegoAudioVideoForegroundView(context, userID) {
    private var containerView = RelativeLayout(context)
    private var chatContainer = RelativeLayout(context)
    private var messageEditText = EditText(context)
    private val messageContainer = LinearLayout(context)  // New LinearLayout to contain messages
    private var sendButton = Button(context)
    private var chatMessagesTextView = TextView(context)
    private val scrollView = ScrollView(context)
    private val database = FirebaseDatabase.getInstance()
    private val messagesRef = database.getReference("messages")

    init {
        initializeContainerView()
        initializeChatContainer()
        initializeChatMessagesView()
        initializeSendButton()
        initializeMessageInput()
        addViewsToContainer()
    }

    private fun initializeContainerView() {
        containerView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun initializeChatContainer() {
        chatContainer.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            600
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            setMargins(16, 8, 16, 16)
        }
        chatContainer.setPadding(16, 8, 16, 16)
        chatContainer.setBackgroundColor(Color.WHITE)

        scrollView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scrollView.isSmoothScrollingEnabled = true
        scrollView.isVerticalScrollBarEnabled = true

        messageContainer.orientation = LinearLayout.VERTICAL
        messageContainer.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        scrollView.addView(messageContainer)
        chatContainer.addView(scrollView)
    }

    private fun initializeChatMessagesView() {
        chatMessagesTextView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ABOVE, messageEditText.id)
            setMargins(0, 0, 0, 10) // Setting the bottom margin to 10dp
        }
        chatMessagesTextView.setPadding(16, 16, 16, 16)
        messageContainer.addView(chatMessagesTextView)
    }

    private fun initializeSendButton() {
        sendButton.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(context, R.drawable.baseline_send),
            null, null, null
        )
        sendButton.setPadding(16, 16, 16, 16)
        sendButton.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_END)
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            setMargins(8, 8, 8, 8)
        }
        sendButton.setOnClickListener { sendMessage(messageEditText.text.toString()) }
        chatContainer.addView(sendButton)
    }

    private fun initializeMessageInput() {
        messageEditText.hint = "Enter your message"
        messageEditText.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.END_OF, sendButton.id)
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            setMargins(8, 8, 8, 8)
        }
        messageEditText.setPadding(16, 16, 16, 16)
        messageEditText.textAlignment = View.TEXT_ALIGNMENT_VIEW_START // Ensures proper alignment for Arabic text
        messageEditText.typeface = Typeface.DEFAULT // Set default typeface that supports Arabic
        messageEditText.gravity = Gravity.START // Ensures text starts from the right for Arabic
        chatContainer.addView(messageEditText)
    }

    private fun addViewsToContainer() {
        containerView.addView(chatContainer)
        addView(containerView)
    }

    private fun sendMessage(text: String) {
        if (text.isNotEmpty()) {
            messagesRef.child(userID).setValue(text) // Send message to Firebase
            messageEditText.text.clear()
            updateChatMessagesView(text)
            val client = OkHttpClient()
            val request = Request.Builder().url("http://192.168.1.53:8000/").build()
        }
    }

    private fun updateChatMessagesView(message: String) {
        chatMessagesTextView.append("\n$message")
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

}
