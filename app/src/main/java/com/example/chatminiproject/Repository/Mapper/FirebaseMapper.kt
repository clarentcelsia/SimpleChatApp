package com.example.chatminiproject.Repository.Mapper

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.reflect.ParameterizedType


abstract class FirebaseMapper<Entity, Model> : IMapper<Entity, Model> {

    fun map(dataSnapshot: DataSnapshot): Model{
        val entity: Entity = dataSnapshot.getValue(getEntityClass())!!
        return map(entity)
    }

    fun mapList(dataSnapshot: DataSnapshot): List<Model>{
        val list = ArrayList<Model>()
        for (snap in dataSnapshot.children){
            list.add(map(snap))
        }
        Log.d("TAG", "mapList: ${list.size.toString()}")
        return list
    }

    @SuppressWarnings("UncheckedCast")
    private fun getEntityClass(): Class<Entity> {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        return superclass.actualTypeArguments[0] as Class<Entity>
    }
}