package com.example.newsify.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsify.ui.data.Articles
import com.example.newsify.R

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>() {
   var list= listOf<Articles>()
    lateinit var context:Context
    class NewsListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        val imageView:ImageView
        val textView2:TextView
        init {
            textView = itemView.findViewById(R.id.tvReadContent)
            imageView=itemView.findViewById(R.id.ivImage)
            textView2=itemView.findViewById(R.id.tv_readfull)
        }
    }
    fun submitList(list:List<Articles>, context: Context){
       this.list=list
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_recycler_view_element, parent, false)

        return NewsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
           holder.textView.text=list[position].content
        Glide.with(holder.imageView.context).
        load(list[position].urlToImage).
        into(holder.imageView)
        holder.textView2.setOnClickListener {
            val websiteUrl=list[position].url
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}