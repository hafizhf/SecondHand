package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.*
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.detail.ProductDataForBid
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.produk.ProdukPreview
import andlima.group3.secondhand.view.bottomsheet.DetailBottomDialogFragment
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_jual.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart
import java.io.File

@AndroidEntryPoint
class DetailFragment : Fragment() {

    lateinit var userManager: UserManager
    private var token = ""
    lateinit var body2: MultipartBody.Part


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

        MarketApplication.isConnected.observe(this, { isConnected ->
            val connectionInterfaceHandler: LinearLayout = requireView()
                .findViewById(R.id.dialog_require_internet)

            if (!isConnected) {
                connectionInterfaceHandler.layoutParams.height = getDeviceScreenHeight(requireActivity())
                connectionInterfaceHandler.visibility = View.VISIBLE
            } else {
                connectionInterfaceHandler.visibility = View.GONE

                userManager.accessTokenFlow.asLiveData().observeForever {
                    token = it
                }

                val productID = arguments?.getInt("SELECTED_ID") as Int
                val product = arguments?.getParcelable("PREVIEW2") as ProdukPreview?
                Log.d("previewproduk", product.toString())
                if (product?.name.isNullOrBlank()){
                    getData(productID)

                }else{
                    setDataPreview(product!!)
                }
            }
        })
    }

    private fun setDataImagee(it : Uri){
        val contentResolver = requireActivity().contentResolver

        // image/png or jpeg or gif

        val type = contentResolver.getType(it)

        // temp-712793019827391820739.tmp < nano time, directory

        // akan terbuat secara otomatis kalau value nya null,> akan di simpan dalam dir cache

        val tempFile = File.createTempFile("temp-", ".jpg", null)

        val inputstream = contentResolver.openInputStream(it)




        tempFile.outputStream().use {

            inputstream?.copyTo(it)

        }




        val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

         body2 =

            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)

    }
    private fun postProduct(token: String, name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String, body2: MultipartBody.Part){
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        showPageLoading(requireView(),true)

        viewModel.sellerPostProductLive.observe(viewLifecycleOwner){
            if (it != null){
                toast(requireContext(), "Berhasil menambah produk")
                btn_goto_terbit_product.visibility = View.GONE
                layout_button.visibility = View.VISIBLE
                Handler().postDelayed({
                    getData(it.id)

                },1200)
            }else{
                toast(requireContext(), "Gagal menambah produk")
            }
            showPageLoading(requireView(), false)
        }
        viewModel.postProductLive(token, name, description, basePrice, categoryIDs, location, body2)


    }

    @SuppressLint("SetTextI18n")
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
        if (produkPreview.sellerImage != null){
            Glide.with(this).load(produkPreview.sellerImage).into(productSellerImage)
        }

        productSellerName.text = produkPreview.sellerName
        productSellerAddress.text = produkPreview.location
        layout_button.visibility = View.GONE
        btn_goto_terbit_product.visibility = View.VISIBLE

        btn_goto_terbit_product.setOnClickListener {
            val listKategori : MutableList<Int> = mutableListOf()
            produkPreview.categories.forEach {
                listKategori.add(it.id)
            }
            setDataImagee(produkPreview.imageUrl!!)
            postProduct(token, produkPreview.name,produkPreview.description, produkPreview.basePrice.toInt(), listKategori, produkPreview.location, body2)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showProductData(data: ProductDetailItemResponse) {
        // Get view id -----------------------------------------------------------------------------
        val productImage : ImageView = view!!.findViewById(R.id.iv_detail_product_image)
        val productName : TextView = view!!.findViewById(R.id.tv_detail_product_name)
        val productCategory : TextView = view!!.findViewById(R.id.tv_detail_product_category)
        val productPrice : TextView = view!!.findViewById(R.id.tv_detail_product_price)
        val productDesc : TextView = view!!.findViewById(R.id.tv_detail_product_description)
        val productIsSold: CardView = view!!.findViewById(R.id.cv_item_product_is_sold)

        val productSellerImage : ImageView = view!!.findViewById(R.id.iv_detail_product_seller_image)
        val productSellerName : TextView = view!!.findViewById(R.id.tv_detail_product_seller_name)
        val productSellerAddress : TextView = view!!.findViewById(R.id.tv_detail_product_seller_address)

        // Set data to view ------------------------------------------------------------------------
        if (data.status != "available" && data.status != "seller") {
            productIsSold.visibility = View.VISIBLE
        }

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
        val btnWishlist: Button = requireView().findViewById(R.id.btn_add_to_wishlist)

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.getDetailProduct(id)
        viewModel.detailProduct.observe(this, { data ->
            if (data != null) {
                btnEditProduct.setOnClickListener {
                    // Disini arahin ke edit produk
                    val dataP = bundleOf("DATAPRODUK" to data)
                    view?.findNavController()?.navigate(R.id.action_detailFragment_to_jualFragment, dataP)
                }
                btn_goto_hapus_product.setOnClickListener {
                    alertDialog(
                        requireContext(),
                        "Hapus Produk",
                        "Anda yakin ingin menghapus produk?"
                    ) {
                        deleteProduct(token, id)
                    }
                }
                showProductData(data)

                if (isUserLoggedIn(userManager)) {
                    if (data.status == "available" || data.status == "seller") {
                        btnImInterested.setOnClickListener {
                            showBottomSheetDialogFragment(
                                ProductDataForBid(data.id, data.name, data.basePrice, data.imageUrl)
                            )
                        }
                    } else {
                        btnImInterested.text = "Stok barang habis"
                        btnImInterested.isEnabled = false
                        btnImInterested.isClickable = false
                    }
                    btnWishlist.setOnClickListener {
                        viewModel.postWishlist(token, data.id)
                        viewModel.wishlistPost.observe(this, {
                            snackbarCustom(
                                requireView(),
                                "Berhasil menambah wishlist",
                                "Lihat Wishlist"
                            ) {
                                Navigation.findNavController(view!!)
                                    .navigate(R.id.action_detailFragment_to_homeWishlistFragment)
                            }
                        })
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
                    btnWishlist.visibility = View.GONE
                    btnEditProduct.visibility = View.VISIBLE
                    btn_goto_hapus_product.visibility = View.VISIBLE



                } else {
                    btnImInterested.visibility = View.VISIBLE
                    btnWishlist.visibility = View.VISIBLE
                    btnEditProduct.visibility = View.GONE
                }
            })
        })
    }

    private fun deleteProduct(token: String, id: Int) {
        val viewModel2 = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        viewModel2.sellerDeleteProductLive.observe(viewLifecycleOwner){
            if (it != null){
                toast(requireContext(), "Produk telah dihapus")
                Log.d("PESANDARIDELETE", it.toString())
                view?.findNavController()?.navigate(R.id.action_detailFragment_to_homeFragment)
            }
        }
        viewModel2.deleteProductLive(token, id)

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