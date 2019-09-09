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


public class TaskCompleteAdapter(mContext:Context, data:List<Item>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


  val mContext: Context=mContext
  val data:List<Item> = data

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val i:Int=holder.itemViewType
    if(i==Item.ITEM){
        var viewHolder:TaskViewHolder=holder as TaskViewHolder
        viewHolder.setIsRecyclable(false)
        var item:Task=data.get(position).item as Task

        viewHolder.container.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation)

        viewHolder.taskProgress.setProgress(item.avance)
        viewHolder.tastDesc.setText(item.desc)
        viewHolder.taskCrea.setText(getDate(item.fechaCrea))
        viewHolder.taskFina.setText(getDate(item.fechaTer))
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
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.item_task_complete,parent,false)
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
    var container:RelativeLayout=itemView.findViewById(R.id.task_ct_container)
      val taskProgress:ProgressBar=itemView.findViewById(R.id.task_ct_progress)
    var tastDesc:TextView=itemView.findViewById(R.id.task_ct_desc)
      var taskCrea:TextView=itemView.findViewById(R.id.task_ct_crea)
      var taskFina:TextView=itemView.findViewById(R.id.task_ct_fina)

  }
  public class HeaderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var number:TextView=itemView.findViewById(R.id.total)
    var desc:TextView=itemView.findViewById(R.id.descrip)
  }
    class HeaderSecViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var titleSection:TextView=itemView.findViewById(R.id.titlesection)
    }
}
