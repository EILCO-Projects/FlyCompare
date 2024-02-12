package fr.eilco.flycompare.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.eilco.flycompare.adapters.MyFlights

class MyViewModel : ViewModel() {
    private val myFlights = MutableLiveData<MyFlights>()

    fun setMyFlights(myFlights: MyFlights) {
        this.myFlights.value = myFlights
    }

    fun getMyFlights(): LiveData<MyFlights> {
        return myFlights
    }

}