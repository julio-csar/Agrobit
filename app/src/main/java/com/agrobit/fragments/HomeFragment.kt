package com.agrobit.fragments

import android.content.Context
import android.graphics.ColorSpace
import android.net.Uri
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.agrobit.R
import com.agrobit.adapters.AdapterHome
import com.agrobit.adapters.AdapterTasks
import com.agrobit.classes.ModelHome

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {

    private lateinit var vista:View
    private lateinit var viewPager:ViewPager
    private lateinit var homeAdapter:AdapterHome
    private lateinit var models:MutableList<ModelHome>

    private lateinit var viewPagerTasks:ViewPager
    private lateinit var tasksAdapter:AdapterTasks

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        vista= inflater.inflate(R.layout.fragment_home, container, false)
        models=ArrayList<ModelHome>()
        models.add(ModelHome(R.drawable.ic_avocado,"Rancho San Gabriel","13 ha - Aguacate","Hoy, 07:07h",1))
        models.add(ModelHome(R.drawable.ic_corn,"Rosales","45.5 ha - Maíz","Ayer, 09:20h",4))
        models.add(ModelHome(R.drawable.ic_avocado,"Sauceda","10 ha - Aguacate","25/08, 07:07h",3))

        homeAdapter= AdapterHome(models,this.context)
        viewPager=vista.findViewById(R.id.viewPager)
        viewPager.adapter=homeAdapter
        viewPager.setPadding(20,0,20,0)

        tasksAdapter= AdapterTasks(null,this.context)
        viewPagerTasks=vista.findViewById(R.id.viewPagerTasks)
        viewPagerTasks.adapter=tasksAdapter
        viewPagerTasks.setPadding(20,0,20,0)


        return vista
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
