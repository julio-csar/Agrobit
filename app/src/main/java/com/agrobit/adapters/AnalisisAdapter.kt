package com.agrobit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agrobit.R
import androidx.recyclerview.widget.RecyclerView.*
import com.agrobit.classes.*
import com.squareup.picasso.Picasso


public class AnalisisAdapter(mContext:Context, data:List<Analisis>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val IMAGE_TYPE = hashMapOf(
            1 to R.drawable.ic_complete,
            2 to R.drawable.ic_yellow_warning
        )
    }
  val mContext: Context=mContext
  val data:List<Analisis> = data

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      var viewHolder:PartnerViewHolder=holder as PartnerViewHolder
      viewHolder.setIsRecyclable(false)
      var item:Analisis=data.get(position)
      viewHolder.imageContainer.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation)
      viewHolder.container.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation)

      viewHolder.imageContainer.setImageDrawable(ContextCompat.getDrawable(mContext,IMAGE_TYPE[item.tipo]!!))


      viewHolder.itemFecha.setText(getDate(item.fecha))
      viewHolder.itemTec.setText(item.tecnico)
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
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.item_analisis_huerta,parent,false)
      return PartnerViewHolder(layout)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  public class PartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container:RelativeLayout=itemView.findViewById(R.id.item_text)
    var imageContainer:ImageView=itemView.findViewById(R.id.imageContainer)
    var itemFecha:TextView=itemView.findViewById(R.id.analisis_fecha)
    var itemTec:TextView=itemView.findViewById(R.id.analisis_tec)

  }
}
