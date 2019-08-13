package com.asadkhan.wiki.base.ui.activities

import android.os.Bundle
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asadkhan.wiki.base.R
import com.asadkhan.wiki.base.tryThis
import io.reactivex.subjects.PublishSubject.create
import timber.log.Timber.e

abstract class BaseActivity : AppCompatActivity() {

  open val searchPublishSubject = create<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    e("SavedInstanceState: $savedInstanceState")
    FragmentManager.enableDebugLogging(true)
  }

  @JvmOverloads
  fun toast(message: String, duration: Int = LENGTH_LONG) {
    tryThis {
      makeText(this, message, duration).show()
    }
  }

  open fun showFragment(
    layoutId: Int,
    fragment: Fragment,
    animIn: Int = R.anim.abc_slide_in_bottom,
    animOut: Int = R.anim.abc_slide_out_bottom
  ) {
    supportFragmentManager
      .beginTransaction()
      .add(layoutId, fragment)
      .setCustomAnimations(animIn, animOut)
      .commitNow()
  }

}
