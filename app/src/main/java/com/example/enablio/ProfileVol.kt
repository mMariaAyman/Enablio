package com.example.enablio

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.enablio.databinding.ActivityProfileBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class ProfileVol : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var rootFBRef: DatabaseReference
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("My Profile")
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        rootFBRef = FirebaseDatabase.getInstance().getReference("Volunteer")


        rootFBRef.child(auth.currentUser?.uid.toString()).child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                binding.editNameTxt.text.append(value.toString())
                binding.userName.text = value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
        rootFBRef.child(auth.currentUser?.uid.toString()).child("gender").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                binding.gender.text.append(value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
        binding.editEmailTxt.text.append(auth.currentUser?.email.toString())
        //View User Image
        val ref = storageReference.child("photos/${auth.currentUser?.uid.toString()}.png")
        ref.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(binding.userImage)
        }
        binding.saveProfile.setOnClickListener {
            val map = mapOf<String,String>(
                "name" to binding.editNameTxt.text.toString(),
                "gender" to binding.gender.text.toString(),

            )
            rootFBRef.child(auth.currentUser?.uid.toString()).updateChildren(map).addOnSuccessListener {
                Toast.makeText(this,"Updated Successfully!", Toast.LENGTH_SHORT).show()
                rootFBRef.child(auth.currentUser?.uid.toString()).child("name").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.value
                        binding.editNameTxt.text.clear()
                        binding.editNameTxt.text.append(value.toString())
                        binding.userName.text = value.toString()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
                rootFBRef.child(auth.currentUser?.uid.toString()).child("gender").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.value
                        binding.gender.text.clear()
                        binding.gender.text.append(value.toString())
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })

            }

        }
        binding.deleteProfile.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
                .setMessage("Are you sure to delete this Account?")
                .setPositiveButton("Delete") { dialog, _ ->
                    // Handle positive button click
                    dialog.dismiss()
                    rootFBRef.child(auth.currentUser?.uid.toString()).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this, "User deleted Successfully", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to delete User", Toast.LENGTH_SHORT).show()

                        }
                    auth.currentUser?.delete()
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "User deleted Successfully", Toast.LENGTH_SHORT).show()
                        }
                    val intent = Intent(this, LoginVol::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Handle negative button click
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()

        }

        binding.uploadPhoto.setOnClickListener {
            selectImage()
        }

    }
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            uploadImage(filePath)
        }
    }

    private fun uploadImage(filePath: Uri?) {
        if (filePath != null) {
            val ref = storageReference.child("photos/${auth.currentUser?.uid.toString()}.png")
            ref.putFile(filePath)
                .addOnSuccessListener {
                    val map = mapOf<String, String>(
                        "photo" to "https://storage.cloud.google.com/enablio2023.appspot.com" + ref.path
                    )
                    rootFBRef.child(auth.currentUser?.uid.toString()).updateChildren(map)
                    ref.downloadUrl.addOnSuccessListener {
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                        Picasso.get().load(it).into(binding.userImage)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}