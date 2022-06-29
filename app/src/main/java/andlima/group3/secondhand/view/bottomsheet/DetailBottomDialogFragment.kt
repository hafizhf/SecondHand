package andlima.group3.secondhand.view.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.func.quickNotifyDialog
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.buyer.order.BuyerOrderRequest
import andlima.group3.secondhand.model.detail.ProductDataForBid
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Handler
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailBottomDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
        val bidSuccess = MutableLiveData(false)
    }

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        bidSuccess.postValue(false)

        val dataForBid = arguments?.getParcelable<ProductDataForBid>("BID_PRODUCT") as ProductDataForBid
        showDataOnPopUp(dataForBid)

        val btnSendBid : Button = requireView().findViewById(R.id.btn_detail_popup_kirim)
        btnSendBid.setOnClickListener {
            val bidInputField : EditText = requireView().findViewById(R.id.et_detail_popup_bid)
            val userBid = bidInputField.text.toString()

            if (userBid != "") {
                if (userBid.toInt() <= dataForBid.price) {
                    sendBid(dataForBid.productId, userBid.toInt())
                } else {
                    toast(requireContext(), "Mohon masukkan harga setidaknya sama atau kurang dari harga asli")
                }
            } else {
                toast(requireContext(), "Masukkan besar tawaranmu untuk produk ini")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val containerID = com.google.android.material.R.id.design_bottom_sheet
        val bottomSheet: FrameLayout? = dialog?.findViewById(containerID)
        bottomSheet?.let {
            BottomSheetBehavior.from<FrameLayout?>(it).state =
                BottomSheetBehavior.STATE_HALF_EXPANDED
            bottomSheet.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
        }

        view?.post {
            val params = (view?.parent as View).layoutParams as (CoordinatorLayout.LayoutParams)
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as (BottomSheetBehavior)
            bottomSheetBehavior.peekHeight = view?.measuredHeight ?: 0
            (bottomSheet?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDataOnPopUp(data: ProductDataForBid) {
        val productImage : ImageView = view!!.findViewById(R.id.iv_detail_popup_product_image)
        val productName : TextView = view!!.findViewById(R.id.tv_detail_popup_product_name)
        val productPrice : TextView = view!!.findViewById(R.id.tv_detail_popup_product_price)

        Glide.with(this).load(data.imageUrl).into(productImage)
        productName.text = data.name
        productPrice.text = "Rp " + data.price
    }

    private fun sendBid(productId: Int, bidPrice: Int) {
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.gotOrderResponse.observe(this, { gotResponse ->
            if (!gotResponse) {
                // Show loading when user click send bid
            } else {
                // Hide loading when bid got response from API
            }
        })
        userManager.accessTokenFlow.asLiveData().observeOnce(this, { token ->
            viewModel.postRequestOrder(token, BuyerOrderRequest(bidPrice, productId)) { code, message ->
                if (code == 201) {
                    this.dialog!!.dismiss()
//                    quickNotifyDialog(requireContext(), "Harga tawaranmu berhasil dikirim ke penjual")
//                    toast(requireContext(), message)
                    bidSuccess.postValue(true)
                    this.dialog!!.dismiss()
                } else {
//                    quickNotifyDialog(requireContext(), "Harga tawaranmu berhasil dikirim ke penjual")
                    toast(requireContext(), message)
                }
            }
        })
    }


}