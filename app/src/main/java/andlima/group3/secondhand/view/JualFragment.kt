@file:Suppress("NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "RemoveRedundantQualifierName",
    "RemoveRedundantQualifierName"
)

package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.*
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.home.newhome.Category
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.kategori.KategoriPilihan
import andlima.group3.secondhand.model.produk.ProdukPreview
import andlima.group3.secondhand.view.adapter.KategoriAdapter
import andlima.group3.secondhand.viewmodel.ProdukViewModel
import andlima.group3.secondhand.viewmodel.SellerViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_jual.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

import android.widget.ImageButton





class JualFragment : Fragment() {
    private var cek = 0
    var idEdit = 0
    lateinit var body2: MultipartBody.Part
    lateinit var userManager: UserManager
    private var uriGambar : Uri? = null
    private var gambarSeller : String? = null
    private var namaSeller : String = ""
    private val gabungan : MutableSet<KategoriPilihan> = mutableSetOf()
    private val listDataID : MutableList<Int> = mutableListOf()
    private val pilihanID : MutableSet<Int?> = mutableSetOf()
    private var statusProduk : String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jual, container, false)
    }

    private fun btnpreview(){
        if (btnPreview.text == "Preview"){
            btnPreview.setOnClickListener {
                val nama = editNamaProduk.text.toString()
                val description = editDeskripsiProduk.text.toString()
                val basePrice = editHargaProduk.text.toString()
                val lokasi = "Preview"

                if (nama.isNotBlank() && description.isNotBlank() && basePrice.isNotBlank() && gabungan.isNotEmpty() && uriGambar != null  && namaSeller.isNotBlank()){
                    val code = bundleOf("PREVIEW2" to ProdukPreview(nama,basePrice,gabungan,description,
                        uriGambar!!, lokasi, gambarSeller, namaSeller))
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_jualFragment_to_detailFragment, code)
                }
            }
        }else{
            btnPreview.setOnClickListener {
                alertDialog(requireContext(), "Hapus Produk", "Anda yakin ingin menghapus produk?") {
                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        deleteProduct(it)
                        }
                }


            }
        }

    }
    private fun deleteProduct(token: String){
        val viewModel2 = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        viewModel2.sellerDeleteProductLive.observe(viewLifecycleOwner){
            if (it != null){
                toast(requireContext(), "Produk telah dihapus")
                Log.d("PESANDARIDELETE", it.toString())
                view?.findNavController()?.navigate(R.id.action_jualFragment_to_homeFragment)
            }
        }
        viewModel2.deleteProductLive(token, idEdit)
    }
    private fun btnterbitkan(){
        val viewModel2 = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        if (btnTerbitkan.text == "Edit"){
            btnTerbitkan.setOnClickListener {
                val nama = editNamaProduk.text.toString()
                val description = editDeskripsiProduk.text.toString()
                val basePrice = editHargaProduk.text.toString().toInt()
                var lokasi = "sementara"

                if (nama.isNotBlank() && description.isNotBlank() && basePrice.toString().isNotBlank()  && listDataID.isNotEmpty() && statusProduk != null ){
                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        viewModel2.userDetailLiveData.observe(viewLifecycleOwner){
                            lokasi = it.city

                        }
                        viewModel2.userDetailLive(it)
                        Handler(Looper.getMainLooper()).postDelayed({
                            editProduct(it,nama, description, basePrice, listDataID,lokasi)


                        }, 1000)

                        Log.d("AKSES TOKEN", it)
                    }
                }else{
                    toast(requireContext(), "Lengkapi semua data produk")

                }
            }


        }else{
            btnTerbitkan.setOnClickListener {
                val nama = editNamaProduk.text.toString()
                val description = editDeskripsiProduk.text.toString()
                val basePrice = editHargaProduk.text.toString().toInt()

                var lokasi = "sementara"

                if (nama.isNotBlank() && description.isNotBlank() && basePrice.toString().isNotBlank()  && listDataID.isNotEmpty()){
                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        viewModel2.userDetailLiveData.observe(viewLifecycleOwner){
                            lokasi = it.city

                        }
                        viewModel2.userDetailLive(it)
                        Handler(Looper.getMainLooper()).postDelayed({
                            postProduct(it,nama, description, basePrice, listDataID,lokasi)

                        }, 1000)

                        Log.d("AKSES TOKEN", it)
                    }
                }else{
                    toast(requireContext(), "Lengkapi semua data produk")

                }
            }
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    fun kategori(listkat : List<Category>?){
        rv_kategori.visibility = View.VISIBLE

        val viewModel = ViewModelProvider(requireActivity())[ProdukViewModel::class.java]
        val items = mutableListOf<String>()
        val itemsID = mutableListOf<Int>()

        val pilihan : MutableSet<String?> = mutableSetOf()

        val listData : MutableList<String> = mutableListOf()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_kategori, items)
        val adapterKategori = KategoriAdapter{
            listData.remove(it.nama)
            listDataID.remove(it.id)
            pilihan.remove(it.nama)
            pilihanID.remove(it.id)
            gabungan.remove(it)
            viewModel.kategoriPilihanLiveData.value = gabungan
        }
        if (listkat != null){
            listkat.forEach {
                gabungan.add(KategoriPilihan(it.id, it.name))
            }
            adapterKategori.setDataProduk(gabungan)
            rv_kategori.visibility = View.VISIBLE
            rv_kategori.adapter = adapterKategori

        }
        viewModel.kategoriLiveData.observe(viewLifecycleOwner){
            it.forEach {
                items.add(it.name)
                itemsID.add(it.id)
            }

        }
        viewModel.getKategoriLive()

        rv_kategori.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.getKategoiPilihanObserver().observe(viewLifecycleOwner){
            adapterKategori.setDataProduk(gabungan)
            adapterKategori.notifyDataSetChanged()
        }

        (textFieldMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (textFieldMenu.editText as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue: String? = adapter.getItem(position)
                listData.clear()
                listDataID.clear()
                pilihan.add(selectedValue)
                pilihanID.add(itemsID[position])
                pilihan.forEach { nama ->
                    listData.add(nama!!)
                }
                pilihanID.forEach { id ->
                    listDataID.add(id!!)
                }
                for ((index, _) in pilihan.withIndex()){
                    gabungan.add(KategoriPilihan(pilihanID.elementAt(index)!!, pilihan.elementAt(index)!!))
                }

                viewModel.kategoriPilihanLiveData.postValue(gabungan)
                viewModel.getKategoiPilihanObserver().observe(viewLifecycleOwner){
                    adapterKategori.setDataProduk(gabungan)
                    adapterKategori.notifyDataSetChanged()
                }

                rv_kategori.visibility = View.VISIBLE


            }
        adapterKategori.setDataProduk(gabungan)
        rv_kategori.adapter = adapterKategori




    }
    private fun gambar(){
        imageFotoProduk.setOnClickListener {
            setImage()
        }
    }

    private fun statusProduk(){
        val items : MutableList<String> = mutableListOf("available", "sold")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_kategori, items)
        (textFieldStatus.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (textFieldStatus.editText as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue: String? = adapter.getItem(position)
                statusProduk = selectedValue


            }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())




        MarketApplication.isConnected.observe(this, { isConnected ->
            val connectionInterfaceHandler: LinearLayout = requireView()
                .findViewById(R.id.dialog_require_internet)

            if (!isConnected) {
                connectionInterfaceHandler.layoutParams.height = getDeviceScreenHeight(requireActivity())
                connectionInterfaceHandler.visibility = View.VISIBLE
            } else {
                connectionInterfaceHandler.visibility = View.GONE

                val requireLoginView: LinearLayout = requireView().findViewById(R.id.dialog_require_login)
                val requireLoginButton: Button = requireView().findViewById(R.id.btn_require_goto_login)
                val isLoggedIn = requireLogin(
                    requireActivity(),
                    requireContext(),
                    userManager,
                    requireLoginView,
                    requireLoginButton
                )

                if (isLoggedIn) {
                    btnBackJual.setOnClickListener {
                        view.findNavController().navigate(R.id.action_jualFragment_to_detailFragment)
                    }
                    val dataProduk = arguments?.getParcelable("DATAPRODUK") as ProductDetailItemResponse?
                    Log.d("dataprodukedit", dataProduk.toString())

                    if (dataProduk != null){
                        editNamaProduk.setText(dataProduk.name)
                        editDeskripsiProduk.setText(dataProduk.description)
                        editHargaProduk.setText(dataProduk.basePrice.toString())
                        txtStatus.setText(dataProduk.status)
                        gambar()
                        statusProduk()
                        kategori(dataProduk.categories)
                        if (uriGambar == null){
                            Glide.with(this).load(dataProduk.imageUrl).into(imageFotoProduk)

                        }
                        dataProduk.categories.forEach {
                            listDataID.add(it.id)
                            pilihanID.add(it.id)
                        }
                        idEdit = dataProduk.id
                        btnTerbitkan.text = "Edit"
                        btnPreview.visibility = View.GONE
                        linearStatusProdukJual.visibility = View.VISIBLE





                    }else{
                        kategori(null)
                    }
                    if (uriGambar!= null){
                        imageFotoProduk.setImageURI(uriGambar)
                    }
                    gambar()
                    btnterbitkan()
                    btnpreview()
                    val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
                    viewModel.clearLiveData.observe(viewLifecycleOwner){
                        if (it == "yes"){
                            clearData()
                            viewModel.clearLiveData.value = "no"
                        }
                    }





                    val userManager = UserManager(requireContext())

                    val viewModel2 = ViewModelProvider(requireActivity())[UserViewModel::class.java]

                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        viewModel2.userDetailLiveData.observe(viewLifecycleOwner){
                            if (it.imageUrl != null){
                                gambarSeller = it.imageUrl
                            }
                            namaSeller = it.fullName

                        }
                        viewModel2.userDetailLive(it)
                    }




                }
            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun clearData(){
        editHargaProduk.setText("")
        editDeskripsiProduk.setText("")
        editNamaProduk.setText("")
        listDataID.clear()
        pilihanID.clear()
        gabungan.clear()
        imageFotoProduk.setBackgroundResource(andlima.group3.secondhand.R.drawable.component_border_dotted)
        (imageFotoProduk as ImageButton).setImageResource(andlima.group3.secondhand.R.drawable.ic_fi_plus)
        rv_kategori.visibility = View.GONE
        txtpilihkategori.setText("Pilih Kategori")
    }

    private fun postProduct(token: String, name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String){
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        showPageLoading(requireView(),true)

        viewModel.sellerPostProductLive.observe(viewLifecycleOwner){
            if (it != null){
                toast(requireContext(), "Berhasil menambah produk")
            }else{
                toast(requireContext(), "Gagal menambah produk")
            }
            showPageLoading(requireView(), false)

        }
        viewModel.postProductLive(token, name, description, basePrice, categoryIDs, location, body2)


    }
    private fun editProduct(token: String, name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String){
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        showPageLoading(requireView(),true)

        viewModel.sellerEditProductLive.observe(viewLifecycleOwner){
            if (it != null){
                Log.d("kenapatoastgamuncul", it.toString())
                toast(requireContext(), "Berhasil mengubah produk")

            }else{
                Log.d("kenapatoastgamuncul", it.toString())

                toast(requireContext(), "Gagal mengubah produk")
            }
            showPageLoading(requireView(), false)
        }
        viewModel.patchProductLive(token,idEdit,statusProduk!!)
        if (cek == 1){
            viewModel.editProductLive(token, idEdit,name, description, basePrice, categoryIDs, location, body2)

        }else{
            viewModel.editProductLive(token, idEdit,name, description, basePrice, categoryIDs, location, null)

        }


    }
    @SuppressLint("QueryPermissionsNeeded")
    private fun setImage() {
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
            uriGambar = uri
            imageFotoProduk.setImageURI(uri)
            Log.d("kenapauri", "dsasd")
            setDataImagee(uri!!)


        }
//        else {
//        }
    }
    private fun setDataImagee(it : Uri){
        val contentResolver = requireActivity().contentResolver
        imageFotoProduk.setImageURI(it)

        // image/png or jpeg or gif

        val type = contentResolver.getType(it)

        // temp-712793019827391820739.tmp < nano time, directory

        // akan terbuat secara otomatis kalau value nya null,> akan di simpan dalam dir cache

        val tempFile = File.createTempFile("temp-", ".jpg", null)

        val inputstream = contentResolver.openInputStream(it)




        tempFile.outputStream().use {

            inputstream?.copyTo(it)

        }




        val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

        body2 =

            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        cek = 1
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
//                }
////                else {
////                    // permission is denied, you can ask for permission again, if you want
////                    //  askForPermissions()
////                }
//                return
//            }
//        }
    }

//    private fun showPermissionDeniedDialog() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Permission Denied")
//            .setMessage("Permission is denied, Please allow permissions from App Settings.")
//            .setPositiveButton("App Settings") { _, _ ->
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