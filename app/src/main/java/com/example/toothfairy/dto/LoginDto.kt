package com.example.toothfairy.dto

class LoginDto {
    var id: String? = null
    var password: String? = null

    constructor(id: String?, password: String?) {
        this.id = id
        this.password = password
    }

    constructor() {}
}