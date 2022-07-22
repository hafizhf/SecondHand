package andlima.group3.secondhand.view.bottomsheet

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.showPageLoading2
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_status_bottom_dialog.*

class StatusBottomDialogFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = arguments?.getString("TOKEN") as String
        val id = arguments?.getInt("ID") as Int
        val buyerID = arguments?.getInt("BUYER_ID") as Int
        val buttonStatus = ""



        var checkedRadioButtonId = radioGroup.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            checkedRadioButtonId = checkedId
            btnKirimBottomSheet.isEnabled = true

        }

        btnKirimBottomSheet.setOnClickListener {
            Log.d("radiobutton", checkedRadioButtonId.toString())
            val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
            val checked : RadioButton = view.findViewById(checkedRadioButtonId)


            if (checked.hint == "sukses"){
                viewModel.patchOrderLive(token, id, "accepted")
                toast(requireContext(), "Berhasil Transaksi")
            }else{
                viewModel.patchOrderLive(token, id, "declined")
                toast(requireContext(), "Transaksi Dibatalkan")

            }

            showPageLoading2(requireView(),true)
            Handler().postDelayed({
                viewModel.getBuyerOrdersLive(token, buyerID)
                showPageLoading2(requireView(),false)
                dismiss()


            },2200)



        }
    }


}