package com.agrobit.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ListView

import com.agrobit.R
import com.agrobit.adapters.OrchardAdapter
import com.agrobit.classes.Orchard
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import kotlin.jvm.internal.Intrinsics
import android.view.MenuItem.OnActionExpandListener
import androidx.core.content.ContextCompat
import android.widget.EditText
import android.R.string
import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.text.InputType
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.agrobit.account.ProfileActivity


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
class OrchardsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,SearchView.OnCloseListener
    {
        override fun onClose(): Boolean {
            searchView.clearFocus()
            searchView.setQuery("",false)
            item.collapseActionView()
            return false
        }

        override fun onRefresh() {
            Toast.makeText(this.context,"Paso aca",Toast.LENGTH_LONG).show()//To change body of created functions use File | Settings | File Templates.

        }


    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var vista:View

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

        listView = vista.findViewById<ListView>(R.id.orchard_list)
        val orchardList = Orchard.getorchardsFromFile(this.context)
        val adapter = OrchardAdapter(this.context, orchardList)
        listView.adapter = adapter
        initToolbar()

        (vista.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout).setOnRefreshListener(this)
        this.setHasOptionsMenu(true)


        return vista
    }


    private fun initToolbar() {
        val toolbar = vista.findViewById(R.id.toolbar_orchards) as Toolbar
        Intrinsics.checkExpressionValueIsNotNull(toolbar, "toolbar_home")
        toolbar.setTitle("Huertas" as CharSequence)
        var activity = activity
        if (activity !is AppCompatActivity) {
            activity = null
        }
        val appCompatActivity = activity as AppCompatActivity?
        appCompatActivity?.setSupportActionBar(vista.findViewById(R.id.toolbar_orchards) as Toolbar)
    }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
      inflater?.inflate(R.menu.menu_orchards,menu)
      super.onCreateOptionsMenu(menu, inflater)

      val manger= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
      item = menu?.findItem(R.id.action_menu_search)!!
      searchView=item?.actionView as SearchView



      searchView.setOnQueryTextFocusChangeListener{view,hasFocus->
      if(hasFocus){
          searchView.showKeyboard()
      }else{
          searchView.hideKeyboard()
      }}
      item.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
          override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
              searchView.isIconified=false
              searchView.requestFocusFromTouch()
              return true
          }

          override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
              searchView.setQuery("",true)
              return true
          }
      })

      searchView.setOnCloseListener(this)
      searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
          override fun onQueryTextSubmit(query:String?):Boolean{
              searchView.clearFocus()
              searchView.setQuery("",false)
              item.collapseActionView()
              Toast.makeText(context,"Loking fo $query",Toast.LENGTH_LONG).show()
              return true
          }

          override fun onQueryTextChange(newText: String?): Boolean {
              return true
          }
      })
  }
        fun View.showKeyboard(){
            val im =context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        fun View.hideKeyboard(){
            val imm=context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken,0)
        }
        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            return true
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
