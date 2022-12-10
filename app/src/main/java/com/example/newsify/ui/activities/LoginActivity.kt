package com.example.newsify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.newsify.viewmodels.MainViewModel
import com.example.newsify.data.User
import com.example.newsify.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
  val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)


      binding.btnLogin.setOnClickListener {
         val number=binding.eTMobileNumber.text.toString()
          val password=binding.eTPassword.text.toString()
          val user = User( mobile_number = number, password = password)
          viewModel.checkValidUser(user)

      }
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewModel.isValidUserExists.observe(this){
            if (it){
                binding.eTMobileNumber.text.clear()
                binding.eTPassword.text.clear()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
              Toast.makeText(this,"Mobile number is not registered",Toast.LENGTH_SHORT).show()
            }
        }
    }
}