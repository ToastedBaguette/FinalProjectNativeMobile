package com.ubaya.projectuasnmp

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.card_meme.view.*

class MemeAdapter (val memes:ArrayList<Meme>)
:RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {
    class MemeViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.card_meme, parent,false)
        return MemeViewHolder(v)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, @SuppressLint("RecyclerView") position: Int) {
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
        holder.v.btnLike.setImageResource(R.drawable.ic_outline_favorite_border_24)
        holder.v.btnLike.tag = R.drawable.ic_outline_favorite_border_24

        holder.v.btnLike.setOnClickListener{
            if (holder.v.btnLike.tag.equals(R.drawable.ic_outline_favorite_border_24)){
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/native/160420041/add_likes.php"
                val stringRequest =  object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener {
                        Log.d("cekparams", it)
                        memes[position].num_likes++
                        var newlikes = memes[position].num_likes
                        holder.v.txtLikes.text = "$newlikes"
                        holder.v.btnLike.setImageResource(R.drawable.ic_baseline_favorite_24)
                        holder.v.btnLike.tag = R.drawable.ic_baseline_favorite_24
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                )
                {
                    override fun getParams() = hashMapOf(
                        "id" to memes[position].id.toString()
                    )
                }
                q.add(stringRequest)
            }else{
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/native/160420041/reduce_likes.php"
                val stringRequest =  object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener {
                        Log.d("cekparams", it)
                        memes[position].num_likes--
                        var newlikes = memes[position].num_likes
                        holder.v.txtLikes.text = "$newlikes"
                        holder.v.btnLike.setImageResource(R.drawable.ic_outline_favorite_border_24)
                        holder.v.btnLike.tag = R.drawable.ic_outline_favorite_border_24
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                )
                {
                    override fun getParams() = hashMapOf(
                        "id" to memes[position].id.toString()
                    )
                }
                q.add(stringRequest)
            }

        }

    }

    override fun getItemCount(): Int {
        return memes.size
    }

}