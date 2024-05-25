package com.marveldatahub

import kotlin.test.Test
import kotlin.test.assertEquals

class Md5Test {
    @Test
    fun `should make md5 not fucked up`() {
        val timestamp = 1716645024165
        val result = md5(timestamp)
        assertEquals("31cdd379edf0d86041a5e7ef82e83395", result)
    }
}
