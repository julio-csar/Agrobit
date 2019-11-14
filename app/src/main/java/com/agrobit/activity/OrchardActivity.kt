package com.agrobit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.agrobit.R
import com.agrobit.adapters.AnalisisAdapter
import com.agrobit.adapters.ViewPagerAdapter
import com.agrobit.classes.Analisis
import com.agrobit.classes.Orchard
import com.agrobit.fragments.HistoricFragment
import com.agrobit.fragments.MetricsFragment
import com.agrobit.fragments.TasksFragment
import com.agrobit.fragments.TasksTotal
import com.google.android.material.tabs.TabLayout

class OrchardActivity : AppCompatActivity() {

    private lateinit var title:TextView
    private lateinit var type:TextView
    private lateinit var size:TextView
    private lateinit var created:TextView
    private lateinit var image:ImageView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orchard)
        val bundle = intent.getBundleExtra("Bundle")
        initCompontens()
        initMethods()
        putData(bundle.getParcelable<Orchard>("orchard"))

        configureAdapter()


    }

    fun configureAdapter(){
        val pos=tabLayout.selectedTabPosition

        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(MetricsFragment(),"Metricas")
        adapter.addFragment(HistoricFragment(),"Histórico")

        adapter.addFragment(TasksTotal(),"Tareas")

        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(pos)?.select()
    }

    fun initCompontens(){
       title=findViewById(R.id.title_orchard)
        type=findViewById(R.id.orchard_type)
        size=findViewById(R.id.size)
        created=findViewById(R.id.created)
        image=findViewById(R.id.image_type)



        tabLayout=findViewById(R.id.tabs_one_orchard)
        viewPager=findViewById(R.id.viewPagerOneOrchard)
    }
    fun initMethods(){

    }

    fun putData(orchard: Orchard){
        title.setText(orchard.name)
        type.setText(ORCHARD_TYPE.get(orchard.type))
        size.setText(orchard.size + " has")
        created.setText(getDate(orchard.crea))
        image.setImageDrawable(ContextCompat.getDrawable(this,IMAGE_TYPE[orchard.type]!!))
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

    companion object {
        private val IMAGE_TYPE = hashMapOf(
            "avocado" to R.drawable.ic_avocado,
            "corn" to R.drawable.ic_corn
        )
        private val ORCHARD_TYPE = hashMapOf(
            "avocado" to "Aguacate",
            "corn" to "Maíz"
        )
    }
}