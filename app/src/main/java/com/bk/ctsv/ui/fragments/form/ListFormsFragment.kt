package com.bk.ctsv.ui.fragments.form

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.ListFormsFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.models.entity.FormType
import com.bk.ctsv.ui.adapter.FormAdapter
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.viewmodels.form.ListFormsViewModel
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class ListFormsFragment : Fragment(), Injectable, OnItemClickListener<Form> {

    companion object {
        fun newInstance() = ListFormsFragment()
    }

    private lateinit var viewModel: ListFormsViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: ListFormsFragmentBinding
    private lateinit var formAdapter: FormAdapter
    private var forms: List<Form> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.list_forms_fragment, container, false)
        formAdapter = FormAdapter(listOf(), this)
        binding.recyclerview.apply {
            adapter = formAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.apply {
            tabLayout2.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        0 -> viewModel.setFilterType(FormType.FORM)
                        1 -> viewModel.setFilterType(FormType.SERVICE)
                        2 -> viewModel.setFilterType(FormType.UTILITY)
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getListForm()
                swipeRefreshLayout.isRefreshing = false
            }

            callback = object : RetryCallback {
                override fun retry() {
                    viewModel.getListForm()
                }
            }

        }
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ListFormsViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            getListForms.observe(viewLifecycleOwner){
                binding.resource = it
                if(checkResource(it) && it.data != null){
                    forms = it.data

                    if(binding.tabLayout2.getTabAt(0)!!.isSelected){
                        viewModel.setFilterType(FormType.FORM)
                    }
                    if(binding.tabLayout2.getTabAt(1)!!.isSelected){
                        viewModel.setFilterType(FormType.SERVICE)
                    }
                    if(binding.tabLayout2.getTabAt(2)!!.isSelected){
                        viewModel.setFilterType(FormType.UTILITY)
                    }
                }
            }
            getFilterType().observe(viewLifecycleOwner){
                formAdapter.forms = forms.filter {form ->
                    form.getType() == it
                }
                formAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(value: Form) {
        Navigation.findNavController(requireView()).navigate(ListFormsFragmentDirections.actionListFormsFragmentToFormDetailFragment(value))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_form, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.list -> {
                openListFormsRegistered()
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

    private fun openListFormsRegistered() {
        Navigation.findNavController(requireView()).navigate(ListFormsFragmentDirections.actionListFormsFragmentToListFormsRegisteredFragment())
    }

}