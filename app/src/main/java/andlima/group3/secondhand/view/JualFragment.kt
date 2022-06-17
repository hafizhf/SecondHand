package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.model.kategori.KategoriPilihan
import andlima.group3.secondhand.view.adapter.KategoriAdapter
import andlima.group3.secondhand.viewmodel.ProdukViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_jual.*


class JualFragment : Fragment() {
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
            Toast.makeText(requireContext(), "$pilihan", Toast.LENGTH_SHORT).show()

        }

    }


}