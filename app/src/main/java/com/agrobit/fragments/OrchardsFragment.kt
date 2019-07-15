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
import android.text.InputType
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var listView: ListView
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
class OrchardsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener
    {/*
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
            Toast.makeText(this.context,"Paso aca",Toast.LENGTH_LONG).show()
        return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
            Toast.makeText(this.context,"Paso aca",Toast.LENGTH_LONG).show()//To change body of created functions use File | Settings | File Templates.
            return true
        }*/

        override fun onRefresh() {
            Toast.makeText(this.context,"Paso aca",Toast.LENGTH_LONG).show()//To change body of created functions use File | Settings | File Templates.

        }

        // TODO: Rename and change types of parameters
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
        initComponents()

        return vista
    }
        private fun initComponents(){
            ( vista.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout).setOnRefreshListener(this)
            setHasOptionsMenu(true)
        }

    private fun initToolbar() {
        val toolbar = vista.findViewById(R.id.toolbar_orchards) as Toolbar
        Intrinsics.checkExpressionValueIsNotNull(toolbar, "toolbar_home")
        toolbar.setTitle("" as CharSequence)
        (vista.findViewById(R.id.toolbar_orchards) as Toolbar).setNavigationIcon(R.drawable.ic_appbar_settings as Int)
        var activity = activity
        if (activity !is AppCompatActivity) {
            activity = null
        }
        val appCompatActivity = activity as AppCompatActivity?
        appCompatActivity?.setSupportActionBar(vista.findViewById(R.id.toolbar_orchards) as Toolbar)
    }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
      inflater?.inflate(R.menu.menu_orchards,menu)

      val manger= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
      val searchItem=menu?.findItem(R.id.action_menu_search)
      val searchView=searchItem?.actionView as SearchView

      searchView.setSearchableInfo(manger.getSearchableInfo(activity!!.componentName))
      searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
          override fun onQueryTextSubmit(query: String?):Boolean{
              searchView.clearFocus()
              searchView.setQuery("",false)
              searchItem.collapseActionView()
              Toast.makeText(context,"Searching for $query",Toast.LENGTH_LONG).show()
              return true
          }
          override fun onQueryTextChange(newText: String?):Boolean {
              return true
          }
      })
      //if(search==null)
        //  Toast.makeText(this.context,"Nulo",Toast.LENGTH_LONG).show()
      //search!!.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
      //search!!.setOnQueryTextListener(this)
  }
        /*override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
            Intrinsics.checkParameterIsNotNull(menu, "menu");
            Intrinsics.checkParameterIsNotNull(inflater, "inflater");
            menu?.clear()
            inflater?.inflate(R.menu.menu_orchards, menu)
            val findItem = menu?.findItem(R.id.action_menu_search)

            val actionView: View? =findItem?.actionView
            Toast.makeText(this.context,"Paso aca",Toast.LENGTH_LONG).show()
            if (actionView != null) {
                val searchView = actionView as SearchView
                searchView.setOnQueryTextListener(this)
                searchView.setQueryHint(getString(R.string.label_hint_search_orchard_screen))
                val findViewById:View = searchView.findViewById(R.id.search_src_text)
                if (findViewById != null) {
                    val editText = findViewById as EditText
                    val activity = activity
                    if (activity == null) {
                        Intrinsics.throwNpe()
                    }
                    //Toast.makeText(this.context,"Paso aca1",Toast.LENGTH_LONG).show()
                    editText.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                    val activity2 = getActivity()
                    if (activity2 == null) {
                        Intrinsics.throwNpe()
                    }
                    editText.setHintTextColor(ContextCompat.getColor(activity2!!, R.color.white_alpha))
                    val a =expandListener(this,menu)
                    //searchView.setOnQueryTextFocusChangeListener(CreateOptionsMenu(this))
                    //findItem.setOnActionExpandListener(a)
                    val findItem2 = menu.findItem(R.id.action_menu_profile)
                    //Toast.makeText(this.context,"Paso aca3",Toast.LENGTH_LONG).show()
                    /*val signUpValues2 = this.signUpValues
                    if (signUpValues2 != null) {
                        val simpleName = javaClass.simpleName
                        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this.javaClass.simpleName")
                        str = signUpValues2!!.getProfilePic(simpleName)
                    }
                    val decode = Base64.decode(str, 0)
                    val decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.size)
                    if (decodeByteArray != null) {
                        val context = context
                        if (context == null) {
                            Intrinsics.throwNpe()
                        }
                        Intrinsics.checkExpressionValueIsNotNull(context!!, "context!!")
                        val bitmapDrawable = BitmapDrawable(
                            resources,
                            PackageUtilsKt.getCroppedBitmap(
                                decodeByteArray,
                                PackageUtilsKt.convertDpToPixel(24.0f, context) as Int
                            )
                        )
                        Intrinsics.checkExpressionValueIsNotNull(findItem2, "profileItem")
                        findItem2.setIcon(bitmapDrawable)
                        return
                    }*/
                    return
                }
                throw TypeCastException("null cannot be cast to non-null type android.widget.EditText")
            }
            throw TypeCastException("null cannot be cast to non-null type androidx.appcompat.widget.SearchView")
        }

        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            when(item?.itemId){

            }
            return true
        }
*/
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
class expandListener internal constructor(
    internal /* synthetic */ val orc: OrchardsFragment,
    internal /* synthetic */ val menu: Menu
) : OnActionExpandListener {

    override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
        Intrinsics.checkParameterIsNotNull(menuItem, "item")
        //val findItem = this.menu.findItem(R.id.action_menu_funding)
        //findItem.isVisible = true
        val findItem2 = this.menu.findItem(R.id.action_menu_profile)
        Intrinsics.checkExpressionValueIsNotNull(findItem2, "menu.findItem(R.id.action_menu_profile)")
        findItem2.isVisible = true
        //this.orc.appearBalance()
        return true
    }

    override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
        Intrinsics.checkParameterIsNotNull(menuItem, "item")
        //val findItem = this.menu.findItem(R.id.action_menu_funding)
        //Intrinsics.checkExpressionValueIsNotNull(findItem, "menu.findItem(R.id.action_menu_funding)")
        //findItem.isVisible = false
        val findItem2 = this.menu.findItem(R.id.action_menu_profile)
        Intrinsics.checkExpressionValueIsNotNull(findItem2, "menu.findItem(R.id.action_menu_profile)")
        findItem2.isVisible = false
        Toast.makeText(this.orc.context,"onMenuItemActionExpand",Toast.LENGTH_LONG).show()
        //ExtensionsKt.log("onMenuItemActionExpand")
        return true
    }
}

class CreateOptionsMenu internal constructor(
    internal var orc:OrchardsFragment
): View.OnFocusChangeListener {
    override fun onFocusChange(p0: View?, p1: Boolean) {
        Toast.makeText(this.orc.context,"Actualizado",Toast.LENGTH_LONG).show()
        //Aqui se hace lo necesario cuando se actualiza
    }

}

