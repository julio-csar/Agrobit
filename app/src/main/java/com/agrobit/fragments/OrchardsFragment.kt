package com.agrobit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.agrobit.R
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.agrobit.activity.NewOrchardActivity
import com.agrobit.adapters.ViewPagerAdapter
import com.agrobit.classes.Orchard
import com.agrobit.framework.shareddata.UserSharedPreference
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.io.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var listView: ListView
private lateinit var searchView: SearchView
private lateinit var item:MenuItem
private var search:SearchView?=null


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OrchardsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OrchardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OrchardsFragment : Fragment()
    {

        private var param1: String? = null
        private var param2: String? = null

        private lateinit var tabLayout:TabLayout
        private lateinit var vista:View
        private lateinit var viewPager:ViewPager

        private lateinit var refresh:ImageView
        private lateinit var rlProgressBar:RelativeLayout

        private lateinit var dbReference: DatabaseReference
        private lateinit var dbrUserO:DatabaseReference
        private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_orchards, container, false)
        tabLayout=vista.findViewById(R.id.tabs_o)
        viewPager=vista.findViewById(R.id.viewPagerOrchardsF)


        configureAdapter()



        rlProgressBar=vista.findViewById(R.id.rlProgressBar)

        vista.findViewById<FloatingActionButton>(R.id.new_orchard).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(vista.context, NewOrchardActivity::class.java))
            }

        })
        vista.findViewById<ImageView>(R.id.refresh_button).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                getList()
            }

        })

        return vista
    }

        fun configureAdapter(){
            val pos=tabLayout.selectedTabPosition

            var adapter:ViewPagerAdapter= ViewPagerAdapter(childFragmentManager)
            adapter.addFragment(OrchardsTotal(),"Todas")
            adapter.addFragment(OrchardsAtencion(),"Atención")
            adapter.addFragment(OrchardsUltimas(),"Últimas")

            viewPager.adapter=adapter
            tabLayout.setupWithViewPager(viewPager)

            tabLayout.getTabAt(pos)?.select()
        }
        fun getList(){
            showProgressDialog()

            database= FirebaseDatabase.getInstance()

            dbReference=database.reference.child("orchards")
            dbrUserO=database.reference.child("user_orchards").child(UserSharedPreference(this.context).user.uuid)

            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var l:MutableMap<String, String> = java.util.HashMap()
                    for (a in dataSnapshot.getChildren()) {
                        l.put(a.key.toString(), a.value.toString())
                    }
                    downloadOrchards(l)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            dbrUserO.addValueEventListener(userListener)
            deleteProgressDialog()
        }

        fun downloadOrchards(hm:MutableMap<String,String>){
            dbReference.addValueEventListener( object:ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val data:MutableList<Orchard> = ArrayList<Orchard>()
                    for(x in p0.children){
                        val a = x.getValue(Orchard::class.java)
                        if(hm.keys.contains(a?.id)){
                            a?.let { data.add(it) }
                        }
                    }
                    fillJSON(data)
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            });
        }
        fun fillJSON(data:MutableList<Orchard>){
            var json=JSONArray()
            for(x in data){
                json.put(addOrchard(x))
            }
            var array=JSONObject()
            array.put("orchards",json)
            writeToFile(array.toString())
        }

        private fun addOrchard(orchard: Orchard):JSONObject{
            return JSONObject()
                .put("id",orchard.id)
                .put("name",orchard.name)
                .put("size",orchard.size)
                .put("base64",orchard.base64)
                .put("type",orchard.type)
                .put("status",orchard.status)
                .put("crea",orchard.crea)
                .put("lasta",orchard.lasta)
                .put("lastproblems",orchard.lastproblems)
                .put("tareasp",orchard.tareasp)
                .put("tareaspro",orchard.tareaspro)
                .put("tareasok",orchard.tareasok)
                .put("cords",addCoord(orchard))
        }
        private fun addCoord(orchard: Orchard):JSONObject{
            var js=JSONObject()
            for(x in orchard.cords){
                js.put(x.key.toString(),x.value)
            }
            return js
        }

        fun writeToFile(data: String) {
            try {
                val outputStreamWriter =
                    OutputStreamWriter(context?.openFileOutput("orchards.json", Context.MODE_PRIVATE))
                outputStreamWriter.write(data)
                outputStreamWriter.close()
                configureAdapter()
            } catch (e: IOException) {
                //Log.e("Exception", "File write failed: " + e.toString())
            }

        }




        fun showProgressDialog(){
            rlProgressBar.visibility=View.VISIBLE
        }
        fun deleteProgressDialog(){
            rlProgressBar.visibility=View.GONE
        }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrchardsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrchardsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
        class ItemUO(
            var uuid:String="",
            var orchards: MutableMap<String,String> = HashMap()){



        }
}
