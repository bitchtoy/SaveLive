package com.example.zoneker.savelive.activity


import com.bumptech.glide.Glide
import com.example.zoneker.savelive.BaseActivity
import com.example.zoneker.savelive.R
import com.example.zoneker.savelive.modle.Img
import com.example.zoneker.savelive.modle.Msgs
import kotlinx.android.synthetic.main.details_layout.*
import org.jetbrains.anko.image


/**
 * Created by Zoneker on 2017/6/15.
 */
class DetailsActivity : BaseActivity(){
    private var arrayList : ArrayList<Msgs>? =null
    private var img : Img? = null
    private var msgs : Msgs? = null
    private var itemPosition : Int? = null
    override fun initView() {
        setContentView(R.layout.details_layout)
        val bundle = intent.extras
        itemPosition = bundle.getInt("itemPosition")
        arrayList = bundle.getSerializable("contentData") as ArrayList<Msgs>
        msgs = arrayList!!.get(itemPosition!!)
        img = msgs!!.img
        Glide.with(this).load(msgs!!.avatar).into(details_circleImgView)
        details_avatar.text = msgs!!.realname
        details_content.text = msgs!!.msg
        details_time.text = msgs!!.time
        if (img!!.preview.isNotEmpty()){
            Glide.with(this).load(img!!.preview).into(details_img)
        }else {
           details_img.setImageResource(R.drawable.ic_photo)
        }


    }

}