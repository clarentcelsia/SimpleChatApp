package com.example.chatminiproject.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatminiproject.Model.Chat
import com.example.chatminiproject.Model.User
import com.example.chatminiproject.Repository.*
import com.example.chatminiproject.Util.Constant.Companion.sender
import kotlinx.coroutines.launch
import java.lang.Exception

class UIViewModel : ViewModel() {

    private var _user = MutableLiveData<List<User>>()
    private var _message = MutableLiveData<List<Chat>>()

    fun getUser(): LiveData<List<User>> {
        loadUser()
        return _user
    }

    private fun loadUser() = viewModelScope.launch {
        UserRepository().addValueEventListener(object :
            FirebaseRepository.FirebaseRepositoryCallback<User> {
            override fun success(data: List<User>) {
                _user.value = data
            }

            override fun error(e: Exception) {
                _user.value = null
            }
        })
    }

    fun getMessage(receiver: String): LiveData<List<Chat>> {
        loadMessage(receiver)
        return _message
    }

    private fun loadMessage(receiver: String) = viewModelScope.launch {
        ChatRepository().addValueEventListener(object :
            FirebaseRepository.FirebaseRepositoryCallback<Chat> {
            override fun success(data: List<Chat>) {
                val list = ArrayList<Chat>()
                for (chats in data) {
                    if (chats.sender == sender && chats.receiver == receiver ||
                        chats.sender == receiver && chats.receiver == sender) {
                            list.add(chats)
                    }
                }
                _message.value = list
            }

            override fun error(e: Exception) {
                _message.value = null
            }

        })
    }

    fun insertMessage(receiver: String, message: String) = viewModelScope.launch {
        ChatRepository().insertMessage(receiver, message)
    }

    fun insertPicture(context: Context, imageUrl: Uri) = viewModelScope.launch {
        StorageRepository().uploadImage(imageUrl, context)

    }


}