package com.agrobit.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.agrobit.R
import com.agrobit.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.jvm.internal.Intrinsics




class MainActivity : AppCompatActivity() {

    private lateinit var bottomN: BottomNavigationView

    private lateinit var orchardsFragment: OrchardsFragment
    private lateinit var tasksFragment: TasksFragment
    private lateinit var moreFragment: MoreFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var  partnerFragment: PartnerFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        initMethods()
        bottomN.selectedItemId=R.id.navigation_start
    }
    private fun initComponents() {
        bottomN = findViewById(R.id.bottom_navigation)

        orchardsFragment = OrchardsFragment.newInstance("", "")
        tasksFragment = TasksFragment.newInstance("", "")
        partnerFragment = PartnerFragment.newInstance("", "")
        moreFragment = MoreFragment.newInstance("", "")
        homeFragment=HomeFragment.newInstance("","")
    }
    private fun initMethods() {
        bottomN.setOnNavigationItemSelectedListener { menuItem ->
            switchBottom(menuItem.itemId)
        }
    }

    override fun onBackPressed() {
        finish()
    }
    private fun switchBottom(itemId: Int): Boolean {
        when (itemId) {
            R.id.navigation_start -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, homeFragment, "home")
                    .commit()
                selectItemTab(R.id.navigation_start)
            }
            R.id.navigation_orchards -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, orchardsFragment, "orchards")
                    .commit()
                selectItemTab(R.id.navigation_orchards)
            }
            R.id.navigation_tasks -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, tasksFragment, "tasks")
                    .commit()
                selectItemTab(R.id.navigation_tasks)
            }
            R.id.navigation_partner->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, partnerFragment, "partner")
                    .commit()
                selectItemTab(R.id.navigation_partner)
            }
            R.id.navigation_more->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, moreFragment, "more")
                    .commit()
                selectItemTab(R.id.navigation_more)
            }
        }
        return true
    }
    private final fun selectItemTab(i:Int) {
        val str:String = "linerToolbar";
            val linearLayout2:LinearLayout = findViewById(com.agrobit.R.id.linerToolbar)
            Intrinsics.checkExpressionValueIsNotNull(linearLayout2, str);
            linearLayout2.setVisibility(View.INVISIBLE);
    }
}

