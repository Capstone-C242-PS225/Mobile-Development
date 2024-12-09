package com.capstone.free.education.view.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.free.education.R
import com.capstone.free.education.data.event.entity.EventEntity
import com.capstone.free.education.data.response.ListEventsItem
import com.capstone.free.education.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel by viewModels<EventViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private var isFavorite = false

    companion object {
        private const val ARG_EVENT = "arg_event"

        fun newInstance(event: ListEventsItem): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_EVENT, event)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = arguments?.getParcelable<ListEventsItem>(ARG_EVENT)
        event?.let {
            displayEventDetails(it)
            detailViewModel.checkIfFavorite(it.id)
        }

        detailViewModel.isFavorite.observe(viewLifecycleOwner) { favoriteStatus ->
            isFavorite = favoriteStatus
            updateFavoriteIcon(isFavorite)
        }

        binding.favoriteButton.setOnClickListener {
            event?.let { toggleFavorite(it) }
        }
    }

    private fun displayEventDetails(event: ListEventsItem) {
        Glide.with(requireContext())
            .load(event.mediaCover)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.error_image))
            .into(binding.imgMediaCover)

        binding.tvEventName.text = event.name
        binding.tvOwnerName.text = "Penyelenggara: ${event.ownerName}"
        binding.tvEventTime.text = "Waktu: ${event.beginTime} - ${event.endTime}"
        binding.tvQuota.text = "Sisa Kuota: ${event.quota - event.registrants} dari ${event.quota}"

        binding.tvDescription.text = Html.fromHtml(
            event.description.replace("\n", "<br/>"),
            Html.FROM_HTML_MODE_COMPACT
        )
        binding.tvEventlink.text = "Register"
        binding.tvEventlink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }

    private fun toggleFavorite(event: ListEventsItem) {
        val eventEntity = EventEntity(
            id = event.id,
            name = event.name,
            mediaCover = event.mediaCover
        )

        if (isFavorite) {
            detailViewModel.deleteFavoriteEvent(event.id)
            Toast.makeText(context, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
        } else {
            detailViewModel.insertFavoriteEvent(eventEntity)
            Toast.makeText(context, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
    }
}
