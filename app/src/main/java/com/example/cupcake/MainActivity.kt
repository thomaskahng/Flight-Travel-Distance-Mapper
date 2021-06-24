package com.example.cupcake

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity() : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the action bar for use with the NavController
        setupActionBarWithNavController(navController)
    }

    //Handle navigation when the user chooses Up from the action bar.
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        //Change the label of the menu based on the state of the app
        val nightMode = AppCompatDelegate.getDefaultNightMode()

        //Set title to change to other mode based on current mode
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.action_night).setTitle(R.string.action_day)
                .setIcon(R.drawable.ic_day)
        }
        else {
            menu.findItem(R.id.action_night).setTitle(R.string.action_night)
                .setIcon(R.drawable.ic_night)
        }
        return true
    }

    //Action based on options menu selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_night) {
            //Get the night mode state of the app
            val nightMode = AppCompatDelegate.getDefaultNightMode()

            //Set the theme mode for the restarted activity
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            //Recreate the activity for the theme change to take effect
            recreate()
        }
        //Show the item selected on main menu
        return super.onOptionsItemSelected(item)
    }
}