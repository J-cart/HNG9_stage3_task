package com.tutorial.hng9_stage3_task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.tutorial.hng9_stage3_task.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragHost = supportFragmentManager.findFragmentById(R.id.fragHost) as NavHostFragment
        navController = fragHost.findNavController()

        //setupActionBarWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

//lifecycleScope.launch {
//    // repeatOnLifecycle launches the block in a new coroutine every time the
//    // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
//    repeatOnLifecycle(Lifecycle.State.STARTED) {
//        // Trigger the flow and start listening for values.
//        // This happens when lifecycle is STARTED and stops
//        // collecting when the lifecycle is STOPPED
//
//    }
//}