package com.example.chatminiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatminiproject.Adapter.ChatAdapter
import com.example.chatminiproject.Util.Util
import com.example.chatminiproject.ViewModel.UIViewModel
import com.example.chatminiproject.databinding.ActivityChatPageBinding

class ChatPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatPageBinding
    private lateinit var myModel: UIViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myModel = ViewModelProvider(this).get(UIViewModel::class.java)

        setSupportActionBar(binding.customToolbar.myToolbar)
        supportActionBar!!.apply {
            elevation = 0F
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        val util = Util()
        val id = intent.getStringExtra("id")!!
        val profile = intent.getStringExtra("profile")!!
        val receiver = intent.getStringExtra("username")!!
        util.setImageResource(this.binding.root, profile, binding.customToolbar.ivProfileBar)
        binding.customToolbar.tvUsernameBar.text = receiver

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            myModel.insertMessage(id, message)
            binding.etMessage.text.clear()
        }

        val chatAdapter = ChatAdapter(profile)
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(this@ChatPageActivity).also {
                it.stackFromEnd = true
            }
            setHasFixedSize(true)
            adapter = chatAdapter
        }

        myModel.getMessage(id).observe(this,
            { t -> chatAdapter.chatDiffer.submitList(t) })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
