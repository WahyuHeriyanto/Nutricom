package org.wahyuheriyanto.nutricom.model

data class UserItem(
    val email: String = "",
    val fullName: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val password: String = ""
)