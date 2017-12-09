package me.karankapoor.androidkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.widget.ImageView

import me.karankapoor.androidkotlin.R
import me.karankapoor.androidkotlin.model.Android
import me.karankapoor.androidkotlin.model.HeadClass
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.recycler_view_row.view.*

class DataAdapter (private val dataList : ArrayList<Android>, private val listener : Listener) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface Listener {

        fun onItemClick(android : Android)
    }

    private val colors : Array<String> = arrayOf("#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(dataList[position], listener, colors, position)
    }

    override fun getItemCount(): Int = dataList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(android: Android, listener: Listener, colors : Array<String>, position: Int) {

            itemView.tv_rank.text = android.rank.toString()
            itemView.tv_name.text = android.country
            itemView.tv_android.text = android.population
            fun loadImage(imageView: ImageView, picture: String) {
                Picasso.with(imageView.context).load(picture).into(imageView)
            }
            loadImage(itemView.img_android, android.flag)
            itemView.setBackgroundColor(Color.parseColor(colors[position % 6]))

            itemView.setOnClickListener{ listener.onItemClick(android) }
        }
    }
}