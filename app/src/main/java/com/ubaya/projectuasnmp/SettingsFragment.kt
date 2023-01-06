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
import android.widget.ImageView
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
    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_CODE_CAMERA = 2
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

    fun refreshSharedPreferences(context: Context){
        val sharedFile = "com.ubaya.projectuasnmp"
        val shared: SharedPreferences = context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
        userId = shared.getInt("userId",0)
        userName = shared.getString("userName","").toString()
        firstName = shared.getString("firstName","").toString()
        lastName = shared.getString("lastName","").toString()
        month = shared.getString("month","").toString()
        year = shared.getString("year","").toString()
        avatarImg = shared.getString("avatarImg","").toString()
        privacySet = shared.getInt("privacySet",0)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        this.context?.let { refreshSharedPreferences(it) }
        if(lastName == "null"){
            lastName = ""
        }
        txtName.text = "$firstName $lastName"
        txtActiveStatus.text = "Active since $month $year"
        txtSettingUsername.text = userName
        txtSettingFirstName.setText(firstName)
        txtSettingLastName.setText(if(lastName =="") "" else lastName)
        if(avatarImg!=""){
            Picasso.get().load(avatarImg).into(imgAvatar)
        }
        cBHideName.isChecked = privacySet == 1
        super.onResume()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        refreshSharedPreferences(context)
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
        val imgAvatar = v.findViewById<ImageView>(R.id.imgAvatar)

        val sharedFile = "com.ubaya.projectuasnmp"
        val shared: SharedPreferences = v.context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE )
        var editor:SharedPreferences.Editor = shared.edit()

        btnSaveChanges?.setOnClickListener{
            if(!txtSettingFirstName.text.isNullOrBlank()){
                firstName = shared.getString("firstName","").toString()
                lastName = shared.getString("lastName","").toString()
                checked = if(cBHideName.isChecked){ 1 } else{ 0 }
                changeFirstName = txtSettingFirstName?.text.toString()
                changeLastName = if(txtSettingLastName?.text.isNullOrBlank()) { "" }else{ txtSettingLastName?.text.toString() }
                val q = Volley.newRequestQueue(activity)
                val url = "https://ubaya.fun/native/160420041/changeprofile.php"
                var stringRequest =
                    @SuppressLint("SetTextI18n")
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
                                txtSettingFirstName?.setText(changeFirstName)
                                txtSettingLastName?.setText(if(changeLastName =="") "" else changeLastName)
                                cBHideName.isChecked = checked == 1

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
            }else{
                Toast.makeText(activity, "Don't leave the first name empty, please!", Toast.LENGTH_SHORT).show()
            }
        }
//        imgAvatar.setOnClickListener{
//            val options = arrayOf<CharSequence>("Take from Gallery", "Take from Camera")
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Choose Picture")
//            builder.setItems(options) { dialog, item ->
//                if (options[item] == "Take from Gallery") {
//                    val intent = Intent(Intent.ACTION_PICK)
//                    intent.type = "image/*"
//                    startActivityForResult(intent, REQUEST_CODE_GALLERY)
//                } else if (options[item] == "Take from Camera") {
//                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(intent, REQUEST_CODE_CAMERA)
//                }
//            }
//            builder.show()
//        }
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