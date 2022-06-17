package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
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
import androidx.recyclerview.widget.GridLayoutManager
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
        val items = listOf("Material", "Design", "Components", "Android")
        val pilihan : MutableSet<String?> = mutableSetOf()
        val listData : MutableList<String> = mutableListOf()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_kategori, items)
        val viewModel = ViewModelProvider(requireActivity()).get(ProdukViewModel::class.java)



        rv_kategori.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var adapterKategori = KategoriAdapter{
            listData.remove(it)
            viewModel.kategoriData.postValue(listData)
        }
        viewModel.getKategoiObserver().observe(viewLifecycleOwner){
            adapterKategori.setDataProduk(listData)
            adapterKategori.notifyDataSetChanged()
        }

        (textFieldMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (textFieldMenu.getEditText() as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String? = adapter.getItem(position)
                listData.clear()
                pilihan.add(selectedValue)
                pilihan.forEach {
                    listData.add(it!!)
                }
                viewModel.kategoriData.postValue(listData)
                viewModel.getKategoiObserver().observe(viewLifecycleOwner){
                    adapterKategori.setDataProduk(listData)
                    adapterKategori.notifyDataSetChanged()
                }

                rv_kategori.visibility = View.VISIBLE


            }
        adapterKategori.setDataProduk(listData)
        adapterKategori.notifyDataSetChanged()
        rv_kategori.adapter = adapterKategori


        btnTerbitkan.setOnClickListener {
            Toast.makeText(requireContext(), "$pilihan", Toast.LENGTH_SHORT).show()

        }

    }


}