package com.firechicken.rollingpictures.dto

data class User(
    var uid: String?,
    var nickname: String?,
    val mute: Boolean,
    val state: Boolean,
    val token: String?
) {
    constructor() : this("", "", false, false, "")
}

