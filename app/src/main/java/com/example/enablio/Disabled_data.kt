package com.example.enablio

import android.media.Image
import android.provider.ContactsContract

class Disabled_data {
    var name:String =""
    var email: String =""
    var password: String=""
    var disability: String = ""
    var gender: String = ""
    var photo = ""
    var numOfCalls = 0

    constructor(name:String, email:String, password:String, disability: String, gender:String, photo:String, numOfCalls:Int){
        this.name = name
        this.email = email
        this.password = password
        this.disability = disability
        this.gender = gender
        this.photo = photo
        this.numOfCalls = numOfCalls
    }
}