package com.bk.ctsv.teacher.fragment.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.bk.ctsv.MainActivity
import com.bk.ctsv.R
import com.bk.ctsv.databinding.TeacherInfoFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.teacher.viewmodel.account.TeacherInfoViewModel
import com.bk.ctsv.utilities.AUTO
import com.bk.ctsv.utilities.autoCleared
import javax.inject.Inject

class TeacherInfoFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var viewModel: TeacherInfoViewModel
    private var binding by autoCleared<TeacherInfoFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.teacher_info_fragment, container, false)

        setupToolbar()
        setupViewModel()
        subscribeUi()
        binding.apply {
            lifecycleOwner = this@TeacherInfoFragment
        }

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherInfoViewModel::class.java)
        viewModel.setParameter(AUTO)
    }

    private fun subscribeUi() {
        with(viewModel){
            user.observe(viewLifecycleOwner) { resource ->
                // binding.activitiesResource = resource
                resource.data?.let {
                    binding.user = it
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity){

        }
    }

    private fun setupToolbar() {
//        binding.apply {
//            (activity as AppCompatActivity).setSupportActionBar(toolbar)
//            setHasOptionsMenu(true)
//            (activity as AppCompatActivity).getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
//            (activity as AppCompatActivity).getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
//            toolbar.title = resources.getString(R.string.user_info_title)
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        // inflater.inflate(R.menu.menu_activity_list_by_user_unit, menu)
        //  this.menu = menu;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                activity!!.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}