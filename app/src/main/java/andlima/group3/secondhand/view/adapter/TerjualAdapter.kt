package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_terjual.view.*
import java.text.SimpleDateFormat

class TerjualAdapter(private var onClick : (SellerOrdersItem)->Unit, var viewModel : SellerViewModel, var token : String, var owner: LifecycleOwner) : RecyclerView.Adapter<TerjualAdapter.ViewHolder>() {

    private var dataProduk : List<SellerOrdersItem>? = null

    fun setDataProduk(produk : List<SellerOrdersItem>){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_terjual, parent, false)
        return ViewHolder(viewItem)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        viewModel.getDetailOrderLive(token, dataProduk!![position].id)
//        viewModel.sellerDetailOrdersLiveData.observe(owner){
//            holder.itemView.tv_terjualBuyer.text = it.user.fullName
//            holder.itemView.tv_terjual.text = "Terjual Rp " + it.price
//
//        }
        holder.itemView.tv_terjualBuyer.text = dataProduk!![position].user.fullName
        holder.itemView.tv_terjual.text = "Terjual Rp " + dataProduk!![position].price
        holder.itemView.tv_namaTerjual.text = dataProduk!![position].productName
        holder.itemView.tv_hargaTerjual.text = "Rp " + dataProduk!![position].basePrice
        Glide.with(holder.itemView.context).load(dataProduk!![position].product.imageUrl).apply(
            RequestOptions()
        ).into(holder.itemView.imageTerjual)
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM, HH:mm")
        val formattedDate = formatter.format(parser.parse(dataProduk!![position].updatedAt)!!)
        holder.itemView.tv_tanggalTerjual.text = formattedDate


        holder.itemView.card_itemTerjual.setOnClickListener{
            onClick(dataProduk!![position])
        }

    }

    override fun getItemCount(): Int {
        return if (dataProduk == null){
            0
        }else{
            dataProduk!!.size

        }
    }
}