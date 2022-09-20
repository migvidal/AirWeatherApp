package com.example.airweatherapp

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.example.airweatherapp.databinding.ActivityMainBinding
import com.example.airweatherapp.databinding.ActivitySearchBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
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
}