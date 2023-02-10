package com.example.music

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private  var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())

        val playButton = findViewById<FloatingActionButton>(R.id.fabPlay)
        playButton.setOnClickListener{
            if (mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this, R.raw.maya)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }
        val pauseButton = findViewById<FloatingActionButton>(R.id.fabPause)
        pauseButton.setOnClickListener{
            mediaPlayer?.pause()
        }
        val stopButton = findViewById<FloatingActionButton>(R.id.fabStop)
        stopButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }
    private fun initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        var playedTime = findViewById<TextView>(R.id.tvPlayed)
        var remainTime = findViewById<TextView>(R.id.tvRemain)
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            handler.postDelayed(runnable,1000)

            var timePlayed = mediaPlayer!!.currentPosition/1000
            playedTime.text = "$timePlayed sec"
            var duration = mediaPlayer!!.duration/1000
            var timeRemain = duration - timePlayed
            remainTime.text = "$timeRemain sec"

        }
        handler.postDelayed(runnable,500)
    }
}
