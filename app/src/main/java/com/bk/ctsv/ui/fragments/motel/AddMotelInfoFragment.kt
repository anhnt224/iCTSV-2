package com.bk.ctsv.ui.fragments.motel

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.SeekBar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.FragmentAddMotelInfoBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showDialogMotel
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.ui.fragments.user.AddNewAddressFragment
import com.bk.ctsv.ui.viewmodels.motel.AddMotelInfoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_add_motel_info.*
import javax.inject.Inject


class AddMotelInfoFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = AddMotelInfoFragment()
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AddMotelInfoViewModel
    private lateinit var binding: FragmentAddMotelInfoBinding
    private var mAddress = UserAddress()
    private var motelInfo = Motel()
    private var typeMotel: List<String> = listOf("Nhà chung chủ", "Nhà không chung chủ", "Khác")
    private var addMotel = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_motel_info, container, false)
        mAddress = AddNewAddressFragment.mAddress
        binding.apply {
            buttonSave.setOnClickListener {
                saveAddress()
            }
            if (mAddress.type == "KTX Bách Khoa" ||  mAddress.type == "KTX Pháp Vân"){
                motelTypeTxt.setText(mAddress.type)
            }
            motelAddressTxt.setText(mAddress.address)
            binding.motelTypeLayout.editText?.setOnClickListener {
                if (mAddress.type == "Nhà trọ"){
                    showAlertPickType()
                }
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    if (i<20){
                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_one)
                    }
                    if ((i >= 20) && (i < 40) ){
                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_two)
                    }
                    if ((i >= 40) && (i < 60) ){
                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_three)
//                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_three)
                    }
                    if ((i >= 60) && (i < 80) ){
                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_four)
//                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_four)
                    }
                    if (i >= 80){
                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_five)
//                        seekBar.thumb = resources.getDrawable(R.drawable.thumb_five)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })

        }

        subscribeUI()
        return binding.root
    }

    private fun setThumbSize(drawable: Int){
        binding.apply {
            val vto = seekBar.getViewTreeObserver()
            vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val res: Resources = resources
                    val thumb: Drawable = res.getDrawable(drawable)
                    val h: Int = (seekBar.getMeasuredHeight() * 0.8).toInt() // 8 * 1.5 = 12
                    val bmpOrg = (thumb as BitmapDrawable).bitmap
                    val bmpScaled = Bitmap.createScaledBitmap(bmpOrg, h, h, true)
                    val newThumb: Drawable = BitmapDrawable(res, bmpScaled)
                    newThumb.setBounds(0, 0, newThumb.intrinsicWidth, newThumb.intrinsicHeight)
                    seekBar.setThumb(newThumb)
                    seekBar.getViewTreeObserver().removeOnPreDrawListener(this)
                    return true
                }
            })
        }
    }

    private fun showAlertPickType(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn loại nhà trọ")
            .setItems(typeMotel.toTypedArray()){ _: DialogInterface?, which: Int ->
                motelTypeLayout.editText?.setText(typeMotel[which])
            }
            .setNegativeButton("Hủy"){_, _ ->
            }.show()
    }

    private fun saveAddress(){
        addMotel = true
        motelInfo.managerName = binding.hostNameTxt.text.toString()
        motelInfo.managerContact = binding.hostPhoneTxt.text.toString()
        motelInfo.description = binding.motelDescriptionTxt.text.toString()
        motelInfo.type = binding.motelTypeTxt.text.toString()
        motelInfo.address = binding.motelAddressTxt.text.toString()
        motelInfo.comment = binding.motelCommentTxt.text.toString()
        motelInfo.rate = binding.seekBar.progress
        if (motelInfo.managerName.isEmpty() || motelInfo.managerContact.isEmpty() || motelInfo.description.isEmpty()){
            showToast("Vui lòng nhập đầy đủ thông tin")
        }else{
            viewModel.updateUserAddress(mAddress, motelInfo)
        }
    }

    private fun subscribeUI(){
        with(viewModel){
            updateUserAddress.observe(viewLifecycleOwner){
                if(checkResource(it) && addMotel){
                    if (it.data != null){
                        addMotel = false
                        val motelID = it.data?.motelID
                        showToast("Thêm địa chỉ thành công")
                        if (mAddress.type == "KTX Pháp Vân" || mAddress.type == "KTX Bách Khoa"){
                            showDialogMotel("Thêm ảnh chụp KTX",
                                "Để có đánh giá khách quan hơn về thông tin ktx bạn cung cấp, iCTSV cần thêm ảnh chụp ktx này. Bạn có sẵn sàng thêm ảnh ktx?",
                                R.drawable.ic_add_image_motel,
                                "Thêm ảnh",
                                {navigateToImageFragment(motelID)},
                                "Bỏ qua",
                                {   showToast("Thêm địa chỉ thành công")
                                    navigateListAddressFragment()}
                            )
                        }else{
                            showDialogMotel("Thêm ảnh chụp nhà trọ",
                                "Để có đánh giá khách quan hơn về thông tin nhà trọ bạn cung cấp, iCTSV cần thêm ảnh chụp nhà trọ này. Bạn có sẵn sàng thêm ảnh phòng trọ?",
                                R.drawable.ic_add_image_motel,
                                "Thêm ảnh",
                                {navigateToImageFragment(motelID)},
                                "Bỏ qua",
                                {   showToast("Thêm địa chỉ thành công")
                                    navigateListAddressFragment()}
                            )
                        }
                    }
                }
            }
        }
    }

    private fun navigateListAddressFragment(){
        Navigation.findNavController(requireView())
            .navigate(AddMotelInfoFragmentDirections.actionAddMotelInfoFragmentToListAddressFragment())
    }
    private fun navigateToImageFragment(motelID: Int){
        Navigation.findNavController(requireView())
            .navigate(AddMotelInfoFragmentDirections.actionAddMotelInfoFragmentToImageMotelFragment(motelID))
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(AddMotelInfoViewModel::class.java)
    }

}