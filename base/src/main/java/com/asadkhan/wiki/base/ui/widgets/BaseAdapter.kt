package com.asadkhan.wiki.base.ui.widgets

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asadkhan.wiki.base.tryHere
import com.asadkhan.wiki.base.tryThis
import com.asadkhan.wiki.base.uiThread
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.subjects.PublishSubject.create
import java.util.concurrent.TimeUnit.MILLISECONDS

@Suppress("UNCHECKED_CAST")
public abstract class BaseAdapter<T>(
  open var items: HashSet<Any?>, open val itemsFull: HashSet<Any?>
) : Adapter<ViewHolder>() {

  private val viewClickSubject = create<T>()
  private val viewClickGenericSubject = create<Any?>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = from(parent.context).inflate(viewHolderLayout(), parent, false)
    val viewHolder = getViewHolder(view)
    viewHolder.itemView.setOnClickListener {
      if (viewHolder is BaseViewHolder<*>) {
        // This conversion should not fail as long as
        // the adapter:view-holder contract is strictly adhered to
        tryThis {
          val item = viewHolder.data as T
          viewClickSubject
            .onNext(tryHere({ item }, defaultValue()))
        }
      }
    }
    return viewHolder
  }

  abstract fun defaultValue(): T

  fun clickStream(): Observable<T> {
    return viewClickSubject.debounce(800, MILLISECONDS, io()).uiThread()
  }

  @LayoutRes
  abstract fun viewHolderLayout(): Int

  abstract fun getViewHolder(itemView: View): ViewHolder

  abstract fun update(newItems: HashSet<Any?>)

  @Deprecated(message = "Deprecated in favor of update()")
  abstract fun append(newItems: HashSet<Any?>, refreshUI: Boolean)

  abstract fun clear(refreshUI: Boolean)

  abstract fun appendAll(newItems: Collection<Any?>, refreshUI: Boolean)

  abstract fun hasData(): Boolean
}
