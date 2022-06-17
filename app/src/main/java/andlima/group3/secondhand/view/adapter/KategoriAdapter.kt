package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.kategori.KategoriPilihan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_daftar_jual.view.*
import kotlinx.android.synthetic.main.item_kategori.view.*

class KategoriAdapter(private var onClick : (KategoriPilihan)->Unit) : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {

    private var dataProduk : MutableSet<KategoriPilihan>? = null

    fun setDataProduk(produk : MutableSet<KategoriPilihan>?){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_kategoriCard.text = dataProduk!!.elementAt(position).nama




        holder.itemView.cardKategori.setOnClickListener{
            onClick(dataProduk!!.elementAt(position))
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