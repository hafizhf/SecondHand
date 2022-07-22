@file:Suppress("IfThenToSafeAccess", "RemoveRedundantCallsOfConversionMethods",
    "RemoveRedundantCallsOfConversionMethods"
)

package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.showPageLoading
import andlima.group3.secondhand.func.showPassword
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.viewmodel.LokasiViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
    private var pilihan = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val targetEditText: EditText = requireView().findViewById(R.id.register_et_password)
        val btnShowPassword: ImageView = requireView().findViewById(R.id.btn_show_pwd)
        btnShowPassword.setOnClickListener {
            showPassword(targetEditText, btnShowPassword)
        }

        register_arrowback.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        register_tv_kembalikelogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
//        register_arrowback.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
//        }


//
        register_btn_daftar.setOnClickListener {
            val namaLengkap = register_et_namalengkap.text.toString()
            val email = register_et_email.text.toString()
            val password =register_et_password.text.toString()
            val alamat = alamatFinal.text.toString()
            val nohp = nohpFinal.text.toString()

            if (nohp.startsWith("62")){
                if (namaLengkap.isNotBlank() && email.isNotBlank() && password.isNotBlank() && alamat.isNotBlank()    && pilihan.isNotBlank()){
                    showPageLoading(requireView(), true, "Registering")
                    register(namaLengkap, email, password,alamat,pilihan,nohp)
                }else{
                    toast(requireContext(), "Isi semua data!")
                }
            }else{
                toast(requireContext(), "Nomor HP harus diawali dengan 62")

            }

        }
        getLokasi()
    }

    private fun getLokasi(){
        val items : MutableList<String> = mutableListOf()
        val items2 : MutableList<String> = mutableListOf()
        val itemsID = mutableListOf<Int>()
        val itemsID2 = mutableListOf<Int>()
        var pilihanID = 0

        val viewModel2 = ViewModelProvider(requireActivity())[LokasiViewModel::class.java]
        viewModel2.provinsiLiveData.observe(viewLifecycleOwner){
            if (it != null){
                it.provinsi.forEach { provinsi ->
                    items.add(provinsi.nama)
                    itemsID.add(provinsi.id)
                }
                Log.d("provinsias", items.toString())
            }
        }
        viewModel2.kotaLiveData.observe(viewLifecycleOwner){
            if (it != null){
                it.kotaKabupaten.forEach { city ->
                    items2.add(city.nama)
                    itemsID2.add(city.id)
                }
            }
        }
        viewModel2.getProvinsi()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_kategori, items)

        (provinsiFinal.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        (provinsiFinal.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                pilihanID = itemsID[position]
                kotaFinal.isEnabled = true
                Log.d("idprovinsi", pilihanID.toString())
                items2.clear()
                viewModel2.getKota(pilihanID)
                val adapter2 = ArrayAdapter(requireContext(), R.layout.list_kategori, items2)
                (kotaFinal.editText as? AutoCompleteTextView)?.setAdapter(adapter2)



            }
        (kotaFinal.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                pilihan = txtKotaFinal.text.toString()
                Log.d("idprovinsi", pilihan.toString())
            }
    }

    private fun register(namaLengkap : String, email : String, password : String, alamat : String, kota : String, nohp : String){
        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel.registerLiveData.observe(requireActivity()){
            showPageLoading(requireView(), false)
            when (it) {
                "201" -> {
                    toast(requireContext(), "Register success")
                    view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)

                }
                "400" -> toast(requireContext(), "Email already exist")
                "500" -> toast(requireContext(), "Internal Service Error")
                else -> alertDialog(requireContext(), "Login failed", "No connection") {}
            }
        }

        viewModel.registerLiveData(namaLengkap, email, password, nohp, alamat, kota)

    }



}