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
import andlima.group3.secondhand.view.adapter.CartAdapter
import andlima.group3.secondhand.view.adapter.WishlistAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeWishlistFragment : Fragment() {

    lateinit var userManager: UserManager
    lateinit var adapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_wishlist, container, false)
    }

    override fun onResume() {
        super.onResume()

        userManager = UserManager(requireContext())

        MarketApplication.isConnected.observe(this, { isConnected ->
            val connectionInterfaceHandler: LinearLayout = requireView()
                .findViewById(R.id.dialog_require_internet)

            if (!isConnected) {
                connectionInterfaceHandler.layoutParams.height = getDeviceScreenHeight(requireActivity())
                connectionInterfaceHandler.visibility = View.VISIBLE
            } else {
                connectionInterfaceHandler.visibility = View.GONE

                val requireLoginView: LinearLayout = requireView().findViewById(R.id.dialog_require_login)
                val requireLoginButton: Button = requireView().findViewById(R.id.btn_require_goto_login)
                val isLoggedIn = requireLogin(
                    requireActivity(),
                    requireContext(),
                    userManager,
                    requireLoginView,
                    requireLoginButton
                )

                if (isLoggedIn) {
                    getWishlist()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getWishlist() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_buyer_wishlist)
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]

        adapter = WishlistAdapter { id, code ->
            // Go to detail with productId (it)
            when (code) {
                // See detail
                1 -> {
                    navigateToDetailProduct(id, requireView(), R.id.action_homeWishlistFragment_to_detailFragment)
                }
                // Remove from wishlist
                2 -> {
                    userManager.accessTokenFlow.asLiveData().observeOnce(this, { token ->
                        viewModel.deleteWishlist(token, id)
                        viewModel.wishlistDelete.observe(this, { response ->
                            if (response != null) {
                                toast(
                                    requireContext(),
                                    "Produk berhasil dihapus dari wishlist"
                                )
                                viewModel.getWishlist(token)
                                adapter.notifyDataSetChanged()
                            }
                        })
                    })
                }
                else -> {
                    toast(requireContext(), "N/A")
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = adapter

        viewModel.wishlistList.observe(this, { list ->
            if (list != null) {
                if (list.isNotEmpty()) {
                    adapter.setProductData(list)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        userManager.accessTokenFlow.asLiveData().observe(this, { accessToken ->
            viewModel.getWishlist(accessToken)
        })
    }
}