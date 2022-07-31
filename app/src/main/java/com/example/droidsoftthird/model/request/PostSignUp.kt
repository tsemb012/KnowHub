package com.example.droidsoftthird.model.request

import com.example.droidsoftthird.model.json.SignUpJson

abstract class PostSignUp {
    data class Request(
        val signUp: SignUpJson
    )
}
