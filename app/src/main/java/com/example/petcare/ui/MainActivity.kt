package com.example.petcare.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.R
import com.example.petcare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle permission grant/deny if necessary
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val itemView = binding.bottomNavigation.findViewById<View>(item.itemId)
            itemView?.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.item_bounce))
            androidx.navigation.ui.NavigationUI.onNavDestinationSelected(item, navController)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment,
                R.id.loginFragment,
                R.id.registerFragment,
                -> {
                    binding.bottomNavigationCard.visibility = View.GONE
                }
                else -> {
                    if (binding.bottomNavigationCard.visibility != View.VISIBLE) {
                        binding.bottomNavigationCard.visibility = View.VISIBLE
                        binding.bottomNavigationCard.startAnimation(
                            android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
                        )
                    }
                }
            }
        }

        val navigateTo = intent.getIntExtra("EXTRA_NAVIGATE_TO", 0)
        if (navigateTo != 0) {
            navController.navigate(navigateTo)
        }
    }
}
