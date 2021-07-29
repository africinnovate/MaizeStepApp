package com.chuks.maizestemapp.maizestemplot.ui


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chuks.maizestemapp.R
import com.chuks.maizestemapp.capturedinsect.viewmodel.CapturedInsectViewModel
import com.chuks.maizestemapp.categoriesofspecies.egyptianarmyworm.viewmodel.EgyptianWormViewModel
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.util.DateValueFormatter
import com.chuks.maizestemapp.common.util.showToast
import com.chuks.maizestemapp.maizestemplot.viewmodel.MaizePlotViewModel
import com.chuks.maizestemapp.maizestemplot.viewmodel.combineWith
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_maize_insect_plot.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList


/**
 * This class handles the plotting of the chart
 */
class MaizeInsectPlot : Fragment() {

    private lateinit var linechart: LineChart
    private val insectViewModel by viewModel<MaizePlotViewModel>()
    private val viewmodel by viewModel<CapturedInsectViewModel>()
    private val viewModel1 by viewModel<EgyptianWormViewModel>()
    private lateinit var progress: ProgressBar
    private var dataVal1 = ArrayList<Entry>()

    var colorArray: MutableList<Int> = mutableListOf(
        Color.RED, Color.BLACK, Color.BLUE, Color.GREEN, Color.GRAY
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            com.chuks.maizestemapp.R.layout.fragment_maize_insect_plot,
            container,
            false
        )
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        progress = view.findViewById<ProgressBar>(R.id.progressBarPlot)
        linechart = view.findViewById(R.id.lineChart)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        initMe()
        showMessage()
        showProgress()
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

//       val  dateTime = getDateTimeFromEpocLongOfSeconds(1626045358)

        swipe.setOnRefreshListener {
            initMe()
            showMessage()
            showProgress()
            swipe.isRefreshing = false
        }
        return view
    }

    private fun lineGraph1(
        africaEntry: List<Entry>,
        egyptEntry: List<Entry>,
        fallEntry: List<Entry>
    ) {
        val africaDataSet = LineDataSet(africaEntry, "AAW")
        val egytDataSet = LineDataSet(egyptEntry, "ECLW")
        val fallDataSet = LineDataSet(fallEntry, "FAW")
        val etLine = LineDataSet(etDataValue(), "ET")
        val eilLine = LineDataSet(eilDataValue(), "EIL")

        africaDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        egytDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
//        fallDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(africaDataSet)
        dataSet.add(egytDataSet)
        dataSet.add(fallDataSet)
        dataSet.add(etLine)
        dataSet.add(eilLine)

        //Limit lines for et and eil
        val et = LimitLine(2.4f, "ET")
        et.lineColor = Color.GREEN
        et.lineWidth = 2f

        val eil = LimitLine(3.6f, "EIL")
        lineChart.axisRight.removeAllLimitLines()
        lineChart.axisRight.addLimitLine(et)
        lineChart.axisRight.addLimitLine(eil)
        eil.textSize = 10f
        et.textSize = 10f

        val leftAxis: YAxis = linechart.axisLeft
        leftAxis.removeAllLimitLines()

        eil.lineColor = Color.RED
        eil.lineWidth = 2f

        africaDataSet.notifyDataSetChanged()
        egytDataSet.notifyDataSetChanged()
        fallDataSet.notifyDataSetChanged()
        etLine.notifyDataSetChanged()
        eilLine.notifyDataSetChanged()

        //Adding features to the lines
        africaDataSet.valueTextSize = 8f
        egytDataSet.valueTextSize = 8f
        fallDataSet.valueTextSize = 8f

        val xAxis: XAxis = linechart.xAxis
        val position = XAxisPosition.BOTTOM
        xAxis.position = position
        xAxis.axisMinimum = 0f
        xAxis.setLabelCount(6, true)
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 7f
//        xAxis.labelRotationAngle = 315f
//        xAxis.setCenterAxisLabels(true)

        africaDataSet.color = Color.rgb(0, 0, 204)
        egytDataSet.color = Color.rgb(0, 204, 204)
        fallDataSet.color = Color.rgb(255, 0, 244)
        etLine.color = Color.GREEN
        eilLine.color = Color.RED

        africaDataSet.lineWidth = 2f
        egytDataSet.lineWidth = 2f
        fallDataSet.lineWidth = 2f

        val description = Description()
        description.text = "Date captured"
        description.textColor = Color.BLACK
        description.textSize = 16f
        linechart.description = description
//        description.text = UISetters.getFullMonthName();
        linechart.setDrawGridBackground(true)
        linechart.setNoDataText("No Data")
        linechart.setNoDataTextColor(Color.RED)
        linechart.setTouchEnabled(true)
        linechart.setPinchZoom(true)


        val linedata = LineData(dataSet)
        linechart.data = linedata
        linechart.invalidate()
    }

    private fun initMe() {
        val africanEntries = insectViewModel.dataEntries("AAW", linechart)
        val egyptianEntries = insectViewModel.dataEntries("ECLW", linechart)
        val fallEntries = insectViewModel.dataEntries("FAW", linechart)
        africanEntries.combineWith(egyptianEntries, fallEntries) { afr, egy, fall ->
            Timber.d("afric ${afr?.size}")
            Timber.d("egy ${egy?.size}")
            Timber.d("fall ${fall?.size}")
            lineGraph1(afr ?: emptyList(), egy ?: emptyList(), fall ?: emptyList())
        }.observe(viewLifecycleOwner, Observer {
        })
    }

    /**
     * This shows the progressbar as the chart loads
     */
    private fun showProgress() {
        insectViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            progressBarPlot.visibility = if (it) View.VISIBLE else View.GONE
        })
    }


    /**
     * This shows an error or failure message
     */
    private fun showMessage() {
        insectViewModel.showMessage.observe(viewLifecycleOwner, Observer {
            val message = it
            context?.showToast(message)
        })
    }


    fun getAllInsects() {
        val dataVal = ArrayList<Entry>()
        viewmodel.capturedInsect.observe(viewLifecycleOwner, Observer { maizePlot ->
            if (maizePlot.isNotEmpty()) {
                linechart.visibility = View.VISIBLE
                val fall: List<Insect> = maizePlot
//                        Timber.d("${maizePlot.}")
                maizePlot.forEach {
                    try {
                        dataVal.add(
                            Entry(
                                it.date.takeLast(2).toInt().toFloat(),
                                it.count.toFloat()
                            )
                        )
                        Timber.d("data value egypt ${dataVal.size}")
                    } catch (e: NumberFormatException) {
                        Timber.d("Exception is $e")
                    }
                }
//                lineGraph1(dataVal)
                Timber.d("data size for egyptian plot ${maizePlot.size}")
            } else {
                linechart.visibility = View.GONE
            }
        })
    }

    private fun etDataValue(): ArrayList<Entry> {
        val dataVal = ArrayList<Entry>()
        dataVal.add(Entry(24f, 2.4f))
        return dataVal
    }

    private fun eilDataValue(): ArrayList<Entry> {
        val dataVal = ArrayList<Entry>()
        dataVal.add(Entry(24f, 3.6f))
        return dataVal
    }

    private fun testDataValue(): ArrayList<Entry> {
        val dataVal = ArrayList<Entry>()
        dataVal.add(Entry(20f, 3.6f))
        dataVal.add(Entry(22f, 5f))
        dataVal.add(Entry(12f, 4f))
        return dataVal
    }

    private fun testDataValue2(): ArrayList<Entry> {
        val dataVal = ArrayList<Entry>()
        dataVal.add(Entry(2f, 6f))
        dataVal.add(Entry(15f, 2f))
        dataVal.add(Entry(10f, 4f))
        dataVal.add(Entry(17f, 3f))
        dataVal.add(Entry(18f, 1f))
        return dataVal
    }
}





