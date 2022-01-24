package com.firechicken.rollingpictures.draw

import android.graphics.Path
import java.io.Writer

/**
 * SPDX-FileCopyrightText: © 2018 Divyanshu Bhargava <https://github.com/divyanshub024/AndroidDraw>
 * SPDX-License-Identifier: Apache-2.0 License
 */

class Move(val x: Float, val y: Float) : Action {

    override fun perform(path: Path) {
        path.moveTo(x, y)
    }

    override fun perform(writer: Writer) {
        writer.write("M$x,$y")
    }
}