package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_empty.view.*

class EmptyAdapter: RecyclerView.Adapter<EmptyAdapter.ViewHolder>() {


    private var dataProduk : List<String>? = null

    fun setDataProduk(produk : List<String>){
        this.dataProduk = produk
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
        return ViewHolder(viewItem)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_empty_message.text = "adsdas"

    }

    override fun getItemCount(): Int {
        return if (dataProduk == null){
            0
        }else{
            dataProduk!!.size
        }
    }
}