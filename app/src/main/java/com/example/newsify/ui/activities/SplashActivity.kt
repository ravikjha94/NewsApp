package com.example.newsify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.example.newsify.viewmodels.MainViewModel
import com.example.newsify.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            viewModel.getAllUser()
            viewModel.isLoggedIn.observe(this){
                if (it){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()

                }
            }

        }, 2500)


    }
}