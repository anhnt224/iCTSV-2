package com.bk.ctsv.teacher.fragment.student

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.ListStudentFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.Student
import com.bk.ctsv.models.entity.StudentInfo
import com.bk.ctsv.teacher.adapters.OnItemStudentButtonClickLister
import com.bk.ctsv.teacher.adapters.StudentAdapter
import com.bk.ctsv.teacher.viewmodel.student.ListStudentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class ListStudentFragment : Fragment(), Injectable, OnItemStudentButtonClickLister {

    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ListStudentViewModel
    private lateinit var binding: ListStudentFragmentBinding
    private lateinit var studentAdapter: StudentAdapter
    private var semesters: Array<String> = arrayOf("2019-1", "2019-2", "2020-1", "2020-2", "2021-1", "2021-2")
    private var semesterSelected: String = "2020-2"
    private var classSelected: String = ""
    private var allClass = "Tất cả"
    private var listClasses: Array<String> = arrayOf()
    private lateinit var searchView: SearchView
    private var students: List<Student> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.list_student_fragment, container, false)

        studentAdapter = StudentAdapter(listOf(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studentAdapter
        }

        binding.btSemester.text = "Kì $semesterSelected"

        binding.btSemester.setOnClickListener {
            chooseSemester()
        }

        binding.btClass.setOnClickListener {
            chooseClass()
        }

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.getListClasses()
            }
        }

        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ListStudentViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            students.observe(viewLifecycleOwner){
                binding.status = it.status
                if(checkResource(it) && it.data != null){
                    studentAdapter.students = it.data
                    this@ListStudentFragment.students = it.data
                    studentAdapter.notifyDataSetChanged()
                }
            }

            classes.observe(viewLifecycleOwner){
                binding.status = it.status
                if(checkResource(it) && it.data != null){
                    listClasses = it.data.toTypedArray()
                    listClasses = listClasses.plus(allClass)
                    if(listClasses.isNotEmpty()){
                        classSelected = listClasses.first()
                        binding.btClass.text = classSelected
                        viewModel.setParameter(semesterSelected, classSelected, "")
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun chooseSemester(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn kì học")
            .setItems(semesters) {_, i ->
                semesterSelected = semesters[i]
                viewModel.setParameter(semesterSelected, classSelected, "")
                binding.btSemester.text = "Kì $semesterSelected"
            }.show()
    }

    private fun chooseClass(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn lớp")
            .setItems(listClasses) {_, i ->
                classSelected = listClasses[i]
                viewModel.setParameter(semesterSelected, classSelected, "")
                binding.btClass.text = classSelected
            }.show()
    }

    /**
     *
     */

    override fun onButtonActivityClick(student: Student) {
        Navigation.findNavController(requireView()).navigate(ListStudentFragmentDirections.actionListStudentFragmentToListActivitiesOfStudentFragment(student))
    }

    override fun onButtonInfoClick(studentID: String) {
        Navigation.findNavController(requireView()).navigate(ListStudentFragmentDirections.actionListStudentFragmentToStudentInfoFragment(studentID))
    }

    override fun onButtonMarkClick(studentID: String, studentName: String) {
        Navigation.findNavController(requireView()).navigate(ListStudentFragmentDirections.actionListStudentFragmentToTeacherMarkFragment(studentID, studentName, semesterSelected))
    }

    /**
     *
     */

    @SuppressLint("DefaultLocale")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_student, menu)

        searchView = menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String): Boolean {
                if(classSelected == allClass){
                    viewModel.setParameter(semesterSelected, classSelected, searchText)
                }
                return true
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                if(classSelected == allClass){
                    return true
                }

                /**
                 *
                 */
                studentAdapter.students = students.filter {
                    it.name.toLowerCase().contains(searchText.toLowerCase())
                }
                studentAdapter.notifyDataSetChanged()
                return true
            }

        })

    }
}