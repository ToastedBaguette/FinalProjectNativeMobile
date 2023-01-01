package com.ubaya.projectuasnmp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*

class MemeAdapter (val memes:ArrayList<Meme>)
:RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {
    class MemeViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.card_meme, parent,false)
        return MemeViewHolder(v)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val url = memes[position].image_url
        Picasso.get().load(url).into(holder.v.imgMeme)
        holder.v.txtTop.text = memes[position].top_text
        holder.v.txtBottom.text = memes[position].bottom_text
        holder.v.txtLikes.text = memes[position].num_likes.toString()
        holder.v.btnComment.setOnClickListener{
            val intent = Intent(holder.v.context, MemeDetailActivity::class.java)
            intent.putExtra("meme", memes[position])
            holder.v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return memes.size
    }

}