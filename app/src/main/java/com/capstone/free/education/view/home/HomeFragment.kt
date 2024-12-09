package com.capstone.free.education.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.capstone.free.education.R
import com.capstone.free.education.databinding.FragmentHomeBinding
import com.capstone.free.education.view.materi.MateriFragment
import com.capstone.free.education.view.konsultasi.KonsultasiFragment
import com.capstone.free.education.view.reportlink.ReportFragment
import com.capstone.free.education.view.selfcheck.selfcheckFragment
import com.capstone.free.education.view.spendingscore.spendingscoreFragment
import com.dicoding.mysubmission.ui.home.EventHomeFragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the buttons and set click listeners
        view.findViewById<View>(R.id.fab1).setOnClickListener {
            navigateToFragment(EventHomeFragment())
        }
        view.findViewById<View>(R.id.fab2).setOnClickListener {
            navigateToFragment(selfcheckFragment())
        }
        view.findViewById<View>(R.id.fab3).setOnClickListener {
            navigateToFragment(spendingscoreFragment())
        }
        view.findViewById<View>(R.id.fab4).setOnClickListener {
            navigateToFragment(ReportFragment())
        }
        view.findViewById<View>(R.id.fab5).setOnClickListener {
            navigateToFragment(KonsultasiFragment())
        }
        // fb6 doesn't need a click listener as it's not used.
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Replace with your container ID
            .addToBackStack(null) // Add to back stack to allow back navigation
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
