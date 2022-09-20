package com.example.airweatherapp.main_activity

import android.Manifest
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.example.airweatherapp.R
import com.example.airweatherapp.SearchActivity
import com.example.airweatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

const val PERMISSION_REQUEST_LOCATION = 1000

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * View binding
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * View model
     */
    private val viewModel: MainActivityViewModel by viewModels()

    /**
     * Location client
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Location
     */
    private lateinit var location: Location



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Listener for button
        binding.btnShowLocation.setOnClickListener {
            showLocation()
        }

        // Observe the viewModel
        viewModel.place.observe(this) { place ->
            binding.apply {
                val text = """Location: ${place.name}, ${place.sys.country}
                    |Temperature: ${place.main.temp}
                    |Min - max: ${place.main.tempMin} - ${place.main.tempMax}
                    |Weather: ${place.weather[0].main}
                    |Detail: ${place.weather[0].description}
                """.trimMargin()
                tvLocation.text = text
            }
        }

    }

    private fun showLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_LOCATION)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.getPlace(it.latitude, it.longitude)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // Search elements
        val searchItem = menu.findItem(R.id.action_search) //Search menu item
        val search: View = searchItem.actionView
        val searchView = search as SearchView //The associated embedded SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        // The class that will perform the search for this activity
        val componentName = ComponentName(this, SearchActivity::class.java)
        // Set the searchable info
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                getLocation()
            } else {
                // Permission request was denied.

            }
        }
    }
}