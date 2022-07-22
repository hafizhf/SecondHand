package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.capitalize
import andlima.group3.secondhand.func.colorList
import andlima.group3.secondhand.model.home.newhome.wishlist.GetWishlistResponse
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_wishlist.view.*

class WishlistAdapter(private var action: (id: Int, actionCode: Int) -> Unit)
    : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    private var productList: List<GetWishlistResponse>? = null

    fun setProductData(product: List<GetWishlistResponse>) {
        this.productList = product
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.apply {
            Glide.with(context)
                .load(productList!![position].product.imageUrl)
                .into(iv_item_wishlist_image)
            tv_item_wishlist_name.text = capitalize(productList!![position].product.name)
            tv_item_wishlist_price.text = productList!![position].product.location
            tv_item_wishlist_status.text = capitalize(productList!![position].product.status)
            tv_item_wishlist_location.text = "Rp " + productList!![position].product.basePrice

            when (productList!![position].product.status) {
                "available" -> {
                    cv_item_wishlist_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.second_hand_success)
                        )
                }
                "sold" -> {
                    cv_item_wishlist_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.second_hand_danger)
                        )
                }
                else -> {
                    cv_item_wishlist_status_container
                        .setCardBackgroundColor(
                            context.colorList(context, R.color.black)
                        )
                }
            }

            item_wishlist.setOnClickListener {
                action(productList!![position].productId, 1)
            }

            btn_remove_wishlist.setOnClickListener {
                // Int action to delete -> 2
                action(productList!![position].id, 2)
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