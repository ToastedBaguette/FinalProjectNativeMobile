package com.ubaya.projectuasnmp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener{
            var username = txtLoginUsername.text.toString()
            var password = txtLoginPassword.text.toString()

            val q = Volley.newRequestQueue(this)
            val url = "http://10.0.2.2/NMP_UAS/login.php"

            var stringRequest = object:StringRequest(Request.Method.POST, url,
                Response.Listener<String> {
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if(obj.getString("result")=="OK"){
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