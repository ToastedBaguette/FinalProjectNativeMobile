package com.ubaya.projectuasnmp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            if (txtPassword.text.toString() == txtRptPassword.text.toString()){
                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.fun/native/160420041/register.php"

                var stringRequest = object: StringRequest(
                    Request.Method.POST, url,
                    Response.Listener<String> {
                        Log.d("apiresult", it)
                        val obj = JSONObject(it)
                        if(obj.getString("result")=="OK"){
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            this.startActivity(intent)
                            finish()
                        }
                    },
                    Response.ErrorListener {
                        Log.e("apiresult", it.message.toString())
                    })
                {
                    override fun getParams() = hashMapOf(
                        "username" to txtUsername.text.toString(),
                        "first_name" to txtFirstName.text.toString(),
                        "password" to txtPassword.text.toString(),
                        "last_name" to txtLastName.text.toString(),
                        "avatar_img" to "",
                        "privacy_setting" to "0"
                    )
                }
                q.add(stringRequest)
            }
            else{
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
            }

        }
    }
}