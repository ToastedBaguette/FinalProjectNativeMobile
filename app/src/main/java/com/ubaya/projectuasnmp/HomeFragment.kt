package com.ubaya.projectuasnmp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject


class HomeFragment : Fragment() {

    var memes:ArrayList<Meme> = ArrayList()
    var userId = 0
    var sorts= arrayOf("Newest", "Popularity", "Most Commented")

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //get shared preferences
        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE )

        userId = shared.getInt("userId",0)
    }

    override fun onResume() {
        super.onResume()
        getMemes("clear")
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
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        var fab : FloatingActionButton? = view.findViewById(R.id.fab)
        var fabFilter :FloatingActionButton? = view.findViewById(R.id.fabFilter)

        fabFilter?.setOnClickListener(){
            val options = arrayOf<CharSequence>("Newest", "Popularity", "Most Commented")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sort Memes By")
            builder.setItems(options) { dialog, item ->
                if (options[item] == "Newest") {
                    getMemes("clear")
                } else if (options[item] == "Popularity") {
                    getMemes("like")
                } else if (options[item] == "Most Commented"){
                    getMemes("comment")
                }
            }
            builder.show()
        }

        fab?.setOnClickListener() {
            val intent = Intent(activity, AddMemeActivity::class.java)
            activity?.startActivity(intent)
        }

        return view
    }

    fun getMemes(sortBy: String){
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420041/get_memes.php"
        var stringRequest = object:StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result")=="OK"){
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
                            memeObj.getInt("users_id"),
                            memeObj.getInt("total_comments")
                        )
                        memes.add(meme)
                    }
                    updateList()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        {
            override fun getParams() = hashMapOf(
                "sort" to sortBy,
            )
        }
        q.add(stringRequest)
    }

}