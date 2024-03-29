package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.capitalize
import andlima.group3.secondhand.func.colorList
import andlima.group3.secondhand.func.priceFormat
import andlima.group3.secondhand.model.buyer.order.GetBuyerOrderResponseItem
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(private var action: (code: Int, orderData: GetBuyerOrderResponseItem) -> Unit)
    : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var productList: List<GetBuyerOrderResponseItem>? = null

    fun setProductData(product: List<GetBuyerOrderResponseItem>) {
        this.productList = product
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.apply {
            Glide.with(context)
                .load(productList!![position].product.imageUrl)
                .into(iv_item_cart_image)
            tv_item_cart_name.text = productList!![position].productName
            tv_item_cart_price.text = "Rp " + priceFormat(productList!![position].basePrice)
            tv_item_cart_status.text = capitalize(productList!![position].status)
            tv_item_cart_my_bid.text = "Tawaranmu: Rp " + priceFormat(productList!![position].price.toString())

            when (productList!![position].status) {
                "terima" -> {
                    cv_item_cart_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.second_hand_pending)
                        )
                    tv_item_cart_status.text = capitalize("Diterima, menunggu konfirmasi")
                }
                "pending" -> {
                    cv_item_cart_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.second_hand_pending)
                        )
                    tv_item_cart_status.text = capitalize("Menunggu respon penjual")
                }
                "accepted" -> {
                    cv_item_cart_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.second_hand_success)
                        )
                    tv_item_cart_status.text = capitalize("Tawaran disetujui")
                    btn_edit_bid.visibility = View.GONE
                }
                "declined" -> {
                    cv_item_cart_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.second_hand_danger)
                        )
                    tv_item_cart_status.text = capitalize("Tawaran ditolak")
                    btn_edit_bid.visibility = View.GONE
                }
                else -> {
                    cv_item_cart_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.black)
                        )
                }
            }

            item_cart.setOnClickListener {
                // Go to detail product
                action(3, productList!![position])
            }

            btn_edit_bid.setOnClickListener {
                // Int action to edit -> 1
                action(1, productList!![position])
//                toast(context, "Edit order is under maintenance")
            }

            btn_cancel_bid.setOnClickListener {
                // Int action to delete -> 2
                alertDialog(
                    context,
                    "Batalkan tawaran",
                    "Apakah kamu yakin ingin membatalkan penawaran?"
                ) {
                    action(2, productList!![position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (productList != null)
            productList!!.size
        else
            0
    }
}