package com.chuks.maizestemapp.common.ui


import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chuks.maizestemapp.R
import com.chuks.maizestemapp.categoriesofspecies.fallarmyworm.viewmodel.FallArmyWormViewModel
import com.chuks.maizestemapp.databinding.FragmentAfricaDetailedBinding
import com.chuks.maizestemapp.databinding.FragmentAfricanWormBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.Exception
import java.util.*


/**
* This Detailed Fragment displays the details of each list items
* */
class AfricanWormDetailedFragment : Fragment() {

    private lateinit var binding: FragmentAfricaDetailedBinding
    private val viewModel by viewModel<FallArmyWormViewModel>()
    lateinit var lat : String
    lateinit  var long : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            com.chuks.maizestemapp.R.layout.fragment_africa_detailed, container, false
        )

        binding.back.setOnClickListener { findNavController().popBackStack() }

        // detailView implementation
        getPlantByNameDetailed("AAW", "africa")
        return binding.root
    }

    private fun getPlantByNameDetailed(name: String, key: String){
               arguments?.let { bundle ->
            bundle.getInt(key)
                ?.let { position ->
                    viewModel.fallArmyList(name).observe(viewLifecycleOwner, Observer {
                        val data = it[position]
                            Glide.with(this).asBitmap().override(1080, 600)
                                .load(data.insect_image).into(binding.insectImage)
                            binding.title.text = data.name
                            lat = data.latitude
                            long = data.longitude
//                        binding.locationLat.text = data.latitude
//                        binding.locationLong.text = data.longitude
                            binding.timeTv.text = data.time
                            binding.calendar.text = data.date
                            binding.count.text = data.count.toString()
                            getAddress(lat, long)
                            openLocationOnMap(lat, long)

                    })
                }
        }
    }

  private  fun getAddress(lat: String, long : String){
        try {
            if (lat.isNotEmpty() && long.isNotEmpty()){
                val geocoder: Geocoder
                val addresses: List<Address>
                geocoder = Geocoder(requireContext(), Locale.getDefault())
                addresses = geocoder.getFromLocation(
                    lat.toDouble(),
                    long.toDouble(),
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                val address: String = addresses[0]
                    .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    val state: String = addresses[0].getAdminArea()
                    val country: String = addresses[0].getCountryName()
                    binding.locationTv.text = "$address $state $country"

            }else{
                Timber.d("No address")
                binding.locationTv.text = getString(R.string.not_available)

            }

        }catch (e: Exception){
            Timber.d(e.message)
        }
    }

   private fun openLocationOnMap(lat: String, long: String){
        binding.locationFab.setOnClickListener {
            try {
//                    geo:0,0?q=my+street+address
                if(lat.isNotEmpty() && long.isNotEmpty()){
                    val uri: String =
                        java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", lat.toFloat(), long.toFloat())

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    context?.startActivity(intent)
                    Timber.d("Lat $lat and $long")
                }else{
                    Timber.d("No address")
                }

            }catch (e: FormatFlagsConversionMismatchException){
                Timber.d(e.message)
            }
        }
    }

    /**
    * The [shareInsect] shares the selected insect
    * */
    private fun shareInsect() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hello dear, Check out Maize Insect App "
        )
        sendIntent.type = "text/plain"
        this.startActivity(sendIntent)
    }

}
