package com.olleh.gyme.gym.ui

import android.arch.lifecycle.ViewModelProvider
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import com.architecture.kotlinmvvm.base.AppToolbarActivity
import com.architecture.kotlinmvvm.model.Gym
import com.blankj.utilcode.util.BarUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.olleh.gyme.R
import com.olleh.gyme.databinding.ActivityGymBinding
import com.olleh.gyme.gym.viewModel.GymViewModel
import com.olleh.gyme.utils.MapUtils
import timber.log.Timber
import javax.inject.Inject


class GymActivity : AppToolbarActivity<ActivityGymBinding, GymViewModel>(),
                    OnMapReadyCallback {

    companion object {

        private val DEFAULT = LatLng(22.296039, 114.172416)
        const val EXTRA_GYM_ID = "extra_id"
    }

    private lateinit var mGoogleMap: GoogleMap

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory
    @Inject
    lateinit var mGymInfoAdapter: GymInfoAdapter
    @Inject
    lateinit var mGymEquipmentAdapter: GymEquipmentAdapter

    override fun layout(): Int = R.layout.activity_gym

    override fun viewModel(): Class<GymViewModel> = GymViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun afterViews() {

        super.afterViews()
        handleExtras()
        setupMap()
        setupRecyclerView()

        mBindings
                .tabLayout
                .addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                    override fun onTabReselected(p0: TabLayout.Tab?) {

                    }

                    override fun onTabUnselected(p0: TabLayout.Tab?) {

                    }

                    override fun onTabSelected(tab: TabLayout.Tab) {

                        when (tab.position) {
                            0 -> mBindings.recyclerView.adapter = mGymInfoAdapter
                            else -> mBindings.recyclerView.adapter = mGymEquipmentAdapter
                        }
                    }
                })
    }

    override fun title(): String = ""

    override fun toolbar(): Toolbar = mBindings.toolbar

    override fun onMapReady(map: GoogleMap) {

        mGoogleMap = map

        mGoogleMap.apply {

            isBuildingsEnabled = false
            isIndoorEnabled = false
            isTrafficEnabled = false
            uiSettings.isMapToolbarEnabled = false
        }

        mViewModel.getGym()
        mViewModel.getEquipments()
    }

    override fun observe() {

        super.observe()

        mViewModel
                .gym
                .observe(this, resolve {

                    val location = LatLng(it.latitude, it.longitude)

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
                    mGoogleMap.addMarker(MarkerOptions()
                                                 .position(location)
                                                 .anchor(0.5F, 0.5F)
                                                 .icon(MapUtils.createIconFromVector(this, R.drawable.ic_gym)))
                    mBindings.gym = it
                    mGymInfoAdapter.data = mutableListOf(it)

                    setupRecyclerView()
                    setupToolbarAgain(it)
                })

        mViewModel
                .equipments
                .observe(this, resolve {

                    mGymEquipmentAdapter.data = it.toMutableList()
                })
    }

    private fun handleExtras() {

        intent?.apply {

            mViewModel.gymId = extras?.getString(EXTRA_GYM_ID) ?: ""
        }
    }

    private fun setupMap() {

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val params = mBindings.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = AppBarLayout.Behavior()
        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
        params.behavior = behavior
    }

    private fun setupToolbarAgain(gym: Gym) {

        mBindings.toolbar.apply {

            setNavigationIcon(R.drawable.ic_back)
            title = gym.name()
            subtitle = gym.name["en"]
            (layoutParams as CollapsingToolbarLayout.LayoutParams).topMargin = BarUtils.getStatusBarHeight()
        }
    }

    private fun setupRecyclerView() {

        mBindings.recyclerView.layoutManager = LinearLayoutManager(this)
        mBindings.recyclerView.adapter = mGymInfoAdapter
    }
}