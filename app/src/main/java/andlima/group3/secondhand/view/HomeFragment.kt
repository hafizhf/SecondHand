package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.getDeviceScreenHeight
import andlima.group3.secondhand.func.isScrollReachedBottom
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.view.adapter.AdapterHomePager
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user click back button twice
        doubleBackExit()

        bannerCarousel()

        val adapter = AdapterHomePager(childFragmentManager)
        viewpager_home.adapter = adapter
        viewpager_home.layoutParams.height = getDeviceScreenHeight(requireActivity()) + 100
        tabs_home.setupWithViewPager(viewpager_home)
        adapter.notifyDataSetChanged()
        retainInstance = true

        isScrollReachedBottom(scroll_view_home) {
            MarketApplication.homeFragmentReachedBottom.value = it
        }

        homeSearchView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun homeSearchView() {
        val makeSearchEvent : CardView = requireView().findViewById(R.id.container_search_event)
        val searchResultContainer : LinearLayout = requireView().findViewById(R.id.container_search_result)
        val a : SearchView = requireView().findViewById(R.id.home_search_bar)

        val searchPlaceholder : TextView = requireView().findViewById(R.id.tv_search_placeholder)

        val paramsResultContainer: ViewGroup.LayoutParams = searchResultContainer.layoutParams
        paramsResultContainer.height = getDeviceScreenHeight(requireActivity())
        searchResultContainer.layoutParams = paramsResultContainer

        // Observe focus change on search view
        a.setOnQueryTextFocusChangeListener { _, focused ->

            // Disable scroll for home when focus on search
            scroll_view_home.setOnTouchListener { _, _ -> focused }

            if (focused) {
                a.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        toast(requireContext(), "Belum bisa submit ges")
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
                searchAdapter.setResultList(listProductNameFiltered)
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

        carousel.pageCount = sampleBanner.size
        carousel.setImageListener { position, imageView ->
            imageView.setImageResource(sampleBanner[position])
        }
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
}