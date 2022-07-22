package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.home.BuyerProductItem
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(private var onClick: (BuyerProductItem) -> Unit)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var productList: List<BuyerProductItem>? = null

    fun setProductData(product: List<BuyerProductItem>) {
        this.productList = product
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context)
                .load(productList!![position].imageUrl)
                .into(iv_home_product_image)
            tv_home_product_name.text = productList!![position].name
            if (productList!![position].categories.isNotEmpty()) {
                tv_home_category.text = productList!![position].categories[0].name
            } else {
                tv_home_category.text = "Uncategorized"
            }
            tv_home_price.text = "Rp " + productList!![position].basePrice.toString()

            item_home_product.setOnClickListener {
                onClick(productList!![position])
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