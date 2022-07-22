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
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeMoreCategoryFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_more_category, container, false)
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

                homeSearchView(requireView(), requireContext(), requireActivity(), this, this)
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
                        .navigate(R.id.action_homeMoreCategoryFragment_to_cartFragment)
                }

                // Go to buyer wishlist
                requireView().findViewById<RelativeLayout>(R.id.btn_goto_wishlist).setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_homeMoreCategoryFragment_to_homeWishlistFragment)
                }

                getBookPreview()
                getSouvenirPreview()
                getPhotographyPreview()

                actionButtonMore(
                    R.id.btn_goto_book_more,
                    R.id.action_homeMoreCategoryFragment_to_homeResultListFragment,
                    21
                )
                actionButtonMore(
                    R.id.btn_goto_souvenir_more,
                    R.id.action_homeMoreCategoryFragment_to_homeResultListFragment,
                    23
                )
                actionButtonMore(
                    R.id.btn_goto_photography_more,
                    R.id.action_homeMoreCategoryFragment_to_homeResultListFragment,
                    24
                )
            }
        })
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

    @SuppressLint("NotifyDataSetChanged")
    private fun getBookPreview() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_books)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeMoreCategoryFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.booksProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                }
//                else {
//
//                }
            }
        })
        viewModel.getBooksProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getSouvenirPreview() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_souvenir)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeMoreCategoryFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.souvenirProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                }
//                else {
//
//                }
            }
        })
        viewModel.getSouvenirProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPhotographyPreview() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_photography)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeMoreCategoryFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.photographyProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                }
//                else {
//
//                }
            }
        })
        viewModel.getPhotographyProducts()
    }
}