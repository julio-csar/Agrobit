package com.agrobit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.agrobit.R
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.util.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Point
import androidx.core.content.ContextCompat
import android.location.Location
import android.widget.FrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlin.collections.ArrayList
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapadoo.alerter.Alerter


class NewOrchardActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnPolygonClickListener {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        points= ArrayList<LatLng>()

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_new_orchard)
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

        val bottomSheet:View=findViewById(R.id.bottomdesign)
        val bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {
            }

        })
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        mMap.setOnMapClickListener {
            points.add(it)
            repaintMap()
        }
/*
        frameLayout=findViewById(R.id.fram_map)
        frameLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                val x = p1!!.getX();
                val y = p1!!.getY();

                val x_co = Math.round(x);
                val y_co = Math.round(y);

                val projection = mMap.getProjection();
                val x_y_points = Point(x_co, y_co);

                val latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                val latitude = latLng.latitude;

                val longitude = latLng.longitude;

                val eventaction = p1.getAction();

                if(eventaction ==MotionEvent.ACTION_DOWN){
                    // finger touches the screen
                    points.add(LatLng(latitude, longitude))
                }else if(eventaction==MotionEvent.ACTION_UP){
                    drawArea(mMap)
                }
                return true;
            }
        })
*/
        /* Add polygons to indicate areas on the map.
        val polygon1 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-27.457, 153.040),
                    LatLng(-33.852, 151.211),
                    LatLng(-37.813, 144.962),
                    LatLng(-34.928, 138.599)
                )
        )*/
        /* Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.tag = "alpha"
        // Style the polygon.
        stylePolygon(polygon1)

        val polygon2 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-31.673, 128.892),
                    LatLng(-31.952, 115.857),
                    LatLng(-17.785, 122.258),
                    LatLng(-12.4258, 130.7932)
                )
        )
        polygon2.tag = "beta"
        stylePolygon(polygon2)*/

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))

        // Set listeners for click events.
        //googleMap.setOnPolylineClickListener(this)
        //googleMap.setOnPolygonClickListener(this)
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
}
