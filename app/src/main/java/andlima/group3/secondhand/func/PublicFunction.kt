package andlima.group3.secondhand.func

import andlima.group3.secondhand.AuthActivity
import andlima.group3.secondhand.R
import andlima.group3.secondhand.local.datastore.UserManager
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

// Function to easy making Toast -------------------------------------------------------------------
/**
 * Function to create **Toast** in **LENGTH_SHORT** easily
 *
 * Put `view's context` in [context] parameter and write `message` in [message] parameter
 */
fun toast(context: Context, message : String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
/**
 * Function to create **Snackbar** in **LENGTH_LONG** easily _without_ any action from button
 *
 * Put `view` in [view] parameter and write `message` in [message] parameter
 *
 * If you wish to add custom action for button, use [snackbarCustom] instead
 */
fun snackbarLong(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Funtion to easy making Snackbar with custom action ----------------------------------------------
/**
 * Function to create **Snackbar** in **LENGTH_LONG** easily _with_ custom action for button
 *
 * Add your custom action in _body expression_ in [action] paramter, or after [buttonText]
 *
 * If you wish to create simple snackbar, use [snackbarLong] or [snackbarIndefinite] instead
 */
fun snackbarCustom(view: View, message: String, buttonText: String, action: Any.() -> Unit) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction(buttonText) {
        action(true)
    }
    snack.show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
/**
 * Function to create **Snackbar** in **LENGTH_INDEFINITE** easily _without_ any action from button
 *
 * Put `view` in [view] parameter and write `message` in [message] parameter
 *
 * If you wish to add custom action for button, use [snackbarCustom] instead
 */
fun snackbarIndefinite(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Function to easy making AlertDialog -------------------------------------------------------------
/**
 * Function to create **Alert Dialog** easily with **custom action** from positive button
 *
 * Add value for every parameter, such as [context], [title], and [message].
 * Add custom action in _body expression_ after [message] or outside parameter
 *
 * If you wish to add custom action for button, use [snackbarCustom] instead
 */
fun alertDialog(context: Context, title: String, message: String, action: Any.()->Unit) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            action(true)
        }
        .setCancelable(false)
        .show()
}

// IMAGE CONVERT METHOD ############################################################################

private fun convertImageToByteArray(imageView: ImageView): ByteArray {
    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

    return stream.toByteArray()
}

fun encodeImageBase64(imageView: ImageView): String {
    return Base64.encodeToString(convertImageToByteArray(imageView), Base64.NO_WRAP)
}

fun decodeBase64Image(base64String: String): Bitmap {
    val imageBytes = Base64.decode(base64String, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

// END OF IMAGE CONVERT METHOD #####################################################################
/**
 * Get device screen height size in pixel
 */
fun getDeviceScreenHeight(activity: Activity): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

    return displayMetrics.heightPixels
}

fun isScrollReachedBottom(scrollView: NestedScrollView, reachBottom: (Boolean) -> Unit) {
    scrollView.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
        if (scrollView.getChildAt(0).bottom == (scrollView.height + scrollView.scrollY)) {
            // Scroll reached bottom
            reachBottom(true)
        } else {
            // Scroll not at bottom
            reachBottom(false)
        }
    })
}

/**
 * Observe value of _LiveData_ variable only **once** to avoid uncontrollable observation in
 * some condition
 */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun isUserLoggedIn(userManager: UserManager): Boolean {
    var userLoggedIn = false

    userManager.emailFlow.asLiveData().observeForever {
        userLoggedIn = it != ""
    }
//    userLoggedIn = false

    return userLoggedIn
}

/**
 *
 */
fun requireLogin(context: Context, userManager: UserManager, linearLayout: LinearLayout, button: Button) {
    if (isUserLoggedIn(userManager)) {
        linearLayout.visibility = View.GONE
    } else {
        linearLayout.visibility = View.VISIBLE

        button.setOnClickListener {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }
}

/**
 * Show success/danger dialog for 3 second
 *
 * **Important:**
 *
 * Need <include layout="@layout/dialog_quick_notification/> in layout with
 * RelativeLayout as parent
 */
@SuppressLint("InflateParams")
fun quickNotifyDialog(view: View, message: String) {
//    val dialog = LayoutInflater.from(context)
//        .inflate(R.layout.dialog_quick_notification, null, false)
//
//    val alert = AlertDialog.Builder(context)
//        .setView(dialog)
//        .create()
//
//    dialog.apply {
//        val dialogContainer : LinearLayout = findViewById(R.id.dialog_container)
//        val dialogMessage : TextView = findViewById(R.id.tv_dialog_message)
//        val closeButton : ImageView = findViewById(R.id.btn_close_dialog)
//
//        dialogMessage.text = message
//        closeButton.setOnClickListener {
//            alert.dismiss()
//        }
//    }
//
//    alert.show()

    val dialog : CardView = view.findViewById(R.id.dialog_quick_notification)
    val dialogContainer : LinearLayout = view.findViewById(R.id.dialog_container)
    val dialogMessage : TextView = view.findViewById(R.id.tv_dialog_message)
    val dialogCloseButton : ImageView = view.findViewById(R.id.btn_close_dialog)

    dialogMessage.text = message
    dialogContainer.setBackgroundColor(Color.BLACK)

    dialog.animate()
        .translationY(150f)
        .alpha(1.0f)
        .setDuration(300)

    dialog.visibility = View.VISIBLE
//    dialog.clearAnimation()

    dialogCloseButton.setOnClickListener {
        dialog.visibility = View.GONE
    }

    Handler(Looper.getMainLooper()).postDelayed({
        dialog.visibility = View.GONE
    }, 3000)
}

/**
 * Directly navigate to detail page of product
 */
fun navigateToDetailProduct(productId: Int, view: View, navigationId: Int) {
    val selectedID = bundleOf("SELECTED_ID" to productId)
    Navigation.findNavController(view)
        .navigate(navigationId, selectedID)
}