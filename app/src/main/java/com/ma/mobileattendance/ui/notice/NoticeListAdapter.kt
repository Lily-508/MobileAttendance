package com.ma.mobileattendance.ui.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ma.mobileattendance.R
import com.ma.mobileattendance.logic.model.Notice

class NoticeListAdapter(private val noticeList:List<Notice>):RecyclerView.Adapter<NoticeListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nTitle: TextView = view.findViewById(R.id.noticeTitle)
        val nContent: TextView = view.findViewById(R.id.noticeContent)
        val noticeDatetime: TextView = view.findViewById(R.id.noticeDatetime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.fragment_notice_item,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(noticeList.isNotEmpty()){
            val notice=noticeList[position]
            holder.nTitle.text=notice.nTitle
            holder.nContent.text=notice.nContent
            holder.noticeDatetime.text=notice.nDatetime
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }
}