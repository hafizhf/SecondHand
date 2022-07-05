package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_penawaran.view.*
import kotlinx.android.synthetic.main.item_tawaran_buyer.view.*
import java.text.SimpleDateFormat


class TawaranBuyerAdapter(private var viewModel : SellerViewModel, var token : String, var context : Context) : RecyclerView.Adapter<TawaranBuyerAdapter.ViewHolder>() {

    private var dataProduk : List<SellerOrdersItem>? = null

    fun setDataProduk(produk : List<SellerOrdersItem>){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_tawaran_buyer, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_namaPenawaran2.text = dataProduk!![position].product.name
        holder.itemView.tv_hargaPenawaran2.text = "Rp " + dataProduk!![position].product.basePrice.toString()
        holder.itemView.tv_ditawarPenawaran2.text = "Ditawar Rp " + dataProduk!![position].price
        Glide.with(holder.itemView.context).load(dataProduk!![position].product.imageUrl).apply(
            RequestOptions()
        ).into(holder.itemView.imagePenawaran2)
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM, HH:mm")
        val formattedDate = formatter.format(parser.parse(dataProduk!![position].updatedAt))
        holder.itemView.tv_tanggalPenawaran2.text = formattedDate

        if (dataProduk!![position].status == "declined"){
            holder.itemView.linearLayoutButton.visibility = View.GONE
        }
        if (dataProduk!![position].status == "accepted"){
            holder.itemView.btnTolak.text = "Status"
            holder.itemView.btnTerima.text = "Hubungi"
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_fi_whatsapp)
            holder.itemView.btnTerima.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable, null)

        }


        holder.itemView.btnTerima.setOnClickListener {
            if (holder.itemView.btnTerima.text == "Terima"){
                viewModel.patchOrderLive(token, dataProduk!![position].id, "accepted")

            }else{

            }

        }
        holder.itemView.btnTolak.setOnClickListener {
            if (holder.itemView.btnTolak.text == "Tolak"){
                viewModel.patchOrderLive(token, dataProduk!![position].id, "declined")
            }else{

            }

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