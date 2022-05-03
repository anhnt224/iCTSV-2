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
import com.bk.ctsv.databinding.FormRegisteredDetailFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.models.entity.Question
import com.bk.ctsv.ui.adapter.FormQuestionAdapter
import com.bk.ctsv.ui.viewmodels.form.FormRegisteredDetailViewModel
import javax.inject.Inject

class FormRegisteredDetailFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = FormRegisteredDetailFragment()
    }

    private lateinit var viewModel: FormRegisteredDetailViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var formQuestionAdapter: FormQuestionAdapter
    private lateinit var binding: FormRegisteredDetailFragmentBinding
    private var questions: List<Question> = listOf()
    private var form = Form()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.form_registered_detail_fragment, container, false)
        val form = FormRegisteredDetailFragmentArgs.fromBundle(requireArguments()).form
        formQuestionAdapter = FormQuestionAdapter(listOf(Question()))
        binding.form = form
        binding.apply {
            recyclerview.apply {
                adapter = formQuestionAdapter
                layoutManager = LinearLayoutManager(context)
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                viewModel.getFormDetail(form.rowId)
            }
        }
        viewModel.getFormDetail(form.rowId)
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(FormRegisteredDetailViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            form.observe(viewLifecycleOwner){
                binding.status = it.status
                if(checkResource(it) && it.data != null){
                    formQuestionAdapter.questions = it.data.questions
                    formQuestionAdapter.notifyDataSetChanged()
                    questions = it.data.questions
                    this@FormRegisteredDetailFragment.form = it.data
                    if (!it.data.isDone()){
                        setHasOptionsMenu(true)
                    }else{
                        setHasOptionsMenu(false)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_form_registered_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.edit -> {
                Navigation.findNavController(requireView()).navigate(FormRegisteredDetailFragmentDirections.actionFormRegisteredDetailFragmentToEditFormFragment(questions.toTypedArray(), form))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}