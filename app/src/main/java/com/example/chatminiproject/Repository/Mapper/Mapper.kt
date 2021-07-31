package com.example.chatminiproject.Repository.Mapper

import com.example.chatminiproject.Entity.ChatEntity
import com.example.chatminiproject.Entity.UserEntity
import com.example.chatminiproject.Model.Chat
import com.example.chatminiproject.Model.User

open class Mapper<Entity, Model>: FirebaseMapper<Entity, Model>(){
    override fun map(from: Entity): Model {
        return map(from)
    }

}
class UserMapper : Mapper<UserEntity, User>() {
    override fun map(from: UserEntity): User {
        val user = User(from.id, from.username, from.profile)
        return user
    }
}

class ChatMapper: Mapper<ChatEntity, Chat>(){
    override fun map(from: ChatEntity): Chat {
        val message = Chat(from.sender, from.receiver, from.message, from.isseen, from.time)
        return message
    }
}
