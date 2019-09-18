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
import com.agrobit.classes.Item
import com.agrobit.classes.Orchard
import androidx.recyclerview.widget.RecyclerView.*
import com.agrobit.classes.HeaderPage
import com.agrobit.classes.Partner
import com.squareup.picasso.Picasso


public class PartnerAdapter(mContext:Context, data:List<Partner>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


  val mContext: Context=mContext
  val data:List<Partner> = data

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      var viewHolder:PartnerViewHolder=holder as PartnerViewHolder
      viewHolder.setIsRecyclable(false)
      var item:Partner=data.get(position)
      viewHolder.imageContainer.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation)
      viewHolder.container.animation=AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation)

      if(item.image!="")
          loadImageFromUrl(item.image,viewHolder.itemImage)
      //viewHolder.itemImage.setImageDrawable(ContextCompat.getDrawable(mContext,IMAGE_TYPE[item.type]!!))
      viewHolder.itemName.setText(item.name)
        viewHolder.itemLast.setText(getDate(item.lasta))
        if(item.tareasp==0)
            viewHolder.tareasp.visibility= View.GONE
        if(item.tareaspro==0)
            viewHolder.tareaspro.visibility= View.GONE
  }
    fun loadImageFromUrl(url:String,imageView:ImageView){
        Picasso.with(this.mContext).load(url).placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(imageView)

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
      val layout:View=LayoutInflater.from(mContext).inflate(R.layout.item_partner,parent,false)
      return PartnerViewHolder(layout)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  public class PartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container:RelativeLayout=itemView.findViewById(R.id.item_text)
    var imageContainer:CardView=itemView.findViewById(R.id.imageContainer)
      var itemImage:ImageView=itemView.findViewById(R.id.partner_image)
    var itemName:TextView=itemView.findViewById(R.id.partner_name)
    var itemLast:TextView=itemView.findViewById(R.id.partner_lasta)
      var tareasp:ImageView=itemView.findViewById(R.id.partner_tp)
      var tareaspro:ImageView=itemView.findViewById(R.id.partner_tpro)

  }
}
