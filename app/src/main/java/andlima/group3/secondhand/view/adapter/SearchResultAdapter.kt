package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_search_result_simple.view.*

class SearchResultAdapter(private var onClick: (String) -> Unit) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var resultList: List<String>? = null

    fun setResultList(result: List<String>) {
        this.resultList = result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result_simple, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_search_result_found.text = resultList!![position]

            item_search_result_found.setOnClickListener {
                onClick(resultList!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (resultList != null)
            resultList!!.size
        else
            0
    }
}