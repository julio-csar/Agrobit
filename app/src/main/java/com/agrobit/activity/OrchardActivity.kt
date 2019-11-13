package com.agrobit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrobit.R
import com.agrobit.adapters.AnalisisAdapter
import com.agrobit.classes.Analisis

class OrchardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orchard)

        val analisisRecyclerView: RecyclerView =findViewById(R.id.orchard_analisis)
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

        val adapter = this.let { AnalisisAdapter(it, order) }
        analisisRecyclerView.adapter=adapter
        analisisRecyclerView.layoutManager= LinearLayoutManager(this)


    }
}