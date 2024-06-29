package com.example.enablio

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.VideoView
import androidx.lifecycle.Observer
import com.example.enablio.databinding.ActivityHomeDisBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream
import kotlin.random.Random

class DisCustomView(context: Context, userID: String) : ZegoAudioVideoForegroundView(context,userID) {
    private var containerView = RelativeLayout(context)
    private var chatContainer = RelativeLayout(context)
    private val messageContainer = LinearLayout(context)  // New LinearLayout to contain messages
    private val scrollView = ScrollView(context)
    private val database = FirebaseDatabase.getInstance()
    private val messagesRef = database.getReference("messages")

    init {
            initializeContainerView()
            initializeChatContainer()
            addViewsToContainer()
            listenForMessages()

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


    private fun addViewsToContainer() {
        containerView.addView(chatContainer)
        addView(containerView)
    }
    private fun listenForMessages() {
        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val userKey = snapshot.key.toString()
                if(userKey==userID) {
                    val message = snapshot.getValue().toString()
                    if (message != "") {
                        find_match(message)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val userKey = snapshot.key.toString()
                if(userKey==userID) {
                    val message = snapshot.getValue().toString()
                    if (message != "") {
                        find_match(message)
                    }
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

    }


    private fun addImageToContainer(bitmap: Bitmap) {
        val imageView = ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(400, 400)
            //setImageResource(R.drawable.moon)
        }
        imageView.setImageBitmap(bitmap)
        /*
        val videoView = VideoView(context).apply{
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                400
            )
            val storageRef = FirebaseStorage.getInstance().reference.child("/videos/test.mp4")
            val f = File.createTempFile("test", ".mp4")


            setOnPreparedListener { mediaPlayer -> mediaPlayer.isLooping = true }

        }
         */
        messageContainer.addView(imageView)
        //videoView.start()

        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }
    private fun find_match(text:String){
        val query = QueryModel(text)
        RetrofitClient.instance.findMatch(query).enqueue(object : Callback<List<MatchResult>> {
            override fun onResponse(call: Call<List<MatchResult>>, response: Response<List<MatchResult>>) {
                if (response.isSuccessful) {
                    val results = response.body()
                    results?.let {
                        // Handle the results
                        Log.d("Result", it.joinToString("\n") { result ->
                            "Word: ${result.word}, Path: ${result.path}, Label: ${result.label}, Type: ${result.type}" })

                        download_file(it)
                    }
                } else {
                    Log.d("Result", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<MatchResult>>, t: Throwable) {
                // Handle the error
                Log.d("Result",  "Error: ${t.message}")
            }
        })

    }
    private fun download_file(list: List<MatchResult>){
        list.forEach {
            RetrofitClient.instance.downloadFile(it.path).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val inputStream: InputStream = responseBody.byteStream()
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            addImageToContainer(bitmap)
                            Log.d("paaaaaath", it.path)
                        }
                    } else {
                        Log.d("DownloadFile", "Error: ${response.code()}, ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("DownloadFile", "Error: ${t.message}")
                }
            })

        }


    }


}


class HomeDis : AppCompatActivity() {
    private lateinit var binding:ActivityHomeDisBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var callBtn: ZegoSendCallInvitationButton
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Home")
        auth=FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Volunteer")
        callBtn = binding.callBtn
        retrieveOnlineUsers()

    }

    private fun retrieveOnlineUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val onlineUsers = mutableListOf<String>()
                snapshot.children.forEach {
                    if (it.child("isOnline").value == true)
                        onlineUsers.add(it.key.toString())
                }

                if (onlineUsers.isNotEmpty()) {
                    val randUser = onlineUsers[Random.nextInt(0,onlineUsers.size)]
                    Log.d("user", randUser)
                    VideoCallServices(auth.currentUser?.uid.toString(),randUser)
                    startVideoCall(randUser)


                } else {
                    // Handle case where there are no online users
                    println("No online users available")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun startVideoCall(receiverId: String) {
        callBtn.setIsVideoCall(true)
        callBtn.resourceID = "zego_uikit_call"
        callBtn.setInvitees(listOf(ZegoUIKitUser(receiverId)))

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
                val intent = Intent(this, MainActivity::class.java)
                auth.signOut()
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun VideoCallServices(userID: String, invitee:String) {
        val appID: Long = 2064059270 // your App ID of Zoge Cloud
        val appSign = "8b63cf644f9991ea897cfc113aece1b1943ad771c20f3f1aa0a340e24732bb29" // your App Sign of Zoge Cloud
        val application = application // Android's application context
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        callInvitationConfig.apply {
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
                            return DisCustomView(parent.context, uiKitUser.userID)
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