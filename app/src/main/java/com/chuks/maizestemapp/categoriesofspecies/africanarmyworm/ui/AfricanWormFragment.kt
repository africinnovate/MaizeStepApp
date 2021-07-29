package com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuks.maizestemapp.R
import com.chuks.maizestemapp.capturedinsect.ui.CapturedInsectFragment
import com.chuks.maizestemapp.capturedinsect.viewmodel.CapturedInsectViewModel
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.viewmodel.AfricanArmyWormViewModel
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.ui.EgyptianWormFragment
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.ui.FallArmywormFragment
import com.chuks.maizestemapp.common.adapter.AfricaBaseRecyclerAdapter
import com.chuks.maizestemapp.common.adapter.BaseRecyclerAdapter
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallback
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallbackAfrica
import com.chuks.maizestemapp.common.util.showToast
import com.chuks.maizestemapp.databinding.FragmentAfricanWormBinding
import kotlinx.android.synthetic.main.fragment_african_worm.*
import kotlinx.android.synthetic.main.fragment_captured_insect.emptyState
import kotlinx.android.synthetic.main.fragment_captured_insect.progressBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * This is AfricanWormFragment. This handles all UI related items
 * and communicates with the AfricanArmyWormViewModel
 */
class AfricanWormFragment : Fragment() {
    private lateinit var africanRecyclerAdapter: AfricaBaseRecyclerAdapter
    private lateinit var binding: FragmentAfricanWormBinding
    private  var insectList: List<Insect> = ArrayList()
    private val TAG : String = "AfricanWormFragment"
    private val africanArmyWormViewModel by viewModel<AfricanArmyWormViewModel>()
    private val capturedInsectViewModel by viewModel<CapturedInsectViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_african_worm,
            container, false
        )

        initializeRecyclerView()
        setData()
        showProgress()
        showMessage()
        africanRecyclerAdapter.layoutId = R.layout.africa_insect_list_item
        africanRecyclerAdapter.items = insectList

        africanRecyclerAdapter.onCustomClickItemListner ={ view, position ->

            val bundle = Bundle()
            bundle.putInt("africa", position)

            NavHostFragment.findNavController(this)
                .navigate(R.id.AfricaDetailedFragment, bundle)

            Toast.makeText(context, "You clicked $position", Toast.LENGTH_LONG).show()

        }

        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        binding.swipe.setOnRefreshListener {
            setData()
            showMessage()
            showProgress()
            binding.swipe.isRefreshing = false
        }
        return binding.root
    }
    fun deleteAfricanInsect(position: Int){
        val predictionId = insectList[position].classPredictionId
        Timber.d("predictionId is $predictionId")
        capturedInsectViewModel.deleteInsect(predictionId)
    }
    /**
     * The [initializeRecyclerView] shows a list of all african worm
     */
    private fun initializeRecyclerView(){
        binding.africanRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            africanRecyclerAdapter = AfricaBaseRecyclerAdapter(context,  this@AfricanWormFragment)
            setHasFixedSize(true)
            adapter = africanRecyclerAdapter
            val itemTouchHelper = ItemTouchHelper(
                SwipeToDeleteCallbackAfrica(
                    africanRecyclerAdapter
                )
            )
            itemTouchHelper.attachToRecyclerView(binding.africanRecyclerView)
        }
    }

    /**
     * The [setData] sets the data on the recyclerview
     */
    private fun setData(){
        africanArmyWormViewModel.africanArmyList("AAW").observe(viewLifecycleOwner, Observer {
            insectList = it
            Timber.d("Afr size ${insectList.size}")
            Log.d(TAG, "captured $it")
            if (it.isNotEmpty()) {
                africanRecyclerView.visibility = View.VISIBLE
                africanRecyclerAdapter.setData(it)
                emptyState.visibility = View.GONE

            } else {
                africanRecyclerView.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
            }
        })
    }

    /**
     * This shows the progressbar when loading the african worm list data
     */
    private fun showProgress(){
        africanArmyWormViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    /**
     * This shows a toast message when an error occurs
     */
    private fun showMessage(){
        africanArmyWormViewModel.showMessage.observe(viewLifecycleOwner, Observer {
            val message = it
            context?.showToast(message)
        })
    }

}
