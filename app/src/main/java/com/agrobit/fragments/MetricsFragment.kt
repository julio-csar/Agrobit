package com.agrobit.fragments

import android.content.Context
import android.graphics.Color
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

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
class MetricsFragment : Fragment(){


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
        vista= inflater.inflate(R.layout.metrics_fragment, container, false)
        var line=vista.findViewById<LineChart>(R.id.line)
        line.data=generateDataLine(0)

        var bar=vista.findViewById<BarChart>(R.id.bar)
        bar.data=generateDataBar(0)


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

    fun  generateDataLine(cnt:Int): LineData {

        var values1:ArrayList<Entry> =  ArrayList<Entry>();

        for (i in 0..11) {
            values1.add( Entry(i.toFloat(), ((Math.random() * 65) + 40).toFloat()));
        }

        var d1: LineDataSet =  LineDataSet(values1, "Humedad");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        var values2:ArrayList<Entry> = ArrayList<Entry>();

        for (i in 0..11) {
            values2.add( Entry(i.toFloat(), values1.get(i).getY() - 30));
        }

        var d2:LineDataSet =  LineDataSet(values2, "Temperatura");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        var sets:ArrayList<ILineDataSet> =  ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        return LineData(sets);
    }
    fun  generateDataBar(cnt:Int): BarData {

        var entries:ArrayList<BarEntry> =  ArrayList<BarEntry>();

        for (i in 0..12) {
            entries.add( BarEntry(i.toFloat(),  ((Math.random() * 7) + 3).toFloat()));
        }

        var d :BarDataSet=  BarDataSet(entries, "Alertas detectadas");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS.toList());
        d.setHighLightAlpha(255);

        var cd:BarData = BarData(d);
        cd.setBarWidth(0.9f);

        return cd;
    }
}
