package com.olleh.gyme.equipment.ui

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.architecture.kotlinmvvm.base.AppFragment
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.olleh.gyme.R
import com.olleh.gyme.databinding.FragmentEquipmentListBinding
import com.olleh.gyme.equipment.viewModel.EquipmentListViewModel
import com.olleh.gyme.main.ui.MainActivity
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.jvm.java

class EquipmentListFragment : AppFragment<FragmentEquipmentListBinding, EquipmentListViewModel>() {

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory
    @Inject
    lateinit var mEquipmentListAdapter: EquipmentListAdapter

    private val mCompositeDisposable = CompositeDisposable()

    companion object {

        fun newInstance(): EquipmentListFragment = EquipmentListFragment()
    }

    override fun viewModel(): Class<EquipmentListViewModel> = EquipmentListViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun layout(): Int = R.layout.fragment_equipment_list

    override fun afterViews() {

        super.afterViews()

        mBindings.count = 0

        mCompositeDisposable.add(

                RxTextView
                        .textChanges(mBindings.etSearch)
                        .throttleFirst(200, TimeUnit.MILLISECONDS)
                        .filter { !mBindings.refreshLayout.isRefreshing }
                        .subscribe {

                            Timber.d("<DEBUG> RxTextView=${it}")
                            mBindings.refreshLayout.isRefreshing = true

                            if (it.isEmpty()) {
                                mViewModel.getEquipments()
                            } else {
                                mViewModel.getEquipments(it.toString())
                            }
                        }
        )

        mCompositeDisposable.add(

                RxView
                        .clicks(mBindings.btnFilter)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {

                            if (mEquipmentListAdapter.selectedEquipment.size > 0) {

                                val intent = Intent(context, EquipmentSearchResultActivity::class.java)
                                intent.putExtra(EquipmentSearchResultActivity.EXTRA_EQUIPMENT_IDS,
                                                mEquipmentListAdapter.selectedEquipment.toTypedArray())
                                startActivity(intent)

                            } else {

                                Snackbar.make(mBindings.wrapper, R.string.equipment_must_have_one, Snackbar.LENGTH_SHORT).show()
                            }
                        }
        )

        mBindings
                .chipCount
                .setOnCloseIconClickListener {

                    mEquipmentListAdapter.reset()
                }

        setupRecyclerView()
    }

    override fun onDestroy() {

        super.onDestroy()
        mCompositeDisposable.clear()
    }

    override fun observe() {

        mViewModel
                .equipments
                .observe(viewLifecycleOwner, resolve {

                    mBindings.refreshLayout.isRefreshing = false
                    mBindings.refreshLayout.isEnabled = false

                    mEquipmentListAdapter.data = it.toMutableList()
                })
    }

    private fun setupRecyclerView() {

        mEquipmentListAdapter.onEquipmentCountChangeListener = {

            mBindings.count = it
        }

        mBindings.rvFacilities.layoutManager = LinearLayoutManager(activity)
        mBindings.rvFacilities.adapter = mEquipmentListAdapter
    }
}