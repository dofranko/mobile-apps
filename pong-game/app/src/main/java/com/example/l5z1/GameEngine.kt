package com.example.l5z1


import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import java.util.*
import kotlin.concurrent.thread


class GameEngine() {
    private lateinit var context: Context;
    var SCREEN_WIDTH: Int = 0
    var SCREEN_HEIGHT: Int = 0
    var SCREEN_OFFSET : Int = 20
    var RECTS_HEIGHT : Int = 200
    var RECTS_WIDTH : Int = 20
    companion object{
        lateinit var gameEngine:GameEngine;
        lateinit var gameThread: GameThread
        var playerPoints : Int = 0
        var enemyPoints : Int = 0
        var textPaint : Paint = Paint()
        fun addPoints(who: Int){
            if(who == 0){
                enemyPoints++;
                if(enemyPoints >=10){
                    this.gameEngine.endGame()
                }
            }
            else{
                playerPoints++;
                if(playerPoints>=10){
                    this.gameEngine.endGame()
                }
            }
        }
    }
    constructor(context: Context, gameThread: GameThread) : this() {
        GameEngine.gameEngine = this
        GameEngine.gameThread = gameThread
        this.context = context
        this.SCREEN_WIDTH = Resources.getSystem().displayMetrics.widthPixels
        this.SCREEN_HEIGHT = Resources.getSystem().displayMetrics.heightPixels
        this.player =  Player(Rect(
            SCREEN_OFFSET, SCREEN_HEIGHT / 2 - RECTS_HEIGHT / 2,
            SCREEN_OFFSET + RECTS_WIDTH, SCREEN_HEIGHT / 2 + RECTS_HEIGHT / 2
        ))
        this.enemy = Enemy(Rect(SCREEN_WIDTH - SCREEN_OFFSET - RECTS_WIDTH, SCREEN_HEIGHT/2 - RECTS_HEIGHT,
            SCREEN_WIDTH - SCREEN_OFFSET, SCREEN_HEIGHT/2 + RECTS_HEIGHT), SCREEN_HEIGHT)
        this.ball  = Ball(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, SCREEN_WIDTH, SCREEN_HEIGHT, this.player, this.enemy)
        resetCompanion()
    }
    fun resetCompanion(){
        playerPoints = 0
        enemyPoints  = 0
        textPaint.color = Color.BLACK
        textPaint.textSize = 100.0f
    }
    class Player {
        var paint: Paint
        var rect: Rect

        constructor(rect: Rect) {
            this.rect = rect;
            this.paint = Paint()
            paint.color = Color.BLACK
        }

        fun setPosition(y: Int) {
            this.rect.set(rect.left, y-rect.height()/2, rect.right, y + rect.height()/2)
        }

        fun draw(canvas: Canvas) {
            canvas.drawRect(rect, paint)
        }
    }
    class Enemy{
        var paint : Paint
        var rect: Rect
        var screen_height:Int = 0
        var speed: Int = 10
        constructor(rect: Rect, screen_height : Int){
            this.rect = rect
            this.paint = Paint()
            paint.color = Color.BLACK
            this.screen_height = screen_height
        }

        fun update(deltaTime: Int){
            this.rect.set(rect.left, rect.top + speed*deltaTime,
                rect.right, rect.bottom + speed*deltaTime)
            if(this.speed < 0 && this.rect.top <= 0 ){
                this.speed *= -1
            }
            else if(this.speed > 0 && this.rect.bottom >= this.screen_height){
                this.speed *= -1
            }

        }
        fun draw(canvas: Canvas){
            canvas.drawRect(rect, paint)
        }
    }
    class Ball{
        var paint : Paint
        var rect: Rect
        val size : Int = 20
        var speedX : Int = 3
        var speedY : Int = 2
        var screen_width : Int = 0
        var screen_height : Int = 0
        var player : Player
        var enemy: Enemy
        constructor(x:Int, y:Int, screen_width : Int, screen_height: Int, player : Player, enemy: Enemy){
            this.rect = Rect(x-size/2, y-size/2,x+size/2,y+size/2)
            this.paint = Paint()
            paint.color = Color.BLACK
            this.screen_width = screen_width
            this.screen_height = screen_height
            this.player = player
            this.enemy = enemy
        }

        fun update(deltaTime: Int){
            val lastRect = Rect(this.rect)
            this.rect.set(rect.left+ speedX*deltaTime, rect.top + speedY*deltaTime,
                rect.right + speedX*deltaTime, rect.bottom + speedY*deltaTime)

            if(this.speedX < 0 ) {
                val deltaRect = Rect(this.rect.left, Math.min(lastRect.top, this.rect.top),
                    lastRect.right ,Math.max(this.rect.bottom, lastRect.bottom) )
                if(Rect.intersects(this.rect, player.rect) || Rect.intersects(deltaRect, player.rect)){
                    this.speedX *= -1
                    increaseSpeedX()
                }
                else if (this.rect.left <= 0) {
                    GameEngine.addPoints(0);
                    this.speedX *= -1
                    increaseSpeedX()
                }
            }
            else {
                val deltaRect = Rect(lastRect.left, Math.min(lastRect.top, this.rect.top),
                    this.rect.right ,Math.max(this.rect.bottom, lastRect.bottom) )
                if(Rect.intersects(this.rect, enemy.rect)|| Rect.intersects(deltaRect, enemy.rect)){
                    this.speedX *= -1
                    increaseSpeedX()
                }
                else if (this.rect.right >= this.screen_width) {
                    GameEngine.addPoints(1);
                    this.speedX *= -1
                    increaseSpeedX()
                }
            }

            if(this.speedY > 0 && this.rect.bottom >= this.screen_height){
                this.speedY *= -1
            }
            else if(this.speedY < 0 && this.rect.top <= 0){
                this.speedY *= -1
            }

        }
        fun increaseSpeedX(){
            if (Math.abs(this.speedX) <= 15) {
                if (this.speedX < 0) {
                    this.speedX -= 1
                } else {
                    this.speedX += 1
                }
            }
        }
        fun draw(canvas: Canvas){
            canvas.drawRect(rect, paint)
        }
    }

    lateinit var player: Player
    lateinit var enemy : Enemy
    lateinit var ball : Ball
    var isPlaying: Boolean = false

    fun update(deltaTime : Long) {
        if(!isPlaying) return
        enemy.update(deltaTime.toInt())
        ball.update(deltaTime.toInt())
    }

    fun draw(canvas: Canvas) {
        player.draw(canvas)
        enemy.draw(canvas)
        ball.draw(canvas)
        var pain = Paint()
        canvas.drawText("$playerPoints:$enemyPoints",
            (SCREEN_WIDTH/2).toFloat(), (SCREEN_HEIGHT/2).toFloat(), GameEngine.textPaint)
    }

    fun endGame(){
        this.isPlaying = false
        GameEngine.gameThread.setRunning(false)
        val t = thread {
            val db = GameDatabase.getInstance(this.context)
            db.resultDao().insert(ResultEntity(0, GameEngine.playerPoints, GameEngine.enemyPoints))
        }
        t.join()
        (this.context as GameActivity).finish()
    }

    fun handleEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if(!isPlaying){
                    isPlaying = true
                    return true
                }
                player.setPosition(event.y.toInt())
            }
        }
        return true
    }


}
