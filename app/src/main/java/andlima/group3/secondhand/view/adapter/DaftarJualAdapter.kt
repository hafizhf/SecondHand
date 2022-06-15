package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_daftar_jual.view.*

class DaftarJualAdapter(private var onClick : (SellerProductsItem)->Unit) : RecyclerView.Adapter<DaftarJualAdapter.ViewHolder>() {

    private var dataProduk : List<SellerProductsItem>? = null

    fun setDataProduk(produk : List<SellerProductsItem>){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_jual, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_namaProduk.text = dataProduk!![position].name
        holder.itemView.tv_kategoriProduk.text = dataProduk!![position].categories[0].name
        Glide.with(holder.itemView.context).load(dataProduk!![position].imageUrl).apply(RequestOptions()).into(holder.itemView.imageItemDaftarJual)



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