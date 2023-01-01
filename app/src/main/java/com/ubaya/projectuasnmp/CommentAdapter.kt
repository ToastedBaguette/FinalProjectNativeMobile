package com.ubaya.projectuasnmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_comment.view.*

class CommentAdapter (val comments:ArrayList<Comment>)
    : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.card_comment, parent,false)
        return CommentViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.v.txtNameUser.text = comments[position].first_name

        if (comments[position].last_name != null){
            holder.v.txtNameUser.text =  holder.v.txtNameUser.text.toString() + " " + comments[position].last_name
        }

        holder.v.txtComment.text = comments[position].content
        holder.v.txtDate.text = comments[position].publish_date
    }

    override fun getItemCount(): Int {
        return comments.size
    }

}