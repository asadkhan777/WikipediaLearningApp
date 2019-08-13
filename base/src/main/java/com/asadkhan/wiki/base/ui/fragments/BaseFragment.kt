package com.asadkhan.wiki.base.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.asadkhan.wiki.base.R
import com.asadkhan.wiki.base.contex
import com.asadkhan.wiki.base.tryThis
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber.e
import androidx.appcompat.R as appR

public abstract class BaseFragment : Fragment() {

  public val bin = CompositeDisposable()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(getLayoutId(), container, false)
  }

  @JvmOverloads
  fun toast(message: String, duration: Int = LENGTH_LONG) {
    tryThis {
      makeText(context, message, duration).show()
    }
  }

  open fun showLoading() {
    println("showLoading")
  }

  open fun hideLoading() {
    println("hideLoading")
  }

  open fun showErrorView(errorMessage: String = "Oops! Something went wrong") {
    println("showErrorView: $errorMessage")
  }

  open fun showDataView() {
    println("showDataView")
  }

  @LayoutRes
  abstract fun getLayoutId(): Int

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    e("REQUEST CODE: $requestCode")
    e("RESULT CODE: $resultCode")
    e("DATA: $data")
  }

  open fun rotationAnimation() = animation(R.anim.rotate_infinite)

  open fun intent(clazz: Class<*>): Intent {
    return Intent(activity, clazz)
  }

  open fun animation(anim: Int = R.anim.rotate_infinite): Animation = activity?.let {
    loadAnimation(activity, anim)
  } ?: defaultAnimation()

  private fun defaultAnimation() = loadAnimation(contex(), defaultAnimationRes())

  open fun defaultAnimationRes() = appR.anim.abc_slide_in_bottom

  override fun onDetach() {
    super.onDetach()
    tryThis {
      bin.clear()
    }
  }

}
