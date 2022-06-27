package andlima.group3.secondhand.func

import andlima.group3.secondhand.AuthActivity
import andlima.group3.secondhand.local.datastore.UserManager
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

// Function to easy making Toast -------------------------------------------------------------------
fun toast(context: Context, message : String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarLong(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Funtion to easy making Snackbar with custom action ----------------------------------------------
fun snackbarCustom(view: View, message: String, buttonText: String, action: Any.() -> Unit) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction(buttonText) {
        action(true)
    }
    snack.show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarIndefinite(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Function to easy making AlertDialog -------------------------------------------------------------
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