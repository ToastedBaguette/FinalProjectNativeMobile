package com.ubaya.projectuasnmp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_meme_detail.*
import kotlinx.android.synthetic.main.card_meme.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import kotlin.collections.ArrayList

private const val ARG_MEME = "meme"
private var meme: Meme? = null
var comments:ArrayList<Comment> = ArrayList()
var meme_id:Int? = 0

class MemeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_detail)

        val sharedFile = "com.ubaya.projectuasnmp"
        val shared: SharedPreferences = this.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )

        val userId = shared.getInt("userId",0)

        meme = intent.getParcelableExtra(ARG_MEME)
        val urlImg = meme?.image_url
        Picasso.get().load(urlImg).into(imgMeme)
        txtTop.text = meme?.top_text
        txtBottom.text = meme?.bottom_text
        txtLikes.text = meme?.num_likes.toString()
        meme_id = meme?.id

        readComment()

//      Declare icon like apperance for the first time
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420041/check_likes.php"
        val stringRequest =  object : StringRequest(
            Request.Method.POST, url,
            Response.Listener {
                Log.d("cekparams", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "LIKED") {
                    btnLike.setImageResource(R.drawable.ic_baseline_favorite_24)
                    btnLike.tag = R.drawable.ic_baseline_favorite_24
                }else{
                    btnLike.setImageResource(R.drawable.ic_outline_favorite_border_24)
                    btnLike.tag = R.drawable.ic_outline_favorite_border_24
                }

            },
            Response.ErrorListener {
                Log.d("cekparams", it.message.toString())
            }
        )
        {
            override fun getParams() = hashMapOf(
                "idmemes" to meme?.id.toString(),
                "idusers" to userId.toString()
            )
        }
        q.add(stringRequest)

        btnLike.setOnClickListener(){
            if (btnLike.tag.equals(R.drawable.ic_outline_favorite_border_24)){
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/native/160420041/add_likes.php"
                val stringRequest =  object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener {
                        Log.d("cekparams", it)
                        var newlikes = meme?.num_likes?.plus(1)
                        txtLikes.text = "$newlikes"
                        btnLike.setImageResource(R.drawable.ic_baseline_favorite_24)
                        btnLike.tag = R.drawable.ic_baseline_favorite_24
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                )
                {
                    override fun getParams() = hashMapOf(
                        "idmemes" to meme?.id.toString(),
                        "idusers" to userId.toString()
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
                        var newlikes = meme?.num_likes?.minus(1)
                        txtLikes.text = "$newlikes"
                        btnLike.setImageResource(R.drawable.ic_outline_favorite_border_24)
                        btnLike.tag = R.drawable.ic_outline_favorite_border_24
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                )
                {
                    override fun getParams() = hashMapOf(
                        "idmemes" to meme?.id.toString(),
                        "idusers" to userId.toString()                    )
                }
                q.add(stringRequest)
            }
        }

        btnSendComment.setOnClickListener(){
            addComment()
            readComment()
        }
    }

    fun updateList() {
        val lm: LinearLayoutManager = LinearLayoutManager(this)
        var recyclerView = commentsView
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = CommentAdapter(comments)
    }

    @SuppressLint("SimpleDateFormat")
    fun readComment(){
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
                        val date  = formatter.parse(commentObj.getString("publish_date"))
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
            override fun getParams() = hashMapOf(
                "memes_id" to meme_id.toString()
            )
        }
        q.add(stringRequest)
    }

    fun addComment(){
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420041/add_comment.php"
        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = this.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )
        var stringRequest = object:StringRequest(Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK") {
                    Toast.makeText(this, "Comment Success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Comment Fail", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams() = hashMapOf(
                "users_id" to shared.getInt("userId",0).toString(),
                "memes_id" to meme_id.toString(),
                "content" to txtWriteComment.text.toString()
            )
        }
        q.add(stringRequest)
    }
}