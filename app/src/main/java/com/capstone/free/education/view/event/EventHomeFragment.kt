package com.dicoding.mysubmission.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.free.education.R
import com.capstone.free.education.data.response.ListEventsItem
import com.capstone.free.education.databinding.FragmentEventHomeBinding
import com.capstone.free.education.view.event.DetailFragment
import com.capstone.free.education.view.event.EventHomeViewModel
import com.capstone.free.education.view.event.FinishedAdapter
import com.capstone.free.education.view.event.UpcomingAdapter


class EventHomeFragment : Fragment() {

    private var _binding: FragmentEventHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinished.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.listUpcomingEvents.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            setUpcomingEvent(events)
        }

        viewModel.listFinishedEvents.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            setFinishedEvent(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            binding.errorTextView.visibility = if (errorMessage != null) View.VISIBLE else View.GONE
            binding.errorTextView.text = errorMessage
        }
    }

    private fun setUpcomingEvent(listEvent: List<ListEventsItem>) {
        val adapter = UpcomingAdapter { event -> navigateToDetail(event) }
        adapter.submitList(listEvent.take(5))
        binding.rvUpcoming.adapter = adapter
    }

    private fun setFinishedEvent(listEvent: List<ListEventsItem>) {
        val adapter = FinishedAdapter { event -> navigateToDetail(event) }
        adapter.submitList(listEvent.take(5))
        binding.rvFinished.adapter = adapter
    }

    private fun navigateToDetail(event: ListEventsItem) {
        val detailFragment = DetailFragment.newInstance(event)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
