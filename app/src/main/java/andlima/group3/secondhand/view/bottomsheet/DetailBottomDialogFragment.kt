@file:Suppress("ControlFlowWithEmptyBody")

package andlima.group3.secondhand.view.bottomsheet

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.func.priceFormat
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.buyer.order.BuyerOrderRequest
import andlima.group3.secondhand.model.detail.EditBid
import andlima.group3.secondhand.model.detail.ProductDataForBid
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
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

    override fun onResume() {
        super.onResume()
        bidSuccess.postValue(false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        bidSuccess.postValue(false)
        val btnSendBid : Button = requireView().findViewById(R.id.btn_detail_popup_kirim)

        // If user never bid/product never been ordered before
        val dataForBid = arguments?.getParcelable<ProductDataForBid>("BID_PRODUCT")
        if (dataForBid != null) {
            showDataOnPopUp(dataToBid = dataForBid)

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

        // If user come from cart/user wanted to edit bid
        val editBidData = arguments?.getParcelable<EditBid>("EDIT_BID")
        if (editBidData != null) {
            showDataOnPopUp(bidToEdit = editBidData)
            val dialogTitle : TextView = requireView().findViewById(R.id.tv_detail_dialog_title)
            dialogTitle.text = "Ubah besar tawaran"

            btnSendBid.setOnClickListener {
                val bidInputField : EditText = requireView().findViewById(R.id.et_detail_popup_bid)
                val userBid = bidInputField.text.toString()

                if (userBid != "") {
                    if (userBid.toInt() <= editBidData.basePrice) {
                        editBid(editBidData.orderId, userBid.toInt())
                    } else {
                        toast(requireContext(), "Mohon masukkan harga setidaknya sama atau kurang dari harga asli")
                    }
                } else {
                    toast(requireContext(), "Masukkan besar tawaranmu untuk produk ini")
                }
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
    private fun showDataOnPopUp(dataToBid: ProductDataForBid? = null, bidToEdit: EditBid? = null) {
        val productImage : ImageView = view!!.findViewById(R.id.iv_detail_popup_product_image)
        val productName : TextView = view!!.findViewById(R.id.tv_detail_popup_product_name)
        val productPrice : TextView = view!!.findViewById(R.id.tv_detail_popup_product_price)
        val productBid : EditText = view!!.findViewById(R.id.et_detail_popup_bid)

        if (dataToBid != null) {
            Glide.with(this).load(dataToBid.imageUrl).into(productImage)
            productName.text = dataToBid.name
            productPrice.text = "Rp " + priceFormat(dataToBid.price.toString())
        }

        if (bidToEdit != null) {
            Glide.with(this).load(bidToEdit.imageUrl).into(productImage)
            productName.text = bidToEdit.name
            productPrice.text = "Rp " + priceFormat(bidToEdit.basePrice.toString())
            if (bidToEdit.bidPrice != 0) {
                productBid.setText(bidToEdit.bidPrice.toString())
            }
        }
    }

    private fun sendBid(productId: Int, bidPrice: Int) {
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.gotOrderResponse.observe(this, { gotResponse ->
            if (!gotResponse) {
                // Show loading when user click send bid
            }
//            else {
//                // Hide loading when bid got response from API
//            }
        })
        userManager.accessTokenFlow.asLiveData().observeOnce(this, { token ->
            viewModel.postRequestOrder(token, BuyerOrderRequest(bidPrice, productId)) { code, message ->
                when (code) {
                    201 -> {
                        this.dialog!!.dismiss()
                        bidSuccess.postValue(true)
                        this.dialog!!.dismiss()
                    }
                    400 -> {
                        toast(requireContext(), "Produk telah mencapai batas order")
                    }
                    500 -> {
                        toast(requireContext(), "Terjadi masalah pada server.\nCoba lagi nanti.")
                    }
                    else -> {
                        toast(requireContext(), "Terjadi masalah.\nCoba lagi nanti.")
                    }
                }
            }
        })
    }

    private fun editBid(orderId: Int, newBidPrice: Int) {
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.editResponse.observe(this, {
            if (it != null) {
                bidSuccess.postValue(true)
                toast(requireContext(), "Edit besar tawaran berhasil")
                this.dialog!!.dismiss()
            } else {
                toast(requireContext(), "Edit tawaran gagal, mohon coba lagi")
            }
        })
        userManager.accessTokenFlow.asLiveData().observeOnce(this, {
            viewModel.editOrder(it, orderId, newBidPrice)
        })
    }
}