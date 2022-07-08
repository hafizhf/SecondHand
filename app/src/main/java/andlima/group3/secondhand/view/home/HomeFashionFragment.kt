package andlima.group3.secondhand.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.homeSearchView
import andlima.group3.secondhand.func.navigateToDetailProduct
import andlima.group3.secondhand.func.showCartQuantity
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFashionFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fashion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())

        homeSearchView(requireView(), requireContext(), requireActivity(), this, this)
        showCartQuantity(requireView(), this, this, userManager)

        // Back button on top bar
        requireView().findViewById<ImageView>(R.id.btn_back).visibility = View.VISIBLE
        requireView().findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Go to buyer order list / cart
        requireView().findViewById<RelativeLayout>(R.id.btn_goto_cart).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFashionFragment_to_cartFragment)
        }

        getClothesProduct()
        getShoesProduct()
        getBagProduct()
        getMuslimFashionProduct()
        getKidFashionProduct()
        getAccessoriesFashionProduct()

        actionButtonMore(
            R.id.btn_goto_clothes_more,
            R.id.action_homeFashionFragment_to_homeSubFashionFragment,
            1
        )
        actionButtonMore(
            R.id.btn_goto_shoes_more,
            R.id.action_homeFashionFragment_to_homeSubFashionFragment,
            2
        )
        actionButtonMore(
            R.id.btn_goto_bag_more,
            R.id.action_homeFashionFragment_to_homeSubFashionFragment,
            3
        )
        actionButtonMore(
            R.id.btn_goto_muslim_more,
            R.id.action_homeFashionFragment_to_homeResultListFragment,
            109
        )
        actionButtonMore(
            R.id.btn_goto_fashion_kid_more,
            R.id.action_homeFashionFragment_to_homeResultListFragment,
            110
        )
        actionButtonMore(
            R.id.btn_goto_accessories_more,
            R.id.action_homeFashionFragment_to_homeResultListFragment,
            102
        )
    }

    private fun actionButtonMore(textId: Int, navigationId: Int, requestCode: Int = 0) {
        val btnGotoMore: TextView = requireView().findViewById(textId)
        btnGotoMore.setOnClickListener {
            if (requestCode != 0) {
                val code = bundleOf("REQUEST_CODE" to requestCode)
                Navigation.findNavController(view!!).navigate(navigationId, code)
            } else {
                Navigation.findNavController(view!!).navigate(navigationId)
            }
        }
    }

    private fun getClothesProduct() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_clothes)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFashionFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.manClothesProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getManClothesProducts()
    }

    private fun getShoesProduct() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_shoes)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFashionFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.manShoesProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getManShoesProducts()
    }

    private fun getBagProduct() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_bag)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFashionFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.manBagProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getManBagProducts()
    }

    private fun getMuslimFashionProduct() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_fashion_muslim)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFashionFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.muslimFashionProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getMuslimFashionProducts()
    }

    private fun getKidFashionProduct() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_for_kids)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFashionFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.kidFashionProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getKidFashionProducts()
    }

    private fun getAccessoriesFashionProduct() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_accessories_fashion)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFashionFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.accessoriesFashionProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getAccessoriesFashionProducts()
    }
}