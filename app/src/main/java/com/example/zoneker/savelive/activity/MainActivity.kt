package com.example.zoneker.savelive.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.zoneker.savelive.BaseActivity
import com.example.zoneker.savelive.R
import com.example.zoneker.savelive.adapter.UserMessageAdapter
import com.example.zoneker.savelive.interfaze.ItemListener
import com.example.zoneker.savelive.modle.Msgs
import com.example.zoneker.savelive.modle.UserData
import com.example.zoneker.savelive.single.SingleVolley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {
    private var year : Int? =null
    private var month : Int? =null
    private var day : Int ? =null
    private val TAG = "MainActivity"
    private var contentData : ArrayList<Msgs>? = null
    private  val URL = "http://blog.fanfou.com/digest/json/"
    private val TAIL = ".daily.json"
    private var recycle : RecyclerView? =null
    private var adapter : UserMessageAdapter? =null
    private var isRefreshing : Boolean = false
    override fun initView() {
        setContentView(R.layout.activity_main)
        recycle = findViewById(R.id.main_recycle_view) as RecyclerView
        recycle!!.layoutManager = LinearLayoutManager(this)
        //初始化日期
        val c : Calendar = Calendar.getInstance();
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        Log.d("---->day",c.get(Calendar.DAY_OF_MONTH).toString())
        if (c.get(Calendar.DAY_OF_MONTH)<8){
            day = c.get(Calendar.DAY_OF_MONTH)-1
        }else{
            day = c.get(Calendar.DAY_OF_MONTH)
        }
        getContentData(parseDate(year!!,month!!,day!!))
        //刷新
        refresh.setOnRefreshListener {

            val c : Calendar = Calendar.getInstance();
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            Log.d("---->day",c.get(Calendar.DAY_OF_MONTH).toString())
            if (c.get(Calendar.DAY_OF_MONTH)<8){
                day = c.get(Calendar.DAY_OF_MONTH)-1
            }else{
                day = c.get(Calendar.DAY_OF_MONTH)
            }
            if (contentData!!.isNotEmpty()){
                contentData!!.clear()
                isRefreshing = true
            }
            getContentData(parseDate(year!!,month!!,day!!))
        }
        Log.d("------time",parseDate(year!!,month!!,day!!))
        //选择日期
        fab.setOnClickListener {
            val c : Calendar = Calendar.getInstance()

          val dialog : DatePickerDialog = DatePickerDialog(this@MainActivity,
                  DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                  if (contentData!=null){
                      contentData!!.clear()
                      adapter!!.notifyDataSetChanged()
                      getContentData(parseDate(year,month,dayOfMonth))
                      this.year = year
                      this.month = month
                      day = dayOfMonth
                  }
                  },year!!,month!!,day!!)
                  //设置日历上的日期范围
            //最大取值范围
            dialog.datePicker.maxDate = c.timeInMillis
            //从新定义c 给他一个小的范围
            c.set(2016,6,13)
            //最小的值
            dialog.datePicker.minDate = c.timeInMillis
            dialog.show()

        }
    }
    fun getContentData(data : String){
        isRefresh(true)
       val jsonRequest = JsonObjectRequest(Request.Method.GET,URL+data+TAIL,null,Response.Listener<JSONObject> {
           jsonObject: JSONObject ->
           isRefresh(false)
           var gson = Gson()
           val data : UserData = gson.fromJson(jsonObject.toString(),UserData::class.java)
           contentData = data.msgs
           //anko，kotlin推荐使用的使用前需要导入compile 'org.jetbrains.anko:anko-sdk15:0.8.3'
          // compile 'org.jetbrains.anko:anko-appcompat-v7:0.8.3'
           //异步线程
           async {
               uiThread {
                   adapter = UserMessageAdapter(this@MainActivity,contentData!!)
                   //解决下拉刷新迅速上划bug
                   recycle!!.setOnTouchListener { v, event ->
                       if (isRefreshing){
                           return@setOnTouchListener true
                       }else{
                           return@setOnTouchListener false
                       }
                   }
                   recycle!!.adapter = adapter
                   isRefreshing = false


                   adapter!!.setItemListener(ItemListener { v, position ->
                      val intent = Intent(this@MainActivity,DetailsActivity::class.java)
                       val bundle = Bundle()
                       //向子activity传递自定义的arrayList
                      bundle.putSerializable("contentData",contentData)
                       bundle.putInt("itemPosition",position)
                      intent.putExtras(bundle)
                           startActivity(intent)


                   })

               }
           }

       } ,Response.ErrorListener {
               volleyError ->
       })
        SingleVolley.getSingleVolley(this).addToQueue(jsonRequest)
    }
    //把时间转换成yyyy-MM-dd的样式 方法
    fun parseDate(year : Int,mouth : Int,day : Int) : String {
        val format : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date : Date = Date(year-1900,mouth,day)
        val s = format.format(date)
        return s
    }
    override fun onStop() {
        super.onStop()
        SingleVolley.getSingleVolley(this).cancelAll(TAG)
    }
    fun isRefresh(boolean : Boolean){
        if (boolean == true){
            refresh.isRefreshing = boolean
        }else if (boolean == false){
            refresh.isRefreshing = boolean
        }
    }



}


