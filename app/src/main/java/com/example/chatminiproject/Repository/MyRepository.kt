package com.example.chatminiproject.Repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.chatminiproject.Entity.ChatEntity
import com.example.chatminiproject.Entity.UserEntity
import com.example.chatminiproject.Model.Chat
import com.example.chatminiproject.Model.User
import com.example.chatminiproject.Repository.Mapper.ChatMapper
import com.example.chatminiproject.Repository.Mapper.FirebaseMapper
import com.example.chatminiproject.Repository.Mapper.UserMapper
import com.example.chatminiproject.Util.Constant.Companion.sender
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

open class MyRepository<Model, Entity>(
    mapper: FirebaseMapper<Entity, Model>
): FirebaseRepository<Model, Entity>(mapper){

    val myRoot = Root()

    override val root: String
        get() = super.root
}

open class UserRepository : MyRepository<User, UserEntity>(UserMapper()){
    override val root: String
        get() = myRoot.userRef

    fun updateProfile(imageUri: String) {
        val reference = FirebaseDatabase.getInstance().getReference(root)
        val map = mutableMapOf<String, Any>()
        map["profile"] = imageUri
        reference.child(sender).updateChildren(map)
    }
}

class ChatRepository : MyRepository<Chat, ChatEntity>(ChatMapper()) {
    override val root: String
        get() = myRoot.chatRef

    fun insertMessage(receiver: String, message: String) = CoroutineScope(Dispatchers.IO).launch {
        val reference = FirebaseDatabase.getInstance().getReference(root)
        val map = mutableMapOf<String, Any>()
        map["sender"] = sender
        map["receiver"] = receiver
        map["message"] = message
        map["isseen"] = false
        map["time"] = getTime()
        reference.push().setValue(map)
    }

    fun getTime(): Long {
        val time = GregorianCalendar().timeInMillis
        return time
    }
}

class StorageRepository: UserRepository(){

    val storage = FirebaseStorage.getInstance().getReference(sender)

    suspend fun uploadImage(data: Uri, context: Context) {
        var uri = ""
        try {
            val imageStorageRef = storage.child(
                    "${System.currentTimeMillis()}.${getFileExtension(data, context)}")
            imageStorageRef.putFile(data).await()
            imageStorageRef.downloadUrl.addOnCompleteListener {
                uri = it.result.toString()
                updateProfile(uri)
            }

        } catch (e: Exception) {
            Log.e("TAG", "updateProfile: ${e.message}")
        }
    }

    fun getFileExtension(data: Uri, context: Context): String{
        val contentResolver = context.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(data))!!
    }

}

