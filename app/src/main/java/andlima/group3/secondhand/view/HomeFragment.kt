package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.*
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.local.room.LocalDatabase
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synnapps.carouselview.CarouselView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Used for double back to exit app
    private var doubleBackToExit = false

    lateinit var userManager: UserManager
    private var mDb: LocalDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        return inflater.inflate(R.layout.home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())
        mDb = LocalDatabase.getInstance(requireContext())

        doubleBackExit()
//        bannerCarousel()

        MarketApplication.isConnected.observe(this, { isConnected ->
            if (isConnected) {
//                getBanner()
                getBannerIGuess()
                homeSearchView(requireView(), requireContext(), requireActivity(), this, this)
                showCartQuantity(requireView(), this, this, userManager)
                showWishlistQuantity(requireView(), this, this, userManager)
            } else {
                offlineBanner()
            }
        })

        // Go to buyer order list / cart
        requireView().findViewById<RelativeLayout>(R.id.btn_goto_cart).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_cartFragment)
        }

        // Go to buyer wishlist
        requireView().findViewById<RelativeLayout>(R.id.btn_goto_wishlist).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_homeWishlistFragment)
        }

        getPreviewData()

        // Click listener on category button
        actionCategoryButton(
            R.id.btn_goto_category_computer,
            R.id.action_homeFragment_to_homeResultListFragment,
            2
        )
        actionCategoryButton(
            R.id.btn_goto_category_handphone,
            R.id.action_homeFragment_to_homeResultListFragment,
            3
        )
        actionCategoryButton(
            R.id.btn_goto_category_voucher,
            R.id.action_homeFragment_to_homeResultListFragment,
            22
        )
        actionCategoryButton(
            R.id.btn_goto_category_health,
            R.id.action_homeFragment_to_homeResultListFragment,
            8
        )
        actionCategoryButton(
            R.id.btn_goto_category_hobby,
            R.id.action_homeFragment_to_homeResultListFragment,
            9
        )
        actionCategoryButton(
            R.id.btn_goto_category_fashion_kid,
            R.id.action_homeFragment_to_homeResultListFragment,
            15
        )
        actionCategoryButton(
            R.id.btn_goto_category_mother_child,
            R.id.action_homeFragment_to_homeResultListFragment,
            16
        )
        actionCategoryButton(
            R.id.btn_goto_category_automotive,
            R.id.action_homeFragment_to_homeResultListFragment,
            19
        )
        actionCategoryButton(
            R.id.btn_goto_category_sports,
            R.id.action_homeFragment_to_homeResultListFragment,
            20
        )
        actionCategoryButton(
            R.id.btn_goto_category_more,
            R.id.action_homeFragment_to_homeMoreCategoryFragment
        )

        // Click listener on more button
        actionButtonMore(
            R.id.btn_goto_electronic_more,
            R.id.action_homeFragment_to_homeElectronicFragment
        )
        actionButtonMore(
            R.id.btn_goto_fashion_more,
            R.id.action_homeFragment_to_homeFashionFragment
        )
        actionButtonMore(
            R.id.btn_goto_food_more,
            R.id.action_homeFragment_to_homeResultListFragment,
            10
        )
        actionButtonMore(
            R.id.btn_goto_home_supplies_more,
            R.id.action_homeFragment_to_homeResultListFragment,
            12
        )

        // Static banner listener
        actionStaticBanner(
            R.id.btn_banner_to_category_computer,
            R.id.action_homeFragment_to_homeResultListFragment,
            2
        )
        actionStaticBanner(
            R.id.btn_banner_to_category_handphone,
            R.id.action_homeFragment_to_homeResultListFragment,
            3
        )

        // Button see all product
        val btnSeeAllProduct: Button = requireView().findViewById(R.id.btn_goto_all_product)
        btnSeeAllProduct.setOnClickListener {
            val code = bundleOf("REQUEST_CODE" to 0)
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_homeResultListFragment, code)
        }
    }

    private fun getPreviewData() {
        getElectronicPreviewData()
        getFashionPreviewData()
        getFoodPreviewData()
        getHomeSuppliesPreviewData()
    }

    private fun actionCategoryButton(linearLayoutId: Int, navigationId: Int, requestCode: Int = 0) {
        val btnGotoCategory: LinearLayout = requireView().findViewById(linearLayoutId)
        btnGotoCategory.setOnClickListener {
            if (requestCode != 0) {
                val code = bundleOf("REQUEST_CODE" to requestCode)
                Navigation.findNavController(view!!).navigate(navigationId, code)
            } else {
                Navigation.findNavController(view!!).navigate(navigationId)
            }
        }
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

    private fun actionStaticBanner(bannerId: Int, navigationId: Int, requestCode: Int = 0) {
        val btnBanner: ImageView = requireView().findViewById(bannerId)
        btnBanner.setOnClickListener {
            if (requestCode != 0) {
                val code = bundleOf("REQUEST_CODE" to requestCode)
                Navigation.findNavController(view!!).navigate(navigationId, code)
            } else {
                Navigation.findNavController(view!!).navigate(navigationId)
            }
        }
    }

    private fun offlineBanner() {
        val carousel : CarouselView = requireView().findViewById(R.id.banner_carousel_home)
        val sampleBanner = listOf(R.drawable.dummy_banner_1, R.drawable.dummy_banner_2)

        carousel.setImageListener { position, imageView ->
            imageView.setImageResource(sampleBanner[position])
        }
        carousel.pageCount = sampleBanner.size
    }

//    private fun bannerCarousel(imageUrlList: List<String>) {
//        val carousel : CarouselView = requireView().findViewById(R.id.banner_carousel_home)
//        carousel.setImageListener { position, imageView ->
//            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
//            val url = URL(imageUrlList[position])
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = BitmapFactory.decodeStream(url.content as InputStream)
//                requireActivity().runOnUiThread {
//                    imageView.setImageBitmap(bitmap)
//                }
//            }
//        }
//        carousel.pageCount = imageUrlList.size
//    }

//    private fun getBanner() {
//        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
//        viewModel.bannerList.observe(this, {
//            if (it != null) {
//                val raw = it
//                val imageUrlList: MutableList<String> = mutableListOf()
//                raw.forEach { banner ->
//                    imageUrlList.add(banner.imageUrl)
//                }
//                bannerCarousel(imageUrlList)
//            }
//        })
//        viewModel.getBanner()
//    }

    private fun getBannerIGuess() {
        val carousel : CarouselView = requireView().findViewById(R.id.banner_carousel_home)
        val sampleBanner = listOf(
            R.drawable.api_banner_1,
            R.drawable.api_banner_2,
            R.drawable.api_banner_3,
            R.drawable.api_banner_4,
            R.drawable.api_banner_5
        )

        carousel.setImageListener { position, imageView ->
            imageView.setImageResource(sampleBanner[position])
        }
        carousel.pageCount = sampleBanner.size
    }

    // Function to exit app with double click on back button----------------------------------------
    private fun doubleBackExit() {
        activity?.onBackPressedDispatcher
            ?.addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (doubleBackToExit) {
                        activity!!.finish()
                    } else {
                        doubleBackToExit = true
                        toast(requireContext(), "Press again to exit")

                        Handler(Looper.getMainLooper()).postDelayed({
                            kotlin.run {
                                doubleBackToExit = false
                            }
                        }, 2000)
                    }
                }
            })
    }

    // LAYOUT BARU
    @SuppressLint("NotifyDataSetChanged")
    private fun getElectronicPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_elektronik)

        val adapter = ProductPreviewAdapter(this, 96) {
            // On item click
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        MarketApplication.isConnected.observe(this, { isConnected ->
            if (isConnected) {
                GlobalScope.launch {
                    mDb?.electronicDao()?.resetData()
                }

                val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
                viewModel.electronicProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            val a : MutableList<ProductItemResponse> = mutableListOf()
                            for (i in 0..7) {
                                a.add(it[i])
                            }
                            adapter.setProductData(a)
                            adapter.notifyDataSetChanged()
//                    })
                        }
//                        else {
//
//                        }
                    }
                })
                viewModel.getElectronicProducts()

            } else {
                GlobalScope.launch {
                    val listData = mDb?.electronicDao()?.getOfflineProduct()
                    Log.d("SecondLocal", listData?.size.toString())

                    requireActivity().runOnUiThread {
                        if (listData?.isNotEmpty()!!) {
                            val dataToShow : MutableList<ProductItemResponse> = mutableListOf()

                            listData.forEach { product ->
                                dataToShow.add(
                                    ProductItemResponse(
                                        product.basePrice!!,
                                        emptyList(),
                                        "",
                                        "",
                                        product.id!!,
                                        "",
                                        encodeByteArrayToString(product.image!!),
                                        product.location!!,
                                        product.name!!,
                                        "",
                                        "",
                                        0
                                    )
                                )
                            }

                            adapter.setProductData(dataToShow)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFashionPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_fashion)

        val adapter = ProductPreviewAdapter(this, 99) {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        MarketApplication.isConnected.observe(this, { isConnected ->
            if (isConnected) {
                GlobalScope.launch {
                    mDb?.fashionDao()?.resetData()
                }

                val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
                viewModel.fashionProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            val a : MutableList<ProductItemResponse> = mutableListOf()
                            for (i in 0..7) {
                                a.add(it[i])
                            }

                            adapter.setProductData(a)
                            adapter.notifyDataSetChanged()
//                    })
                        }
//                        else {
//
//                        }
                    }
                })
                viewModel.getFashionProducts()

            } else {
                GlobalScope.launch {
                    val listData = mDb?.fashionDao()?.getOfflineProduct()

                    requireActivity().runOnUiThread {
                        if (listData?.isNotEmpty()!!) {
                            val dataToShow : MutableList<ProductItemResponse> = mutableListOf()

                            listData.forEach { product ->
                                dataToShow.add(
                                    ProductItemResponse(
                                        product.basePrice!!,
                                        emptyList(),
                                        "",
                                        "",
                                        product.id!!,
                                        "",
                                        encodeByteArrayToString(product.image!!),
                                        product.location!!,
                                        product.name!!,
                                        "",
                                        "",
                                        0
                                    )
                                )
                            }

                            adapter.setProductData(dataToShow)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFoodPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_food)

        val adapter = ProductPreviewAdapter(this, 105) {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        MarketApplication.isConnected.observe(this, { isConnected ->
            if (isConnected) {
                GlobalScope.launch {
                    mDb?.foodDao()?.resetData()
                }

                val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
                viewModel.foodProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            val a : MutableList<ProductItemResponse> = mutableListOf()
                            for (i in 0..7) {
                                a.add(it[i])
                            }

                            adapter.setProductData(a)
                            adapter.notifyDataSetChanged()
//                    })
                        }
//                        else {
//
//                        }
                    }
                })
                viewModel.getFoodProducts()

            } else {
                GlobalScope.launch {
                    val listData = mDb?.foodDao()?.getOfflineProduct()

                    requireActivity().runOnUiThread {
                        if (listData?.isNotEmpty()!!) {
                            val dataToShow : MutableList<ProductItemResponse> = mutableListOf()

                            listData.forEach { product ->
                                dataToShow.add(
                                    ProductItemResponse(
                                        product.basePrice!!,
                                        emptyList(),
                                        "",
                                        "",
                                        product.id!!,
                                        "",
                                        encodeByteArrayToString(product.image!!),
                                        product.location!!,
                                        product.name!!,
                                        "",
                                        "",
                                        0
                                    )
                                )
                            }

                            adapter.setProductData(dataToShow)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHomeSuppliesPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_home_supplies)

        val adapter = ProductPreviewAdapter(this, 107) {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        MarketApplication.isConnected.observe(this, { isConnected ->
            if (isConnected) {
                GlobalScope.launch {
                    mDb?.homeSuppliesDao()?.resetData()
                }

                val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
                viewModel.homeSuppliesProducts.observe(this, {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            val a : MutableList<ProductItemResponse> = mutableListOf()
                            for (i in 0..7) {
                                a.add(it[i])
                            }

                            adapter.setProductData(a)
                            adapter.notifyDataSetChanged()
//                    })
                        }
//                        else {
//
//                        }
                    }
                })
                viewModel.getHomeSuppliesProducts()

            } else {
                GlobalScope.launch {
                    val listData = mDb?.homeSuppliesDao()?.getOfflineProduct()

                    requireActivity().runOnUiThread {
                        if (listData?.isNotEmpty()!!) {
                            val dataToShow : MutableList<ProductItemResponse> = mutableListOf()

                            listData.forEach { product ->
                                dataToShow.add(
                                    ProductItemResponse(
                                        product.basePrice!!,
                                        emptyList(),
                                        "",
                                        "",
                                        product.id!!,
                                        "",
                                        encodeByteArrayToString(product.image!!),
                                        product.location!!,
                                        product.name!!,
                                        "",
                                        "",
                                        0
                                    )
                                )
                            }

                            adapter.setProductData(dataToShow)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

}