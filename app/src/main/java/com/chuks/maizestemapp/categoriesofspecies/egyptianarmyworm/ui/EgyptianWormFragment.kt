package com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuks.maizestemapp.R
import com.chuks.maizestemapp.capturedinsect.ui.CapturedInsectFragment
import com.chuks.maizestemapp.capturedinsect.viewmodel.CapturedInsectViewModel
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.ui.AfricanWormFragment
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.viewmodel.EgyptianWormViewModel
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.ui.FallArmywormFragment
import com.chuks.maizestemapp.common.adapter.BaseRecyclerAdapter
import com.chuks.maizestemapp.common.adapter.EqyptianBaseRecyclerAdapter
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallback
import com.chuks.maizestemapp.common.util.SwipeToDeleteCallbackEgyptian
import com.chuks.maizestemapp.common.util.showToast
import com.chuks.maizestemapp.databinding.FragmentEgyptianWormBinding
import kotlinx.android.synthetic.main.fragment_african_worm.emptyState
import kotlinx.android.synthetic.main.fragment_african_worm.progressBar
import kotlinx.android.synthetic.main.fragment_egyptian_worm.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * This is EgyptianWormFragment. This handles all UI related items
 * and communicates with the EgyptianWormViewModel
 */
class EgyptianWormFragment : Fragment() {

    private lateinit var egyptianRecyclerAdapter: EqyptianBaseRecyclerAdapter
    private lateinit var binding: FragmentEgyptianWormBinding
    private  var insectList: List<Insect> = ArrayList()
    private val TAG : String = "EgyptianWormFragment"
    private val egyptianViewModel by viewModel<EgyptianWormViewModel>()
    private val capturedInsectViewModel by viewModel<CapturedInsectViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_egyptian_worm, container, false )

        initializeRecyclerView()
        setData()
        showProgress()
        showMessage()
        egyptianRecyclerAdapter.layoutId = R.layout.egyt_insect_list_item
        egyptianRecyclerAdapter.items = insectList

        egyptianRecyclerAdapter.onCustomClickItemListner ={view, position ->

            val bundle = Bundle()
            bundle.putInt("egypt", position )

            NavHostFragment.findNavController(this)
                .navigate(R.id.egyptianWormDetailedFragment, bundle)

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
    fun deleteEgytianInsect(position: Int){
        val predictionId = insectList[position].classPredictionId
        Timber.d("predictionId is $predictionId")
        capturedInsectViewModel.deleteInsect(predictionId)
    }
    /**
     * The [initializeRecyclerView] shows a list of all egyptian worm
     */
    private fun initializeRecyclerView(){
        binding.egyptianRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            egyptianRecyclerAdapter = EqyptianBaseRecyclerAdapter(context, this@EgyptianWormFragment)
            setHasFixedSize(true)
            adapter = egyptianRecyclerAdapter
            val itemTouchHelper = ItemTouchHelper(
                SwipeToDeleteCallbackEgyptian(
                    egyptianRecyclerAdapter
                )
            )
            itemTouchHelper.attachToRecyclerView(binding.egyptianRecyclerView)
        }
    }

    /**
     * The [setData] sets the data on the recyclerview
     */
    private fun setData(){
        egyptianViewModel.egyptianList("ECLW").observe(viewLifecycleOwner, Observer {
            insectList = it
            Timber.d("egy size ${insectList.size}")
            Log.d(TAG , "captured $it")
            if(it.isNotEmpty()){
                egyptianRecyclerView.visibility = View.VISIBLE
                egyptianRecyclerAdapter.setData(it)
                emptyState.visibility = View.GONE

            }else{
                egyptianRecyclerView.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
            }
        })
    }

    /**
     * This shows the progressbar when loading the egyptian worm list data
     */
    private fun showProgress(){
        egyptianViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })
    }

    /**
     * This shows a toast message when an error occurs
     */
    private fun showMessage(){
        egyptianViewModel.showMessage.observe(viewLifecycleOwner, Observer {
            val message = it
            context?.showToast(message)
        })
    }

}
