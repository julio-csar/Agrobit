package com.agrobit.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrobit.R
import com.agrobit.classes.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class FindPartnerActivity : AppCompatActivity() {

    private lateinit var ref:DatabaseReference
    private lateinit var txtSearch:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:FirebaseRecyclerAdapter<User,PartnerViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_partner)

        ref = FirebaseDatabase.getInstance().getReference().child("users")

        recyclerView=findViewById(R.id.totalpartner)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this)
        txtSearch=findViewById(R.id.search_partner_et)
        txtSearch.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                firebaseUserSearch(p0.toString())
            }

        })

    }

    override fun onStart() {
        super.onStart()
        //adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        //adapter.stopListening()
    }

    fun firebaseUserSearch (searchText:String) {

        var firebaseSearchQuery = ref.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        //set Options
        var options = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(firebaseSearchQuery,User::class.java).build()

        adapter = object: FirebaseRecyclerAdapter<User, PartnerViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerViewHolder {
                val itemView = LayoutInflater.from(this@FindPartnerActivity).inflate(R.layout.item_find_partner,parent,false)
                return PartnerViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: PartnerViewHolder, position: Int, model: User) {
                holder.fill(model.base64,model.name,model.birthCountry)
            }
        }


        recyclerView.setAdapter(adapter);
        recyclerView.adapter?.notifyDataSetChanged()
        adapter.startListening()

    }

    public class PartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: RelativeLayout =itemView.findViewById(R.id.item_text)
        var imageContainer: CardView =itemView.findViewById(R.id.imageContainer)
        var itemImage: ImageView =itemView.findViewById(R.id.partner_image)
        var itemName: TextView =itemView.findViewById(R.id.partner_name)
        var itemCountry: TextView =itemView.findViewById(R.id.country)

        fun fill(image:String,name:String,country:String){
            if(!image.equals("")) {
                var imageBytes = Base64.decode(image, Base64.DEFAULT);
                 itemImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
            }
            itemName.setText(name)
            itemCountry.setText(country)
        }

    }
}
