package com.example.chatminiproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.chatminiproject.Util.Constant.Companion.sender
import com.example.chatminiproject.Util.Util
import com.example.chatminiproject.ViewModel.UIViewModel
import com.example.chatminiproject.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var myModel: UIViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myModel = ViewModelProvider(this).get(UIViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.apply {
            elevation = 0F
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        fetchUser()
        binding.tvUsername.text = sender
        binding.ivProfile.setOnClickListener {
            openGallery()
        }
    }

    private fun fetchUser(){
        myModel.getUser().observe(this, {
            for(user in it){
                if(user.id == sender)
                    Util().setImageResource(this.binding.root, user.profile, binding.ivProfile)
            }
        })
    }

    private fun openGallery(){
        val intent = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val data = it.data?.data!!
            myModel.insertPicture(this, data)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}