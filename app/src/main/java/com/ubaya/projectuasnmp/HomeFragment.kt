package com.ubaya.projectuasnmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    var memes:ArrayList<Meme> = ArrayList()
    var userId = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = context.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )

        userId = shared.getInt("userId",0)
    }

    override fun onResume() {
        super.onResume()
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420041/get_memes.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    memes.clear()
                    for(i in 0 until data.length()) {
                        val memeObj = data.getJSONObject(i)
                        val meme = Meme(
                            memeObj.getInt("idmemes"),
                            memeObj.getString("image_url"),
                            memeObj.getString("top_text"),
                            memeObj.getString("bottom_text"),
                            memeObj.getInt("num_likes"),
                            memeObj.getInt("users_id")
                        )
                        memes.add(meme)
                    }
                    updateList()
                    Log.d("cekisiarray", memes.toString())
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }

    fun updateList() {
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.memelistView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = MemeAdapter(memes, userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        var fab : FloatingActionButton? = view.findViewById(R.id.fab)
        fab?.setOnClickListener() {
            val intent = Intent(activity, AddMemeActivity::class.java)
            activity?.startActivity(intent)
        }
        return view
    }
}