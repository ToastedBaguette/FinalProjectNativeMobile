package com.ubaya.projectuasnmp

import android.content.Context
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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class MyCreationFragment : Fragment() {
    var myCreationList:ArrayList<Meme> = ArrayList()
    var userId = 0

    override fun onResume() {
        super.onResume()
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420041/get_My_Memes.php"
        var stringRequest = object:StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result")=="OK"){
                    val data = obj.getJSONArray("data")
                    myCreationList.clear()
                    for(i in 0 until data.length()){
                        val playObj = data.getJSONObject(i)
                        val memes = Meme(
                            playObj.getInt("idmemes"),
                            playObj.getString("image_url"),
                            playObj.getString("top_text"),
                            playObj.getString("bottom_text"),
                            playObj.getInt("num_likes"),
                            playObj.getInt("users_id"),
                            playObj.getInt("total_comments"),
                        )
                        myCreationList.add(memes)
                    }
                    updateList()
                    Log.d("cekisiarray", myCreationList.toString())
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["users_id"] = userId.toString()
                return params
            }
        }
        q.add(stringRequest)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = context.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )

        userId = shared.getInt("userId",0)
    }

    fun updateList() {
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.memesView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = MemeAdapter(myCreationList, userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_creation, container, false)
    }

}