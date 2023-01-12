package com.ubaya.projectuasnmp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LeaderboardFragment : Fragment() {
    var leaderboard:ArrayList<Leaderboard> = ArrayList()

    override fun onResume() {
        super.onResume()
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420041/getleaderboard.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    leaderboard.clear()
                    for(i in 0 until data.length()) {
                        val leadObj = data.getJSONObject(i)
                        val lead = Leaderboard(
                            leadObj.getString("fullname"),
                            leadObj.getString("avatar_img"),
                            leadObj.getInt("total_likes"),
                            leadObj.getInt("privacy_setting")
                        )
                        leaderboard.add(lead)
                    }
                    updateList()
                    Log.d("cekisiarray", leaderboard.toString())
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }
    fun updateList() {
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.leaderboardviews)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = LeaderboardAdapter(leaderboard)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

}