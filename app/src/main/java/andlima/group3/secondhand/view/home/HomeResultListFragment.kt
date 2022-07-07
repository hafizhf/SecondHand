package andlima.group3.secondhand.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.navigateToDetailProduct
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.view.adapter.ProductResultAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeResultListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_result_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestCode = arguments?.getInt("REQUEST_CODE")
        val searchKeyword = arguments?.getString("SEARCH_KEYWORD")

        val searchResultTitle: TextView = requireView().findViewById(R.id.tv_search_result)
        val resultTitle: TextView = requireView().findViewById(R.id.tv_result_title)

        if (searchKeyword != null) {
            resultTitle.text = searchKeyword
            getResult(1)
        }

        if (requestCode != null) {
            resultTitle.text = getResultTitle(requestCode)
            getResult(requestCode)
        }
    }

    private fun getResultTitle(requestCode: Int?): String {
        return when (requestCode) {
            0 -> "Semua Produk"
            96 -> "Elektonik"
            97 -> "Komputer dan Aksesoris"
            98 -> "Handphone dan Aksesoris"
            99 -> "Pakaian Pria"
            100 -> "Sepatu Pria"
            101 -> "Tas Pria"
            102 -> "Aksesoris Fashion"
            103 -> "Kesehatan"
            104 -> "Hobi dan Koleksi"
            105 -> "Makanan dan Minuman"
            106 -> "Perawatan dan Kecantikan"
            107 -> "Perlengkapan Rumah"
            108 -> "Pakaian Wanita"
            109 -> "Pakaian Muslim"
            110 -> "Fashion Bayi dan Anak"
            111 -> "Ibu dan Bayi"
            112 -> "Sepatu Wanita"
            113 -> "Tas Wanita"
            114 -> "Otomotif"
            115 -> "Olahraga dan Outdoor"
            116 -> "Buku dan Alat Tulis"
            117 -> "Voucher"
            118 -> "Souvenir dan Pesta"
            119 -> "Fotografi"
            else -> "Hasil Pencarian"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getResult(requestCode: Int?, searchKeyword: String? = null) {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_result_list)
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        val adapter = ProductResultAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeResultListFragment_to_detailFragment)
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        when (requestCode) {
            // Semua
            0 -> {
                viewModel.getAllProducts()
                viewModel.allProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Elektronik
            96 -> {
                viewModel.getElectronicProducts()
                viewModel.electronicProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Komputer dan Aksesoris
            97 -> {
                viewModel.getComputerProducts()
                viewModel.computerProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Handphone dan Aksesoris
            98 -> {
                viewModel.getPhoneProducts()
                viewModel.phoneProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Pakaian Pria
            99 -> {
                viewModel.getManClothesProducts()
                viewModel.manClothesProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Sepatu Pria
            100 -> {
                viewModel.getManShoesProducts()
                viewModel.manShoesProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Tas Pria
            101 -> {
                viewModel.getManBagProducts()
                viewModel.manBagProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Aksesoris Fashion
            102 -> {
                viewModel.getAccessoriesFashionProducts()
                viewModel.accessoriesFashionProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Kesehatan
            103 -> {
                viewModel.getHealthProducts()
                viewModel.healthProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Hobi dan Koleksi
            104 -> {
                viewModel.getHobbyProducts()
                viewModel.hobbyProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Makanan dan Minuman
            105 -> {
                viewModel.getFoodProducts()
                viewModel.foodProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Perawatan dan Kecantikan
            106 -> {
                viewModel.getBeautyProducts()
                viewModel.beautyProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Perlengkapan Rumah
            107 -> {
                viewModel.getHomeSuppliesProducts()
                viewModel.homeSuppliesProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Pakaian Wanita
            108 -> {
                viewModel.getWomanClothesProducts()
                viewModel.womanClothesProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Pakaian Muslim
            109 -> {
                viewModel.getMuslimFashionProducts()
                viewModel.muslimFashionProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Fashion Bayi dan Anak
            110 -> {
                viewModel.getKidFashionProducts()
                viewModel.kidFashionProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Ibu dan Bayi
            111 -> {
                viewModel.getMotherChildProducts()
                viewModel.motherChildProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Sepatu Wanita
            112 -> {
                viewModel.getWomanShoesProducts()
                viewModel.womanShoesProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Tas Wanita
            113 -> {
                viewModel.getWomanBagProducts()
                viewModel.womanBagProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Otomotif
            114 -> {
                viewModel.getAutomotiveProducts()
                viewModel.automotiveProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Olahraga dan Outdoor
            115 -> {
                viewModel.getSportsProducts()
                viewModel.sportsProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Buku dan Alat Tulis
            116 -> {
                viewModel.getBooksProducts()
                viewModel.booksProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Voucher
            117 -> {
                viewModel.getVoucherProducts()
                viewModel.voucherProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Souvenir dan Pesta
            118 -> {
                viewModel.getSouvenirProducts()
                viewModel.souvenirProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Fotografi
            119 -> {
                viewModel.getPhotographyProducts()
                viewModel.photographyProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {

                        }
                    } else {

                    }
                })
            }
            // Search result
            else -> {
                viewModel.getAllProducts()
                viewModel.allProducts.observe(this, {
                    if (it != null) {
                        if (searchKeyword != null) {

                            val listProduct = it
//                            val listProductName : MutableList<String> = mutableListOf()


//                            listProduct.forEach { item ->
//                                if (item.name != null) {
//                                    listProductName.add(item.name)
//                                }
//                            }

                            val a = listProduct
                            val b = a.filter { item ->
                                item.name.contains(searchKeyword, ignoreCase = true)
                            }
                            Log.d("HASIL", b.toString())

//                            val listProductNameFiltered = listProductName.filter { item ->
//                                item.contains(searchKeyword, ignoreCase = true)
//                            }

                            if (searchKeyword != "") {
                                adapter.setProductData(b)
                            } else {
                                adapter.setProductData(emptyList())
                            }
                            adapter.notifyDataSetChanged()

                        } else {

                        }
                    } else {

                    }
                })

                toast(requireContext(), "N/A")
            }
        }
    }


}