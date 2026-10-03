package com.example.petcare.ui.locations

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petcare.R
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.data.local.entities.PetLocation
import com.example.petcare.databinding.FragmentMapBinding
import com.example.petcare.ui.viewmodel.LocationViewModel
import com.example.petcare.ui.viewmodel.PetViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val locationViewModel: LocationViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private var locationList: List<PetLocation> = emptyList()
    private var petList: List<Pet> = emptyList()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableMyLocationOnMap()
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.msg_location_permission_explanation),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            com.google.android.gms.maps.MapsInitializer.initialize(requireContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        setupWebViewMap()

        binding.toggleMapMode.check(R.id.btn_mode_street)
        showNativeMap(isSatellite = false)

        binding.toggleMapMode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_mode_satellite -> showNativeMap(isSatellite = true)
                    R.id.btn_mode_street -> showNativeMap(isSatellite = false)
                }
            }
        }

        petViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petList = pets ?: emptyList()
            updateAllMapMarkers()
        }

        locationViewModel.allLocations.observe(viewLifecycleOwner) { locations ->
            locationList = locations ?: emptyList()
            updateAllMapMarkers()
        }
    }

    private fun updateAllMapMarkers() {
        val isSatellite = binding.toggleMapMode.checkedButtonId == R.id.btn_mode_satellite
        updateGoogleMapMarkers(locationList)
        updateWebMapMarkers(locationList, isSatellite)
    }

    private fun showNativeMap(isSatellite: Boolean = false) {
        binding.mapView.visibility = View.VISIBLE
        binding.webMapView.visibility = View.GONE
        googleMap?.mapType = if (isSatellite) GoogleMap.MAP_TYPE_HYBRID else GoogleMap.MAP_TYPE_NORMAL
        updateGoogleMapMarkers(locationList)
    }

    private fun setupWebViewMap() {
        binding.webMapView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            loadWithOverviewMode = true
            useWideViewPort = true
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            userAgentString = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 PetCareApp/1.0"
        }
        binding.webMapView.webViewClient = WebViewClient()
        updateWebMapMarkers(locationList, isSatellite = false)
    }

    private fun updateWebMapMarkers(locations: List<PetLocation>, isSatellite: Boolean = false) {
        val displayLocations = if (locations.isNotEmpty()) locations else listOf(
            PetLocation(1L, null, "Happy Paws Vet Clinic", "Veterinary Clinic", 51.5074, -0.1278, "123 High St, London", "24/7 Emergency Vet"),
            PetLocation(2L, null, "Greenwood Dog Park", "Dog Park", 51.5150, -0.1410, "Greenwood Ave, London", "Agility park & fenced run"),
            PetLocation(3L, null, "PetCare Supply Shop", "Pet Store", 51.4990, -0.1350, "King's Road, London", "Grooming & Supplies")
        )

        val centerLat = displayLocations.first().latitude
        val centerLng = displayLocations.first().longitude

        val tileUrl = if (isSatellite) {
            "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
        } else {
            "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}"
        }

        val markersListJson = StringBuilder("[")
        displayLocations.forEachIndexed { index, loc ->
            val escapedName = loc.name.replace("'", "\\'").replace("\"", "\\\"")
            val escapedType = loc.type.replace("'", "\\'").replace("\"", "\\\"")
            val escapedNotes = loc.notes.replace("'", "\\'").replace("\"", "\\\"")
            val escapedAddr = loc.address.replace("'", "\\'").replace("\"", "\\\"")

            val matchedPet = petList.find { it.petId == loc.petId }
            val petText = if (matchedPet != null) "${matchedPet.name} (${matchedPet.species} • ${matchedPet.breed})" else "General / All Pets"
            val escapedPet = petText.replace("'", "\\'").replace("\"", "\\\"")

            markersListJson.append("""
                {"id": ${loc.locationId}, "name": "$escapedName", "type": "$escapedType", "lat": ${loc.latitude}, "lng": ${loc.longitude}, "address": "$escapedAddr", "notes": "$escapedNotes", "pet": "$escapedPet"}${if (index < displayLocations.size - 1) "," else ""}
            """.trimIndent())
        }
        markersListJson.append("]")

        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" crossorigin="" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js" crossorigin=""></script>
                <style>
                    html, body, #map { width: 100%; height: 100%; margin: 0; padding: 0; background: #e5e7eb; font-family: -apple-system, Roboto, sans-serif; }
                    .info-card { background: white; padding: 12px 14px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); font-size: 13px; max-width: 240px; }
                    .info-card h4 { margin: 0 0 4px 0; color: #4f46e5; font-size: 15px; font-weight: bold; }
                    .info-card p { margin: 3px 0; color: #4b5563; }
                    .badge { display: inline-block; background: #e0e7ff; color: #4338ca; padding: 2px 8px; border-radius: 12px; font-size: 11px; font-weight: bold; margin-bottom: 6px; }
                    .pet-badge { display: inline-block; background: #fef3c7; color: #b45309; padding: 2px 8px; border-radius: 12px; font-size: 11px; font-weight: bold; margin-bottom: 6px; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var locations = $markersListJson;
                    var map = L.map('map').setView([$centerLat, $centerLng], 12);

                    L.tileLayer('$tileUrl', {
                        maxZoom: 19,
                        attribution: 'Tiles © Esri'
                    }).addTo(map);

                    var markersGroup = new L.FeatureGroup();

                    locations.forEach(function(loc) {
                        var popupContent = '<div class="info-card">' +
                            '<div><span class="badge">' + loc.type + '</span> <span class="pet-badge">🐾 ' + loc.pet + '</span></div>' +
                            '<h4>' + loc.name + '</h4>' +
                            '<p>📍 ' + loc.address + '</p>' +
                            '<p style="color:#6b7280; font-size:12px;">' + loc.notes + '</p>' +
                            '</div>';

                        var marker = L.marker([loc.lat, loc.lng]).bindPopup(popupContent);
                        markersGroup.addLayer(marker);
                    });

                    map.addLayer(markersGroup);

                    if (locations.length > 0) {
                        map.fitBounds(markersGroup.getBounds().pad(0.15));
                    }
                </script>
            </body>
            </html>
        """.trimIndent()

        binding.webMapView.loadDataWithBaseURL("https://openstreetmap.org", htmlContent, "text/html", "UTF-8", null)
    }

    private fun updateGoogleMapMarkers(locations: List<PetLocation>) {
        googleMap?.let { map ->
            map.clear()
            val displayLocations = if (locations.isNotEmpty()) locations else listOf(
                PetLocation(1L, null, "Happy Paws Vet Clinic", "Veterinary Clinic", 51.5074, -0.1278, "123 High St, London", "24/7 Emergency Vet"),
                PetLocation(2L, null, "Greenwood Dog Park", "Dog Park", 51.5150, -0.1410, "Greenwood Ave, London", "Agility park & fenced run"),
                PetLocation(3L, null, "PetCare Supply Shop", "Pet Store", 51.4990, -0.1350, "King's Road, London", "Grooming & Supplies")
            )

            val builder = LatLngBounds.Builder()
            displayLocations.forEach { loc ->
                val pos = LatLng(loc.latitude, loc.longitude)
                val matchedPet = petList.find { it.petId == loc.petId }
                val petName = matchedPet?.name ?: "All Pets"
                map.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title("${loc.name} (Pet: $petName)")
                        .snippet("${loc.type} • ${loc.address}")
                )
                builder.include(pos)
            }
            try {
                val bounds = builder.build()
                val padding = 120 // pixels
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                map.animateCamera(cu)
            } catch (e: Exception) {
                val firstLoc = LatLng(displayLocations.first().latitude, displayLocations.first().longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, 12f))
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkLocationPermission()
        updateGoogleMapMarkers(locationList)
    }

    private fun checkLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationOnMap()
        } else {
            locationPermissionLauncher.launch(permission)
        }
    }

    private fun enableMyLocationOnMap() {
        try {
            googleMap?.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.mapView?.onSaveInstanceState(outState)
    }
}
