package com.agrobit.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.agrobit.R
import com.agrobit.fragments.OrchardsFragment
import com.agrobit.fragments.SettingsFragment
import com.agrobit.fragments.SupportFragment
import com.agrobit.fragments.TasksFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.jvm.internal.Intrinsics


class MainActivity : AppCompatActivity() {

    private lateinit var bottomN: BottomNavigationView

    private lateinit var orchardsFragment: OrchardsFragment
    private lateinit var tasksFragment: TasksFragment
    private lateinit var settingFragment: SettingsFragment
    private lateinit var supportFragment: SupportFragment




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        initMethods()
    }

    private fun initComponents() {
        bottomN = findViewById(R.id.bottom_navigation)

        orchardsFragment = OrchardsFragment.newInstance("", "")
        tasksFragment = TasksFragment.newInstance("", "")
        settingFragment = SettingsFragment.newInstance("", "")
        supportFragment = SupportFragment.newInstance("", "")


    }

    private fun initMethods() {
        bottomN.setOnNavigationItemSelectedListener { menuItem ->
            switchBottom(menuItem.itemId)
        }
    }

    private fun switchBottom(itemId: Int): Boolean {
        when (itemId) {
            R.id.navigation_orchards -> {
                //showFragment(TAG_FRAGMENT_ORCHARDS)
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
            R.id.navigation_settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, settingFragment, "settings")
                    .commit()
                selectItemTab(R.id.navigation_settings)
            }
            R.id.navigation_support->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, supportFragment, "support")
                    .commit()
                selectItemTab(R.id.navigation_support)
            }
        }
        return true
    }
    private final fun selectItemTab(i:Int) {
        val str:String = "linerToolbar";
        if (i == R.id.navigation_support) {
            val linearLayout:LinearLayout = findViewById(R.id.linerToolbar);
            Intrinsics.checkExpressionValueIsNotNull(linearLayout, str);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            val linearLayout2:LinearLayout = findViewById(com.agrobit.R.id.linerToolbar);
            Intrinsics.checkExpressionValueIsNotNull(linearLayout2, str);
            linearLayout2.setVisibility(View.INVISIBLE);
        }
    }
}

