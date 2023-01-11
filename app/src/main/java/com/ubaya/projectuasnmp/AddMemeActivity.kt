package com.ubaya.projectuasnmp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_meme.*
import kotlinx.android.synthetic.main.card_meme.*

class AddMemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meme)

        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = getSharedPreferences(sharedFile, Context.MODE_PRIVATE )
        var editor: SharedPreferences.Editor = shared.edit()

        txtImageURLCreate.addTextChangedListener {
            if (txtImageURLCreate.text.toString() != "") {
                var url = txtImageURLCreate.text.toString()
                Picasso.get().load(url).into(imageMeme)
            }
        }

        txtTopTextCreate.addTextChangedListener {
            txtTopPreview.text = (txtTopTextCreate.text.toString())
        }

        txtBottomTextCreate.addTextChangedListener {
            txtBottomPreview.text = (txtBottomTextCreate.text.toString())
        }

        btnSubmit.setOnClickListener(){
            var userId = shared.getInt("userId",0)
            val q= Volley.newRequestQueue(it.context)
            val url="https://ubaya.fun/native/160420041/add_memes.php"
            val stringRequest= object: StringRequest(
                Request.Method.POST, url,
                Response.Listener{
                    Log.d("cekparams",it)
                    Toast.makeText(this, "Add meme success", Toast.LENGTH_SHORT).show() },
                Response.ErrorListener{
                    Log.d("cekparams",it.message.toString())

                }
            ){
                override fun getParams() = hashMapOf(
                    "image_url" to txtImageURLCreate.text.toString(),
                    "top_text" to txtTopTextCreate.text.toString(),
                    "bottom_text" to txtBottomTextCreate.text.toString(),
                    "users_id" to userId.toString()
                )
            }
            q.add(stringRequest)
            finish()
        }
    }
}