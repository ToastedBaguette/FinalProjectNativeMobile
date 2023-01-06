package com.ubaya.projectuasnmp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.card_meme.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONObject
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var userId:Int = 0
    var userName = ""
    var firstName = ""
    var lastName = ""
    var month = ""
    var year = ""
    var avatarImg =""
    var privacySet = 0

    var changeFirstName:String= ""
    var changeLastName:String = ""
    var checked:Int = 0

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        if(lastName == "null"){
            lastName = ""
        }
        txtName.text = "$firstName $lastName"
        txtActiveStatus.text = "Active since $month $year"
        txtSettingUsername.text = userName
        txtSettingFirstName.hint = firstName
        txtSettingLastName.hint = if(lastName =="") "" else lastName
        if(avatarImg!=""){
            Picasso.get().load(avatarImg).into(imgAvatar)
        }
        cBHideName.isChecked = privacySet == 1
        super.onResume()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sharedFile = "com.ubaya.projectuasnmp"
        val shared: SharedPreferences = context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE )
        userId = shared.getInt("userId",0)
        userName = shared.getString("userName","").toString()
        firstName = shared.getString("firstName","").toString()
        lastName = shared.getString("lastName","").toString()
        month = shared.getString("month","").toString()
        year = shared.getString("year","").toString()
        avatarImg = shared.getString("avatarImg","").toString()
        privacySet = shared.getInt("privacySet",0)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        val btnSaveChanges = v?.findViewById<Button>(R.id.btnSaveChanges)
        val txtName = v.findViewById<TextView>(R.id.txtName)
        val txtSettingFirstName = v.findViewById<EditText>(R.id.txtSettingFirstName)
        val txtSettingLastName = v.findViewById<EditText>(R.id.txtSettingLastName)
        val cBHideName = v.findViewById<CheckBox>(R.id.cBHideName)
        val fabLogout = v.findViewById<FloatingActionButton>(R.id.fabLogOut)
        val sharedFile = "com.ubaya.projectuasnmp"
        val shared: SharedPreferences = v.context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE )
        var editor:SharedPreferences.Editor = shared.edit()

        btnSaveChanges?.setOnClickListener{
            firstName = shared.getString("firstName","").toString()
            lastName = shared.getString("lastName","").toString()
            var showlastname =  if(lastName =="") firstName else lastName
            if(cBHideName.isChecked){
                checked = 1
            }
            if(txtSettingFirstName?.text.isNullOrBlank()) {
                Toast.makeText(activity, "Don't leave your first name empty", Toast.LENGTH_SHORT).show()
            }else{
                changeFirstName = txtSettingFirstName?.text.toString()
                changeLastName = if(txtSettingLastName?.text.isNullOrBlank()) { "" } else { txtSettingLastName.text.toString() }
                val q = Volley.newRequestQueue(activity)
                val url = "https://ubaya.fun/native/160420041/changeprofile.php"
                var stringRequest =
                    object:StringRequest(Request.Method.POST, url,
                        Response.Listener<String> {
                            Log.d("apiresult", it)
                            val obj = JSONObject(it)
                            if(obj.getString("result")=="OK"){
                                editor.putString("firstName",changeFirstName)
                                editor.putString("lastName",changeLastName)
                                editor.putInt("privacySet",checked)
                                editor.apply()

                                txtName?.text = "$changeFirstName $changeLastName"
                                txtSettingFirstName?.hint = changeFirstName
                                txtSettingLastName?.hint = if(changeLastName =="") "" else changeLastName
                                cBHideName?.isChecked = checked == 1
                                txtSettingFirstName?.setText("")
                                txtSettingLastName?.setText("")
                                Toast.makeText(activity, "Save changes success", Toast.LENGTH_SHORT).show()
                            }else{
                                Log.e("error","Failed to save")
                            }
                        },
                        Response.ErrorListener {
                            Log.e("apiresult", it.message.toString())
                        })
                    {
                        override fun getParams() = hashMapOf(
                            "firstname" to changeFirstName,
                            "lastname" to changeLastName,
                            "privacySet" to checked.toString(),
                            "iduser" to userId.toString()
                        )
                    }
                q.add(stringRequest)
            }
        }
        fabLogout.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            editor.clear()
            editor.apply()
            this.startActivity(intent)
            activity?.finish()
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}