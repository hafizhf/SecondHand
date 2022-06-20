package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainContainerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationInteraction()
    }

    private fun bottomNavigationInteraction() {
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()

        val bottomNavigation : BottomNavigationView = view!!.findViewById(R.id.bottom_navigation)
        bottomNavigation.isSelected

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_home -> {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_notifikasi -> {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, NotifikasiFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_jual -> {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, JualFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_daftar_jual -> {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, DaftarJualFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_akun -> {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, AkunFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}