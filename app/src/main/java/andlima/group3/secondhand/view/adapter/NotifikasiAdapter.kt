package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.notification.NotifData
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_notifikasi_berhasilditerbitkan.view.*
import kotlinx.android.synthetic.main.item_notifikasi_penawaranproduk.view.*
import kotlinx.android.synthetic.main.item_penawaran.view.*
import java.text.SimpleDateFormat

class NotifikasiAdapter(private var onClick : (NotificationResponseItem)->Unit) : RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>() {

    private var dataNotif : List<NotificationResponseItem>? = null

    fun setDataNotif(notif : List<NotificationResponseItem>){
        this.dataNotif = notif
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_notifikasi_penawaranproduk, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataNotif!![position].status == "bid"){
            holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Penawaran Produk"
        }else{
            holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Sukses Terbit"

        }
        if (dataNotif!![position].read){
            holder.itemView.layout_item_notif.setBackgroundColor(Color.GRAY)
        }
        if (dataNotif!![position].productName != null){
            holder.itemView.itemnotifikasi_tv_namaprodukpenawaran.text = dataNotif!![position].product.name
            holder.itemView.itemnotifikasi_tv_hargaproduk.text = "Rp " + dataNotif!![position].product.basePrice.toString()
            holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Ditawar Rp " + dataNotif!![position].bidPrice.toString()
            Glide.with(holder.itemView.context).load(dataNotif!![position].imageUrl).apply(
                RequestOptions()
            ).into(holder.itemView.itemnotifikasi_image)
            val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM, HH:mm")
            val formattedDate = formatter.format(parser.parse(dataNotif!![position].transactionDate))
            holder.itemView.itemnotifikasi_tv_tanggalpenawaran.text = formattedDate
        }




        holder.itemView.layout_item_notif.setOnClickListener{
            onClick(dataNotif!![position])
        }

    }

    override fun getItemCount(): Int {

        if (dataNotif == null){
            return 0
        }else{
            return dataNotif!!.size

        }
    }
}