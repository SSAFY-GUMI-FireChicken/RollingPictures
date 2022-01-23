package com.firechicken.rollingpictures.draw

import android.graphics.Path
import java.io.Serializable
import java.io.Writer

/**
 * SPDX-FileCopyrightText: © 2018 Divyanshu Bhargava <https://github.com/divyanshub024/AndroidDraw>
 * SPDX-License-Identifier: Apache-2.0 License
 */

interface Action : Serializable {
    fun perform(path: Path)

    fun perform(writer: Writer)
}
