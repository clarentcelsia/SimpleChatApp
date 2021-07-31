package com.example.chatminiproject.Repository

import com.example.chatminiproject.Model.Chat
import com.example.chatminiproject.Repository.Mapper.FirebaseMapper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BaseValueEventListener<Model, Entity>(
    val mapper: FirebaseMapper<Entity, Model>,
    val firebaseRepositoryCallback: FirebaseRepository.FirebaseRepositoryCallback<Model>
) : ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        val data = mapper.mapList(snapshot)
        firebaseRepositoryCallback.success(data)
    }

    override fun onCancelled(error: DatabaseError) {
        firebaseRepositoryCallback.error(error.toException())
    }

}