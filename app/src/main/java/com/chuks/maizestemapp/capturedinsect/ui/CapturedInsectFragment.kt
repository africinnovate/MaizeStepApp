package com.chuks.maizestemapp.capturedinsect.ui

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuks.maizestemapp.R
import com.chuks.maizestemapp.capturedinsect.viewmodel.CapturedInsectViewModel
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.ui.AfricanWormFragment
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.ui.EgyptianWormFragment
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.ui.FallArmywormFragment
import com.chuks.maizestemapp.common.adapter.BaseRecyclerAdapter
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallback
import com.chuks.maizestemapp.common.util.showToast
import com.chuks.maizestemapp.databinding.FragmentCapturedInsectBinding
import kotlinx.android.synthetic.main.fragment_captured_insect.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * This is CapturedInsectFragment. This handles all UI related items
 * and communicates with the CapturedInsectViewModel
 */
class CapturedInsectFragment : Fragment() {

    private lateinit var capturedInsectRecyclerAdapter: BaseRecyclerAdapter
    private lateinit var binding: FragmentCapturedInsectBinding
    private var capturedInsectList: List<Insect> = ArrayList()
    private val TAG: String = "CapturedInsectFragment"
    private val capturedInsectViewModel by viewModel<CapturedInsectViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_captured_insect, container, false
        )


        initializeRecyclerView()
        setData()
        showProgress()
        showMessage()
        capturedInsectRecyclerAdapter.layoutId = R.layout.insect_list_item
        capturedInsectRecyclerAdapter.items = capturedInsectList

        capturedInsectRecyclerAdapter.onCustomClickItemListner = { view, position ->
            val bundle = Bundle()
            bundle.putInt("position", position)
            NavHostFragment.findNavController(this)
                .navigate(R.id.detailedFragment, bundle)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    //Delete all method will be called
                    deleteAll()
                    context?.showToast("Delete clicked")
                    true
                }
                else -> {
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    super.onOptionsItemSelected(item)
                }
            }
        }

        binding.swipe.setOnRefreshListener {
            setData()
            showMessage()
            showProgress()
            binding.swipe.isRefreshing = false
        }

//        ItemTouchHelper(object :
//            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                capturedInsectViewModel.deleteInsect("ECLW-37cf76e5-01f8-4422-ab95-3d247a27f28a")
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                // on recycler view item swiped then we are deleting the item of our recycler view.
//                val position = viewHolder.adapterPosition
//                val predictionId = capturedInsectList[position].classPredictionId
//                Timber.d("prediction $predictionId")
//                capturedInsectViewModel.deleteInsect(predictionId)
//                capturedInsectRecyclerAdapter.notifyItemRemoved(position)
//                capturedInsectRecyclerAdapter.notifyItemChanged(position)
//                context?.showToast("insect successfully deleted $position")
//            }
//        }).attachToRecyclerView(binding.captureRecyclerView)


        return binding.root
    }

    fun deleteInsect(position: Int){
       val predictionId = capturedInsectList[position].classPredictionId
        Timber.d("predictionId is $predictionId")
        capturedInsectViewModel.deleteInsect(predictionId)
    }
    /**
     * The [initializeRecyclerView] shows a list of all captured insects
     */
    fun initializeRecyclerView() {
        binding.captureRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            capturedInsectRecyclerAdapter = BaseRecyclerAdapter(
                context,
                this@CapturedInsectFragment
            )
            setHasFixedSize(true)
            adapter = capturedInsectRecyclerAdapter
            val itemTouchHelper = ItemTouchHelper(
                SwipeToDeleteCallback(
                    capturedInsectRecyclerAdapter
                )
            )
            itemTouchHelper.attachToRecyclerView(binding.captureRecyclerView)
        }
    }

    /**
     * The [setData] sets the data on the recyclerview
     */
    private fun setData() {
        capturedInsectViewModel.capturedInsect.observe(viewLifecycleOwner, Observer {
            capturedInsectList = it
            Log.d(TAG, "captured $it.size")
            Log.d(TAG, "captured insects ${capturedInsectList.size}")
            if (it.isNotEmpty()) {
                captureRecyclerView.visibility = View.VISIBLE
                capturedInsectRecyclerAdapter.setData(it)
                emptyState.visibility = View.GONE

            } else {
                binding.captureRecyclerView.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
            }
        })
    }

    /**
     * This shows the progressbar when loading the captured list data
     */
    private fun showProgress() {
        capturedInsectViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    /**
     * This shows a toast message when an error occurs
     */
    private fun showMessage() {
        capturedInsectViewModel.showMessage.observe(viewLifecycleOwner, Observer {
            val message = it
            context?.showToast(message)
        })
    }

   private fun deleteAll() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(activity)
        builder.setTitle(R.string.confirm)
        builder.setMessage(R.string.are_you_sure)
        builder.setPositiveButton(
            R.string.yes
        ) { dialogInterface, i ->
            try {
                capturedInsectViewModel.deleteAllInsect()
            }catch (e: NetworkErrorException){
                e.message
            }
        }
        builder.setNegativeButton(
            R.string.no
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.show()
    }


      companion object fun newInstance(): CapturedInsectFragment? {
           val args = Bundle()
//        args.putBoolean("yourKey", yourValue)
           val fragment = CapturedInsectFragment()
           fragment.arguments = args
           return fragment
       }


}

