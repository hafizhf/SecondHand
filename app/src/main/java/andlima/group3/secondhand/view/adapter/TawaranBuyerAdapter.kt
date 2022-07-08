package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.MainActivity
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.quickNotifyDialog
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.detail.ProductDataForBid
import andlima.group3.secondhand.view.bottomsheet.DetailBottomDialogFragment
import andlima.group3.secondhand.view.bottomsheet.OrderBottomDialogFragment
import andlima.group3.secondhand.view.bottomsheet.StatusBottomDialogFragment
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_penawaran.view.*
import kotlinx.android.synthetic.main.item_tawaran_buyer.view.*
import java.net.URLEncoder
import java.text.SimpleDateFormat


class TawaranBuyerAdapter(private var viewModel : SellerViewModel, var token : String, var context : Context, var manager : FragmentManager, var buyerId : Int) : RecyclerView.Adapter<TawaranBuyerAdapter.ViewHolder>() {

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
            holder.itemView.tv_ditawarPenawaran2.paintFlags =
                holder.itemView.tv_ditawarPenawaran2.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        }else if (dataProduk!![position].status == "terima"){
            holder.itemView.btnTolak.text = "Status"
            holder.itemView.btnTerima.text = "Hubungi"
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_fi_whatsapp)
            holder.itemView.btnTerima.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable, null)
        }else if (dataProduk!![position].status == "accepted"){
            holder.itemView.tv_ditawarPenawaran2.text = "Terjual Rp " + dataProduk!![position].price
            holder.itemView.linearLayoutButton.visibility = View.GONE

        }


        holder.itemView.btnTerima.setOnClickListener {
            if (holder.itemView.btnTerima.text == "Terima"){
                viewModel.patchOrderLive(token, dataProduk!![position].id, "terima")
                viewModel.getBuyerOrdersLive(token, buyerId)
                showBottomSheetDialogFragment(dataProduk!![position], token, buyerId)


            }else{
                val url = "https://api.whatsapp.com/send?phone=${dataProduk!![position].user.phoneNumber}"+"&text=" +
                        URLEncoder.encode("Hai kak, saya penjual dari produk ${dataProduk!![position].productName}.", "UTF-8")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(url))
                context.startActivity(intent )

            }

        }
        holder.itemView.btnTolak.setOnClickListener {
            if (holder.itemView.btnTolak.text == "Tolak"){
                viewModel.patchOrderLive(token, dataProduk!![position].id, "declined")
                viewModel.getBuyerOrdersLive(token, buyerId)

                toast(context, "Tawaran telah ditolak")
            }else{
                showBottomSheetDialogFragment(token, dataProduk!![position].id)

            }

        }
    }
    private fun showBottomSheetDialogFragment(token: String, id : Int) {
        val bottomSheet = StatusBottomDialogFragment()
        val dataForBid = bundleOf("TOKEN" to token, "ID" to id, "BUYER_ID" to buyerId)
        bottomSheet.arguments = dataForBid
        bottomSheet.show(manager, "STATUSSHEET")

    }
    private fun showBottomSheetDialogFragment(data: SellerOrdersItem,token: String, id : Int ) {
        val bottomSheet = OrderBottomDialogFragment()
        val dataForBid = bundleOf("ORDER" to data, "TOKEN" to token,  "BUYER_ID" to buyerId)
        bottomSheet.arguments = dataForBid
        bottomSheet.show(manager, "ORDERSHEET")

    }

    override fun getItemCount(): Int {
        if (dataProduk == null){
            return 0
        }else{
            return dataProduk!!.size

        }
    }
}