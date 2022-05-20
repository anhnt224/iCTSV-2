package com.bk.ctsv.ui.fragments.running

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.RunDashboardFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.*
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.run.RunResult
import com.bk.ctsv.ui.adapter.running.ListRunResultAdapter
import com.bk.ctsv.ui.adapter.running.RunResultAdapter
import com.bk.ctsv.ui.viewmodels.running.ChartType
import com.bk.ctsv.ui.viewmodels.running.RunDashboardViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayout
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RunDashboardFragment : Fragment(), Injectable {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
        const val REQUEST_BACKGROUND_LOCATION_PERMISSION = 1002
    }

    private lateinit var viewModel: RunDashboardViewModel
    private lateinit var binding: RunDashboardFragmentBinding
    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var runResultAdapter: ListRunResultAdapter
    private var chartType: ChartType = ChartType.BY_WEEK

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.run_dashboard_fragment, container, false)
        setUpViewModel()
        setUpRecyclerView()

        chartType = ChartType.BY_WEEK
        initWeek()

        binding.apply {
            runButton.setOnClickListener {
                checkPermission()
            }

            nextButton.setOnClickListener {
                when(chartType){
                    ChartType.BY_WEEK -> increaseWeek()
                    ChartType.BY_MONTH -> increaseMonth()
                }
            }

            prevButton.setOnClickListener {
                when(chartType){
                    ChartType.BY_WEEK -> decreaseWeek()
                    ChartType.BY_MONTH -> decreaseMonth()
                }
            }

            chartTypeTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position){
                        0 -> {
                            chartType = ChartType.BY_WEEK
                            initWeek()
                        }
                        1 -> {
                            chartType = ChartType.BY_MONTH
                            initMonth()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

        }

        subscribeUI()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUI(){
        with(viewModel){
            runResultList.observe(viewLifecycleOwner){resource ->
                if (checkResource(resource) && resource.data != null){
                    statisticRunResult(resource.data)
                }
            }

            recentlyRunResults.observe(viewLifecycleOwner){resource ->
                if(checkResource(resource)  && resource.data != null){
                    var runResultsMap = resource.data.groupBy {
                        it.timeStart.toDateStr()
                    }
                    runResultAdapter.runResultMap = runResultsMap
                    runResultAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(RunDashboardViewModel::class.java)
    }

    private fun setUpRecyclerView(){
        runResultAdapter = ListRunResultAdapter(mapOf())
        binding.recyclerView.apply {
            adapter = runResultAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun statisticRunResult(runResults: List<RunResult>){
        var totalDistance = 0.0
        runResults.forEach {
            totalDistance += it.distance
        }
        binding.apply {
            totalDistanceTextView.text = String.format("%.2f", totalDistance / 1000)
        }

        when(chartType){
            ChartType.BY_MONTH -> showBarChartByMonth(runResults)
            ChartType.BY_WEEK -> showBarChartByWeek(runResults)
        }


    }

    private fun showBarChartByWeek(runResults: List<RunResult>){
        val labels: ArrayList<String> = arrayListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
        val runResultEntries: ArrayList<BarEntry> = arrayListOf()

        val runResultsMap = runResults.groupBy {
            it.timeStartToDateOfWeek()
        }

        labels.forEachIndexed { index, s ->
            val runResultList = runResultsMap[s]
            if(runResultList == null){
                runResultEntries.add(BarEntry((index + 0.5).toFloat(), 0f))
            }else{
                var totalDistance = 0.0
                runResultList.forEach {
                    totalDistance += it.distance / 1000
                }
                runResultEntries.add(BarEntry((index + 0.5).toFloat(), totalDistance.toFloat()))
            }
        }

        val runResultDataSet = BarDataSet(runResultEntries, "")

        val barData = BarData(runResultDataSet)

        binding.barChart.apply {
            xAxis.valueFormatter = object: IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if(0 <= value && value <= labels.size - 1){
                        return labels[value.toInt()]
                    }
                    return ""
                }
            }
            xAxis.setCenterAxisLabels(true)
            xAxis.granularity = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.axisMinimum = 0f


            //
            axisRight.isEnabled = false
            setNoDataText("Không có dữ liệu")

            //
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP

            //
            axisLeft.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f
            description = null


            val vf = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if(value == 0.0f){
                        ""
                    }else{
                        String.format("%.2f", value)
                    }
                }
            }
            barData.setValueFormatter(vf)

            data = barData
            invalidate()
            animateY(300)
        }
    }

    private fun showBarChartByMonth(runResults: List<RunResult>){
        val timeStart = viewModel.getTimeStart()
        val timeEnd = viewModel.getTimeEnd()
        if(timeStart == null || timeEnd == null){
            return
        }

        val labels = (1..(timeEnd.getDayOfMonth())).map {
            it.toString()
        }

        Log.d("_BAR_CHART", labels.toString())

        val runResultEntries: ArrayList<BarEntry> = arrayListOf()
        val runResultsMap = runResults.groupBy {
            it.timeStart.getDayOfMonth().toString()
        }

        labels.forEachIndexed { index, s ->
            val runResultList = runResultsMap[s]
            if(runResultList == null){
                runResultEntries.add(BarEntry((index).toFloat(), 0f))
            }else{
                var totalDistance = 0.0
                runResultList.forEach {
                    totalDistance += it.distance / 1000
                }
                runResultEntries.add(BarEntry((index).toFloat(), totalDistance.toFloat()))
            }
        }

        val runResultDataSet = BarDataSet(runResultEntries, "")

        val barData = BarData(runResultDataSet)

        binding.barChart.apply {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.mAxisMinimum = 1f
            xAxis.setCenterAxisLabels(false)

            xAxis.valueFormatter = object: IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if(0 <= value && value <= labels.size - 1){
                        return labels[value.toInt()]
                    }
                    return ""
                }
            }

            //
            axisRight.isEnabled = false
            setNoDataText("Không có dữ liệu")

            //
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP

            //
            axisLeft.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f
            description = null

            val vf = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if(value == 0.0f){
                        ""
                    }else{
                        String.format("%.2f", value)
                    }
                }
            }
            barData.setValueFormatter(vf)

            data = barData
            invalidate()
            animateY(300)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initWeek(){
        // get today and clear time of day
        val cal = Calendar.getInstance()

        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        // get start of this week in milliseconds
        cal[Calendar.DAY_OF_WEEK] = cal.firstDayOfWeek

        val timeStart = cal.time
        // End of week
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        cal.add(Calendar.SECOND, -1)

        val timeEnd = cal.time

        binding.timeRangeTextView.text = "${timeStart.getMonthDayStr()} - ${timeEnd.getDateStr()}"
        viewModel.setTime(timeStart, timeEnd)
    }

    @SuppressLint("SetTextI18n")
    private fun decreaseWeek(){
        val firstDayOfCurrentWeek = viewModel.getTimeStart() ?: return

        val cal = Calendar.getInstance()
        cal.time = firstDayOfCurrentWeek

        //decrease 1 week
        cal.add(Calendar.WEEK_OF_YEAR, - 1)

        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        val timeStart = cal.time

        // End of week
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        cal.add(Calendar.SECOND, -1)

        val timeEnd = cal.time

        binding.timeRangeTextView.text = "${timeStart.getMonthDayStr()} - ${timeEnd.getDateStr()}"
        viewModel.setTime(timeStart, timeEnd)
    }

    @SuppressLint("SetTextI18n")
    private fun increaseWeek(){
        val firstDayOfCurrentWeek = viewModel.getTimeStart() ?: return

        val cal = Calendar.getInstance()
        cal.time = firstDayOfCurrentWeek

        //increase 1 week
        cal.add(Calendar.WEEK_OF_YEAR, 1)

        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        val timeStart = cal.time

        // End of week
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        cal.add(Calendar.SECOND, -1)
        val timeEnd = cal.time

        binding.timeRangeTextView.text = "${timeStart.getMonthDayStr()} - ${timeEnd.getDateStr()}"
        viewModel.setTime(timeStart, timeEnd)
    }

    @SuppressLint("SetTextI18n")
    private fun initMonth(){
        // get today and clear time of day
        val cal = Calendar.getInstance()

        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        cal[Calendar.DAY_OF_MONTH] = 1

        val timeStart = cal.time
        // End of week
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.SECOND, -1)

        val timeEnd = cal.time

        binding.timeRangeTextView.text = "${timeStart.getMonthDayStr()} - ${timeEnd.getDateStr()}"
        viewModel.setTime(timeStart, timeEnd)
    }

    @SuppressLint("SetTextI18n")
    private fun decreaseMonth(){
        val firstDayOfCurrentWeek = viewModel.getTimeStart() ?: return

        val cal = Calendar.getInstance()
        cal.time = firstDayOfCurrentWeek

        //decrease 1 week
        cal.add(Calendar.MONTH, - 1)

        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        val timeStart = cal.time

        // End of week
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.SECOND, -1)

        val timeEnd = cal.time

        binding.timeRangeTextView.text = "${timeStart.getMonthDayStr()} - ${timeEnd.getDateStr()}"
        viewModel.setTime(timeStart, timeEnd)
    }

    @SuppressLint("SetTextI18n")
    private fun increaseMonth(){
        val firstDayOfCurrentWeek = viewModel.getTimeStart() ?: return

        val cal = Calendar.getInstance()
        cal.time = firstDayOfCurrentWeek

        //increase 1 week
        cal.add(Calendar.MONTH, 1)

        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        val timeStart = cal.time

        // End of week
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.SECOND, -1)
        val timeEnd = cal.time

        binding.timeRangeTextView.text = "${timeStart.getMonthDayStr()} - ${timeEnd.getDateStr()}"
        viewModel.setTime(timeStart, timeEnd)
    }

    /**
     * Check permission
     */
    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            if(isPermissionDenied(Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                if(
                    isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
                ){
                    showDialogRequestLocationPermission()
                }else{
                    showDialogRequestBackgroundLocation()
                }
            }else{
                handleRun()
            }

        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(
                isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION) &&
                isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
            ){
                showDialogRequestLocationPermission()
            }else{
                handleRun()
            }
        }else{
            handleRun()
        }
    }

    private fun isPermissionDenied(permission: String): Boolean{
        return checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_DENIED
    }

    private fun showDialogRequestLocationPermission(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.request_permission_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val doneButton = dialog.findViewById<Button>(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss()
            requestLocationPermission()
        }

        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun requestLocationPermission(){
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun showDialogRequestBackgroundLocation(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.request_bg_location_permission)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val doneButton = dialog.findViewById<Button>(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss()
            requestLocationBackgroundPermission()
        }

        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun requestLocationBackgroundPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            requestPermissions(permissions, REQUEST_BACKGROUND_LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_LOCATION_PERMISSION -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                        handleRun()
                    }else{
                        showDialogRequestBackgroundLocation()
                    }
                }
            }
            REQUEST_BACKGROUND_LOCATION_PERMISSION -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    handleRun()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun handleRun(){
        if(sharedPrefsHelper.getEmail().contains("@hust.edu.vn")){
            // is teacher
            Navigation.findNavController(requireView())
                .navigate(
                    R.id.action_runDashboardFragment2_to_runFragment2
                )
        }else{
            // is student
            Navigation.findNavController(requireView())
                .navigate(
                    R.id.action_runDashboardFragment_to_runFragment
                )
        }


    }

    /*private fun convertListRunResultToListListRunResult(): List<List<RunResult>>{
        var runResults: List<RunResult> = listOf()
        var runResultsMap = runResults.groupBy {
            it.timeStart.toDateStr()
        }
        return listListRunResult
    }*/

}