package com.example.newsify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import com.example.newsify.constant.Constant.USER
import com.example.newsify.viewmodels.MainViewModel
import com.example.newsify.data.User
import com.example.newsify.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private var user: User? = null
    lateinit var binding: ActivityRegisterBinding
    val viewModel: MainViewModel by viewModels()
    private lateinit var gender: String
    private lateinit var name: String
    private lateinit var password: String
    private lateinit var number: String
    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val radioButtonGrp = binding.radiogrp

        viewModel.isUserExists.observe(this) {
            if (it){
                Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvName.text.clear()
                binding.tvMobileNumber.text.clear()
                binding.tvPassword.text.clear()
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(USER,user)
                }
                startActivity(intent)
                finish()
            }
        }
        binding.btnRegister.setOnClickListener {
            name = binding.tvName.text.toString()
            Log.d("nam", "$name")
            number = binding.tvMobileNumber.text.toString()
            password = binding.tvPassword.text.toString()
            val selected = radioButtonGrp.checkedRadioButtonId
            val radio = findViewById<RadioButton>(selected)
            if (radio != null) {
                gender = radio.text.toString()
            }
            user = User(name = name, mobile_number = number, password = password, gender = gender, isLogin = true)
            user?.let {
                viewModel.checkUser(it)
            }
        }
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}