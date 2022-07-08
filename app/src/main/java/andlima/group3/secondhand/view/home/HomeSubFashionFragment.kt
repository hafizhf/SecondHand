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
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeSubFashionFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_sub_fashion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())

        // Back button on top bar
        requireView().findViewById<ImageView>(R.id.btn_back).visibility = View.VISIBLE
        requireView().findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Go to buyer order list / cart
        requireView().findViewById<RelativeLayout>(R.id.btn_goto_cart).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeSubFashionFragment_to_cartFragment)
        }

        homeSearchView(requireView(), requireContext(), requireActivity(), this, this)
        showCartQuantity(requireView(), this, this, userManager)

        val requestCode = arguments?.getInt("REQUEST_CODE") as Int

        getDetailFashion(requestCode)
    }

    @SuppressLint("SetTextI18n")
    private fun getDetailFashion(requestCode: Int) {
        val manTitle: TextView = requireView().findViewById(R.id.tv_man_title)
        val womanTitle: TextView = requireView().findViewById(R.id.tv_woman_title)

        when (requestCode) {
            // Pakaian
            1 -> {
                manTitle.text = "Pakaian Pria"
                womanTitle.text = "Pakaian Wanita"
                ManItems(this, requireView()).getClothesPreview(this, requireContext())
                WomanItems(this, requireView()).getClothesPreview(this, requireContext())
            }
            // Sepatu
            2 -> {
                manTitle.text = "Sepatu Pria"
                womanTitle.text = "Sepatu Wanita"
                ManItems(this, requireView()).getShoesPreview(this, requireContext())
                WomanItems(this, requireView()).getShoesPreview(this, requireContext())
            }
            // Tas
            3 -> {
                manTitle.text = "Tas Pria"
                womanTitle.text = "Tas Wanita"
                ManItems(this, requireView()).getBagPreview(this, requireContext())
                WomanItems(this, requireView()).getBagPreview(this, requireContext())
            }
            else -> {
                toast(requireContext(), "N/A")
            }
        }
    }

    private class ManItems(owner: ViewModelStoreOwner, view: View) {
        private val recyclerView: RecyclerView = view.findViewById(R.id.rv_preview_man)
        private val viewModel = ViewModelProvider(owner)[BuyerViewModel::class.java]
        private val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, view, R.id.action_homeSubFashionFragment_to_detailFragment)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun getClothesPreview(owner: LifecycleOwner, context: Context) {
            recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.adapter = adapter

            viewModel.getManClothesProducts()
            viewModel.manClothesProducts.observe(owner, {
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

        @SuppressLint("NotifyDataSetChanged")
        fun getShoesPreview(owner: LifecycleOwner, context: Context) {
            recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.adapter = adapter

            viewModel.getManShoesProducts()
            viewModel.manShoesProducts.observe(owner, {
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

        @SuppressLint("NotifyDataSetChanged")
        fun getBagPreview(owner: LifecycleOwner, context: Context) {
            recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.adapter = adapter

            viewModel.getManBagProducts()
            viewModel.manBagProducts.observe(owner, {
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
    }

    private class WomanItems(owner: ViewModelStoreOwner, view: View) {
        private val recyclerView: RecyclerView = view.findViewById(R.id.rv_preview_woman)
        private val viewModel = ViewModelProvider(owner)[BuyerViewModel::class.java]
        private val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, view, R.id.action_homeSubFashionFragment_to_detailFragment)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun getClothesPreview(owner: LifecycleOwner, context: Context) {
            recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.adapter = adapter

            viewModel.getWomanClothesProducts()
            viewModel.womanClothesProducts.observe(owner, {
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

        @SuppressLint("NotifyDataSetChanged")
        fun getShoesPreview(owner: LifecycleOwner, context: Context) {
            recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.adapter = adapter

            viewModel.getWomanShoesProducts()
            viewModel.womanShoesProducts.observe(owner, {
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

        @SuppressLint("NotifyDataSetChanged")
        fun getBagPreview(owner: LifecycleOwner, context: Context) {
            recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.adapter = adapter

            viewModel.getWomanBagProducts()
            viewModel.womanBagProducts.observe(owner, {
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
    }
}