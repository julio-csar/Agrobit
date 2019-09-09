package com.agrobit.fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.agrobit.R
import com.agrobit.adapters.TaskCompleteAdapter
import com.agrobit.classes.HeaderPage
import com.agrobit.classes.HeaderSection
import com.agrobit.classes.Item
import com.agrobit.classes.Task
import java.util.stream.Collectors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TasksComplete.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TasksComplete.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TasksComplete : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var vista:View

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista= inflater.inflate(R.layout.fragment_tasks_complete, container, false)


        val orchardRecyclerView: RecyclerView =vista.findViewById(R.id.completetasks_rv)
        orchardRecyclerView.setHasFixedSize(true)

        val tasksL= Task.getTasksFromFile(this.context,"tasks.json","tasks")
        val itemList=ArrayList<Item>()
        val temp=ArrayList<Task>()

        for(x in tasksL){
            if(x.avance==100)
                temp.add(x)
        }

        var tasksByOrchard=temp.stream().collect(Collectors.groupingBy(Task::nameHuerta)) as HashMap<String, List<Task>>
        for (x in tasksByOrchard){
            itemList.add(Item(Item.HEADER_SECTION, HeaderSection(x.key.toString())))
            for (y in x.value){
                itemList.add(Item(Item.ITEM,y))
            }
        }
        itemList.add(0,Item(Item.HEADER_PAGE, HeaderPage("Tareas terminadas", temp.size)))
        val adapter = this.context?.let { TaskCompleteAdapter(it, itemList) }
        orchardRecyclerView.adapter=adapter
        orchardRecyclerView.layoutManager= LinearLayoutManager(context)
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
         * @return A new instance of fragment TasksComplete.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TasksComplete().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
