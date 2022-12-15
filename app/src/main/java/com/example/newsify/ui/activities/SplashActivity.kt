package com.example.newsify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.example.newsify.viewmodels.MainViewModel
import com.example.newsify.R
import com.example.newsify.constant.Constant.USER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.getAllUser()
        viewModel.lastUser.observe(this){ users ->
            val user = users.find { it.isLogin }
            if (user != null){
                startActivity(Intent(this, MainActivity::class.java).apply { putExtra(USER,user) })
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }
}