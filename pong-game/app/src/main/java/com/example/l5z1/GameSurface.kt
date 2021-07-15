package com.example.l5z1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameSurface(context: Context?) : SurfaceView(context),
    SurfaceHolder.Callback {
    private var gameThread: GameThread? = null
    private var gameEngine: GameEngine? = null
    override fun surfaceCreated(holder: SurfaceHolder) {
        gameThread = GameThread(this, holder)
        gameEngine = GameEngine(this.context, this.gameThread!!)
        gameThread!!.setRunning(true)
        gameThread!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                gameThread?.setRunning(false)
                gameThread?.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }

    /**
     * Wywołuje update() silnika gry
     */
    fun update(deltaTime : Long) {
        var delta = deltaTime
        if(delta <0)
            delta = 1
        gameEngine?.update(delta)
    }

    /**
     * Wywołuje draw() silnika gry
     * @param canvas kanwas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.WHITE)
        gameEngine?.draw(canvas)
    }

    /**
     * Przechwycenie eventów naciskania ekranu i przekazanie do silnika
     * @param event event ekranu
     * @return czy obsłużono
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gameEngine?.handleEvent(event) == true
    }

    init {
        this.isFocusable = true
        this.holder.addCallback(this)
    }
}
