package com.example.droidsoftthird.model.infra_model.json.request

import com.example.droidsoftthird.model.infra_model.json.SignUpJson

abstract class PostSignUp {
    data class Request(
        val signUp: SignUpJson
    )
}
