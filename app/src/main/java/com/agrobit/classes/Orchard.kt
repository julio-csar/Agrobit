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

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader


@SuppressLint("ParcelCreator")
class Orchard(
    var id:String="",
    var name: String="",
    var size: String="",
    var base64: String="",
    var type: String="",
    var status:Int=5,
    var crea:String="",
    var lasta:String="",
    var lastproblems:String="0",
    var tareasp:Int=0,
    var tareaspro:Int=0,
    var tareasok:Int=0,
    var cords: MutableMap<String, Double>? = HashMap()): Parcelable {

    //For save orchard
    private lateinit var dbrOrchards: DatabaseReference
    private lateinit var dbrUserOrchards: DatabaseReference
    private lateinit var database: FirebaseDatabase

    constructor(parcel:Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
         parcel.readSerializable() as MutableMap<String, Double>?)

    fun save(uuid:String):Boolean{
        database= FirebaseDatabase.getInstance()
        dbrOrchards=database.reference.child("orchards")
        dbrUserOrchards=database.reference.child("user_orchards")

         id = dbrOrchards.push().key.toString()

        if (id == null) {
            return false
        }

        var ret = true

        dbrOrchards.child(id).setValue(this.toMap())
        dbrOrchards.child(id).setValue(this.toMap(), object :DatabaseReference.CompletionListener {
            override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                if(p0!=null)
                    ret =  false
            }
        })

        //Insertar en lista
        var l:MutableMap<String, String> = HashMap()
        l.put(id,name)
        dbrUserOrchards.child(uuid).child(id).setValue(name)

        return ret
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "size" to size,
            "base64" to base64,
            "type" to type,
            "status" to status,
            "crea" to crea,
            "lasta" to lasta,
            "lastproblems" to lastproblems,
            "tareasp" to tareasp,
            "tareaspro" to tareaspro,
            "tareasok" to tareasok,
            "cords" to cords
        )
    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(size)
        parcel.writeString(base64)
        parcel.writeString(type)
        parcel.writeInt(status)
        parcel.writeString(crea)
        parcel.writeString(lasta)
        parcel.writeString(lastproblems)
        parcel.writeInt(tareasp)
        parcel.writeInt(tareaspro)
        parcel.writeInt(tareasok)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR:Parcelable.Creator<Orchard>{
        override fun createFromParcel(p0: Parcel?): Orchard {
            return p0?.let { Orchard(it) }!!
        }

        override fun newArray(p0: Int): Array<Orchard?> {
            return arrayOfNulls(p0)
        }

        //For orchards
        fun getorchardsFromFile(context: Context?,filename:String,array:String): ArrayList<Orchard> {
            val recipeList = ArrayList<Orchard>()

            try {
                // Load data
                val jsonString = /*readFromFile(context!!,filename)*/context?.let { loadJsonFromAsset(filename, it) }
                if(jsonString==null)
                    return recipeList
                val json = JSONObject(jsonString)
                val orchards = json.getJSONArray(array)


                // Get Recipe objects from data
                (0 until orchards.length()).mapTo(recipeList) {
                    Orchard(orchards.getJSONObject(it).getString("id"),
                        orchards.getJSONObject(it).getString("name"),
                        orchards.getJSONObject(it).getString("size"),
                        orchards.getJSONObject(it).getString("base64"),
                        orchards.getJSONObject(it).getString("type"),
                        orchards.getJSONObject(it).getInt("status"),
                        orchards.getJSONObject(it).getString("crea"),
                        orchards.getJSONObject(it).getString("lasta"),
                        orchards.getJSONObject(it).getString("lastproblems"),
                        orchards.getJSONObject(it).getInt("tareasp"),
                        orchards.getJSONObject(it).getInt("tareaspro"),
                        orchards.getJSONObject(it).getInt("tareasok"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return recipeList
        }
        private fun loadJsonFromAsset(filename: String, context: Context): String? {
            var json: String?

            try {
                val inputStream = context?.openFileInput(filename);//context.assets.open("orchards.json")
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
        fun readFromFile(context: Context,filename: String):String {

            var ret:String = "";

            try {
                var inputStream = context?.openFileInput(filename);

                if ( inputStream != null ) {
                    var inputStreamReader =  InputStreamReader(inputStream);
                    var bufferedReader = BufferedReader(inputStreamReader);
                    var receiveString = "";
                    var stringBuilder = StringBuilder();

                    for(x in bufferedReader.readLine() ) {
                        stringBuilder.append(x);
                    }

                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            }
            catch (e: FileNotFoundException) {
                //Log.e("login activity", "File not found: " + e.toString());
            } catch (e: IOException) {
                //Log.e("login activity", "Can not read file: " + e.toString());
            }

            return ret;
        }

    }

}