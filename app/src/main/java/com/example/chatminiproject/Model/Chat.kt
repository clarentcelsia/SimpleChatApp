package com.example.chatminiproject.Model

data class Chat(
    val sender: String,
    val receiver: String,
    val message: String,
    val isseen: Boolean,
    val time: Long
)
