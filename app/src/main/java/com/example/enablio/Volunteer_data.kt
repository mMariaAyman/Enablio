package com.example.enablio

class Volunteer_data {
    var id: Int = 0
    var name:String =""
    var email: String =""
    var password: String=""
    var signLanguage: Boolean = false
    constructor(id: Int, name:String, email:String, password:String, signLanguage: Boolean){
        this.id = id
        this.name = name
        this.email = email
        this.password = password
        this.signLanguage = signLanguage
    }
}