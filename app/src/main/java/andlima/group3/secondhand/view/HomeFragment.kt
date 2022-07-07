package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.getDeviceScreenHeight
import andlima.group3.secondhand.func.navigateToDetailProduct
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.view.adapter.SearchResultAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synnapps.carouselview.CarouselView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Used for double back to exit app
    private var doubleBackToExit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        return inflater.inflate(R.layout.home_layout, container, false)
    }

    // INI DARI YG SEBELUMNYA
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Check if user click back button twice
//        doubleBackExit()
//
//        val adapter = AdapterHomePager(childFragmentManager)
//        viewpager_home.adapter = adapter
//        viewpager_home.layoutParams.height = getDeviceScreenHeight(requireActivity()) + 100
//        tabs_home.setupWithViewPager(viewpager_home)
//        adapter.notifyDataSetChanged()
//        retainInstance = true
//
//        isScrollReachedBottom(scroll_view_home) {
//            MarketApplication.homeFragmentReachedBottom.value = it
//        }
//
//        homeSearchView()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeSearchView()

        getElectronicPreviewData()
        getFashionPreviewData()
        getFoodPreviewData()
        getHomeSuppliesPreviewData()

        // Click listener on category button
        actionCategoryButton(
            R.id.btn_goto_category_computer,
            R.id.action_homeFragment_to_homeResultListFragment,
            97
        )
        actionCategoryButton(
            R.id.btn_goto_category_handphone,
            R.id.action_homeFragment_to_homeResultListFragment,
            98
        )
        actionCategoryButton(
            R.id.btn_goto_category_voucher,
            R.id.action_homeFragment_to_homeResultListFragment,
            117
        )
        actionCategoryButton(
            R.id.btn_goto_category_health,
            R.id.action_homeFragment_to_homeResultListFragment,
            103
        )
        actionCategoryButton(
            R.id.btn_goto_category_hobby,
            R.id.action_homeFragment_to_homeResultListFragment,
            104
        )
        actionCategoryButton(
            R.id.btn_goto_category_fashion_kid,
            R.id.action_homeFragment_to_homeResultListFragment,
            110
        )
        actionCategoryButton(
            R.id.btn_goto_category_mother_child,
            R.id.action_homeFragment_to_homeResultListFragment,
            111
        )
        actionCategoryButton(
            R.id.btn_goto_category_automotive,
            R.id.action_homeFragment_to_homeResultListFragment,
            114
        )
        actionCategoryButton(
            R.id.btn_goto_category_sports,
            R.id.action_homeFragment_to_homeResultListFragment,
            115
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
            105
        )
        actionButtonMore(
            R.id.btn_goto_home_supplies_more,
            R.id.action_homeFragment_to_homeResultListFragment,
            107
        )

        // Static banner listener
        actionStaticBanner(
            R.id.btn_banner_to_category_computer,
            R.id.action_homeFragment_to_homeResultListFragment,
            97
        )
        actionStaticBanner(
            R.id.btn_banner_to_category_handphone,
            R.id.action_homeFragment_to_homeResultListFragment,
            98
        )

        // Button see all product
        val btnSeeAllProduct: Button = requireView().findViewById(R.id.btn_goto_all_product)
        btnSeeAllProduct.setOnClickListener {
            val code = bundleOf("REQUEST_CODE" to 0)
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_homeResultListFragment, code)
        }
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

    override fun onResume() {
        super.onResume()
        bannerCarousel()
    }

    // INI DARI YG SEBELUMNYA
    @SuppressLint("ClickableViewAccessibility")
    private fun homeSearchView() {
        val makeSearchEvent : CardView = requireView().findViewById(R.id.container_search_event)
        val searchResultContainer : LinearLayout = requireView().findViewById(R.id.container_search_result)
        val a : SearchView = requireView().findViewById(R.id.home_search_bar_new)

        val searchPlaceholder : TextView = requireView().findViewById(R.id.tv_search_placeholder)

        val paramsResultContainer: ViewGroup.LayoutParams = searchResultContainer.layoutParams
        paramsResultContainer.height = getDeviceScreenHeight(requireActivity())
        searchResultContainer.layoutParams = paramsResultContainer

        // Observe focus change on search view
        a.setOnQueryTextFocusChangeListener { _, focused ->

            // Disable scroll for home when focus on search
//            scroll_view_home.setOnTouchListener { _, _ -> focused }

            if (focused) {
                a.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        toast(requireContext(), "Belum bisa submit ges")

                        val keyword = bundleOf("SEARCH_KEYWORD" to p0)
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_homeFragment_to_homeResultListFragment, keyword)

                        return false
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onQueryTextChange(p0: String?): Boolean {
                        getSearchResult(p0!!)
                        if (p0 != "") {
                            searchPlaceholder.text = p0
                        } else {
                            searchPlaceholder.text = "Cari di Second Chance"
                        }
                        return false
                    }
                })
            }
        }

        // Dim screen when search bar clicked
        makeSearchEvent.setOnClickListener {
            searchResultContainer.visibility = View.VISIBLE
            a.requestFocus()

            showSoftKeyboard()
        }

        // Hide search container when user click on dim side
        searchResultContainer.setOnClickListener {
            searchResultContainer.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getSearchResult(userInput: String) {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_search_result)
        val searchAdapter = SearchResultAdapter() {
            toast(requireContext(), "You thought this was $it? But it was me, Dio!")
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchAdapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.allProductData.observe(this, {
            if (it != null) {
                val listProduct = it
                val listProductName : MutableList<String> = mutableListOf()
                listProduct.forEach { item ->
                    if (item.name != null) {
                        listProductName.add(item.name)
                    }
                }
                val listProductNameFiltered = listProductName.filter { item ->
                    item.contains(userInput, ignoreCase = true)
                }
                if (userInput != "") {
                    searchAdapter.setResultList(listProductNameFiltered)
                } else {
                    searchAdapter.setResultList(emptyList())
                }
                searchAdapter.notifyDataSetChanged()
            } else {
                // Something to show when there is no product
            }
        })
    }

    private fun showSoftKeyboard() {
        val view = activity?.currentFocus
        val methodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun bannerCarousel() {
        val carousel : CarouselView = requireView().findViewById(R.id.banner_carousel_home)
        val sampleBanner = listOf(R.drawable.dummy_banner_1, R.drawable.dummy_banner_2)

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

                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
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

        val adapter = ProductPreviewAdapter {
            // On item click
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

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
                } else {

                }
            }
        })
        viewModel.getElectronicProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFashionPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_fashion)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.fashionProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getFashionProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFoodPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_food)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.foodProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getFoodProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHomeSuppliesPreviewData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_home_supplies)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.homeSuppliesProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getHomeSuppliesProducts()
    }

}