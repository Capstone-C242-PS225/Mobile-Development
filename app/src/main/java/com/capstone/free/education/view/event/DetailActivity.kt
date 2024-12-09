package com.capstone.free.education.view.event

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat

import com.bumptech.glide.Glide
import com.capstone.free.education.R
import com.capstone.free.education.data.event.entity.EventEntity
import com.capstone.free.education.data.response.ListEventsItem
import com.capstone.free.education.databinding.ActivityEventDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailBinding
    private val detailViewModel by viewModels<EventViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var isFavorite = false

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Pengecekan versi API sebelum memanggil insetsController
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val id = intent.getIntExtra(EXTRA_ID, 0)
        detailViewModel.getItemById(id)

        window.statusBarColor = Color.TRANSPARENT
        detailViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        detailViewModel.event.observe(this) { event ->
            bindEventDetails(event)
            detailViewModel.checkIfFavorite(event.id)
        }

        detailViewModel.isFavorite.observe(this) { isFavorite ->
            this.isFavorite = isFavorite
            updateFavoriteIcon(isFavorite)
        }

        binding.favoriteButton.setOnClickListener {
            toggleFavorite(detailViewModel.event.value)
        }
    }

    private fun bindEventDetails(event: ListEventsItem) {
        Glide.with(this@DetailActivity)
            .load(event.imageLogo)
            .placeholder(R.drawable.loading_placeholder)
            .into(binding.imageDetail)

        binding.titleDetail.text = event.name
        binding.summary.text = event.summary
        binding.ownerDetail.text = "Diselenggarakan oleh: \n${event.ownerName}"
        binding.beginTimeDetail.text = "Mulai: ${event.beginTime}"
        binding.endTimeDetail.text = "Selesai: ${event.endTime}"
        binding.quotaDetail.text = String.format("Sisa Kuota: %s Peserta", event.quota - event.registrants)

        binding.descriptionDetail.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.buttonDetail.setOnClickListener {
            val link = event.link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
    }

    private fun toggleFavorite(event: ListEventsItem?) {
        if (event == null) return

        val eventEntity = EventEntity(
            id = event.id,
            name = event.name,
            mediaCover = event.mediaCover
        )

        if (isFavorite) {
            detailViewModel.deleteFavoriteEvent(event.id)
            Log.d("DetailActivity", "Event deleted from favorites")
            showToast("Event removed from favorites")

            detailViewModel.triggerRefreshFavoriteEvents()
            finish()
        } else {
            detailViewModel.insertFavoriteEvent(eventEntity)
            Log.d("DetailActivity", "Event added to favorites")
            showToast("Event added to favorites")
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
