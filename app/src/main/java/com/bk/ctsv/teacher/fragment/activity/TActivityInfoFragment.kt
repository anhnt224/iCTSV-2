package com.bk.ctsv.teacher.fragment.activity

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bk.ctsv.MainActivity
import com.bk.ctsv.R
import com.bk.ctsv.common.LogoutCallback
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.TActivityInfoFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.extension.handleTokenInvalid
import com.bk.ctsv.extension.logoutClick
import com.bk.ctsv.extension.showEditextDialog
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.entity.Criteria
import com.bk.ctsv.models.entity.UserActivity
import com.bk.ctsv.teacher.viewmodel.activity.TActivityInfoViewModel
import com.bk.ctsv.ui.adapter.activity.CriteriaByActivityAdapter
import com.bk.ctsv.ui.fragments.activity.ActivityDetailByUserUnitFragmentArgs
import com.bk.ctsv.ui.fragments.activity.ActivityDetailByUserUnitFragmentDirections
import com.bk.ctsv.utilities.InjectorUtils
import com.bk.ctsv.utilities.autoCleared
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class TActivityInfoFragment : androidx.fragment.app.Fragment() , Injectable {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var sharedPrefsHelper: SharedPrefsHelper
    private var binding by autoCleared<TActivityInfoFragmentBinding>()
    private lateinit var mViewmodel: TActivityInfoViewModel
    private var aId = 0
    private var mActivity = Activity()
    private var mCriterias: List<Criteria> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aId = ActivityDetailByUserUnitFragmentArgs.fromBundle(arguments!!).aId
        binding = DataBindingUtil.inflate(
            inflater, R.layout.t_activity_info_fragment, container, false)
        setupViewModel()

        binding.apply {

            lifecycleOwner = this@TActivityInfoFragment
            viewmodel = mViewmodel
            subscribeUi()

            txtADesc.movementMethod = LinkMovementMethod.getInstance()

            binding.activityStatic = UserActivity.Companion

//            btnAssignUserActivity.setOnClickListener {
//                this@TActivityInfoFragment.context?.showEditextDialog("Lý do đăng kí: ",null,
//                    positive = {text -> mViewmodel.assignUserActivity(text,aId,
//                        InjectorUtils.UserRoleUntils.USER_ROLE_MEMBER,"",1,"")})
//            }
//
//            btnApproveActivity.setOnClickListener { showApproveActivityDialog() }
//
//            txtCriteriaText.setOnClickListener {
//                setupCriteria(mCriterias)
//            }
//
//            relativeUser.setOnClickListener {
////                Navigation.findNavController(it).
////                    navigate(ActivityDetailByUserUnitFragmentDirections.
////                        actionActivityDetailByUserUnitFragmentToUserListByActivityFragment(aId,mActivity!!.aGId!!,mActivity!!.userRole!!))
//            }
//
//            btnNextToUserActivityInfo.setOnClickListener {
//                Navigation.findNavController(it).
//                navigate(
//                    ActivityDetailByUserUnitFragmentDirections.
//                actionActivityDetailByUserUnitFragmentToUserCheckInActivityInfoFragment(aId,sharedPrefsHelper.getUserName(),mActivity!!.aGId!!,mActivity!!.name!!))
//            }

            retryCallback = object : RetryCallback {
                override fun retry() {
                    mViewmodel.retry()
                }
            }

            logoutCallback = object : LogoutCallback {
                override fun logout() {
                    logoutClick(sharedPrefsHelper)
                }
            }



        }
        return binding.root
    }

    private fun setupViewModel() {
        mViewmodel = ViewModelProviders.of(this, viewModelFactory).get(TActivityInfoViewModel::class.java)
        mViewmodel.setId(aId)
    }

    private fun subscribeUi() {
        with(mViewmodel){
            activity.observe(viewLifecycleOwner, Observer { resource ->
                binding.activityResource = resource
                if (resource.data != null){
                    when(resource.status){
                        Status.SUCCESS_NETWORK -> {
                            binding.mActivity = resource.data
                            binding.userActivity = UserActivity(uaStatus = resource.data.uAStatus)
                        }

                        Status.ERROR -> {
                            showToast(resource.respText.toString())
                        }
                        Status.ERROR_TOKEN -> {
                            handleTokenInvalid()
                        }
                        else -> {}
                    }
                }
            })

            criterias.observe(viewLifecycleOwner, Observer { criteriaList ->
                mCriterias = criteriaList
            })

            assignUserActivityResource.observe(viewLifecycleOwner, Observer { resource ->
                binding.assignActivityResource = resource
                if (resource.data != null && isAssignUserActivity){
                    if (resource.status == Status.SUCCESS_NETWORK){
                        showToast("Đăng kí thành công")
                        isAssignUserActivity = false
                    }
                    if (resource.status == Status.ERROR){
                        showToast(resource.respText!!)
                        isAssignUserActivity = false
                    }
                    if(resource.status == Status.ERROR_TOKEN){
                        handleTokenInvalid()
                    }
                }
            })
        }
    }



    private fun showApproveActivityDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Bạn có muốn duyệt hoạt động không?")
        builder.setPositiveButton("Có"){_, _ ->

        }
        builder.setNegativeButton("Không"){_,_ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_activity_detail_by_user_unit, menu)
    }

    @SuppressLint("InflateParams")
    private fun setupCriteria(criterias: List<Criteria>) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_criteria_list, null)
        val  cartRecycler= view.findViewById<View>(R.id.cartRecycler) as androidx.recyclerview.widget.RecyclerView

        val cartAdapter = CriteriaByActivityAdapter(activity, criterias)
        cartRecycler.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        cartRecycler.layoutManager = layoutManager
        cartRecycler.adapter = cartAdapter
        val dialog = BottomSheetDialog(activity!!)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity){
            activity!!.navigation.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity!!.onBackPressed()
                true
            }
            R.id.txtSaveActivity ->{
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}