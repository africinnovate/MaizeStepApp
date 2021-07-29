package com.chuks.maizestemapp.maizestemplot.viewmodel

import androidx.lifecycle.*
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.MaizePlot
import com.chuks.maizestemapp.common.util.DateValueFormatter
import com.chuks.maizestemapp.common.util.MyUtils
import com.chuks.maizestemapp.maizestemplot.repository.MaizePlotRepository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * This is MaizePlotViewModel. This has a param [maizePlotRepository]
 * and extends [ViewModel]
 */
class MaizePlotViewModel(private val maizePlotRepository: MaizePlotRepository) : ViewModel() {

    private val _showMessage: MutableLiveData<String> = MutableLiveData()
    val showMessage: LiveData<String> get() = _showMessage

    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean> get() = _showProgress

    private val _maize: MutableLiveData<List<MaizePlot>> = MutableLiveData()
    val maize: LiveData<List<MaizePlot>> get() = _maize

    fun maizePlot(name: String) = maizePlotRepository.getInsect(name)

    fun dataEntries(name: String,lineChart: LineChart): LiveData<List<Entry>> =
        Transformations.map(maizePlot(name)) { insects: List<Insect> ->
            if (insects.isEmpty()) emptyList<Entry>()
//            val xAxis: XAxis = lineChart.xAxis
//           xAxis.valueFormatter =  DateValueFormatter(insects)
            insects.map {
                Entry(
                it.date.takeLast(2).toInt().toFloat(), it.count.toFloat()
                )
            }
        }


    init {

        viewModelScope.launch {
            try {
                _showProgress.postValue(true)
                maizePlotRepository.requestInsect()
                _showProgress.postValue(false)
            } catch (e: Throwable) {
                _showMessage.postValue("Unable to connect to server!")
                _showProgress.postValue(false)
            }
        }
    }
}

fun <T, K, R, Y> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    secondliveData: LiveData<Y>,
    block: (T?, K?, Y?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value, secondliveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value, secondliveData.value)
    }
    result.addSource(secondliveData) {
        result.value = block(this.value, liveData.value, secondliveData.value)
    }
    return result
}