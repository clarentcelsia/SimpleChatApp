package com.example.chatminiproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatminiproject.Model.Chat
import com.example.chatminiproject.R
import com.example.chatminiproject.Util.Util
import de.hdodenhof.circleimageview.CircleImageView

private const val SENDER = 0
private const val RECEIVER = 1
class ChatAdapter(val imageUrl: String): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Chat>(){
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.equals(newItem)
        }

    }

    val chatDiffer = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val senderView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_sender, parent, false)
        val receiverView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_receiver, parent, false)
        return when(viewType){
            0 -> ChatViewHolder(senderView)
            else -> ChatViewHolder(receiverView)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatIDX = chatDiffer.currentList[position]
        val profile = holder.itemView.findViewById<CircleImageView>(R.id.rProfile)
        val message = holder.itemView.findViewById<TextView>(R.id.tvMessage)
        val read = holder.itemView.findViewById<TextView>(R.id.tvHasRead)
        val time = holder.itemView.findViewById<TextView>(R.id.tvTime)

        val util = Util()
        holder.itemView.apply{
            util.setImageResource(this, imageUrl, profile)
            message.text = chatIDX.message
            if(position == chatDiffer.currentList.size - 1) {
                read.visibility = if (chatIDX.isseen) View.VISIBLE
                else
                    View.GONE
            }
            time.text = convertLongToTime(chatIDX.time)
        }
    }

    override fun getItemCount(): Int = chatDiffer.currentList.size

    private fun convertLongToTime(time: Long): String{
        return Util().convertLongToTime(time, pattern = "HH:mm")
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatDiffer.currentList[position].sender == "Anonymous")
            SENDER
        else
            RECEIVER
    }
}
