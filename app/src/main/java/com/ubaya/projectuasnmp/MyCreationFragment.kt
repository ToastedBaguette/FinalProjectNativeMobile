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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_my_creation.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyCreationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCreationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var myCreationList:ArrayList<Meme> = ArrayList()
    var userId = 0

    override fun onResume() {
        super.onResume()
        val q = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2/NMP_UAS/get_My_Memes.php"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun updateList() {
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.memesView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = MemeAdapter(myCreationList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_creation, container, false)
        var view = inflater.inflate(R.layout.fragment_my_creation, container, false)
        var fab : FloatingActionButton? = view.findViewById(R.id.fab)
        fab?.setOnClickListener() {
            val intent = Intent(activity, AddMemeActivity::class.java)
            activity?.startActivity(intent)
        }
        updateList()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyCreationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyCreationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}