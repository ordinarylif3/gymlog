import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutViewModel : ViewModel() {
    private val _sessionID = MutableLiveData<String>()
    val sessionID: LiveData<String> get() = _sessionID

    fun setSessionID(sessionID: String) {
        _sessionID.value = sessionID
    }
}
