package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.penawaran.Product
import andlima.group3.secondhand.model.penawaran.SellerOrdersItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_daftar_jual.view.*
import kotlinx.android.synthetic.main.item_penawaran.view.*

class PenawaranAdapter(private var onClick : (SellerOrdersItem)->Unit) : RecyclerView.Adapter<PenawaranAdapter.ViewHolder>() {

    private var dataProduk : List<SellerOrdersItem>? = null

    fun setDataProduk(produk : List<SellerOrdersItem>){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_penawaran, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_namaPenawaran.text = dataProduk!![position].product.name
        holder.itemView.tv_hargaPenawaran.text = "Rp " + dataProduk!![position].product.basePrice.toString()
        holder.itemView.tv_ditawarPenawaran.text = "Ditawar Rp " + dataProduk!![position].price
        Glide.with(holder.itemView.context).load(dataProduk!![position].product.imageUrl).apply(
            RequestOptions()
        ).into(holder.itemView.imagePenawaran)



        holder.itemView.card_itemDaftarJual.setOnClickListener{
            onClick(dataProduk!![position])
        }

    }

    override fun getItemCount(): Int {
        if (dataProduk == null){
            return 0
        }else{
            return dataProduk!!.size

        }
    }
}