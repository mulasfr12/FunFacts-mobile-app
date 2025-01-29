import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.repository.FactRepository
import kotlinx.coroutines.launch

class FactViewModel(private val repository: FactRepository) : ViewModel() {
    private val _facts = MutableLiveData<List<Fact>>()
    val facts: LiveData<List<Fact>> get() = _facts

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchRandomFacts(count: Int = 5) {
        viewModelScope.launch {
            try {
                val randomFacts = repository.fetchRandomFacts(count) // Fetch `count` facts
                _facts.postValue(randomFacts)
            } catch (e: Exception) {
                _error.postValue("Failed to fetch facts: ${e.message}")
            }
        }
    }
    fun saveToFavorites(fact: Fact) {
        viewModelScope.launch {
            repository.saveFactToFavorites(fact)
        }
    }
}
