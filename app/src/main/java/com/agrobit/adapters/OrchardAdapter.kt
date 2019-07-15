package com.agrobit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.agrobit.R
import com.agrobit.classes.Orchard


class OrchardAdapter(private val context: Context?,
                     private val dataSource: ArrayList<Orchard>) : BaseAdapter() {

  private val inflater: LayoutInflater
      = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

  companion object {
    private val IMAGE_TYPE = hashMapOf(
        "avocado" to R.drawable.ic_avocado,
        "corn" to R.drawable.ic_corn
    )
  }

  override fun getCount(): Int {
    return dataSource.size
  }

  override fun getItem(position: Int): Any {
    return dataSource[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view: View
    val holder: ViewHolder

    // 1
    if (convertView == null) {

      // 2
      view = inflater.inflate(R.layout.list_item, parent, false)

      // 3
      holder = ViewHolder()
      holder.itemImage = view.findViewById(R.id.item_image) as ImageView
      holder.itemTitle = view.findViewById(R.id.item_title) as TextView
      holder.itemSize = view.findViewById(R.id.item_size) as TextView

      // 4
      view.tag = holder
    } else {
      // 5
      view = convertView
      holder = convertView.tag as ViewHolder
    }

    // 6
    val itemTitle = holder.itemTitle
    val itemSize = holder.itemSize
    val itemImage = holder.itemImage

    val orchard = getItem(position) as Orchard

    itemTitle.text = orchard.name
    itemSize.text = orchard.size
      itemImage.setImageDrawable(ContextCompat.getDrawable(this.context!!, IMAGE_TYPE[orchard.type]!!))
    return view
  }

  private class ViewHolder {
    lateinit var itemTitle: TextView
    lateinit var itemSize: TextView
    lateinit var itemImage: ImageView
  }
}
