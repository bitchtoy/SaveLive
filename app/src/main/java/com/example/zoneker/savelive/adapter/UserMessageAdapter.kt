package com.example.zoneker.savelive.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.zoneker.savelive.R
import com.example.zoneker.savelive.interfaze.ItemListener
import com.example.zoneker.savelive.modle.Msgs
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * Created by Zoneker on 2017/6/13.
 */
class UserMessageAdapter(val ctx : Context,val list: ArrayList<Msgs>) : RecyclerView.Adapter<UserMessageAdapter.UserHolder>() {
    private var inflater : LayoutInflater
    private var itemListener : ItemListener? =null
    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val item = list.get(position)
        Glide.with(ctx).load(item.avatar).into(holder.itemView.avatar)
        if (!item.img.preview.isNullOrEmpty()){
            holder.itemView.iv_main.visibility = View.VISIBLE
        }else{
            holder.itemView.iv_main.visibility = View.INVISIBLE
        }
        holder.itemView.author_name.text = item.realname
        holder.itemView.tv_content.text = item.msg
        holder.itemView.tv_time.text = item.time
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserHolder {
        var view : View = inflater.inflate(R.layout.item_layout, null)
        var holder = UserHolder(view,itemListener!!)
        return holder
    }


    class UserHolder(itemView : View,val itemListener : ItemListener) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        private var card_view : CardView
        override fun onClick(v: View) {
            itemListener.itemListener(v,position)
        }

        init {
           card_view = itemView.findViewById(R.id.card_view) as CardView
            card_view.setOnClickListener(this)
            card_view.setBackgroundResource(R.drawable.angler_bg)
        }

    }
    fun setItemListener(listener : ItemListener){
        this.itemListener = listener
    }
}