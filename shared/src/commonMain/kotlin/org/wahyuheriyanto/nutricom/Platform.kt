package org.wahyuheriyanto.nutricom

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform