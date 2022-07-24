@file:Suppress("KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation",
    "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation",
    "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation",
    "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation",
    "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation",
    "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation",
    "EqualsBetweenInconvertibleTypes", "RemoveRedundantCallsOfConversionMethods",
    "RemoveRedundantCallsOfConversionMethods", "RemoveRedundantCallsOfConversionMethods",
    "RemoveRedundantCallsOfConversionMethods", "IfThenToSafeAccess", "IfThenToSafeAccess",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter"
)

package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.viewmodel.LokasiViewModel
import andlima.group3.secondhand.viewmodel.ProfileViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_info_akun.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class InfoAkunFragment : Fragment() {
    lateinit var body: MultipartBody.Part
    lateinit var userManager: UserManager
    private var pilihan = ""
    private var cek = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userManager = UserManager(requireContext())

        infoakun_arrowback.setOnClickListener {
            view.findNavController().navigate(R.id.action_infoAkunFragment2_to_akunFragment)
        }
        userManager.accessTokenFlow.asLiveData().observeOnce(viewLifecycleOwner){
            getDataUser(it)
        }
        getLokasi()




        infoAkun_btn_Simpan.setOnClickListener {
            val nama = infoAkun_et_nama.text.toString()


            val alamat = infoAkun_et_alamat.text.toString()
            val nohp = infoAkun_et_nohp.text.toString()
            if (nohp.startsWith("62")){
                if (nama.isNotBlank()  && alamat.isNotBlank() && pilihan.isNotBlank()){
                    userManager.accessTokenFlow.asLiveData().observeOnce(viewLifecycleOwner){
                        profile(it,nama,pilihan,alamat,nohp)

                    }
                }else{
                    toast(requireContext(), "Isi semua data")
                }
            }else{
                toast(requireContext(), "Nomor HP harus diawali dengan 62")
            }

        }
        ImageProfileInfoAkun.setOnClickListener {
            setImageprofile()
        }
    }
    private fun getDataUser(token : String){
        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel.userDetailLiveData.observeOnce(viewLifecycleOwner){
            if (it != null){
                if (it.imageUrl != null){
                    Glide.with(requireContext()).load(it.imageUrl).apply(
                        RequestOptions()
                    ).into(ImageProfileInfoAkun)
                }
                if (it.address != "A"){
                    infoAkun_et_alamat.setText(it.address)
                }
                if (it.city != "Surabaya"){
                    txtKota.setText(it.city)
                }
                if (!(it.phoneNumber.equals(0))){
                    infoAkun_et_nohp.setText(it.phoneNumber.toString())
                }
                infoAkun_et_nama.setText(it.fullName)

            }
        }
        viewModel.userDetailLive(token)
    }
    private fun getLokasi(){
        val items : MutableList<String> = mutableListOf()
        val items2 : MutableList<String> = mutableListOf()
        val itemsID = mutableListOf<Int>()
        val itemsID2 = mutableListOf<Int>()
        var pilihanID: Int

        val viewModel2 = ViewModelProvider(requireActivity())[LokasiViewModel::class.java]
        viewModel2.provinsiLiveData.observe(viewLifecycleOwner){
            if (it != null){
                it.provinsi.forEach {
                    items.add(it.nama)
                    itemsID.add(it.id)
                }
                Log.d("provinsias", items.toString())
            }
        }
        viewModel2.kotaLiveData.observe(viewLifecycleOwner){
            if (it != null){
                it.kotaKabupaten.forEach {
                    items2.add(it.nama)
                    itemsID2.add(it.id)
                }
            }
        }
        viewModel2.getProvinsi()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_kategori, items)

        (textFieldProvinsi.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        (textFieldProvinsi.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                pilihanID = itemsID[position]
                textFieldKota.isEnabled = true
                Log.d("idprovinsi", pilihanID.toString())
                items2.clear()
                viewModel2.getKota(pilihanID)
                val adapter2 = ArrayAdapter(requireContext(), R.layout.list_kategori, items2)
                (textFieldKota.editText as? AutoCompleteTextView)?.setAdapter(adapter2)



            }
        (textFieldKota.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                pilihan = txtKota.text.toString()
                Log.d("idprovinsi", pilihan.toString())





            }
            }
    fun profile(token: String,nama : String, kota : String, alamat : String, nohp : String){
        val viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        viewModel.profileLiveData.observe(requireActivity()){
            when (it) {
                "200" -> {
                    toast(requireContext(), "Berhasil ubah profile")
                }
                "400" -> toast(requireContext(), "Email telah digunakan")
                "500" -> alertDialog(requireContext(), "Masalah pada server", "Mohon coba lagi nanti") {}
                else -> alertDialog(requireContext(), "Update gagal", "Tidak ada koneksi internet") {}
            }
        }
        if (cek == 1){
            viewModel.profileLiveData(token, nama, kota, alamat,nohp,body)
        }



    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setImageprofile() {
        val cameraIntent = Intent(Intent.ACTION_GET_CONTENT)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 2000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 2000 && data != null){
            val uri = data.data
            ImageProfileInfoAkun.setImageURI(uri)
            setDataImagee(uri!!)
            cek = 1


        }
//        else {
//        }
    }
//
private fun setDataImagee(it : Uri){
        val contentResolver = requireActivity().contentResolver
        // image/png or jpeg or gif
        val type = contentResolver.getType(it)
        // temp-712793019827391820739.tmp < nano time, directory
        // akan terbuat secara otomatis kalau value nya null,> akan di simpan dalam dir cache
        val tempFile = File.createTempFile("temp-", null, null)
        val inputstream = contentResolver.openInputStream(it)
        tempFile.outputStream().use {
            inputstream?.copyTo(it)
        }
        val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
        body =
            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
    }


//    private fun isPermissionsAllowed(): Boolean {
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//    }

//    fun askForPermissions(): Boolean {
//        if (!isPermissionsAllowed()) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                showPermissionDeniedDialog()
//            } else {
//                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2000)
//            }
//            return false
//        }
//        return true
//    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
//        when (requestCode) {
//            2000 -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission is granted, you can perform your operation here
//                } else {
//                    // permission is denied, you can ask for permission again, if you want
//                    //  askForPermissions()
//                }
//                return
//            }
//        }
    }

//    private fun showPermissionDeniedDialog() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Permission Denied")
//            .setMessage("Permission is denied, Please allow permissions from App Settings.")
//            .setPositiveButton("App Settings"
//            ) { _, _ ->
//                // send to app settings if permission is denied permanently
//                val intent = Intent()
//                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                val uri = Uri.fromParts("package", "andlima.group3.secondhand", null)
//                intent.data = uri
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel",null)
//            .show()
//    }



}