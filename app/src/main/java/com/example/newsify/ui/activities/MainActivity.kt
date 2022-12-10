package com.example.newsify.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsify.*
import com.example.newsify.constant.Constant
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
        setContentView(view)
        setUpUi()
        initListner()
        getNewsDeatils( "global" , Constant.API_KEY)
        viewModel.getAllUser()
        viewModel.lastUser.observe(this){
            if (it!=null){
                name=it.name
                password=it.password
                mobile=it.mobile_number
                gender=it.gender
            }else{
                Log.d("error","Error")
            }
        }

    }
    private fun initListner(){

        binding.btnNext.setOnClickListener {
            val query =binding.eTQuery.text.toString()
            getNewsDeatils( query , Constant.API_KEY)

        }
        binding.btnLogout.setOnClickListener {
            setLogInFalse()
        }
    }
    fun setLogInFalse(){
        viewModel.setLogoutForUser(User(name = name, mobile_number = mobile, password = password, isLogin = false))
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
   fun getNewsDeatils(query:String,apikey:String){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val services: NewApiInterface =retrofit.create(NewApiInterface::class.java)
        val listCall: Call<NewsResponseDataClass> = services.getNews(query=query,apikey=apikey)
        showCustomProgressDialog()
        listCall.enqueue(object : Callback<NewsResponseDataClass> {
            override fun onResponse(
                call: Call<NewsResponseDataClass>,
                response: Response<NewsResponseDataClass>
            ) {
                if (response.isSuccessful){
                    val newsList =response.body()
                    hideProgressDialog()
                    if (newsList != null) {
                        newsListAdapter.submitList(newsList.articles,this@MainActivity)
                    }
                  newsListAdapter.notifyDataSetChanged()
                    Log.d("WeatherList","$newsList")
                }else{
                    val rc=response.code()
                    when(rc){
                        400->{
                            Log.d("Error 400","Bad Connecton")
                        }
                        404->{
                            Log.d("Error 404","Data not found")
                        }
                        else->{
                            Log.d("Error Generic","generic error")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewsResponseDataClass>, t: Throwable) {
                hideProgressDialog()
                Log.e("error", t.message.toString())

            }

        })
    }
    private fun setUpUi(){
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