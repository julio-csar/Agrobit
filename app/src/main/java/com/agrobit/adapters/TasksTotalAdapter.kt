package com.agrobit.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.agrobit.R
import com.agrobit.classes.Item
import androidx.recyclerview.widget.RecyclerView.*
import com.agrobit.classes.HeaderPage
import com.agrobit.classes.HeaderSection
import com.agrobit.classes.Task


public class TasksTotalAdapter(mContext:Context, data:List<Item>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


  val mContext: Context=mContext
  val data:List<Item> = data

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val i:Int=holder.itemViewType
    if(i==Item.ITEM){
        var viewHolder:TaskViewHolder=holder as TaskViewHolder
        viewHolder.setIsRecyclable(false)
        var item:Task=data.get(position).item as Task

        viewHolder.container.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation)

        viewHolder.taskProgress.setProgress(item.avance)/*
        if(item.avance>0&&item.avance<=20){
            viewHolder.taskProgress.progressDrawable=this.mContext.getDrawable(R.drawable.cpb1)
        }else if(item.avance>20&&item.avance<=40){
            viewHolder.taskProgress.progressDrawable=this.mContext.getDrawable(R.drawable.cpb2)
        }else if(item.avance>40&&item.avance<=60){
            viewHolder.taskProgress.progressDrawable=this.mContext.getDrawable(R.drawable.cpb3)
        }else if(item.avance>60&&item.avance<=80){
            viewHolder.taskProgress.progressDrawable=this.mContext.getDrawable(R.drawable.cpb4)
        }else{
            viewHolder.taskProgress.progressDrawable=this.mContext.getDrawable(R.drawable.cpb5)
        }*/
        viewHolder.taskPerc.setText(item.avance.toString()+"%")
        viewHolder.tastDesc.setText(item.desc)
        viewHolder.taskCrea.setText(getDate(item.fechaCrea))
    }
    else if(i==Item.HEADER_PAGE)
    {
      var viewHolder:HeaderViewHolder=holder as HeaderViewHolder
      var item: HeaderPage = data.get(position).item as HeaderPage
      viewHolder.setIsRecyclable(false)
      viewHolder.number.setText(Integer.toString(item.total))
      viewHolder.desc.setText(item.name)
    }
      else if(i==Item.HEADER_SECTION){
        var viewHolder:HeaderSecViewHolder=holder as HeaderSecViewHolder
        var item:HeaderSection=data.get(position).item as HeaderSection
        viewHolder.setIsRecyclable(false)
        viewHolder.titleSection.setText(item.name)
    }
  }
    fun getDate(valor:String): String {
        if(valor.equals("1")){
            return "Nunca"
        }
        else
        {
            var fecha:String=""

            fecha+=valor.toString().subSequence(6,8).toString()
            fecha+="/"
            fecha+=valor.toString().subSequence(4,6).toString()
            fecha+="/"
            fecha+=valor.toString().subSequence(0,4).toString()
            fecha+=", "
            fecha+=valor.toString().subSequence(8,10).toString()
            fecha+=":"
            fecha+=valor.toString().substring(10)
            fecha+="hs"

            return fecha
        }
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
    if(viewType==Item.HEADER_PAGE){
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.header_page,parent,false)
      return HeaderViewHolder(layout)
    }else if(viewType==Item.ITEM){
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.item_task,parent,false)
      return TaskViewHolder(layout)
    }else{
        val layout:View=LayoutInflater.from(mContext).inflate(R.layout.header_section,parent,false)
        return HeaderSecViewHolder(layout)
    }
  }


  override fun getItemCount(): Int {
    return data.size
  }

  override fun getItemViewType(position: Int): Int {
    return data.get(position).tipo
  }

  public class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container:RelativeLayout=itemView.findViewById(R.id.task_container)
      val taskProgress:ProgressBar=itemView.findViewById(R.id.task_progress)
    var taskPerc:TextView=itemView.findViewById(R.id.task_perc)
    var tastDesc:TextView=itemView.findViewById(R.id.task_desc)
      var taskCrea:TextView=itemView.findViewById(R.id.task_crea)

  }
  public class HeaderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var number:TextView=itemView.findViewById(R.id.total)
    var desc:TextView=itemView.findViewById(R.id.descrip)
  }
    class HeaderSecViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var titleSection:TextView=itemView.findViewById(R.id.titlesection)
    }
}
