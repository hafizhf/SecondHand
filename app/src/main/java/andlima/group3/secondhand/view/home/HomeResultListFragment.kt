package andlima.group3.secondhand.view.home

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.*
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.view.adapter.ProductResultAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeResultListFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_result_list, container, false)
    }

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

                homeSearchView(requireView(), requireContext(), requireActivity(), this, this, true)
                showCartQuantity(requireView(), this, this, userManager)
                showWishlistQuantity(requireView(), this, this, userManager)

                // Back button on top bar
                requireView().findViewById<ImageView>(R.id.btn_back).visibility = View.VISIBLE
                requireView().findViewById<ImageView>(R.id.btn_back).setOnClickListener {
                    parentFragmentManager.popBackStack()
                }

                // Go to buyer order list / cart
                requireView().findViewById<RelativeLayout>(R.id.btn_goto_cart).setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_homeResultListFragment_to_cartFragment)
                }

                // Go to buyer wishlist
                requireView().findViewById<RelativeLayout>(R.id.btn_goto_wishlist).setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_homeResultListFragment_to_homeWishlistFragment)
                }

                val requestCode = arguments?.getInt("REQUEST_CODE")
                val searchKeyword = arguments?.getString("SEARCH_KEYWORD")

                val searchResultTitle: TextView = requireView().findViewById(R.id.tv_search_result)
                val resultTitle: TextView = requireView().findViewById(R.id.tv_result_title)

                if (searchKeyword != null) {
                    resultTitle.text = getResultTitle(-1, searchKeyword)
                    getResult(-1, searchKeyword)
                } else {
                    if (requestCode != null) {
                        resultTitle.text = getResultTitle(requestCode)
                        getResult(requestCode)
                    }
                }
            }
        })
    }

    private fun getResultTitle(requestCode: Int?, searchKeyword: String? = null): String {
        return when (requestCode) {
            -1 -> searchKeyword!!
            0 -> "Semua Produk"
            1 -> "Elektonik"
            2 -> "Komputer dan Aksesoris"
            3 -> "Handphone dan Aksesoris"
            4 -> "Pakaian Pria"
            5 -> "Sepatu Pria"
            6 -> "Tas Pria"
            7 -> "Aksesoris Fashion"
            8 -> "Kesehatan"
            9 -> "Hobi dan Koleksi"
            10 -> "Makanan dan Minuman"
            11 -> "Perawatan dan Kecantikan"
            12 -> "Perlengkapan Rumah"
            13 -> "Pakaian Wanita"
            14 -> "Pakaian Muslim"
            15 -> "Fashion Bayi dan Anak"
            16 -> "Ibu dan Bayi"
            17 -> "Sepatu Wanita"
            18 -> "Tas Wanita"
            19 -> "Otomotif"
            20 -> "Olahraga dan Outdoor"
            21 -> "Buku dan Alat Tulis"
            22 -> "Voucher"
            23 -> "Souvenir dan Pesta"
            24 -> "Fotografi"
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
            // Search result
            -1 -> {
//                toast(requireContext(), searchKeyword!!)
                viewModel.getSearchResult(searchKeyword!!)
                viewModel.searchResult.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            adapter.setProductData(it)
                            adapter.notifyDataSetChanged()
                        } else {
                            toast(requireContext(), "No result found")
                        }
                    } else {
                        toast(requireContext(), "No result found")
                    }
                })

            }
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
            1 -> {
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
            2 -> {
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
            3 -> {
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
            4 -> {
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
            5 -> {
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
            6 -> {
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
            7 -> {
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
            8 -> {
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
            9 -> {
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
            10 -> {
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
            11 -> {
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
            12 -> {
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
            13 -> {
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
            14 -> {
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
            15 -> {
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
            16 -> {
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
            17 -> {
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
            18 -> {
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
            19 -> {
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
            20 -> {
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
            21 -> {
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
            22 -> {
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
            23 -> {
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
            24 -> {
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
                toast(requireContext(), "N/A")
            }
        }
    }


}