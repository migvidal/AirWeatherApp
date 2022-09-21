package com.example.airweatherapp.main_activity

import android.Manifest
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.example.airweatherapp.CommonViewModel
import com.example.airweatherapp.R
import com.example.airweatherapp.ResponseStatus.DONE
import com.example.airweatherapp.databinding.ActivityMainBinding
import com.example.airweatherapp.search.SearchActivity
import com.example.airweatherapp.weatherText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

const val PERMISSION_REQUEST_LOCATION = 1000

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * View binding
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * View model
     */
    private val viewModel: CommonViewModel by viewModels()

    /**
     * Location client
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        } catch (e: Exception) {
            Timber.e(e)
        }

        // layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        // Observe the viewModel
        viewModel.place.observe(this) { place ->
            binding.apply {
                tvLocation.text = weatherText(place)
                tvHeading.text = place.name
            }

        }

        // observe status
        viewModel.status.observe(this) { status ->
            binding.apply {
                when (status) {
                    DONE -> {
                        loadingScreen.loadingScreen.visibility = View.GONE
                        mainScreen.visibility = View.VISIBLE
                    }
                    else -> {
                        loadingScreen.loadingScreen.visibility = View.VISIBLE
                        mainScreen.visibility = View.GONE
                    }
                }
            }
        }

        // listeners
        binding.btnRefresh.setOnClickListener {
            showLocation()
        }

        // show location on launch
        showLocation()

    }

    private fun showLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            // location permission message
            val snackMessage =
                "Para que la app funcione debes activar la localización en Ajustes. (No sé usar los permisos)"
            Snackbar.make(
                this,
                binding.root,
                snackMessage,
                Snackbar.LENGTH_LONG
            ).show()

            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_LOCATION)
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_LOCATION
            )
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
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }
}