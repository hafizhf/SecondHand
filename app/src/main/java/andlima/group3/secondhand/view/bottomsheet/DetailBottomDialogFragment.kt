package andlima.group3.secondhand.view.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.detail.ProductDataForBid
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailBottomDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataForBid = arguments?.getParcelable<ProductDataForBid>("BID_PRODUCT") as ProductDataForBid
        showDataOnPopUp(dataForBid)
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
}