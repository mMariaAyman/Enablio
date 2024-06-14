package com.example.enablio

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.accessibility.AccessibilityViewCommand.ScrollToPositionArguments
import com.example.enablio.databinding.ActivityProfileBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class ProfileDis : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var rootFBRef: DatabaseReference
    private lateinit var sDB: StorageReference
    private var uri:Uri?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("My Profile")
        auth = FirebaseAuth.getInstance()
        rootFBRef = FirebaseDatabase.getInstance().getReference("Disabled")
        sDB = FirebaseStorage.getInstance().reference.child("photos/")


        rootFBRef.child(auth.currentUser?.uid.toString()).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.value
                    binding.editNameTxt.text.append(value.toString())
                    binding.userName.text = value.toString()
                }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
        rootFBRef.child(auth.currentUser?.uid.toString()).child("gender").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                binding.gender.text.append(value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
        binding.editEmailTxt.text.append(auth.currentUser?.email.toString())

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
                    val intent = Intent(this, LoginDis::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Handle negative button click
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()

        }
        //Upload User Photo
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.userImage.setImageURI(it)
            uri = it

        }
        binding.uploadPhoto.setOnClickListener {
            pickImage.launch("image/*")

        }


        binding.saveProfile.setOnClickListener {
            var imageurl = ""
            if(uri != null){
                sDB.child(auth.currentUser?.uid.toString()+".jpg").putFile(uri!!)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {url->
                            imageurl = url!!.toString()
                        }

                    }
            }
            val map = mapOf<String,String>(
                "name" to binding.editNameTxt.text.toString(),
                "gender" to binding.gender.text.toString(),
                "photo" to "https://firebasestorage.googleapis.com/v0/b/enablio2023.appspot.com/o/photos%2FGVbKYdkLoddBXZp9Ii9THh8FdDi1.jpg?alt=media&token=e155a852-639f-4483-bb27-0873b8ef41be"
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
                rootFBRef.child(auth.currentUser?.uid.toString()).child("gender").addListenerForSingleValueEvent(object :
                    ValueEventListener {
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


    }
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                binding.userImage.setImageURI(uri)
                auth = FirebaseAuth.getInstance()
                val storageRef = FirebaseStorage.getInstance().getReference("images")
                val photoRef = storageRef.child("${auth.currentUser?.uid}.jpg")
                photoRef.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Photo uploaded successfully
                        // You can retrieve the download URL of the uploaded photo using taskSnapshot.metadata.downloadUrl
                        Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors that occurred during the upload process
                        Toast.makeText(this, "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }
                photoRef.downloadUrl.addOnSuccessListener {
                    binding.userImage.setImageURI(it)

                }

            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
*/
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
                val intent = Intent(this, LoginDis::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}


