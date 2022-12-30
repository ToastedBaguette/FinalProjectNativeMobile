package com.ubaya.projectuasnmp
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Meme (val id:Int  , val image_url: String,
            val top_text:String,
            val bottom_text:String,
            var num_likes: Int,
            val user_id:Int) : Parcelable