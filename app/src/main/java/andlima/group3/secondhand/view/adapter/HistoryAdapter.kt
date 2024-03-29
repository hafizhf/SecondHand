package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.history.HistoryResponseItem
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.SimpleDateFormat

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var dataProduk : List<HistoryResponseItem>? = null

    fun setDataProduk(produk : List<HistoryResponseItem>){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
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
        when (dataProduk!![position].status) {
            "accepted" -> {
                holder.itemView.tvRiwayatStatus.text = "Status : Sukses Transaksi"
            }
            "terima" -> {
                holder.itemView.tvRiwayatStatus.text = "Status : Menunggu Konfirmasi"
            }
            else -> {
                holder.itemView.tvRiwayatStatus.text = "Status : Batal Transaksi"
            }
        }
        holder.itemView.tvRiwayatNama.text = dataProduk!![position].productName

        holder.itemView.tvRiwayatHarga.text = "Rp " + dataProduk!![position].price
        Glide.with(holder.itemView.context).load(dataProduk!![position].imageUrl).apply(
            RequestOptions()
        ).into(holder.itemView.imageHistory)
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM, HH:mm")
        val formattedDate = formatter.format(parser.parse(dataProduk!![position].updatedAt)!!)
        holder.itemView.tvTanggalRiwayat.text = formattedDate

    }

    override fun getItemCount(): Int {
        return if (dataProduk == null){
            0
        }else{
            dataProduk!!.size
        }
    }
}