package com.arslan.reeltime.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arslan.reeltime.adapter.CastListAdapter
import com.arslan.reeltime.adapter.GenreEachFilmAdapter
import com.arslan.reeltime.databinding.ActivityDetailFilmBinding
import com.arslan.reeltime.model.Film
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import eightbitlab.com.blurview.RenderScriptBlur
import java.io.File
import java.io.FileOutputStream

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setVariables()

        binding.shareBtn.setOnClickListener {
            shareMovieDetails()
        }
    }

    private fun setVariables() {
        val film = intent.getSerializableExtra("object") as Film
        val requestOptions = RequestOptions().transform(
            CenterCrop(),
            GranularRoundedCorners(0f, 0f, 50f, 50f)
        )

        Glide.with(this)
            .load(film.Poster)
            .apply(requestOptions)
            .into(binding.filmPic)

        binding.titleTxt.text = film.Title
        binding.imbdTxt.text= "IMDB ${film.Imdb}"
        binding.movieTimeTxt.text = "${film.Year} - ${film.Time}"
        binding.movieSummaryTxt.text = film.Description

        binding.backBtn.setOnClickListener {
            finish()
        }

        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background

        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(radius)

        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true


        film.Genre?.let{
            binding.genreView.adapter = GenreEachFilmAdapter(it)
            binding.genreView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        film.Casts?.let{
            binding.castListView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.castListView.adapter = CastListAdapter(it)
        }

        binding.buyTicketBtn.setOnClickListener {
            val intent = Intent(this, SeatListActivity::class.java)
            intent.putExtra("film", film)
            startActivity(intent)
        }
    }

    private fun shareMovieDetails(){
        val bitmap = getScreenShotFromView(binding.movieDetail)
        if (bitmap != null) {
            val file = File(externalCacheDir, "movie.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            val uri = FileProvider.getUriForFile(this, "com.arslan.reeltime.provider", file)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Movie"))
        }
    }

    private fun getScreenShotFromView(view: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            view.draw(canvas)
        } catch (e: Exception){
            e.printStackTrace()
        }
        return screenshot
    }
}
