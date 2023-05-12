package com.ma.mobileattendance.ui.affair.workoutside

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ma.mobileattendance.R
import com.ma.mobileattendance.logic.model.Affair
import com.ma.mobileattendance.logic.model.EnumNoun

class WorkOutsidePunchAdapter(private val workOutsideList:List<Affair>):RecyclerView.Adapter<WorkOutsidePunchAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.woTitle)
        val content: TextView = view.findViewById(R.id.woContent)
        val woStartTime: TextView = view.findViewById(R.id.woStartTimeText)
        val woEndTIme: TextView = view.findViewById(R.id.woEndTImeText)
        val woTotalText: TextView = view.findViewById(R.id.woTotalText)
        val woRuleText: TextView = view.findViewById(R.id.woRuleText)
        val woResult: TextView = view.findViewById(R.id.woResult)
        val woEditBtn: Button = view.findViewById(R.id.woEditBtn)
        val woPunchBtn: Button = view.findViewById(R.id.woPunchBtn)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOutsidePunchAdapter.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.fragment_work_outside_punch_item,parent,false)
        val viewHolder=ViewHolder(view)
        viewHolder.woEditBtn.setOnClickListener {
            Toast.makeText(parent.context,"功能待完善!",Toast.LENGTH_SHORT).show()
        }
        viewHolder.woPunchBtn.setOnClickListener {
            val position=viewHolder.adapterPosition
            val workOutside=workOutsideList[position]
            if(workOutside.result == EnumNoun.AUDIT_AGREE_RESULT){
                val controller=Navigation.findNavController(it)
                val bundle=Bundle()
                bundle.putString("rCategory", EnumNoun.OUTSIDE_PUNCH)
                bundle.putInt("recordRuleId", workOutside.aId?:0)
                controller.navigate(R.id.action_workOutsidePunchFragment_to_punchMapFragment,bundle)
            }else{
                Toast.makeText(parent.context,"审核通过后才能考勤!",Toast.LENGTH_SHORT).show()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: WorkOutsidePunchAdapter.ViewHolder, position: Int) {
        if(workOutsideList.isNotEmpty()){
            val workOutside=workOutsideList[position]
            holder.title.text="外派事务${position+1}"
            holder.content.text=workOutside.content
            holder.woStartTime.text=workOutside.startTime
            holder.woEndTIme.text=workOutside.endTime
            holder.woTotalText.text= workOutside.total.toString()
            holder.woRuleText.text=workOutside.aId.toString()
            holder.woResult.text=workOutside.result
        }
    }
    override fun getItemCount(): Int {
        return workOutsideList.size
    }
}