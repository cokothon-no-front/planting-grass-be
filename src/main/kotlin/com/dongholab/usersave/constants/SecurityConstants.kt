package com.dongholab.usersave.constants

object SecurityConstants {
    fun localhost(port: Int, protocol: String = "http") = "${protocol}://localhost:$port"
}