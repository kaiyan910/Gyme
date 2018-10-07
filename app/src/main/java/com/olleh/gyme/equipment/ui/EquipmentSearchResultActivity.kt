package com.olleh.gyme.equipment.ui

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.support.design.chip.Chip
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.architecture.kotlinmvvm.base.AppToolbarActivity
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import com.olleh.gyme.R
import com.olleh.gyme.databinding.ActivityEquipmentSearchResultBinding
import com.olleh.gyme.equipment.viewModel.EquipmentSearchResultViewModel
import com.olleh.gyme.gym.ui.GymActivity
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class EquipmentSearchResultActivity : AppToolbarActivity<ActivityEquipmentSearchResultBinding, EquipmentSearchResultViewModel>() {

    companion object {

        const val EXTRA_EQUIPMENT_IDS = "extra_equipment_ids"
    }

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory
    @Inject
    lateinit var mListAdapter: EquipmentSearchResultGymListAdapter

    private val mCompositeDisposable = CompositeDisposable()
    private val mOnChipCloseListener = View.OnClickListener {

        if (mBindings.chipGroup.childCount > 1) {

            mBindings.chipGroup.removeView(it)
            mViewModel.removeEquipment((it.tag as Equipment).id)

        } else {

            Snackbar.make(mBindings.wrapper, R.string.search_gym_last_filter, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun title(): String = getString(R.string.search_gym_title)

    override fun toolbar(): Toolbar = mBindings.toolbar

    override fun layout(): Int = R.layout.activity_equipment_search_result

    override fun viewModel(): Class<EquipmentSearchResultViewModel> = EquipmentSearchResultViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun afterViews() {

        super.afterViews()
        setupRecyclerView()
        handleExtras()
    }

    override fun onDestroy() {

        super.onDestroy()
        mCompositeDisposable.clear()
    }

    override fun observe() {

        super.observe()

        mViewModel
                .equipmentList
                .observe(this, resolve { createChips(it) })

        mViewModel
                .gymList
                .observe(this, resolve {

                    mListAdapter.data = it.toMutableList()

                    mBindings
                            .recyclerView
                            .scheduleLayoutAnimation()
                })
    }

    private fun handleExtras() {

        intent?.apply {

            mViewModel.equipmentIds = extras?.getStringArray(EXTRA_EQUIPMENT_IDS)?.toMutableList() ?: mutableListOf()
            mViewModel.getEquipments()
            mViewModel.getGyms()
        }
    }

    private fun createChips(equipmentList: List<Equipment>) {

        mCompositeDisposable.add(

                Flowable.fromIterable(equipmentList)
                        .map {

                            val chip = Chip(this)
                            chip.text = it.nameZH
                            chip.closeIcon = getDrawable(R.drawable.ic_close)
                            chip.isCloseIconVisible = true
                            chip.setOnCloseIconClickListener(mOnChipCloseListener)
                            chip.tag = it

                            chip
                        }
                        .doOnSubscribe { mBindings.chipGroup.removeAllViews() }
                        .subscribe {

                            mBindings.chipGroup.addView(it)
                        }
        )
    }

    private fun setupRecyclerView() {

        mListAdapter.onItemClickListener = { gym, _ ->

            val intent = Intent(this, GymActivity::class.java)
            intent.putExtra(GymActivity.EXTRA_GYM_ID, gym.id)

            startActivity(intent)
        }

        mBindings.recyclerView.layoutManager = LinearLayoutManager(this)
        mBindings.recyclerView.adapter = mListAdapter
    }
}