package com.tsemb.droidsoftthird.model.infra_model.json.request

import com.tsemb.droidsoftthird.model.infra_model.json.SignUpJson

abstract class PostSignUpJson {
    data class Request(
        val signUp: SignUpJson
    )
}
