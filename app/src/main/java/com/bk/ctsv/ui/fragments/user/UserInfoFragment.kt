package com.bk.ctsv.ui.fragments.user


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bk.ctsv.MainActivity
import com.bk.ctsv.R
import com.bk.ctsv.common.LogoutCallback
import com.bk.ctsv.databinding.FragmentUserInfoBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.ui.viewmodels.user.UserInfoViewModel
import com.bk.ctsv.extension.logoutClick
import com.bk.ctsv.extension.showDialog
import com.bk.ctsv.utilities.AUTO
import com.bk.ctsv.utilities.autoCleared
import javax.inject.Inject


class UserInfoFragment : androidx.fragment.app.Fragment(), Injectable {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var viewModel: UserInfoViewModel
    private var binding by autoCleared<FragmentUserInfoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)

        setupToolbar()
        setupViewModel()
        subscribeUi()
        binding.apply {
            lifecycleOwner = this@UserInfoFragment
//            btnChangePassword.setOnClickListener {
//                Navigation.findNavController(it).
//                    navigate(UserInfoFragmentDirections.actionUserInfoFragmentToChangePasswordFragment())
//            }
//
//            btnLogOut.setOnClickListener {
//                this@UserInfoFragment.context?.showDialog("Bạn có muốn đăng xuất không?",null){
//                    logoutClick(sharedPrefsHelper)
//                }
//            }

        }

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserInfoViewModel::class.java)
        viewModel.setParameter(AUTO)
    }

    private fun subscribeUi() {
        with(viewModel){
            user.observe(viewLifecycleOwner, Observer { resource ->
               // binding.activitiesResource = resource
                resource.data?.let {
                    binding.user = it
                }
            })

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

        when (item.itemId) {
            android.R.id.home -> {
                activity!!.onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}
