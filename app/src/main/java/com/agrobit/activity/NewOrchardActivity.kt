package com.agrobit.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.agrobit.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.util.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.location.Location
import android.os.Handler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlin.collections.ArrayList
import android.view.View
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.agrobit.adapters.NOSpinnerAdapter
import com.agrobit.classes.NOSpinnerItem
import com.agrobit.classes.Orchard
import com.agrobit.framework.shareddata.UserSharedPreference
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tapadoo.alerter.Alerter
import java.text.SimpleDateFormat


class NewOrchardActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnPolygonClickListener, AdapterView.OnItemSelectedListener,GoogleMap.OnMapClickListener
{
    override fun onMapClick(p0: LatLng?) {
        if (p0 != null) {
            points.add(p0)
        }
        repaintMap()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    private lateinit var frameLayout: FrameLayout


    private val COLOR_ORANGE_ARGB = -0x1f7cff
    private val COLOR_BLUE_ARGB = -0x701f7cff

    private val POLYGON_STROKE_WIDTH_PX = 8
    private val PATTERN_DASH_LENGTH_PX = 20
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT = Dot()
    private val DASH = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val GAP = Gap(PATTERN_GAP_LENGTH_PX.toFloat())



    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private val PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP)


    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private lateinit var mLastKnownLocation: Location

    private val TAG = NewOrchardActivity::class.java!!.getSimpleName()
    private lateinit var mMap: GoogleMap
    private lateinit var mCameraPosition: CameraPosition

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    private val DEFAULT_ZOOM = 18
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false

    // The entry point to the Fused Location Provider.
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var points:ArrayList<LatLng>

    private lateinit var bottomSheetBehavior:BottomSheetBehavior<View>

    private lateinit var lista:ArrayList<NOSpinnerItem>
    private lateinit var txtArea:EditText

    private lateinit var rlProgressBar:RelativeLayout

    //For save orchard
    //private lateinit var
    private lateinit var dbrOrchards: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private lateinit var btn_save_orchard:CircularProgressButton
    private lateinit var orchardName:EditText
    private lateinit var spinner:Spinner

    private var saved:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        points= ArrayList<LatLng>()

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_new_orchard)

        rlProgressBar=findViewById(R.id.rlProgressBar)

        findViewById<ImageView>(R.id.back).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }

        })
        findViewById<FloatingActionButton>(R.id.remove).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                if(points.size!=0)
                    points.removeAt(points.size-1)
                repaintMap()
            }

        })
        findViewById<FloatingActionButton>(R.id.clean).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                if(points.size!=0)
                    points.clear()
                repaintMap()
            }

        })

        configureBS()

        findViewById<FloatingActionButton>(R.id.next).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                if(points.size<4){
                    Alerter.create(this@NewOrchardActivity)
                        .setTitle("¡Algo anda mal!")
                        .setText("Selecciona un área de almenos 4 puntos")
                        .setIcon(R.drawable.ic_fail_white)
                        .setEnterAnimation(R.anim.abc_slide_in_top)
                        .setExitAnimation(R.anim.alerter_exit)
                        .setIconColorFilter(0) // Optional - Removes white tint
                        .setBackgroundColorRes(R.color.darkRed)
                        .show()
                }else{
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }else{
                        var ar:Double=calculateAreaOfGPSPolygonOnEarthInSquareMeters(points,6371000)/1000
                        txtArea.setText("%.2f".format(ar)+" has")
                        mMap.uiSettings.setAllGesturesEnabled(false)
                        mMap.setOnMapClickListener(null)
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            }

        })

        
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    fun calculateAreaOfGPSPolygonOnEarthInSquareMeters(locs:List<LatLng>,radius:Int):Double {
    return calculateAreaOfGPSPolygonOnSphereInSquareMeters(locs, radius);
  }

  fun calculateAreaOfGPSPolygonOnSphereInSquareMeters(locs:List<LatLng>, radius:Int):Double {
    if (locs.size < 3) {
      return 0.0;
    }

    val diameter:Double = (radius * 2.0);
    val circumference:Double = diameter * Math.PI;
    val listY:MutableList<Double>  = ArrayList<Double>();
    val listX:MutableList<Double>  = ArrayList<Double>();
    val listArea:MutableList<Double>  = ArrayList<Double>();
    // calculate segment x and y in degrees for each point
    val latitudeRef:Double = locs.get(0).latitude
    val longitudeRef:Double = locs.get(0).longitude
    for (x in locs) {
      val latitude:Double= x.latitude
      val longitude:Double = x.longitude
        listY.add(calculateYSegment(latitudeRef, latitude, circumference));
      //Log.d(LOG_TAG, String.format("Y %s: %s", listY.size() - 1, listY.get(listY.size() - 1)));
      listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference));
      //Log.d(LOG_TAG, String.format("X %s: %s", listX.size() - 1, listX.get(listX.size() - 1)));
    }

    // calculate areas for each triangle segment
    for (i in 1..(listX.size-1)) {
      val x1:Double = listX.get(i - 1);
      val y1:Double = listY.get(i - 1);
      val x2:Double = listX.get(i);
      val y2:Double= listY.get(i);
      listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2));
      //Log.d(LOG_TAG, String.format("area %s: %s", listArea.size() - 1, listArea.get(listArea.size() - 1)));
    }

    // sum areas of all triangle segments
    var areasSum:Double = 0.0;
    for (area in listArea) {
      areasSum = areasSum + area;
    }

    // get abolute value of area, it can't be negative
    return Math.abs(areasSum);// Math.sqrt(areasSum * areasSum);
  }

  fun calculateAreaInSquareMeters(x1:Double,x2:Double,y1:Double, y2:Double):Double {
    return (y1 * x2 - x1 * y2) / 2;
  }

  fun calculateYSegment(latitudeRef:Double,latitude:Double,circumference:Double):Double {
    return (latitude - latitudeRef) * circumference / 360.0;
  }

  fun calculateXSegment(longitudeRef:Double, longitude:Double, latitude:Double,
      circumference:Double) :Double{
    return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0;
  }

    fun configureBS(){
        //Configure BottonSheet
        val bottomSheet:View=findViewById(R.id.bottomdesign)
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {

            }
            override fun onStateChanged(p0: View, p1: Int) {
                if(bottomSheetBehavior.state==BottomSheetBehavior.STATE_HIDDEN){
                    mMap.uiSettings.setAllGesturesEnabled(true)
                    mMap.setOnMapClickListener(this@NewOrchardActivity)
                }else{
                    mMap.uiSettings.setAllGesturesEnabled(false)
                    mMap.setOnMapClickListener(null)
                }
            }

        })
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Configure spinner
        spinner = bottomSheet.findViewById(R.id.no_spinner)
        lista=getItemsSpinner()
        val adapter=NOSpinnerAdapter(this,lista)
        if(spinner!=null){
            spinner.adapter=adapter
            spinner.setOnItemSelectedListener(this)
        }

        txtArea=bottomSheet.findViewById<EditText>(R.id.areaOrchard)
        //Configure button
        orchardName=bottomSheet.findViewById<EditText>(R.id.orchard_name)

        //Declare the button for save data
        btn_save_orchard = bottomSheet.findViewById(R.id.btn_save_orchard)
        btn_save_orchard.run { setOnClickListener { morphDoneAndRevert(this@NewOrchardActivity) }}

    }

    private fun ProgressButton.morphDoneAndRevert(
        context: Context,
        fillColorSuccess: Int = defaultColor(context),
        fillColorError: Int = errorColor(context),
        bitmap: Bitmap = successDoneImage(context.resources),
        bitmapError: Bitmap = errorDoneImage(context.resources),
        doneTime: Long = 3000,
        revertTime: Long = 4000,
        finishTime: Long = 5000
    ) {
        progressType = ProgressType.INDETERMINATE
        startAnimation()
        Handler().run {
            postDelayed({
                saved=saveOrchard()
                if(saved)
                    doneLoadingAnimation(fillColorSuccess, bitmap)
                else
                    doneLoadingAnimation(fillColorError,bitmapError)
            }, doneTime)
            postDelayed({
                btn_save_orchard.revertAnimation()

                btn_save_orchard.setTextColor(Color.parseColor("#FFFFFF"))
                btn_save_orchard.setBackgroundResource(R.drawable.button_blue_round)


                //EnableSaveItemButton()
            }, revertTime)
            postDelayed({
                if(saved){
                    //bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
                    finish()
                }
            },finishTime)
        }
    }

    fun saveOrchard():Boolean{
        if(orchardName.text.toString().equals("")){
            orchardName.setError("Requerido")
            return false
        }else{
            orchardName.setError(null)

            val current=SimpleDateFormat("yyyyMMddHHmm").format(Calendar.getInstance().time)

            var tp:String
            if(spinner.selectedItemPosition==0)
                tp="avocado"
            else
                tp="corn"

            var l:MutableMap<String, Double> = HashMap()
            for(x in 0..points.size-1){
                l.put("la"+x,points[x].latitude)
                l.put("lo"+x,points[x].longitude)
            }

            var or = Orchard("",orchardName.text.toString(),txtArea.text.toString().removeSuffix(" has"),"",tp,5, current,
                "0","",0,0,0,l)
            return or.save(UserSharedPreference(this).getUser().uuid)
        }
    }

    private fun defaultColor(context: Context) = ContextCompat.getColor(context, R.color.darkGreen)
    private fun errorColor(context: Context) = ContextCompat.getColor(context, R.color.darkRed)
    private fun successDoneImage(resources: Resources) =getResources().getDrawable(R.drawable.ic_check).toBitmap()
    private fun errorDoneImage(resources: Resources) =getResources().getDrawable(R.drawable.ic_fail_white).toBitmap()

    private fun getItemsSpinner(): ArrayList<NOSpinnerItem> {
        lista= ArrayList()
        lista.add(NOSpinnerItem("Aguacate",R.drawable.ic_avocado))
        lista.add(NOSpinnerItem("Maíz",R.drawable.ic_corn))
        return lista
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }

    fun repaintMap(){
        mMap.clear()
        if(points.size>=3){
            val p=mMap.addPolygon(
            PolygonOptions()
                .addAll(points)

            )
            stylePolygon(p)
        }
    }
    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap=googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID;


        mMap.setOnMapClickListener(this)

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();



    }

    fun drawArea(googleMap: GoogleMap){
        val polygon = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .addAll(points)
        )
        polygon.tag = "beta"
        stylePolygon(polygon)
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient.getLastLocation()
                locationResult.addOnCompleteListener(this, object : OnCompleteListener<Location> {
                    override fun onComplete(task: Task<Location>) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult()!!
                            mMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        mLastKnownLocation.latitude,
                                        mLastKnownLocation.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        } else {
                            //Log.d(TAG, "Current location is null. Using defaults.")
                            //Log.e(TAG, "Exception: %s", task.getException())
                            mMap!!.moveCamera(
                                CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM.toFloat())
                            )
                            mMap.getUiSettings().isMyLocationButtonEnabled = false
                        }
                    }
                })
            }
        } catch (e: SecurityException) {
            //Log.e("Exception: %s", e.message)
        }

    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                //mLastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
           // Log.e("Exception: %s", e.message)
        }

    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private fun stylePolygon(polygon: Polygon) {
               // Apply a stroke pattern to render a line of dots and dashes, and define colors.
               var pattern = PATTERN_POLYGON_BETA
                var strokeColor = COLOR_ORANGE_ARGB
                var fillColor = COLOR_BLUE_ARGB

        polygon.setStrokePattern(pattern)
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX.toFloat())
        polygon.setStrokeColor(strokeColor)
        polygon.setFillColor(fillColor)
    }


    /**
     * Listens for clicks on a polygon.
     * @param polygon The polygon object that the user has clicked.
     */
    override fun onPolygonClick(polygon: Polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        var color = polygon.getStrokeColor() xor 0x00ffffff
        polygon.setStrokeColor(color)
        color = polygon.getFillColor() xor 0x00ffffff
        polygon.setFillColor(color)

        Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show()
    }
    private fun getLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior!=null && bottomSheetBehavior.getState() !=
            BottomSheetBehavior.STATE_HIDDEN) {
            mMap.uiSettings.setAllGesturesEnabled(true)
            mMap.setOnMapClickListener (this)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed()
        return
    }

}
