package com.example.enablio

class Volunteer_data {
    var name:String =""
    var email: String =""
    var password: String=""
    var signLanguage: Boolean = false
    constructor(name:String, email:String, password:String, signLanguage: Boolean){
        this.name = name
        this.email = email
        this.password = password
        this.signLanguage = signLanguage
    }
}