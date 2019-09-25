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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


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

        var adapter:ViewPagerAdapter= ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(OrchardsTotal(),"Todas")
        adapter.addFragment(OrchardsAtencion(),"Atención")
        adapter.addFragment(OrchardsUltimas(),"Últimas")

        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)

        vista.findViewById<FloatingActionButton>(R.id.new_orchard).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(vista.context, NewOrchardActivity::class.java))
            }

        })

        return vista
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
}
