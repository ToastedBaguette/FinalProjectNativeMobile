package com.ubaya.projectuasnmp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class SettingsFragment : Fragment() {
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
        userName = shared.getString("username","").toString()
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

        cBHideName.isChecked = privacySet == 1
        super.onResume()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        refreshSharedPreferences(context)
    }

    @SuppressLint("IntentReset", "SetTextI18n")
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

        if(avatarImg!=""){
            Picasso.get().load(avatarImg).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(imgAvatar)
        }

        btnSaveChanges?.setOnClickListener{
            if(!txtSettingFirstName.text.isNullOrBlank()){

                //Set Confirmation Dialog for changing data
                val alertChangeData = AlertDialog.Builder(requireContext())
                alertChangeData.setTitle("Change Profile")
                alertChangeData.setMessage("Are you sure want to change profile?")

                //if confirmation is yes, changes data and restart activity
                alertChangeData.setPositiveButton("Yes") { dialog, which ->
                    firstName = shared.getString("firstName","").toString()
                    lastName = shared.getString("lastName","").toString()
                    checked = if(cBHideName.isChecked){ 1 } else{ 0 }
                    changeFirstName = txtSettingFirstName?.text.toString()
                    changeLastName = if(txtSettingLastName?.text.isNullOrBlank()) { "" }else{ txtSettingLastName?.text.toString() }

                    val bitmap = (imgAvatar.drawable as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val imageInByte = stream.toByteArray()
                    val encodeImageString = Base64.encodeToString(imageInByte, Base64.DEFAULT)

                    val q = Volley.newRequestQueue(activity)
                    val url = "https://ubaya.fun/native/160420041/changeprofile.php"
                    var stringRequest =
                        @SuppressLint("SetTextI18n")
                        object:StringRequest(
                            Method.POST, url,
                            Response.Listener<String> {
                                Log.d("apiresult", it)
                                val obj = JSONObject(it)
                                if(obj.getString("result")=="OK"){
                                    editor.putString("firstName",changeFirstName)
                                    editor.putString("lastName",changeLastName)
                                    editor.putInt("privacySet",checked)
                                    editor.putString("avatarImg", "https://ubaya.fun/native/160420041/images/usrprofile$userId.jpg")
                                    editor.apply()


                                    activity?.finish()
                                    val intent = Intent(activity, LoginActivity::class.java)
                                    this.startActivity(intent)

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
                                "avatar_img" to encodeImageString,
                                "iduser" to userId.toString()
                            )
                        }
                    q.add(stringRequest)
                }

                //if confirmation is no, revert to previous data
                alertChangeData.setNegativeButton("No") { dialog, which ->
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

                    Toast.makeText(activity, "Revert changes", Toast.LENGTH_SHORT).show()
                }

                alertChangeData.show()


            }else{
                Toast.makeText(activity, "Don't leave the first name empty, please!", Toast.LENGTH_SHORT).show()
            }
        }

        imgAvatar.setOnClickListener{
            val options = arrayOf<CharSequence>("Take from Gallery", "Take from Camera")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose Picture")

            builder.setItems(options) { dialog, item ->
                if (options[item] == "Take from Gallery") {
                    if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
                    }else{
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intent.type = "image/*"
                        startActivityForResult(intent, REQUEST_CODE_GALLERY)
                    }
                } else if (options[item] == "Take from Camera") {
                    if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
                    }else{
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, REQUEST_CODE_CAMERA)
                    }
                }
            }
            builder.show()
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

    @Deprecated("Deprecated in Java")
    @SuppressLint("IntentReset")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_CAMERA -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CODE_CAMERA)
                } else{
                    Toast.makeText(requireContext(), "You must grant permission to access the camera.", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CODE_GALLERY -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE_GALLERY)
                } else{
                    Toast.makeText(requireContext(), "You must grant permission to access the gallery.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            val extras = data.extras
            val bitmap = extras!!.get("data") as Bitmap
            imgAvatar.setImageBitmap(bitmap)
        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val filePath: Uri? = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, filePath)
            imgAvatar.setImageBitmap(bitmap)
        }
    }
}