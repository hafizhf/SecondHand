package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.notification.NotificationResponseItem
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
//        if (dataNotif!![position].status)
//        holder.itemView.tv_namaPenawaran.text = dataProduk!![position].product.name
//        holder.itemView.tv_hargaPenawaran.text = "Rp " + dataProduk!![position].product.basePrice.toString()
//        holder.itemView.tv_ditawarPenawaran.text = "Ditawar Rp " + dataProduk!![position].price
//        Glide.with(holder.itemView.context).load(dataProduk!![position].product.imageUrl).apply(
//            RequestOptions()
//        ).into(holder.itemView.imagePenawaran)
//        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//        val formatter = SimpleDateFormat("dd MMM, HH:mm")
//        val formattedDate = formatter.format(parser.parse(dataProduk!![position].updatedAt))
//        holder.itemView.tv_tanggalPenawaran.text = formattedDate
//
//
//
//
//
//        holder.itemView.itemnotifikasi_card.setOnClickListener{
//            onClick(dataNotif!![position])
//        }

    }

    override fun getItemCount(): Int {
        return 0
//        if (dataProduk == null){
//            return 0
//        }else{
//            return dataProduk!!.size
//
//        }
    }
}