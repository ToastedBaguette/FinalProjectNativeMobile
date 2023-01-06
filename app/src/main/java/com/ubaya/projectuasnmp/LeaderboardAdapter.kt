package com.ubaya.projectuasnmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_leaderboard.view.*

class LeaderboardAdapter(val leaderboard:ArrayList<Leaderboard>) :RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    class LeaderboardViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.card_leaderboard, parent,false)
        return LeaderboardViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val url = leaderboard[position].imgAvatar
        if(url.isNotEmpty()){
            Picasso.get().load(url).into(holder.v.imgLeaderboardAvatar)
        }
        if((leaderboard[position].privacy) == 0){
            holder.v.txtLeaderboardName.text = leaderboard[position].fullname
        }else{
            var name = leaderboard[position].fullname.split(" ")
            var fullname = ""
            for (i in name.indices) {
                if(i==0){
                    fullname = name[0].replaceRange(3 until name[0].length, "*".repeat(name[0].length-3))
                }else{
                    fullname +=  " " + "*".repeat(name[i].length)
                }
            }
            holder.v.txtLeaderboardName.text = fullname;
        }

        holder.v.txtLeaderboardNumLikes.text = leaderboard[position].nums_like.toString()
    }

    override fun getItemCount(): Int {
        return leaderboard.size
    }
}