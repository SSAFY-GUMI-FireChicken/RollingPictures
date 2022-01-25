package com.firechicken.rollingpictures.dto

data class User(
    var uid: String?,
    var nickName: String?,
    val mute: Boolean,
    val state: Boolean,
    val token: String?
) {
    constructor() : this("", "", false, false, "")
    constructor(nickName: String?) : this("", nickName, false, false, "")
}

