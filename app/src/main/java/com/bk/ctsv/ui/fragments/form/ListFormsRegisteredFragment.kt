package com.bk.ctsv.ui.fragments.form

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.ListFormsRegisteredFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.hideKeyboard
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.ui.adapter.FormRegisteredAdapter
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.OnItemLongClickListener
import com.bk.ctsv.ui.viewmodels.form.ListFormsRegisteredViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class ListFormsRegisteredFragment : Fragment(), Injectable, OnItemClickListener<Form>, OnItemLongClickListener<Form>, FormRegisteredAdapter.OnButtonItemClick {

    companion object {
        fun newInstance() = ListFormsRegisteredFragment()
    }

    private lateinit var viewModel: ListFormsRegisteredViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: ListFormsRegisteredFragmentBinding
    private lateinit var formRegisteredAdapter: FormRegisteredAdapter
    private lateinit var formSelected: Form
    private var rating: Int = 5
    private var comment = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.list_forms_registered_fragment, container, false)
        formRegisteredAdapter = FormRegisteredAdapter(arrayListOf(), this, this, this)
        binding.apply {
            recyclerview.apply {
                adapter = formRegisteredAdapter
                layoutManager = LinearLayoutManager(context)
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                viewModel.getListForm()
            }
            buttonCancelRating.setOnClickListener {
                ratingLayout.visibility = View.GONE
            }
            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (rating <= 1){
                    ratingBar.rating = 1f
                }
                if (rating <= 2){
                    textRatingLayout.visibility = View.VISIBLE
                }else{
                    textRatingLayout.visibility = View.GONE
                }
            }

            buttonRating.setOnClickListener {
                rating()
            }

            callback = object : RetryCallback {
                override fun retry() {
                    viewModel.getListForm()
                }
            }

            textRatingLayout.editText!!.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    //
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textRatingLayout.error = null
                }

            })
        }
        subscribeUI()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ListFormsRegisteredViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            getListForms.observe(viewLifecycleOwner){
                binding.resource = it
                if(checkResource(it) && it.data != null){
                    formRegisteredAdapter.forms = ArrayList(it.data)
                    formRegisteredAdapter.notifyDataSetChanged()
                }
            }

            deleteForm.observe(viewLifecycleOwner){
                if (checkResource(it)){
                    showToast("Xoá giấy tờ đang đăng kí thành công")
                    formRegisteredAdapter.forms.remove(formSelected)
                    formRegisteredAdapter.notifyDataSetChanged()
                }
            }

            rateForm.observe(viewLifecycleOwner){
                if(checkResource(it) && (buttonRateTapped.value == true)){
                    buttonRateTapped.value = false
                    showToast("Đánh giá giấy tờ thành công")
                    formSelected.rating = rating
                    formSelected.comment = comment
                    formRegisteredAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onClick(value: Form) {
        Navigation.findNavController(requireView()).navigate(ListFormsRegisteredFragmentDirections.actionListFormsRegisteredFragmentToFormRegisteredDetailFragment(value))
    }

    override fun onLongClick(value: Form) {
        formSelected = value
        if(!value.isDone()){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận xoá giấy tờ đăng kí")
                .setMessage("Bạn muốn xoá giấy tờ đang đăng kí: ${value.description}?")
                .setPositiveButton("Xoá"){_,_ ->
                    viewModel.deleteForm(value.rowId)
                }.setNegativeButton("Huỷ", null)
                .show()
        }
    }

    private fun showRatingView(form: Form){
        binding.apply {
            textTitle.text = form.description
            textRatingLayout.editText?.setText("")
            ratingLayout.visibility = View.VISIBLE
        }
    }

    override fun onButtonRatingClick(form: Form) {
        formSelected = form
        showRatingView(form)
    }

    private fun rating(){
        binding.apply {
            hideKeyboard()
            val rating = ratingBar.rating
            if (rating <= 2 && textRatingLayout.editText?.text.toString().isEmpty()){
                textRatingLayout.error = "Nhập nội dung đánh giá"
                return
            }
            this@ListFormsRegisteredFragment.rating = ratingBar.rating.toInt()
            viewModel.buttonRateTapped.value = true
            comment = textRatingLayout.editText!!.text.toString()
            viewModel.ratingForm(formSelected, this@ListFormsRegisteredFragment.rating, comment)
            ratingLayout.visibility = View.GONE
        }
    }

}