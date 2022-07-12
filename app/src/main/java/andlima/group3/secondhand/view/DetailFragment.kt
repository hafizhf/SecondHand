package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.*
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.detail.ProductDataForBid
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.produk.ProdukPreview
import andlima.group3.secondhand.view.bottomsheet.DetailBottomDialogFragment
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

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
        val product = arguments?.getParcelable("PREVIEW2") as ProdukPreview?
        Log.d("previewproduk", product.toString())
        if (product?.name.isNullOrBlank()){
            getData(productID)

        }else{
            setDataPreview(product!!)
        }
    }
    private fun setDataPreview(produkPreview: ProdukPreview){
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
        productImage.setImageURI(produkPreview.imageUrl)
        productName.text = produkPreview.name
        if (produkPreview.categories.isNotEmpty()) {
            productCategory.text = produkPreview.categories.elementAt(0).nama
        } else {
            productCategory.text = "Uncategorized"
        }
        productPrice.text = "Rp " + produkPreview.basePrice
        productDesc.text = produkPreview.description

        Glide.with(this).load(produkPreview.sellerImage).into(productSellerImage)
        productSellerName.text = produkPreview.sellerName
        productSellerAddress.text = produkPreview.location
        btn_saya_tertarik_ingin_nego.text = "Preview"
        btn_saya_tertarik_ingin_nego.isEnabled = false

    }

    @SuppressLint("SetTextI18n")
    private fun showProductData(data: ProductDetailItemResponse) {
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
        if (data.categories.isNotEmpty()) {
            productCategory.text = data.categories[0].name
        } else {
            productCategory.text = "Uncategorized"
        }
        productPrice.text = "Rp " + data.basePrice.toString()
        productDesc.text = data.description

        Glide.with(this).load(data.user.imageUrl).into(productSellerImage)
        productSellerName.text = data.user.fullName
        productSellerAddress.text = data.user.address
    }

    @SuppressLint("SetTextI18n")
    private fun getData(id: Int) {
        val btnImInterested : Button = requireView().findViewById(R.id.btn_saya_tertarik_ingin_nego)
        val btnEditProduct : Button = requireView().findViewById(R.id.btn_goto_edit_product)

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.getDetailProduct(id)
        viewModel.detailProduct.observe(this, { data ->
            if (data != null) {
                showProductData(data)

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

        userManager.accessTokenFlow.asLiveData().observeOnce(this, { accessToken ->
            viewModel.checkProductOwnedBySeller(accessToken, id)
            viewModel.isSellerProduct.observe(this, {
                if (it) {
                    btnImInterested.visibility = View.GONE
                    btnEditProduct.visibility = View.VISIBLE

                    btnEditProduct.setOnClickListener {
                        // Disini arahin ke edit produk
                        toast(requireContext(), "WIP: Edit product")
                    }

                } else {
                    btnImInterested.visibility = View.VISIBLE
                    btnEditProduct.visibility = View.GONE
                }
            })
        })
    }

    private fun showBottomSheetDialogFragment(data: ProductDataForBid) {
        val bottomSheet = DetailBottomDialogFragment()
        val dataForBid = bundleOf("BID_PRODUCT" to data)
        bottomSheet.arguments = dataForBid
        bottomSheet.show(parentFragmentManager, DetailBottomDialogFragment.TAG)

        DetailBottomDialogFragment.bidSuccess.observe(this, {
            if (it) {
                quickNotifyDialog(requireView(), "Harga tawaranmu berhasil dikirim ke penjual")
            }
        })
    }
}