package com.chuks.maizestemapp.common.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProviders.*
import androidx.recyclerview.widget.RecyclerView
import com.chuks.maizestemapp.capturedinsect.ui.CapturedInsectFragment
import com.chuks.maizestemapp.capturedinsect.viewmodel.CapturedInsectViewModel
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.ui.AfricanWormFragment
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.ui.EgyptianWormFragment
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.ui.FallArmywormFragment
import com.chuks.maizestemapp.common.data.Insect
import kotlinx.android.synthetic.main.insect_list_item.*


/**
 *Common adapter to load the recyclerView
 */

class AfricaBaseRecyclerAdapter(
    val context: Context,
    val fragment: AfricanWormFragment
) :
    RecyclerView.Adapter<AfricaBaseRecyclerAdapter.BaseViewHolder>() {

    //Layout ID that needs to be given to inflate the row
    @LayoutRes
    var layoutId: Int = 0

    //List of items that will be inflated in the layout
    lateinit var items: List<Insect>

//    private lateinit var capturedInsectViewModel : CapturedInsectViewModel
//    private val capturedInsectViewModel by viewModel<CapturedInsectViewModel>()

    /**
     * Use [setData] to update the data from the adapter
     * @param edited A list of the generic type
     ***/
    fun setData(list: List<Insect>) {
        items = list
        notifyDataSetChanged()
    }

    /**
     *   Event handler, can be used in the row layout to register any click events,
     *   these events can be handled in the parent.
     */
    var onCustomClickItemListner = fun(view: View, position: Int): Unit = Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            layoutId,
            parent,
            false
        )
//        capturedInsectViewModel = of((context as FragmentActivity)!!).get(
//            CapturedInsectViewModel::class.java
//        )
        return BaseViewHolder(binding)
    }


    /**
     *   Count of items inside the adapter
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     *   This binds the views to the viewHolder
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])

    }

    inner class BaseViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ob: Any?) {
            binding.setVariable(BR.model, ob)
            binding.setVariable(BR.handler, this)
            binding.setVariable(BR.position, adapterPosition)
            binding.executePendingBindings()
        }

        /**
         *   Propagate clicks to parent via the event handler
         */
        fun onCustomClick(view: View, position: Int) {
            onCustomClickItemListner(view, position)
        }
    }

    fun getInsertAt(position: Int): Insect {
        return items[position]
    }

//    fun getContext() : Context {
//       return context
//    }


    fun deleteInsect(position: Int) {

//        mRecentlyDeletedItem = items[position]
//        mRecentlyDeletedItemPosition = position
//        items.removeAt(position)
        fragment.deleteAfricanInsect(position)
        notifyItemRemoved(position)
        notifyItemChanged(position)
//        showUndoSnackbar()
    }


//    private fun showUndoSnackbar() {
//        val view: View = mActivity.findViewById(R.id.coordinator_layout)
//        val snackbar: Snackbar = Snackbar.make(
//            view, R.string.snack_bar_text,
//            Snackbar.LENGTH_LONG
//        )
//        snackbar.setAction(R.string.snack_bar_undo) { v -> undoDelete() }
//        snackbar.show()
//    }
//
//    private fun undoDelete() {
//        items.add(
//            mRecentlyDeletedItemPosition,
//            mRecentlyDeletedItem
//        )
//        notifyItemInserted(mRecentlyDeletedItemPosition)
//    }


}