package com.example.chatminiproject.Adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatminiproject.Model.Chat
import com.example.chatminiproject.Model.User
import com.example.chatminiproject.R
import com.example.chatminiproject.Repository.ChatRepository
import com.example.chatminiproject.Repository.FirebaseRepository
import com.example.chatminiproject.Repository.Root
import com.example.chatminiproject.Util.Util
import com.example.chatminiproject.ViewModel.UIViewModel
import com.example.chatminiproject.databinding.UserItemBinding
import com.google.firebase.database.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.Format
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(val context: LifecycleOwner): RecyclerView.Adapter<UserAdapter.UserViewHolder>()  {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = UserItemBinding.bind(itemView)
    }

    private val differCallback = object: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    val userDiffer = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val userIndex = userDiffer.currentList[position]

        with(holder){
            Util().setImageResource(this.itemView, userIndex.profile, binding.itemProfile)
            binding.itemUsername.text = userIndex.username
            bindMessageView(userIndex.id, binding.itemMessage)
            binding.root.setOnClickListener {
                onItemCallback?.let {
                    it(userIndex)
                }
            }
        }

    }

    override fun getItemCount(): Int = userDiffer.currentList.size

    private var onItemCallback: ((User)->Unit)? = null
    fun setOnItemCallback(listener: (User)->Unit){
        onItemCallback = listener
    }

    private fun checkLastSentMessage(data: ArrayList<Chat>, binding: TextView){
        CoroutineScope(Dispatchers.IO).launch {
            val time = ArrayList<Long>()
            for (long in data) {
                time.add(long.time)
            }
            val util = Util()
            val result = util.compareTime(time)

            var string = ""
            for (message in data) {
                if (message.time == result) {
                    string = message.message
                }
            }

            withContext(Dispatchers.Main){
                if (string != null) {
                    binding.visibility = View.VISIBLE
                    binding.text = string
                }
            }
        }
    }

    private fun bindMessageView(receiver: String, tv: TextView){
        val user = ArrayList<Chat>()
        ChatRepository().addValueEventListener(object : FirebaseRepository.FirebaseRepositoryCallback<Chat>{
            override fun success(data: List<Chat>) {
                    for (to in data) {
                        if (to.receiver == receiver) {
                            user.add(to)
                        }
                    }
                    checkLastSentMessage(user, tv)
            }

            override fun error(e: Exception) {
                TODO("Not yet implemented")
            }

        })

    }

}