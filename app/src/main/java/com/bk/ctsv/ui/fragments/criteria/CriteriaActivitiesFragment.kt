package com.bk.ctsv.ui.fragments.criteria

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.FragmentCriteriaActivitiesBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.generated.callback.RetryCallback
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.entity.CriteriaType
import com.bk.ctsv.models.entity.Semester
import com.bk.ctsv.models.entity.UserCriteriaActivity
import com.bk.ctsv.ui.adapter.criteria.CActivityAdapter
import javax.inject.Inject

class CriteriaActivitiesFragment : Fragment(), Injectable {

    private lateinit var criteriaTypes: List<CriteriaType>
    private lateinit var semester: Semester
    private lateinit var semesters: Array<Semester>
    private lateinit var viewModel: CriteriaActivitiesViewModel
    private lateinit var binding: FragmentCriteriaActivitiesBinding
    private lateinit var caAdapter: CActivityAdapter
    private var activities: List<Activity> = listOf()
    private val t = "_C_A"

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentCriteriaActivitiesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        initData()

        binding.apply {
            gradingBtn.setOnClickListener {
                navToGrading()
            }
            callback = object : com.bk.ctsv.common.RetryCallback {
                override fun retry() {
                    viewModel.getCriteriaActivities(this@CriteriaActivitiesFragment.semester.name)
                }
            }
        }

        viewModel.getCriteriaActivities(semester.name)
        subscribeUI()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(CriteriaActivitiesViewModel::class.java)
    }

    private fun initData() {
        val args = CriteriaActivitiesFragmentArgs.fromBundle(requireArguments())
        criteriaTypes = args.criteriaTypes.toList()
        semester = args.semester
        semesters = args.semesters
        binding.semester = semester.name
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUI() {
        with(viewModel) {
            criteriaActivities.observe(viewLifecycleOwner) {
                binding.status = it.status
                if (checkResource(it)) {
                    caAdapter.activities = it.data ?: listOf()
                    caAdapter.notifyDataSetChanged()
                    activities = it.data ?: listOf()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        caAdapter = CActivityAdapter(listOf())
        binding.activityList.apply {
            adapter = caAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun navToGrading() {
        val aRaw = activities.map {
            it.criteriaLst?.map { c -> c.id } ?: listOf()
        }

        val aRawString = aRaw.map {
            it.joinToString(separator = ",")
        }

        val aList = activities.map {
            UserCriteriaActivity(it)
        }

        if (activities.isEmpty()) {
            showToast("Rất tiếc, không có hoạt động nào để chấm điểm")
            return
        }

        val action =
            CriteriaActivitiesFragmentDirections.actionCriteriaActivitiesFragmentToGradingAutoFragment(
                criteriaTypes.toTypedArray(),
                semester,
                aList.toTypedArray(),
                aRawString.toTypedArray(),
                semesters
            )
        Navigation.findNavController(requireView()).navigate(action)
        Log.d(t, aRawString.toString())
    }

}