package com.example.l5z1


import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder


class GameThread(private val gameSurface: GameSurface, private val surfaceHolder: SurfaceHolder) :
    Thread() {
    private var running = false
    private val MAX_FPS = 30
    override fun run() {
        var startTime: Long
        var deltaTime : Long = 30
        val targetWaitTimeMilis = (1000 / MAX_FPS).toLong()
        while (running) {
            //Updating and drawing game
            startTime = System.nanoTime()
            var canvas: Canvas? = null
            try {
                // Get Canvas from Holder and lock it.
                canvas = surfaceHolder.lockCanvas()
                synchronized(canvas) {
                    gameSurface.update(deltaTime/10)
                    gameSurface.draw(canvas)
                }
            } catch (e: Exception) {
                Log.d("Thread.canvas", e.message!!)
            } finally {
                if (canvas != null) {
                    // Unlock Canvas.
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            // Calculating wait time
            // (Change nanoseconds to milliseconds)
            var waitTime = targetWaitTimeMilis - ((System.nanoTime() - startTime) / 1000000)
            try {
                if (waitTime > 0) sleep(waitTime)
            } catch (e: InterruptedException) {
                Log.d("Thread.sleep", e.message!!)
            }
            deltaTime = (System.nanoTime() - startTime)/1000000
        }
    }

    fun setRunning(running: Boolean) {
        this.running = running
    }
}
