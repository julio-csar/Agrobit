package com.agrobit.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agrobit.R
import com.agrobit.classes.Item
import com.agrobit.classes.Orchard
import androidx.recyclerview.widget.RecyclerView.*
import com.agrobit.classes.Header
import java.time.LocalDateTime


public class OrchardTotalAdapter(mContext:Context, data:List<Item>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


  val mContext: Context=mContext
  val data:List<Item> = data

  companion object {
    private val IMAGE_TYPE = hashMapOf(
      "avocado" to R.drawable.ic_avocado,
      "corn" to R.drawable.ic_corn
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val i:Int=holder.itemViewType
    if(i==2){
      var viewHolder:OrchardViewHolder=holder as OrchardViewHolder
      viewHolder.setIsRecyclable(false)
      var item:Orchard=data.get(position).item as Orchard
      viewHolder.itemImage.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation)
      viewHolder.container.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation)

      viewHolder.itemImage.setImageDrawable(ContextCompat.getDrawable(mContext,IMAGE_TYPE[item.type]!!))
      viewHolder.itemTitle.setText(item.name)
      viewHolder.itemSize.setText(item.size+" has")
        viewHolder.lasta.setText(getDate(item.lasta))
        if(item.tareasp==0)
            viewHolder.tareasp.visibility= View.GONE
        if(item.tareaspro==0)
            viewHolder.tareaspro.visibility= View.GONE
    }
    else
    {
      var viewHolder:HeaderViewHolder=holder as HeaderViewHolder
      var item:Header= data.get(position).item as com.agrobit.classes.Header
      viewHolder.setIsRecyclable(false)
      viewHolder.number.setText(Integer.toString(item.total))
      viewHolder.desc.setText(item.name)
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
    if(viewType==1){
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.header_orchards,parent,false)
      return HeaderViewHolder(layout)
    }else{
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false)
      return OrchardViewHolder(layout)
    }

  }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun getItemViewType(position: Int): Int {
    return data.get(position).tipo
  }

  public class OrchardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container:RelativeLayout=itemView.findViewById(R.id.item_text)
    var itemImage:ImageView=itemView.findViewById(R.id.item_image)
    var itemTitle:TextView=itemView.findViewById(R.id.item_title)
    var itemSize:TextView=itemView.findViewById(R.id.item_size)
      var lasta:TextView=itemView.findViewById(R.id.lasta)
      var tareasp:ImageView=itemView.findViewById(R.id.tareasp)
      var tareaspro:ImageView=itemView.findViewById(R.id.tareaspro)

  }
  public class HeaderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var number:TextView=itemView.findViewById(R.id.total)
    var desc:TextView=itemView.findViewById(R.id.descrip)
  }
}
