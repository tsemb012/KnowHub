package com.tsemb.droidsoftthird.model.infra_model.json

import com.squareup.moshi.Json

data class SignUpJson ( //TODO ダイジェスト認証の正しいやり方を確認する。
        val name: String,
        val email: String,
        val password: String,
        @Json(name = "password_confirmation")
        val passwordConfirmation: String,
)
