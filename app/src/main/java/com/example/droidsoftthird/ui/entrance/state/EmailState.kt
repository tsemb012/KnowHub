package com.example.droidsoftthird.ui.entrance.state

import java.util.regex.Pattern

//TODO API側のValidationと齟齬がないか確認する。
private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState : TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidationError)

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun emailValidationError(email: String): String {
    return "無効なメールアドレス: $email"
}

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}

val EmailStateSaver = textFieldStateSaver(EmailState())
