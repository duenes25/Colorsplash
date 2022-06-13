/*
 * Copyright (c) 2022 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 * 
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.colorsplash

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

  var contentHasLoaded = false

  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    startLoadingContent()
    setupSplashScreen(splashScreen)
  }

  private fun startLoadingContent() {
    // For this example, the Timer delay represents awaiting a response from a network call
    Timer().schedule(3000){
      contentHasLoaded = true
    }
  }

  private fun setupSplashScreen(splashScreen: SplashScreen) {
    val content: View = findViewById(android.R.id.content)
    content.viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
          override fun onPreDraw(): Boolean {
            return if (contentHasLoaded) {
              content.viewTreeObserver.removeOnPreDrawListener(this)
              true
            } else false
          }
        }
    )

    splashScreen.setOnExitAnimationListener { splashScreenView ->
      val slideBack = ObjectAnimator.ofFloat(
          splashScreenView.view,
          View.TRANSLATION_X,
          0f,
          -splashScreenView.view.width.toFloat()
      ).apply {
        interpolator = DecelerateInterpolator()
        duration = 800L
        doOnEnd { splashScreenView.remove() }
      }

      slideBack.start()
    }
  }
}
