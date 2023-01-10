package com.ubaya.projectuasnmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var userId = 0
        var userName = ""
        var firstName = ""
        var lastName = ""
        var month = ""
        var year = ""
        var avatarImg = ""
        var privacySet = 0

        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = this.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )
        userId = shared.getInt("userId",0)

        if (userId != 0){
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            this.finish()
        }


        btnSignIn.setOnClickListener{
            var username = txtLoginUsername.text.toString()
            var password = txtLoginPassword.text.toString()
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.fun/native/160420041/login.php"
            var stringRequest = object:StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> {
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if(obj.getString("result")=="OK"){
                        val data = obj.getJSONObject("data")
                        userId = data.getInt("idusers")
                        userName = data.getString("username")
                        firstName = data.getString("first_name")
                        lastName = data.getString("last_name")
                        month = data.getString("month")
                        year = data.getString("year")
                        avatarImg = data.getString("avatar_img")

                        var sharedFile = "com.ubaya.projectuasnmp"
                        var shared: SharedPreferences = getSharedPreferences(sharedFile, Context.MODE_PRIVATE )
                        var editor:SharedPreferences.Editor = shared.edit()

                        editor.putInt("userId",userId)
                        editor.putString("username",userName)
                        editor.putString("firstName",firstName)
                        editor.putString("lastName",lastName)
                        editor.putString("month",month)
                        editor.putString("year",year)
                        editor.putString("avatarImg", avatarImg)
                        editor.putInt("privacySet",privacySet)
                        editor.apply()


                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                        this.finish()
                    }else{
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Log.e("apiresult", it.message.toString())
                })
                {
                    override fun getParams() = hashMapOf(
                        "username" to username,
                        "password" to password
                    )
                }
            q.add(stringRequest)
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
        }
    }
}