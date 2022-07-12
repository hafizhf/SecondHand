package andlima.group3.secondhand.func

import andlima.group3.secondhand.AuthActivity
import andlima.group3.secondhand.R
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.SearchResultAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.util.*

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

/**
 * Image encoder or converter from image that displayed in ImageView into ByteArray
 */
fun encodeImageToByteArray(imageView: ImageView): ByteArray {
    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

    return stream.toByteArray()
}

/**
 * Image encoder or converter from Drawable into ByteArray
 */
fun encodeDrawableToByteArray(drawable: Drawable): ByteArray {
//    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val bitmap = (drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

    return stream.toByteArray()
}

/**
 * Image decoder or converter from ByteArray to Bitmap to be used on ImageView
 */
fun decodeImageFromByteArray(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

/**
 * Encoder or converter ByteArray to String
 */
fun encodeByteArrayToString(byteArray: ByteArray): String {
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

/**
 * Image encoder or converter from image that displayed in ImageView into Base64String
 */
fun encodeImageBase64String(imageView: ImageView): String {
    return Base64.encodeToString(encodeImageToByteArray(imageView), Base64.NO_WRAP)
}

/**
 * Image decoder or converter from Base64String to Bitmap to be used on ImageView
 */
fun decodeBase64ImageString(base64String: String): Bitmap {
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
//    dialogContainer.setBackgroundColor(Color.BLACK)
    dialog.setCardBackgroundColor(view.context.colorList(view.context, R.color.second_hand_success))

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

// INI DARI YG SEBELUMNYA
/**
 * Make event for search view on home pages
 */
@SuppressLint("ClickableViewAccessibility")
fun homeSearchView(
    view: View,
    context: Context,
    activity: Activity,
    owner: ViewModelStoreOwner,
    lifecycleOwner: LifecycleOwner,
    newSearch: Boolean? = null
) {
    val makeSearchEvent : CardView = view.findViewById(R.id.container_search_event)
    val searchResultContainer : LinearLayout = view.findViewById(R.id.container_search_result)
    val a : SearchView = view.findViewById(R.id.home_search_bar_new)

    val searchPlaceholder : TextView = view.findViewById(R.id.tv_search_placeholder)

    val paramsResultContainer: ViewGroup.LayoutParams = searchResultContainer.layoutParams
    paramsResultContainer.height = getDeviceScreenHeight(activity)
    searchResultContainer.layoutParams = paramsResultContainer

    // Observe focus change on search view
    a.setOnQueryTextFocusChangeListener { _, focused ->

        // Disable scroll for home when focus on search
//            scroll_view_home.setOnTouchListener { _, _ -> focused }

        if (focused) {
            a.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    toast(context, "Belum bisa submit ges")

                    val keyword = bundleOf("SEARCH_KEYWORD" to p0)
                    if (newSearch != null) {
                        if (newSearch) {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_homeResultListFragment_self, keyword)
                        }
                    } else {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_homeFragment_to_homeResultListFragment, keyword)
                    }

                    return false
                }

                @SuppressLint("SetTextI18n")
                override fun onQueryTextChange(p0: String?): Boolean {
                    getSearchResult(p0!!, view, context, owner, lifecycleOwner, newSearch)
                    if (p0 != "") {
                        searchPlaceholder.text = p0
                    } else {
                        searchPlaceholder.text = "Cari di Second Chance"
                    }
                    return false
                }
            })
        }
    }

    // Dim screen when search bar clicked
    makeSearchEvent.setOnClickListener {
        searchResultContainer.visibility = View.VISIBLE
        a.requestFocus()

        showSoftKeyboard(activity)
    }

    // Hide search container when user click on dim side
    searchResultContainer.setOnClickListener {
        searchResultContainer.visibility = View.GONE
    }
}

@SuppressLint("NotifyDataSetChanged")
private fun getSearchResult(
    userInput: String,
    view: View,
    context: Context,
    owner: ViewModelStoreOwner,
    lifecycleOwner: LifecycleOwner,
    newSearch: Boolean? = null
) {
    val recyclerView: RecyclerView = view.findViewById(R.id.rv_search_result)
    val searchAdapter = SearchResultAdapter() {
//        toast(context, "You thought this was $it? But it was me, Dio!")
        val keyword = bundleOf("SEARCH_KEYWORD" to it)
        if (newSearch != null) {
            if (newSearch) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeResultListFragment_self, keyword)
            }
        } else {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_homeResultListFragment, keyword)
        }
    }

    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = searchAdapter

    val viewModel = ViewModelProvider(owner)[BuyerViewModel::class.java]
    viewModel.allProductData.observe(lifecycleOwner, {
        if (it != null) {
            val listProduct = it
            val listProductName : MutableList<String> = mutableListOf()
            listProduct.forEach { item ->
                if (item.name != null) {
                    listProductName.add(item.name)
                }
            }
            val listProductNameFiltered = listProductName.filter { item ->
                item.contains(userInput, ignoreCase = true)
            }
            if (userInput != "") {
                searchAdapter.setResultList(listProductNameFiltered)
            } else {
                searchAdapter.setResultList(emptyList())
            }
            searchAdapter.notifyDataSetChanged()
        } else {
            // Something to show when there is no product
        }
    })
}

private fun showSoftKeyboard(activity: Activity) {
    val view = activity.currentFocus
    val methodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    assert(view != null)
    methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Directly navigate to detail page of product
 */
fun navigateToDetailProduct(productId: Int, view: View, navigationId: Int) {
    val selectedID = bundleOf("SELECTED_ID" to productId)
    Navigation.findNavController(view)
        .navigate(navigationId, selectedID)
}

/**
 * Show cart quantity/amount badge on home pages
 */
fun showCartQuantity(view: View, owner: ViewModelStoreOwner, lifecycleOwner: LifecycleOwner, userManager: UserManager) {
    val viewModel = ViewModelProvider(owner)[BuyerViewModel::class.java]
    userManager.accessTokenFlow.asLiveData().observeOnce(lifecycleOwner, {
        viewModel.orderQuantity.observeForever { quantity ->
            if (quantity != 0) {
                view.findViewById<CardView>(R.id.cart_info).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.tv_item_amount).text = quantity.toString()
            } else {
                view.findViewById<CardView>(R.id.cart_info).visibility = View.GONE
            }
        }
        viewModel.getBuyerOrderQuantity(it)
    })
}

/**
 * To be able to use color from R.color
 *
 * Example:
 *
 * cardView.setCardBackgroundColor(context.colorList(context, R.color.white))
 */
fun Context.colorList(context: Context, id: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(context, id))
}

/**
 * Capitalize or make first letter of string to upper case
 */
fun capitalize(string: String): String {
    return string.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}

/**
 * To validate URL on string
 */
fun isURLValid(urlString: String): Boolean {
    return Patterns.WEB_URL.matcher(urlString).matches()
}