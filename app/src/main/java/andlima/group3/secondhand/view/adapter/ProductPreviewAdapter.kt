package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*


class ProductPreviewAdapter(private var onClick: (ProductItemResponse) -> Unit)
    : RecyclerView.Adapter<ProductPreviewAdapter.ViewHolder>() {

    private var productList: List<ProductItemResponse>? = null

    fun setProductData(product: List<ProductItemResponse>) {
        this.productList = product
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.apply {
            Glide.with(context)
                .load(productList!![position].imageUrl)
                .into(item_product_image)
            item_product_name.text = productList!![position].name

            item_product_location.text = productList!![position].location
            item_product_price.text = "Rp " + productList!![position].basePrice.toString()

            if (position == productList!!.size-1) {
                item_product_data.visibility = View.GONE
                item_product_more.visibility = View.VISIBLE
                spacer_end_item.visibility = View.VISIBLE

                item_product.setOnClickListener {
                    onClick(productList!![position])
                }
            } else {
                item_product_data.visibility = View.VISIBLE
                item_product_more.visibility = View.GONE
                spacer_end_item.visibility = View.GONE

                item_product.setOnClickListener {
                    onClick(productList!![position])
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