package com.capstone.free.education.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.capstone.free.education.R
import com.capstone.free.education.view.materi.MateriFragment
import com.capstone.free.education.view.konsultasi.KonsultasiFragment
import com.capstone.free.education.view.reportlink.ReportFragment
import com.capstone.free.education.view.selfcheck.selfcheckFragment
import com.capstone.free.education.view.setting.SettingFragment
import com.capstone.free.education.view.spendingscore.spendingscoreFragment


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menghubungkan CardView dari XML ke fragment terkait
        view.findViewById<View>(R.id.card_setting).setOnClickListener {
            navigateToFragment(SettingFragment())
        }
        view.findViewById<View>(R.id.card_self_check).setOnClickListener {
            navigateToFragment(selfcheckFragment())
        }
        view.findViewById<View>(R.id.card_spending_score).setOnClickListener {
            navigateToFragment(spendingscoreFragment())
        }
        view.findViewById<View>(R.id.card_report).setOnClickListener {
            navigateToFragment(ReportFragment())
        }
        view.findViewById<View>(R.id.card_consultation).setOnClickListener {
            navigateToFragment(KonsultasiFragment())
        }

        view.findViewById<View>(R.id.card_material).setOnClickListener {
            navigateToFragment(MateriFragment())
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Ganti dengan ID container di layout utama
            .addToBackStack(null) // Tambahkan ke back stack untuk navigasi kembali
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
