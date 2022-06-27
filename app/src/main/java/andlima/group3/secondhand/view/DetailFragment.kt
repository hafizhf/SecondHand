package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.isUserLoggedIn
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.detail.ProductDataForBid
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.view.bottomsheet.DetailBottomDialogFragment
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())

        val productID = arguments?.getInt("SELECTED_ID") as Int
        getData(productID)
    }

    @SuppressLint("SetTextI18n")
    private fun showProductData(data: BuyerProductDetail) {
        // Get view id -----------------------------------------------------------------------------
        val productImage : ImageView = view!!.findViewById(R.id.iv_detail_product_image)
        val productName : TextView = view!!.findViewById(R.id.tv_detail_product_name)
        val productCategory : TextView = view!!.findViewById(R.id.tv_detail_product_category)
        val productPrice : TextView = view!!.findViewById(R.id.tv_detail_product_price)
        val productDesc : TextView = view!!.findViewById(R.id.tv_detail_product_description)

        val productSellerImage : ImageView = view!!.findViewById(R.id.iv_detail_product_seller_image)
        val productSellerName : TextView = view!!.findViewById(R.id.tv_detail_product_seller_name)
        val productSellerAddress : TextView = view!!.findViewById(R.id.tv_detail_product_seller_address)

        // Set data to view ------------------------------------------------------------------------
        Glide.with(this).load(data.imageUrl).into(productImage)
        productName.text = data.name
//        productCategory.text = "Tipe data categories masih Any"
        productCategory.text = data.categories[0].name
        productPrice.text = "Rp " + data.basePrice.toString()
        productDesc.text = data.description
    }

    @SuppressLint("SetTextI18n")
    private fun getData(id: Int) {
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.getDetailProduct(id)
        viewModel.detailProduct.observe(this, { data ->
            if (data != null) {
                showProductData(data)

                val btnImInterested : Button = requireView().findViewById(R.id.btn_saya_tertarik_ingin_nego)
                if (isUserLoggedIn(userManager)) {
                    btnImInterested.setOnClickListener {
                        showBottomSheetDialogFragment(
                            ProductDataForBid(data.id, data.name, data.basePrice, data.imageUrl)
                        )
                    }
                } else {
                    btnImInterested.text = "Login untuk bisa melakukan penawaran"
                    btnImInterested.isEnabled = false
                    btnImInterested.isClickable = false
                }
            } else {
                alertDialog(requireContext(), "Get product data failed", "null") {}
            }
        })
    }

    private fun showBottomSheetDialogFragment(data: ProductDataForBid) {
        val bottomSheet = DetailBottomDialogFragment()
        val dataForBid = bundleOf("BID_PRODUCT" to data)
        bottomSheet.arguments = dataForBid
        bottomSheet.show(parentFragmentManager, DetailBottomDialogFragment.TAG)
    }
}