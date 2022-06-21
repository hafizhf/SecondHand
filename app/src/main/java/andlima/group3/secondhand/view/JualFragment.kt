package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.kategori.KategoriPilihan
import andlima.group3.secondhand.view.adapter.KategoriAdapter
import andlima.group3.secondhand.viewmodel.ProdukViewModel
import andlima.group3.secondhand.viewmodel.SellerViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_jual.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class JualFragment : Fragment() {
    private lateinit var imgFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = mutableListOf<String>()
        val itemsID = mutableListOf<Int>()
        val gabungan : MutableSet<KategoriPilihan>? = mutableSetOf()


        val pilihan : MutableSet<String?> = mutableSetOf()
        val pilihanID : MutableSet<Int?> = mutableSetOf()

        val listData : MutableList<String> = mutableListOf()
        val listDataID : MutableList<Int> = mutableListOf()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_kategori, items)
        val viewModel = ViewModelProvider(requireActivity()).get(ProdukViewModel::class.java)
        viewModel.kategoriLiveData.observe(viewLifecycleOwner){
            it.forEach {
                items.add(it.name)
                itemsID.add(it.id)
            }

        }
        viewModel.getKategoriLive()



        rv_kategori.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var adapterKategori = KategoriAdapter{
            listData.remove(it.nama)
            listDataID.remove(it.id)
            pilihan.remove(it.nama)
            pilihanID.remove(it.id)
            gabungan?.remove(it)
            viewModel.kategoriPilihanLiveData.value = gabungan
        }
        viewModel.getKategoiPilihanObserver().observe(viewLifecycleOwner){
            adapterKategori.setDataProduk(gabungan)
            adapterKategori.notifyDataSetChanged()
        }

        (textFieldMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (textFieldMenu.getEditText() as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
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
                for ((index, element) in pilihan.withIndex()){
                    gabungan?.add(KategoriPilihan(pilihanID.elementAt(index)!!, pilihan.elementAt(index)!!))
                }

                viewModel.kategoriPilihanLiveData.postValue(gabungan)
                viewModel.getKategoiPilihanObserver().observe(viewLifecycleOwner){
                    adapterKategori.setDataProduk(gabungan)
                    adapterKategori.notifyDataSetChanged()
                }
                toast(requireContext(), pilihanID.toString())

                rv_kategori.visibility = View.VISIBLE


            }
        adapterKategori.setDataProduk(gabungan)
        adapterKategori.notifyDataSetChanged()
        rv_kategori.adapter = adapterKategori


        btnTerbitkan.setOnClickListener {
            val nama = editNamaProduk.text.toString()
            val description = editDeskripsiProduk.text.toString()
            val basePrice = editHargaProduk.text.toString().toInt()
            val lokasi = "sementara"
            val requestFile = RequestBody.create("multipart/from-data".toMediaTypeOrNull(), imgFile)
            val foto = MultipartBody.Part.createFormData("foto", imgFile.name, requestFile)

            var userManager = UserManager(requireContext())
            userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                postProduct(it,nama, description, basePrice, listDataID,lokasi, foto)
                Log.d("AKSES TOKEN", it)
            }
        }
        imageFotoProduk.setOnClickListener {
            setImage()
        }

    }
    fun postProduct(token: String, name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String, image : MultipartBody.Part){
        val viewModel = ViewModelProvider(requireActivity()).get(SellerViewModel::class.java)
        viewModel.sellerPostProductLive.observe(viewLifecycleOwner){
            if (it != null){
                toast(requireContext(), "Berhasil menambah produk")
            }else{
                toast(requireContext(), "Gagal menambah produk")
            }
        }
        viewModel.postProductLive(token, name, description, basePrice, categoryIDs, location, image)

    }
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
            val uri = data?.data
            imgFile = File(uri?.path)
            imageFotoProduk.setImageURI(uri)
        }else {

        }
    }

    fun isPermissionsAllowed(): Boolean {
        return if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            false
        } else true
    }

    fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2000)
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
        when (requestCode) {
            2000 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                }
                return
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", "andlima.group3.secondhand", null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel",null)
            .show()
    }



}