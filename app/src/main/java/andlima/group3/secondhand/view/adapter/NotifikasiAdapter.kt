package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.notification.NotifData
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_notifikasi_berhasilditerbitkan.view.*
import kotlinx.android.synthetic.main.item_notifikasi_penawaranproduk.view.*
import kotlinx.android.synthetic.main.item_penawaran.view.*
import kotlinx.android.synthetic.main.item_tawaran_buyer.view.*
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
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM, HH:mm")
        holder.itemView.itemnotifikasi_tv_namaprodukpenawaran.text = dataNotif!![position].product.name
        holder.itemView.itemnotifikasi_tv_hargaproduk.text = "Rp " + dataNotif!![position].product.basePrice.toString()
        Glide.with(holder.itemView.context).load(dataNotif!![position].imageUrl).apply(
            RequestOptions()
        ).into(holder.itemView.itemnotifikasi_image)
        if (dataNotif!![position].status == "bid"){
            if (dataNotif!![position].productName != null && dataNotif!![position].product != null && dataNotif!![position].transactionDate != null){
                if (dataNotif!![position].receiverId == dataNotif!![position].product.userId){
                    holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Penawaran Produk"
                    holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Ditawar Rp " + dataNotif!![position].bidPrice.toString()
                }else{
                    holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Penawaran Produk"
                    holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Menawar Rp " + dataNotif!![position].bidPrice.toString()
                }
                val formattedDate = formatter.format(parser.parse(dataNotif!![position].transactionDate))
                holder.itemView.itemnotifikasi_tv_tanggalpenawaran.text = formattedDate

            }

        }else if(dataNotif!![position].status == "create"){
            holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Sukses Terbit"
            holder.itemView.itemnotifikasi_tv_hargaditawar.visibility = View.INVISIBLE
            val formattedDate = formatter.format(parser.parse(dataNotif!![position].createdAt))
            holder.itemView.itemnotifikasi_tv_tanggalpenawaran.text = formattedDate
        }else if (dataNotif!![position].status == "terima"){
            if (dataNotif!![position].receiverId == dataNotif!![position].product.userId){
                holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Terima Order"
                holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Ditawar Rp " + dataNotif!![position].bidPrice.toString()
            }else{
                holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Order Diterima"
                holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Menawar Rp " + dataNotif!![position].bidPrice.toString()
            }
            val formattedDate = formatter.format(parser.parse(dataNotif!![position].transactionDate))
            holder.itemView.itemnotifikasi_tv_tanggalpenawaran.text = formattedDate

        }else if(dataNotif!![position].status == "declined"){
            if (dataNotif!![position].receiverId == dataNotif!![position].product.userId){
                holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Transaksi Batal"
                holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Ditawar Rp " + dataNotif!![position].bidPrice.toString()
            }else{
                holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Transaksi Batal"
                holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Menawar Rp " + dataNotif!![position].bidPrice.toString()
            }
            val formattedDate = formatter.format(parser.parse(dataNotif!![position].transactionDate))
            holder.itemView.itemnotifikasi_tv_tanggalpenawaran.text = formattedDate
            holder.itemView.itemnotifikasi_tv_hargaditawar.paintFlags =
                holder.itemView.itemnotifikasi_tv_hargaditawar.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG

        }else if (dataNotif!![position].status == "accepted"){
            if (dataNotif!![position].receiverId == dataNotif!![position].product.userId){
                holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Transaksi Sukses"
                holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Ditawar Rp " + dataNotif!![position].bidPrice.toString()
            }else{
                holder.itemView.itemnotifikasi_tv_penawaranproduk.text = "Transaksi Sukses"
                holder.itemView.itemnotifikasi_tv_hargaditawar.text = "Menawar Rp " + dataNotif!![position].bidPrice.toString()
            }
            val formattedDate = formatter.format(parser.parse(dataNotif!![position].transactionDate))
            holder.itemView.itemnotifikasi_tv_tanggalpenawaran.text = formattedDate
        }
        if (dataNotif!![position].read){
            holder.itemView.layout_item_notif.setBackgroundColor(Color.WHITE)
            holder.itemView.circlenotif.visibility = View.GONE
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