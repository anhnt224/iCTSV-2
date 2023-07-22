package com.bk.ctsv.ui.fragments.criteria

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.FragmentGradingAutoBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.models.GA
import com.bk.ctsv.models.entity.*
import com.bk.ctsv.utilities.runOnIoThread
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GradingAutoFragment : Fragment(), Injectable {

    private var criteriaTypes: List<CriteriaType> = listOf()
    private var activities: List<UserCriteriaActivity> = listOf()
    private var aRaw: Array<Array<Int>> = arrayOf()
    private lateinit var semester: Semester
    private lateinit var semesters: Array<Semester>

    private var groups: List<CriteriaGroup> = listOf()
    private var cList: ArrayList<UserCriteriaDetail> = arrayListOf()
    private var cGroupList: ArrayList<Int> = arrayListOf()

    private lateinit var viewModel: GradingAutoViewModel
    private lateinit var binding: FragmentGradingAutoBinding
    private lateinit var timer: Runnable
    private val handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var factory: ViewModelFactory

    private val TAG = "_C_A"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentGradingAutoBinding.inflate(inflater, container, false)

        binding.apply {
            gradingBtn.setOnClickListener {
                onGradingBtnTap()
            }
            saveBtn.setOnClickListener {
                markCriteria()
            }
        }
        setupData()
        onGradingBtnTap()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoadingLayout()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(GradingAutoViewModel::class.java)
    }

    private fun showLoading(){
        binding.loadingLayout.visibility = View.VISIBLE
        timer = object : Runnable {
            override fun run() {
                val loadingText = (20..99).random().toString()
                binding.loadingText.text = loadingText
                handler.postDelayed(this, 100) // Delay 0.1 seconds
            }
        }

        handler.postDelayed(timer, 100)
    }

    private fun hideLoadingLayout(){
        binding.loadingLayout.visibility = View.GONE
        handler.removeCallbacks(timer)
    }

    private fun setupData() {
        // fetch data from args
        val args = GradingAutoFragmentArgs.fromBundle(requireArguments())
        criteriaTypes = args.criteriaTypes.toList()
        semester = args.semester
        semesters = args.semesters
        aRaw = args.aRawString.map {
            it.split(",").map { s ->
                s.toIntOrNull() ?: 0
            }.toTypedArray()
        }.toTypedArray()
        activities = args.activities.toList()
        groups = criteriaTypes.flatMap {
            if (it.maxPoint < 0) {
                listOf()
            }else{
                it.criteriaGroups
            }
        }

        cList = arrayListOf()
        cGroupList = arrayListOf(0)
        groups.forEachIndexed { index, element ->
            cList.addAll(element.userCriteriaDetails)
            val criteriaGroupIndices = List(element.userCriteriaDetails.size) { index + 1 }
            cGroupList.addAll(criteriaGroupIndices)
        }

        Log.d(TAG, aRaw.toString())
        Log.d(TAG, activities.toString())

        clearOldResult()
    }

    private fun clearOldResult(){
        // Clear criteria
        criteriaTypes.forEach { t ->
            t.criteriaGroups.forEach { g ->
                g.userCriteriaDetails.forEach {
                    if (it.needProof()) {
                        it.userCriteriaActivities = arrayListOf()
                        it.sPoint = 0
                    }
                }
            }
        }
    }

    private fun onGradingBtnTap(){
        clearOldResult()
        showLoading()
        runOnIoThread {
            solve()
        }
    }

    private fun solve() {
        val start = Date()
        val cIds = cList.map {
            it.cID
        }
        val aList: Array<Array<Int>> = aRaw.map { element ->
            element.map { cIds.indexOf(it) + 1 }.toTypedArray()
        }.toTypedArray()

        val cScoreList = arrayListOf(0)
        cScoreList.addAll(cList.map { it.maxPoint })

        val maxGroup = arrayListOf(0)
        maxGroup.addAll(groups.map { it.maxPoint })

        val t = groups.size
        val m = cIds.size

        val n = aList.size

        // Initialize isIn list
        val isIn: Array<IntArray> = Array(m + 1) { IntArray(n) }

        // Populate the isIn list
        for (i in 0 until n) {
            val inputValues = aList[i]
            for (x in inputValues) {
                isIn[x][i] = 1
            }
        }

        // Sort the aList based on criteria scores
        for (i in 0 until n) {
            aList[i].sortedWith(kotlin.Comparator { u, v ->
                cScoreList[v] - cScoreList[u]
            })

        }

        val solver = GA(
            n,
            m,
            t,
            aList,
            cScoreList.toIntArray(),
            cGroupList.toIntArray(),
            maxGroup.toIntArray(),
            isIn
        )
        val ans = solver.solve()

        val end = Date()

        Log.d(TAG, "success ${end.time - start.time}")
        Log.d(TAG, "####### KQ: ${ans.curVal} #######")

        val selectedIds = ans.pos.map { p ->
            if (p != 0) {
                cList[p - 1].cID
            } else {
                0
            }
        }

        calcScore(posArr = ans.pos, cIds = cIds)
    }

    @SuppressLint("SetTextI18n")
    private fun calcScore(posArr: IntArray, cIds: List<Int>) {
        val activityMap = mutableMapOf<Int, UserCriteriaActivity>()
        posArr.forEachIndexed { index, pos ->
            if (pos != 0) {
                val cId = cIds[pos - 1]
                activityMap[cId] = activities[index]
            }
        }
        val activityMapKeys = activityMap.keys

        // set activity for criteria
        criteriaTypes.forEach { t ->
            t.criteriaGroups.forEach { g ->
                g.userCriteriaDetails.forEach { c ->
                    if (activityMapKeys.contains(c.cID)) {
                        c.sPoint = c.maxPoint
                        c.userCriteriaActivities = arrayListOf(activityMap[c.cID]!!)
                    }
                }
            }
        }

        var sumStudentPoint = 0
        criteriaTypes.forEach { item ->
            sumStudentPoint += item.getStudentPoint()
        }

        requireActivity().runOnUiThread {
            binding.resultText.text = "$sumStudentPoint ĐRL"
            hideLoadingLayout()
        }
    }

    private fun markCriteria(){
        viewModel.markCriteriaUser(semester.name, criteriaTypes)
        with(viewModel){
            markCriteriaUser.observe(viewLifecycleOwner){
                binding.resourse = it
                if(checkResource(it)){
                    markCriteriaUser.removeObservers(viewLifecycleOwner)
                    showSuccessDialog()
                }
            }
        }
    }

    private fun showSuccessDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chấm điểm rèn luyện thành công")
            .setPositiveButton("Đóng"){_, _ ->
                val action = GradingAutoFragmentDirections.actionGradingAutoFragmentToCriteriaFragment(semester, semesters)
                Navigation.findNavController(requireView()).navigate(action)
            }
            .setCancelable(false )
            .show()
    }

}