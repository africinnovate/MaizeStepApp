package com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.ui


import android.accounts.NetworkErrorException
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
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.ui.AfricanWormFragment
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.ui.EgyptianWormFragment
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.viewmodel.FallArmyWormViewModel
import com.chuks.maizestemapp.common.adapter.BaseRecyclerAdapter
import com.chuks.maizestemapp.common.adapter.FallBaseRecyclerAdapter
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallback
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallbackFall
import com.chuks.maizestemapp.common.util.showToast
import com.chuks.maizestemapp.databinding.FragmentFallArmywormBinding
import kotlinx.android.synthetic.main.fragment_egyptian_worm.emptyState
import kotlinx.android.synthetic.main.fragment_egyptian_worm.progressBar
import kotlinx.android.synthetic.main.fragment_fall_armyworm.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * This is FallArmywormFragment. This handles all UI related items
 * and communicates with the FallArmyWormViewModel
 */
class FallArmywormFragment : Fragment() {

    private lateinit var fallArmyRecyclerAdapter: FallBaseRecyclerAdapter
    private lateinit var binding: FragmentFallArmywormBinding
    private  var insectList: List<Insect> = ArrayList()
    private val TAG : String = "FallArmywormFragment"
    private val fallArmyViewModel by viewModel<FallArmyWormViewModel>()
    private val capturedInsectViewModel by viewModel<CapturedInsectViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_fall_armyworm, container,
            false )

        initializeRecyclerView()
        setData()
        showProgress()
        showMessage()
        fallArmyRecyclerAdapter.layoutId = R.layout.fall_insect_list_item
        fallArmyRecyclerAdapter.items = insectList

        fallArmyRecyclerAdapter.onCustomClickItemListner ={view, position ->

            val bundle = Bundle()
            bundle.putInt("fall", position )

            NavHostFragment.findNavController(this)
                .navigate(R.id.fallWormDetailedFragment, bundle)

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
    fun deleteFallInsect(position: Int){
        val predictionId = insectList[position].classPredictionId
        Timber.d("predictionId is $predictionId")
        capturedInsectViewModel.deleteInsect(predictionId)
    }
    /**
     * The [initializeRecyclerView] shows a list of all fall armyworm
     */
    private fun initializeRecyclerView(){
        binding.fallRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            fallArmyRecyclerAdapter = FallBaseRecyclerAdapter(context,
                this@FallArmywormFragment)
            setHasFixedSize(true)
            adapter = fallArmyRecyclerAdapter
            val itemTouchHelper = ItemTouchHelper(
                SwipeToDeleteCallbackFall(
                    fallArmyRecyclerAdapter
                )
            )
            itemTouchHelper.attachToRecyclerView(binding.fallRecyclerView)
        }
    }

    /**
     * The [setData] sets the data on the recyclerview
     */
    private fun setData(){
        fallArmyViewModel.fallArmyList("FAW").observe(viewLifecycleOwner, Observer {
            insectList = it
            Timber.d("fall list ${insectList.size}")
            Log.d(TAG , "captured $it")
            if(it.isNotEmpty()){
                fallRecyclerView.visibility = View.VISIBLE
                fallArmyRecyclerAdapter.setData(it)
                emptyState.visibility = View.GONE

            }else{
                fallRecyclerView.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
            }
        })
    }

    /**
     * This shows the progressbar when loading the fall armyworm list data
     */
    private fun showProgress(){
        fallArmyViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })
    }

    /**
     * This shows a toast message when an error occurs
     */
    private fun showMessage(){
        fallArmyViewModel.showMessage.observe(viewLifecycleOwner, Observer {
            val message = it
            context?.showToast(message)
        })
    }
}
