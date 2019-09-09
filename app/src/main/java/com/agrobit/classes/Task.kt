/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.agrobit.classes

import android.content.Context
import org.json.JSONException
import org.json.JSONObject


class Task(
    val id: String,
    val desc:String,
    val idHuerta:String,
    val nameHuerta: String,
    
    val type: Int,//0-Fertilizacion,1-Mantenimiento
    val fert:String,
    val dosis:String,
    val avance:Int,
    val status:Int,//0-Pendiente,1- En progreso,2-Terminada
    val statusAsig:Int,//0- Sin asignar, 1-Asignado sin confirmacion, 2-Asignado aceptado, 3-Asignado rechazado
    
    val idSocio:String,
    val nameSocio:String,
    
    val idSocioAs:String,
    val nameSocioAs:String,
    
    val fechaAsigna:String,
    val fechaAR:String,
    val fechaCrea:String,
    val fechaTer:String){

  companion object {
    fun getTasksFromFile(context: Context?,filename:String,array:String): ArrayList<Task> {
      var recipeList:List<Task>
        recipeList = ArrayList()

      try {
        // Load data
        val jsonString = context?.let { loadJsonFromAsset(filename, it) }
        val json = JSONObject(jsonString)
        val tasks = json.getJSONArray(array)

        // Get Recipe objects from data
        (0 until tasks.length()).mapTo(recipeList) {
          Task(
              tasks.getJSONObject(it).getString("id"),
              tasks.getJSONObject(it).getString("desc"),
              tasks.getJSONObject(it).getString("idHuerta"),
              tasks.getJSONObject(it).getString("nameHuerta"),
              tasks.getJSONObject(it).getInt("type"),
              tasks.getJSONObject(it).getString("ins"),
              tasks.getJSONObject(it).getString("dosis"),
              tasks.getJSONObject(it).getInt("avance"),
              tasks.getJSONObject(it).getInt("status"),
              tasks.getJSONObject(it).getInt("statusAsig"),
              tasks.getJSONObject(it).getString("idSocio"),
              tasks.getJSONObject(it).getString("nameSocio"),
              tasks.getJSONObject(it).getString("idSocioAs"),
              tasks.getJSONObject(it).getString("nameSocioAs"),
              tasks.getJSONObject(it).getString("fechaAsigna"),
              tasks.getJSONObject(it).getString("fechaAR"),
              tasks.getJSONObject(it).getString("fechaCrea"),
              tasks.getJSONObject(it).getString("fechaTer"))
        }
      } catch (e: JSONException) {
        e.printStackTrace()
      }
      return recipeList
    }

    private fun loadJsonFromAsset(filename: String, context: Context): String? {
      var json: String?

      try {
        val inputStream = context.assets.open(filename)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
      } catch (ex: java.io.IOException) {
        ex.printStackTrace()
        return null
      }

      return json
    }
  }
}