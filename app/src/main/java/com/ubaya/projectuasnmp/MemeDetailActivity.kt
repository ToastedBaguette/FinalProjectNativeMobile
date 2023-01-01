package com.ubaya.projectuasnmp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_meme_detail.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_MEME = "meme"
private var meme: Meme? = null
var comments:ArrayList<Comment> = ArrayList()
var meme_id:Int? = 0

class MemeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_detail)
        meme = intent.getParcelableExtra(ARG_MEME)

        val url = meme?.image_url
        Picasso.get().load(url).into(imgMeme)
        txtTop.text = meme?.top_text
        txtBottom.text = meme?.bottom_text
        txtLikes.text = meme?.num_likes.toString()
        meme_id = meme?.id
    }

    override fun onResume() {
        super.onResume()
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420041/get_comments.php"
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        var stringRequest = object:StringRequest(Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    comments.clear()
                    for(i in 0 until data.length()) {
                        val commentObj = data.getJSONObject(i)
                        val date  = formatter.parse("2023-01-01 06:56:19")
                        val currDate = SimpleDateFormat("DD MMM yy").format(date)
                        val comment = Comment(
                            commentObj.getString("first_name"),
                            commentObj.getString("last_name"),
                            commentObj.getString("content"),
                            currDate
                        )
                        comments.add(comment)
                    }
                    updateList()
                    Log.d("cekisiarray", comments.toString())
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
            {
                override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["memes_id"] = meme_id.toString()
                        return params
                    }
            }
        q.add(stringRequest)
        updateList()

    }

    fun updateList() {
        val lm: LinearLayoutManager = LinearLayoutManager(this)
        var recyclerView = commentsView
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = CommentAdapter(comments)
    }
}