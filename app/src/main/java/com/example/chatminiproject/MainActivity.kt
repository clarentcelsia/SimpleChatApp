package com.example.chatminiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatminiproject.Adapter.UserAdapter
import com.example.chatminiproject.Model.User
import com.example.chatminiproject.ViewModel.UIViewModel
import com.example.chatminiproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myModel: UIViewModel

    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myModel = ViewModelProvider(this).get(UIViewModel::class.java)
        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.apply {
            elevation = 0F
            title = "Message"
            setDisplayHomeAsUpEnabled(false)
        }

        userAdapter = UserAdapter()
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        userAdapter.setOnItemCallback { user->
            startActivity(Intent(this, ChatPageActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("id", user.id)
                .putExtra("profile", user.profile)
                .putExtra("username", user.username))
        }

        myModel.getUser().observe(this,
            { t -> userAdapter.userDiffer.submitList(t) })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                true
            }
            R.id.exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
