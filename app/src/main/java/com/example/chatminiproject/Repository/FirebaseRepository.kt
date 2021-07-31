package com.example.chatminiproject.Repository

import com.example.chatminiproject.Repository.Mapper.FirebaseMapper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

open class FirebaseRepository<Model, Entity>(
    val mapper: FirebaseMapper<Entity, Model>
){

    private var listener: BaseValueEventListener<Model, Entity>? = null
    private lateinit var reference : DatabaseReference
    open val root: String = ""

    fun addValueEventListener(callback: FirebaseRepositoryCallback<Model>){
        reference  = FirebaseDatabase.getInstance().getReference(root)
        listener = BaseValueEventListener(mapper, callback)
        reference.addValueEventListener(listener!!)
    }

    fun removeEventListener(){
        if(listener != null) {
            reference.removeEventListener(listener!!)
        }
    }

    interface FirebaseRepositoryCallback<T>{
        fun success(data: List<T>)
        fun error(e: Exception)
    }
}