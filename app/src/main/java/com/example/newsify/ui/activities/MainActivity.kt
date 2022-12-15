package com.example.newsify.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsify.*
import com.example.newsify.constant.Constant
import com.example.newsify.constant.Constant.USER
import com.example.newsify.data.User
import com.example.newsify.databinding.ActivityMainBinding
import com.example.newsify.network.NewApiInterface
import com.example.newsify.ui.adapter.NewsListAdapter
import com.example.newsify.ui.data.NewsResponseDataClass
import com.example.newsify.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var user: User? = null
    val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    var mCustomProgressDialog:Dialog? = null
    var name=""
    var password=""
    var mobile=""
    var gender=""
    private lateinit var newsListAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(USER,User::class.java)
        } else {
            intent.getParcelableExtra(USER)
        }
        setContentView(view)
        setUpUi()
        initListner()
        initObserver()
        showCustomProgressDialog()
        user?.let { viewModel.setLogInForUser(it) }
        viewModel.getNewsDeatils("global")

    }

    private fun initObserver() {
        viewModel.showToast.observe(this) {
            hideProgressDialog()
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

        viewModel.response.observe(this){
            hideProgressDialog()
            if (it != null) {
                newsListAdapter.submitList(it.articles,this@MainActivity)
            }
            newsListAdapter.notifyDataSetChanged()
        }
    }

    private fun initListner(){

        binding.btnNext.setOnClickListener {
            val query =binding.eTQuery.text.toString()
            showCustomProgressDialog()
            viewModel.getNewsDeatils(query)
        }
        binding.btnLogout.setOnClickListener {
            setLogInFalse()
        }
    }
    fun setLogInFalse(){
        user?.let { viewModel.setLogoutForUser(it) }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun setUpUi(){
        binding.eTQuery.requestFocus()
        val recyclerView = binding.recyclerviewNews
                newsListAdapter = NewsListAdapter()
        recyclerView.adapter = newsListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }
    private fun showCustomProgressDialog(){
        mCustomProgressDialog= Dialog(this)
        mCustomProgressDialog!!.setContentView(R.layout.custom_dialog_progress)
        mCustomProgressDialog!!.show()

    }
    fun hideProgressDialog(){
        if(mCustomProgressDialog!=null){
            mCustomProgressDialog!!.dismiss()
        }
    }
}