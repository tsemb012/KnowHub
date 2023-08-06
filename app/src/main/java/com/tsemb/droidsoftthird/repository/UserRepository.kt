package com.tsemb.droidsoftthird.repository

import androidx.compose.runtime.Immutable
import javax.inject.Inject


sealed class TestUser {
    @Immutable
    data class LoggedInUser(val email: String) : TestUser()
    object GuestUser : TestUser()
    object NoUserLoggedIn : TestUser()
}

//TODO 実験的に追加するRepository　RailsApiRepositoryと統合する。
class UserRepository @Inject constructor() {

    private var _user: TestUser = TestUser.NoUserLoggedIn
    val user: TestUser
        get() = _user

    @Suppress("UNUSED_PARAMETER")
    fun signIn(email: String, password: String) {
        _user = TestUser.LoggedInUser(email)
    }

    @Suppress("UNUSED_PARAMETER")
    fun signUp(email: String, password: String) {
        _user = TestUser.LoggedInUser(email)
    }

    fun signInAsGuest() {
        _user = TestUser.GuestUser
    }

    fun isKnownUserEmail(email: String): Boolean {
        // if the email contains "sign up" we consider it unknown
        return !email.contains("signup")
    }
}