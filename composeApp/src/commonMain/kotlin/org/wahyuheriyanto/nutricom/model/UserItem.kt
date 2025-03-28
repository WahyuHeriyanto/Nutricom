package org.wahyuheriyanto.nutricom.model

data class UserItem(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val gender:String = "",
    val dateOfBirth: String = "",
    val point: Int = 0

)

data class LoginItem(
    val email: String = "",
    val password: String = ""
)