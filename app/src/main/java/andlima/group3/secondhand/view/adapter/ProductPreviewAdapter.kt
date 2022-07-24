package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.MarketApplication
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.decodeBase64ImageString
import andlima.group3.secondhand.func.encodeDrawableToByteArray
import andlima.group3.secondhand.func.isURLValid
import andlima.group3.secondhand.func.priceFormat
import andlima.group3.secondhand.local.room.LocalDatabase
import andlima.group3.secondhand.local.room.electronictable.ElectronicLocal
import andlima.group3.secondhand.local.room.fashiontable.FashionLocal
import andlima.group3.secondhand.local.room.foodtable.FoodLocal
import andlima.group3.secondhand.local.room.homesupplytable.HomeSuppliesLocal
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@DelicateCoroutinesApi
class ProductPreviewAdapter(
    lifecycleOwner: LifecycleOwner? = null,
    categoryId: Int? = null,
    private var onClick: (ProductItemResponse) -> Unit
)
    : RecyclerView.Adapter<ProductPreviewAdapter.ViewHolder>() {

    private var productList: List<ProductItemResponse>? = null
    private var mDb: LocalDatabase? = null
    private val owner = lifecycleOwner
    private val mainId = categoryId
    private var dataNumber = 1

    fun setProductData(product: List<ProductItemResponse>) {
        this.productList = product
    }

//    var item : MutableList<String> = mutableListOf()
//    private var imageTag: MutableLiveData<MutableList<String>> = MutableLiveData(item)
//    fun getImageTag(): LiveData<MutableList<String>> {
//        return imageTag
//    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.itemView.apply {

            mDb = LocalDatabase.getInstance(context)

            // Check if device have internet connection, image would be loaded from URL, else from local
            if (owner != null && mainId != null) {
                MarketApplication.isConnected.observe(owner, { isConnected ->
                    if (isConnected) {
                        Log.d("Get data status", "Online")

                        Glide.with(context)
                            .load(productList!![position].imageUrl)
                            .listener(object : RequestListener<Drawable>{
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    saveNewDataToLocal(owner, context, mainId, productList!![position], resource)
                                    return false
                                }

                            })
                            .into(item_product_image)

                        if (productList!![position].status != "available" && productList!![position].status != "seller") {
                            cv_item_product_preview_is_sold.visibility = View.VISIBLE
                        } else {
                            cv_item_product_preview_is_sold.visibility = View.GONE
                        }

                    } else {
                        Log.d("Get data status", "Offline")
                        /*
                        * When app started from offline state to online inside app,
                        * observer on HomeFragment will detect it first but there is
                        * a delay in here. So, to prevent real string URL from API got
                        * decoded as Base64, string on imageURL should be validate first.
                        * If it not a valid URL, then it is a Base64 from local database.
                        */
                        if (!(isURLValid(productList!![position].imageUrl))) {
                            item_product_image
                                .setImageBitmap(decodeBase64ImageString(productList!![position].imageUrl))
                        }
                    }
                })
            } else {
                Glide.with(context)
                    .load(productList!![position].imageUrl)
                    .into(item_product_image)
            }

            item_product_name.text = productList!![position].name
            item_product_location.text = productList!![position].location
            item_product_price.text = "Rp " + priceFormat(productList!![position].basePrice.toString())

            if (position == productList!!.size-1) {
                item_product_data.visibility = View.GONE
                item_product_more.visibility = View.VISIBLE
                spacer_end_item.visibility = View.VISIBLE

                item_product.setOnClickListener {
                    onClick(productList!![position])
                }
            } else {
                item_product_data.visibility = View.VISIBLE
                item_product_more.visibility = View.GONE
                spacer_end_item.visibility = View.GONE

                item_product.setOnClickListener {
                    onClick(productList!![position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (productList != null)
            productList!!.size
        else
            0
    }

    /**
     * Only data on main home page will be saved to local,
     * such as preview for electronic, fashion, food, and home supplies category
     */
    private fun saveNewDataToLocal(
        lifecycleOwner: LifecycleOwner,
        context: Context,
        categoryId: Int,
        rawData: ProductItemResponse,
        drawable: Drawable?
    ) {
        MarketApplication.isConnected.observe(lifecycleOwner, {
            mDb = LocalDatabase.getInstance(context)

            if (drawable != null) {
                if (it) {
                    GlobalScope.launch {

                        when (categoryId) {
                            // Electronic Preview
                            96 -> {
                                if (dataNumber <= 5) {
                                    Log.d("SecondLocal", "Saving data of id $categoryId")
                                    GlobalScope.launch {
                                        val addNewData = mDb?.electronicDao()?.addData(
                                            ElectronicLocal(
                                                null,
                                                rawData.name,
                                                rawData.basePrice,
                                                rawData.location,
                                                encodeDrawableToByteArray(drawable)
                                            )
                                        )
                                        Log.d("SecondLocal", "Data saved ($addNewData)")
                                    }
                                    dataNumber++
                                    Log.d("SecondLocal", "Data number:( $dataNumber)")
                                }
                            }

                            // Fashion Preview
                            99 -> {
                                if (dataNumber <= 5) {
                                    Log.d("SecondLocal", "Saving data of id $categoryId")
                                    GlobalScope.launch {
                                        val addNewData = mDb?.fashionDao()?.addData(
                                            FashionLocal(
                                                null,
                                                rawData.name,
                                                rawData.basePrice,
                                                rawData.location,
                                                encodeDrawableToByteArray(drawable)
                                            )
                                        )
                                        Log.d("SecondLocal", "Data saved ($addNewData)")
                                    }
                                    dataNumber++
                                    Log.d("SecondLocal", "Data number:( $dataNumber")
                                }
                            }

                            // Food Preview
                            105 -> {
                                if (dataNumber <= 5) {
                                    Log.d("SecondLocal", "Saving data of id $categoryId")
                                    GlobalScope.launch {
                                        val addNewData = mDb?.foodDao()?.addData(
                                            FoodLocal(
                                                null,
                                                rawData.name,
                                                rawData.basePrice,
                                                rawData.location,
                                                encodeDrawableToByteArray(drawable)
                                            )
                                        )
                                        Log.d("SecondLocal", "Data saved ($addNewData)")
                                    }
                                    dataNumber++
                                    Log.d("SecondLocal", "Data number:( $dataNumber")
                                }
                            }

                            // Home Supplies Preview
                            107 -> {
                                if (dataNumber <= 5) {
                                    Log.d("SecondLocal", "Saving data of id $categoryId")
                                    GlobalScope.launch {
                                        val addNewData = mDb?.homeSuppliesDao()?.addData(
                                            HomeSuppliesLocal(
                                                null,
                                                rawData.name,
                                                rawData.basePrice,
                                                rawData.location,
                                                encodeDrawableToByteArray(drawable)
                                            )
                                        )
                                        Log.d("SecondLocal", "Data saved ($addNewData)")
                                    }
                                    dataNumber++
                                    Log.d("SecondLocal", "Data number:( $dataNumber")
                                }
                            }

                            else -> {
                                Log.d("SecondLocal", "Non-category-on-home are inputted: $categoryId")
                            }
                        }
                    }
                }
            }
        })
    }
}