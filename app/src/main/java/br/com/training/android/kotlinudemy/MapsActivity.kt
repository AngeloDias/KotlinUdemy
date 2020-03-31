package br.com.training.android.kotlinudemy

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _accessLocation = 123
    private var locationNotAllowed = "Isn't possible to access your location. Please grant the app permission"
    private var location: Location? = null
    private var pockemons = ArrayList<Pockemon>()
    private var oldLocation: Location? = null
    private var playerPower = 0.0
    private val _catchingMessage = "You catch a new pockemon and your power is: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun checkPermission() {
        if(Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), _accessLocation)
        }
    }

    fun getUserLocation() {
        val myLocation = MyLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkPermission()

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        val myThread = MyThread()

        myThread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            _accessLocation -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, locationNotAllowed, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    fun loadPockemons() {
        pockemons.add(Pockemon("Charmander", "From the course", R.drawable.charmander, 70.0, 37.33, -122.0, false))
        pockemons.add(Pockemon("Bulbasaur", "From the course", R.drawable.bulbasaur, 97.0, 37.7949, -122.41049, false))
        pockemons.add(Pockemon("Charmander", "From the course", R.drawable.charmander, 37.0, 37.78166, -122.41225, false))
    }

    inner class MyLocationListener: LocationListener {

        constructor() {
            location = Location("Start")

            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(p0: Location?) {
            location = p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

        override fun onProviderEnabled(p0: String?) {}

        override fun onProviderDisabled(p0: String?) {}

    }

    inner class MyThread() : Thread() {
        init {
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }

        override fun run() {
            while (true) {

                try {
                    loadPockemons()

                    if(oldLocation!!.distanceTo(location) == 0f) {
                        continue
                    }

                    oldLocation = location

                    runOnUiThread {
                        mMap.clear()

                        val sydney = LatLng(location!!.latitude, location!!.longitude)

                        mMap.addMarker(
                            MarkerOptions().position(sydney).title("Marker in Sydney")
                                .snippet(" here is my location")
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                        for(i in 0 until pockemons.size) {
                            var newPockemon = pockemons[i]

                            if(newPockemon.isCatch == false) {
                                val pockemonLocation = LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(sydney)
                                        .title(newPockemon.name)
                                        .snippet(newPockemon.description + ", power: " + newPockemon.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!))
                                )

                                if(location!!.distanceTo(newPockemon.location) < 2) {
                                    newPockemon.isCatch = true
                                    pockemons[i] = newPockemon
                                    playerPower += newPockemon.power!!

                                    Toast.makeText(applicationContext, _catchingMessage + playerPower, Toast.LENGTH_LONG).show()
                                }

                            }
                        }

                    }

                    sleep(1000)
                } catch (ex: Exception) {}

            }
        }
    }

}
