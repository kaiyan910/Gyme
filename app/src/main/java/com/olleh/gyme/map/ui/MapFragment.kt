package com.olleh.gyme.map.ui

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.support.design.widget.TabLayout
import android.view.View
import com.architecture.kotlinmvvm.base.AppFragment
import com.architecture.kotlinmvvm.model.Gym
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.olleh.gyme.R
import com.olleh.gyme.databinding.FragmentMapBinding
import com.olleh.gyme.gym.ui.GymActivity
import com.olleh.gyme.main.ui.MapInfoWindow
import com.olleh.gyme.map.viewModel.MapViewModel
import com.olleh.gyme.utils.MapUtils
import javax.inject.Inject

class MapFragment : AppFragment<FragmentMapBinding, MapViewModel>(),
                    OnMapReadyCallback,
                    GoogleMap.OnMarkerClickListener,
                    GoogleMap.InfoWindowAdapter,
                    GoogleMap.OnInfoWindowClickListener {

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory

    private val DEFAULT = LatLng(22.296039, 114.172416)

    private lateinit var mGoogleMap: GoogleMap

    companion object {

        fun newInstance(): MapFragment = MapFragment()
    }

    override fun onMapReady(map: GoogleMap) {

        mGoogleMap = map

        mGoogleMap.apply {

            moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT, 12f))

            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false
            uiSettings.isMapToolbarEnabled = false

            setInfoWindowAdapter(this@MapFragment)
            setOnInfoWindowClickListener(this@MapFragment)
            setOnMarkerClickListener(this@MapFragment)
        }

        mViewModel.getGymLocation()
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        val cameraUpdate = CameraUpdateFactory
                .newLatLngZoom(
                        LatLng(marker.position.latitude, marker.position.longitude),
                        15F
                )

        mGoogleMap.animateCamera(cameraUpdate, 500, null)

        marker.showInfoWindow()

        return true
    }

    override fun getInfoContents(marker: Marker): View? {

        val room = marker.tag as Gym

        val window = MapInfoWindow(activity!!)
        window.bind(room)

        return window
    }

    override fun getInfoWindow(marker: Marker): View? = null

    override fun onInfoWindowClick(marker: Marker) {

        // do nothing when click on the info window
        val intent = Intent(context, GymActivity::class.java)
        intent.putExtra(GymActivity.EXTRA_GYM_ID, (marker.tag as Gym).id)

        startActivity(intent)
    }

    override fun layout(): Int = R.layout.fragment_map

    override fun viewModel(): Class<MapViewModel> = MapViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun afterViews() {

        super.afterViews()

        setupMap()

        mBindings.swipeRefreshLayout.isRefreshing = true
        mBindings.swipeRefreshLayout.isEnabled = false

        mBindings
                .tabLayout
                .addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                    override fun onTabReselected(p0: TabLayout.Tab?) {

                    }

                    override fun onTabUnselected(p0: TabLayout.Tab?) {

                    }

                    override fun onTabSelected(tab: TabLayout.Tab) {

                        mGoogleMap.clear()
                        mBindings.swipeRefreshLayout.isRefreshing = true

                        when (tab.text.toString()) {

                            getString(R.string.map_all) -> mViewModel.getGymLocation()
                            getString(R.string.map_hk) -> mViewModel.getGymLocationByArea("HK")
                            getString(R.string.map_kln) -> mViewModel.getGymLocationByArea("KLN")
                            getString(R.string.map_nt) -> mViewModel.getGymLocationByArea("NT")

                            else -> { }
                        }
                    }
                })
    }


    override fun observe() {

        super.observe()

        mViewModel
                .gymLocations
                .observe(this, resolve { data ->

                    mBindings.swipeRefreshLayout.isRefreshing = false
                    createMarker(data)
                })
    }

    private fun setupMap() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun createMarker(locations: List<Gym>) {

        locations.forEach { room ->

            val location = LatLng(room.latitude, room.longitude)
            val marker = mGoogleMap.addMarker(MarkerOptions()
                                                      .position(location)
                                                      .anchor(0.5F, 0.5F)
                                                      .icon(MapUtils.createIconFromVector(activity!!,
                                                                                          R.drawable.ic_gym)))
            marker.tag = room
        }
    }

}