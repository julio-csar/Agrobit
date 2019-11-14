package com.agrobit.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.agrobit.R
import com.agrobit.adapters.AnalisisAdapter
import com.agrobit.adapters.OrchardTotalAdapter
import com.agrobit.classes.Analisis
import com.agrobit.classes.HeaderPage
import com.agrobit.classes.Item
import com.agrobit.classes.Orchard

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OrchardsTotal.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OrchardsTotal.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HistoricFragment : Fragment(){


    private lateinit var vista:View
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_historic, container, false)


        val analisisRecyclerView: RecyclerView =vista.findViewById(R.id.orchard_analisis)
        analisisRecyclerView.isNestedScrollingEnabled=true
        analisisRecyclerView.setHasFixedSize(true)


        val models=ArrayList<Analisis>()
        models.add(Analisis("1","Andrade","Carlos Antonio","201901111520",2))
        models.add(Analisis("2","Andrade","Genaro Suarez","201902111520",1))
        models.add(Analisis("3","Andrade","Oscar Nuñez","201903111520",1))
        models.add(Analisis("4","Andrade","Pedro Dominguez","201904111520",2))
        models.add(Analisis("5","Andrade","Carlos Antonio","201905111520",1))
        models.add(Analisis("6","Andrade","Carlos Antono","201906111520",2))
        models.add(Analisis("7","Andrade","Carlos Antonio","201907111520",2))

        val order=models.sortedWith(compareBy({ it.fecha }))

        val adapter = this.context?.let { AnalisisAdapter(it, order) }
        analisisRecyclerView.adapter=adapter
        analisisRecyclerView.layoutManager= LinearLayoutManager(context)

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
         * @return A new instance of fragment OrchardsTotal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrchardsTotal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
