package com.agrobit.fragments

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.ColorSpace
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.viewpager.widget.ViewPager

import com.agrobit.R
import com.agrobit.adapters.AdapterAnalisis
import com.agrobit.adapters.AdapterHome
import com.agrobit.adapters.AdapterTasks
import com.agrobit.classes.ModelAnalisis
import com.agrobit.classes.ModelHome
import com.agrobit.classes.Orchard
import com.nex3z.notificationbadge.NotificationBadge
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    //Para la lista de analisis
    private lateinit var viewPagerAnalisis:ViewPager
    private lateinit var analisisAdapter:AdapterAnalisis
    private lateinit var modelsAnalisis:MutableList<ModelAnalisis>

    private lateinit var viewPagerTasks:ViewPager
    private lateinit var tasksAdapter:AdapterTasks

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    //Badge farmer
    private lateinit var badgeF:NotificationBadge
    private lateinit var badgeN:NotificationBadge

    //Notification
    var CHANNEL_ID_ANDROID="com.agrobit.ANDROID"
    var CHANNEL_NAME="ANDROID_CHANNEL"

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

        val orchardsL= Orchard.getorchardsFromFile(this.context,"orchards.json","orchards")
        val models=ArrayList<ModelHome>()

        for (x in orchardsL){
            if(x.status<=2){
                var i=0
                var t=""
                if(x.type.equals("avocado")) {
                    i = R.drawable.ic_avocado
                    t = "Aguacate"
                }
                else
                {
                    i=R.drawable.ic_corn
                    t="Maíz"
                }
                models.add(ModelHome(i,x.name,x.size+" ha - "+t,getDate(x.lasta),x.status))
            }

        }
        if(models.size==0)
            models.add(ModelHome(R.drawable.ic_correct,"Sin pendientes","0 huertas requieren atención","Todo parece normal",5))
        //models.add(ModelHome(R.drawable.ic_corn,"Rosales","45.5 ha - Maíz","Ayer, 09:20h",4))
        //models.add(ModelHome(R.drawable.ic_avocado,"Sauceda","10 ha - Aguacate","25/08, 07:07h",3))

        homeAdapter= AdapterHome(models,this.context)
        viewPager=vista.findViewById(R.id.viewPager)
        viewPager.adapter=homeAdapter
        viewPager.setPadding(20,0,20,0)

        //Para la lista de analisis recientes
        modelsAnalisis=ArrayList<ModelAnalisis>()
        //modelsAnalisis.add(ModelAnalisis(R.drawable.ic_avocado,"Los camichimes","43","Roberto S","15/08/2019","16:00"))
        //modelsAnalisis.add(ModelAnalisis(R.drawable.ic_corn,"Zapotiltic","10","Pedro Mata","31/09/2019","09:00"))
        //modelsAnalisis.add(ModelAnalisis(R.drawable.ic_avocado,"Jiquilpan","34","Carla","12/10/2019","10:15"))
        val current= SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        val hour= SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
        modelsAnalisis.add(ModelAnalisis(R.drawable.ic_correct,"No hay análisis","0 mapeos próximos","Se te informarán cambios",current,hour))


        analisisAdapter= AdapterAnalisis(modelsAnalisis,this.context)
        viewPagerAnalisis=vista.findViewById(R.id.viewPagerAnalisis)
        viewPagerAnalisis.adapter=analisisAdapter
        viewPagerAnalisis.setPadding(20,0,20,0)

        tasksAdapter= AdapterTasks(null,this.context)
        viewPagerTasks=vista.findViewById(R.id.viewPagerTasks)
        viewPagerTasks.adapter=tasksAdapter
        viewPagerTasks.setPadding(20,0,20,0)

        //Badge farmer
        badgeF=vista.findViewById(R.id.badge1) as NotificationBadge
        badgeF.setNumber(3)

        //Badge notifications
        badgeN=vista.findViewById(R.id.badge2) as NotificationBadge
        badgeN.setNumber(9)

        vista.btnmashome.setOnClickListener{view ->
        notificacion(view)}
        return vista
    }

    fun getDate(valor:String): String {
        if(valor.length==1){
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

    @SuppressLint("WrongConstant")
    fun notificacion(view:View){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val imp=NotificationManager.IMPORTANCE_HIGH
            val mNotificationChannel=NotificationChannel(CHANNEL_ID_ANDROID,CHANNEL_NAME,imp)
            val notificationBuilder:Notification.Builder= Notification.Builder(this@HomeFragment.context,CHANNEL_ID_ANDROID)
                .setSmallIcon(R.drawable.ic_agrobit)
                .setContentTitle("¡Alguien te necesita!")
                .setContentText("Ricardo Perez te ha delegado una tarea")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManager:NotificationManager= this.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mNotificationChannel)
            notificationManager.notify(0,notificationBuilder.build())
        }else{
            var notificationBuilder2:NotificationCompat.Builder=NotificationCompat.Builder(this.context)
                .setSmallIcon(R.drawable.ic_agrobit)
                .setContentTitle("¡Alguien te necesita!")
                .setContentText("Ricardo Perez te ha delegado una tarea")
            val notificationManager:NotificationManager= this.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0,notificationBuilder2.build())
        }
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
