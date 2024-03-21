package com.example.enablio

import android.media.Image
import android.provider.ContactsContract

class Volunteer_data {
    var name:String =""
    var email: String =""
    var password: String=""
    var signLanguage: String = ""
    var gender: String = ""
    var photo = ""

    constructor(name:String, email:String, password:String, signLanguage: String, gender:String, photo:String){
        this.name = name
        this.email = email
        this.password = password
        this.signLanguage = signLanguage
        this.gender = gender
        this.photo = photo
    }
}