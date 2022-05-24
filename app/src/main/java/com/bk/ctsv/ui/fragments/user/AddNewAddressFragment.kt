package com.bk.ctsv.ui.fragments.user

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.AddNewAddressFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkLocationPermission
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showDialogMotel
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.models.res.GetPlaceNameAutoByMapRes
import com.bk.ctsv.teacher.fragment.motel.TAddNewAddressFragment
import com.bk.ctsv.ui.viewmodels.user.AddNewAddressViewModel
import com.bk.ctsv.utilities.*
import com.bk.ctsv.webservices.WebService
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.add_new_address_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AddNewAddressFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = AddNewAddressFragment()
        var newLatLng: LatLng? = null
        var address: String = ""
        var mAddress = UserAddress()
    }
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: AddNewAddressViewModel
    private lateinit var binding: AddNewAddressFragmentBinding
    private var cities: List<String> = listOf()
    private var districts: List<String> = listOf()
    private var wards: List<String> = listOf()
    private var types: List<String> = listOf("KTX Bách Khoa", "KTX Pháp Vân", "Nhà trọ", "Nhà riêng")
    private lateinit var webService : WebService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.add_new_address_fragment, container, false)
        binding.address = mAddress
        if (newLatLng != null){
            autoGetPlaceName(newLatLng!!.longitude, newLatLng!!.latitude)
        }
        binding.apply {
         //   disableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
            textLocation.setEndIconOnClickListener {
                if(checkLocationPermission()){
                    pickLocation()
                }
            }
            buttonSave.setOnClickListener {
                saveAddress()
            }
            textCity.editText?.setOnClickListener {
                showAlertPickCity()
            }
            textDistrict.editText?.setOnClickListener {
                showAlertPickDistrict()
            }
            textWard.editText?.setOnClickListener {
                showAlertPickWard()
            }
            textType.editText?.setOnClickListener {
                showAlertPickType()
            }
        }
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(AddNewAddressViewModel::class.java)
    }

    private fun pickLocation(){
        setAddress()
        Navigation.findNavController(requireView()).navigate(AddNewAddressFragmentDirections.actionAddNewAddressFragmentToPickLocationFragment())
    }

    private fun subscribeUI(){
        with(viewModel){
            getAddress().observe(viewLifecycleOwner){address ->
                mAddress = address
                if(newLatLng != null){
                    mAddress.latitude = newLatLng!!.latitude
                    mAddress.longtitude = newLatLng!!.longitude
                    if(mAddress.type == types[0] || mAddress.type == types[1]){
                        disableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                    }
                    else{
                        enableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                    }
                }else{
                    disableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                }
                binding.address = mAddress
            }

            mediatorLiveGetListCities.observe(viewLifecycleOwner) {resource ->
                binding.resource = resource
                if(checkResource(resource) && resource.data != null){
                    cities = resource.data
                }
            }

            mediatorLiveGetListDistricts.observe(viewLifecycleOwner) {resource ->
                binding.resource = resource
                if(checkResource(resource) && resource.data != null){
                    districts = resource.data
                }
            }

            mediatorLiveGetListWards.observe(viewLifecycleOwner) {resource ->
                if(checkResource(resource) && resource.data != null){
                    wards = resource.data
                }
            }

            updateUserAddress.observe(viewLifecycleOwner){
                binding.resource = it
                if(checkResource(it)){
                    if (it.data != null && it.data.respCode == 0){
                        showToast("Thêm địa chỉ thành công")
                        Navigation.findNavController(requireView()).navigateUp()
                    }
                }
            }
        }
    }

    private fun setAddress(){
        mAddress.studentID = sharedPrefsHelper.getUserName()
        mAddress.studentName = sharedPrefsHelper.getFullName()
        mAddress.contact = binding.textPhone.editText?.text.toString()
        mAddress.type = binding.textType.editText?.text.toString()
        mAddress.city = binding.textCity.editText?.text.toString()
        mAddress.district = binding.textDistrict.editText?.text.toString()
        mAddress.ward = binding.textWard.editText?.text.toString()
        mAddress.address = binding.textAddress.editText?.text.toString()
        mAddress.email = binding.emailTextInputLayout.editText?.text.toString()
        if(newLatLng != null){
            mAddress.latitude = newLatLng!!.latitude
            mAddress.longtitude = newLatLng!!.longitude
        }
        viewModel.setAddress(mAddress)
    }

    private fun saveAddress() {
        mAddress.studentID = sharedPrefsHelper.getUserName()
        mAddress.studentName = sharedPrefsHelper.getFullName()
        mAddress.contact = binding.textPhone.editText?.text.toString()
        mAddress.type = binding.textType.editText?.text.toString()
        mAddress.city = binding.textCity.editText?.text.toString()
        mAddress.district = binding.textDistrict.editText?.text.toString()
        mAddress.ward = binding.textWard.editText?.text.toString()
        mAddress.address = binding.textAddress.editText?.text.toString()
        mAddress.email = binding.emailTextInputLayout.editText?.text.toString()
        if (newLatLng != null) {
            mAddress.latitude = newLatLng!!.latitude
            mAddress.longtitude = newLatLng!!.longitude
        }

        if (mAddress.city.isEmpty() || mAddress.district.isEmpty() || mAddress.ward.isEmpty() || mAddress.contact.isEmpty()
            || mAddress.type.isEmpty() || mAddress.longtitude == 0.0 || mAddress.latitude == 0.0 || mAddress.address.isEmpty() || mAddress.email.isEmpty()
        ) {
            showToast("Vui lòng nhập đầy đủ thông tin")
        } else {
            if (mAddress.isValidEmail()) {
                if (mAddress.type == types[2]) {
                    showDialogMotel("Chia sẻ đánh giá về nhà trọ",
                        "Bạn có sẵn sàng chia sẻ những thông tin, đánh giá về nhà trọ bạn đang ở cho iCTSV. Chúng tôi sử dụng thông tin này để .... ",
                        R.drawable.ic_share_motel,
                        "Thêm thông tin",
                        { navigateToAddMotelFragment() },
                        "Bỏ qua",
                        { viewModel.updateUserAddress(mAddress) })
                }
                if (mAddress.type == types[0] || mAddress.type == types[1]) {
                    showDialogMotel("Chia sẻ đánh giá về ktx",
                        "Bạn có sẵn sàng chia sẻ những thông tin, đánh giá về ktx bạn đang ở cho iCTSV. Chúng tôi sử dụng thông tin này để .... ",
                        R.drawable.ic_share_motel,
                        "Thêm thông tin",
                        { navigateToAddMotelFragment() },
                        "Bỏ qua",
                        { viewModel.updateUserAddress(mAddress) })
                }
                if (mAddress.type == types[3]){
                    viewModel.updateUserAddress(mAddress)
                }
            }else {
                showToast("Địa chỉ email không hợp lệ")
            }
        }
    }

    private fun navigateToAddMotelFragment(){
        Navigation.findNavController(requireView()).
        navigate(AddNewAddressFragmentDirections.actionAddNewAddressFragmentToAddMotelInfoFragment())
    }
    private fun showAlertPickCity(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn Tỉnh/Thành phố")
            .setItems(cities.toTypedArray()){_: DialogInterface?, which: Int ->
                binding.textCity.editText?.setText(cities[which])
                viewModel.getListDistricts(binding.textCity.editText?.text.toString())
                binding.textDistrict.editText?.setText("")
                binding.textWard.editText?.setText("")
            }
            .setNegativeButton("Hủy"){_, _ ->
            }
            .show()
    }

    private fun showAlertPickDistrict(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn Quận/Huyện")
            .setItems(districts.toTypedArray()){_: DialogInterface?, which: Int ->
                binding.textDistrict.editText?.setText(districts[which])
                viewModel.getListWards(binding.textCity.editText?.text.toString(), binding.textDistrict.editText?.text.toString())
                binding.textWard.editText?.setText("")
            }
            .setNegativeButton("Hủy"){_, _ ->
            }
            .show()
    }

    private fun showAlertPickWard(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn Quận/Huyện")
            .setItems(wards.toTypedArray()){_: DialogInterface?, which: Int ->
                binding.textWard.editText?.setText(wards[which])
            }
            .setNegativeButton("Hủy"){_, _ ->
            }
            .show()
    }
    private fun autoGetPlaceName(long: Double, lat: Double){
        val retrofit =
            Retrofit.Builder().baseUrl(API_MAP).addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(WebService::class.java)
        val request = service.getPlaceNameAuto(long, lat)
        request.enqueue(object : Callback<GetPlaceNameAutoByMapRes>{
            override fun onResponse(
                call: Call<GetPlaceNameAutoByMapRes>,
                response: Response<GetPlaceNameAutoByMapRes>
            ) {
                val getPlaceResponse = response.body()
                if (getPlaceResponse != null){
                    if ((getPlaceResponse.data.cityName != null) and
                        (getPlaceResponse.data.districtName != null) and
                        ( getPlaceResponse.data.wardName != null)){
                        binding.apply {
                            textCity.editText?.setText(getPlaceResponse.data.cityName)
                            textDistrict.editText?.setText(getPlaceResponse.data.districtName)
                            textWard.editText?.setText(getPlaceResponse.data.wardName)
                            enableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                            getDistrictWard()
                        }
                    }

                }
            }

            override fun onFailure(call: Call<GetPlaceNameAutoByMapRes>, t: Throwable) {
                Log.d("_MAPPLACENAME", "ERROR MAP ${t.message}")
            }

        })
    }

    private fun getDistrictWard(){
        viewModel.getListDistricts(binding.textCity.editText?.text.toString())
        viewModel.getListWards(binding.textCity.editText?.text.toString(), binding.textDistrict.editText?.text.toString())
    }

    private fun showAlertPickType(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn loại nơi ở")
            .setItems(types.toTypedArray()){_: DialogInterface?, which: Int ->
                textType.editText?.setText(types[which])
                when(which){
                    0 -> {
                        binding.apply {
                            textCity.editText?.setText(DEFAULT_CITY)
                            textDistrict.editText?.setText(KTX_BK_DISTRICT)
                            textWard.editText?.setText(KTX_BK_WARD)
                            textAddress.helperText = "VD: P404 KTX B3"
                            newLatLng = KTX_BK_LATLNG
                            mAddress.longtitude = newLatLng!!.longitude
                            mAddress.latitude = newLatLng!!.latitude
                            textLocation.editText?.setText(mAddress.getLocation())
                            disableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                        }
                    }

                    1 -> {
                        binding.apply {
                            textCity.editText?.setText(DEFAULT_CITY)
                            textDistrict.editText?.setText(KTX_PV_DISTRICT)
                            textWard.editText?.setText(KTX_PV_WARD)
                            textAddress.helperText = "VD: P404 Tòa A6"
                            newLatLng = KTX_PV_LATLNG
                            mAddress.longtitude = newLatLng!!.longitude
                            mAddress.latitude = newLatLng!!.latitude
                            textLocation.editText?.setText(mAddress.getLocation())
                            disableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                        }
                    }
                    else -> {
                        mAddress.latitude = 0.0
                        mAddress.longtitude = 0.0
                        newLatLng = null
                        binding.apply {
                            textLocation.editText?.setText(mAddress.getLocation())
                            enableClick(textCity.editText!!, textDistrict.editText!!, textWard.editText!!)
                            clearText(textDistrict.editText!!, textWard.editText!!)
                            textAddress.helperText = "VD: Số 1, Đại Cồ Việt, Hai Bà Trưng, Hà Nội"
                        }
                    }
                }
            }
            .setNegativeButton("Hủy"){_, _ ->
            }.show()
    }

    private fun disableClick(vararg editTexts: EditText){
        editTexts.forEach {
            it.isEnabled = false
            it.setBackgroundColor(resources.getColor(R.color.iron))
        }
    }

    private fun enableClick(vararg editTexts: EditText){
        editTexts.forEach {
            it.isEnabled = true
        }
    }

    private fun clearText(vararg editTexts: EditText){
        editTexts.forEach {
            it.setText("")
        }
    }
}