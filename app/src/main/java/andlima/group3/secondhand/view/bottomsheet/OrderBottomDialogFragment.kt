package andlima.group3.secondhand.view.bottomsheet

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.priceFormat
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_order_bottom_dialog.*
import java.net.URLEncoder


class OrderBottomDialogFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_bottom_dialog, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataOrder = arguments?.getParcelable<SellerOrdersItem>("ORDER") as SellerOrdersItem
        val token = arguments?.getString("TOKEN") as String
        val buyerID = arguments?.getInt("BUYER_ID") as Int
        tvNamaPembeliBottomSheet.text = dataOrder.user.fullName
        tvKotaPembeliBottomSheet.text = dataOrder.user.city
        tvNamaProdukBottomSheet.text = dataOrder.productName
        tvBasePriceProdukBottomSheet.text = "Rp ${dataOrder.product.basePrice}"
        tvDitawarProdukBottomSheet.text = "Ditawar Rp ${dataOrder.price}"
        if (dataOrder.user.imageUrl != null){
            Glide.with(requireActivity()).load(dataOrder.user.imageUrl).apply(
                RequestOptions()
            ).into(imagePembeliBottomSheet)
        }

        Glide.with(requireActivity()).load(dataOrder.imageProduct).apply(
            RequestOptions()
        ).into(imageProdukBottomSheet)
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        viewModel.getBuyerOrdersLive(token, buyerID)

        btnHubungiBottomSheet.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=${dataOrder.user.phoneNumber}"+"&text=" +
                    URLEncoder.encode(
                        "Hai kak, saya penjual produk ${dataOrder.productName} dari aplikasi SecondHand. " +
                                "Saya mendapat tawaran sebesar Rp ${priceFormat(dataOrder.price.toString())} " +
                                "dari kakak, apakah benar?",
                        "UTF-8"
                    )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }



}