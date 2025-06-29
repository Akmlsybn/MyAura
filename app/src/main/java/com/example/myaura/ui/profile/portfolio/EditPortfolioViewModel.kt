package com.example.myaura.ui.profile.portfolio

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ImgurApiService
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.usecase.GetPortfolioUseCase
import com.example.myaura.domain.usecase.UpdatePortfolioUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class EditPortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val updatePortfolioUseCase: UpdatePortfolioUseCase,
    private val auth: FirebaseAuth,
    private val imgurApiService: ImgurApiService,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _editState = MutableStateFlow(EditPortfolioState())
    val editState = _editState.asStateFlow()


    private val portfolioId: String = savedStateHandle.get<String>("portfolioId")!!

    init {
        loadPortfolioDetails()
    }

    private fun loadPortfolioDetails() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch
            getPortfolioUseCase(uid, portfolioId)
                .onSuccess { portfolioItem ->
                    if (portfolioItem != null) {
                        _editState.value = _editState.value.copy(
                            isLoading = false,
                            title = portfolioItem.title,
                            description = portfolioItem.description,
                            startDate = portfolioItem.dateRange.split(" - ")[0],
                            endDate = portfolioItem.dateRange.split(" - ").getOrElse(1){ "" },
                            isCurrent = portfolioItem.dateRange.contains("Saat Ini"),
                            skill = portfolioItem.skill,
                            projectUrl = portfolioItem.projectUrl,
                            imageUrl = portfolioItem.imageUrl
                        )
                    } else {
                        _editState.value = _editState.value.copy(isLoading = false, error = "Portofolio tidak ditemukan.")
                    }
                }
                .onFailure {
                    _editState.value = _editState.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun onTitleChange(title: String) { _editState.value = _editState.value.copy(title = title) }
    fun onDescriptionChange(desc: String) { _editState.value = _editState.value.copy(description = desc) }
    fun onSkillChange(skill: String) { _editState.value = _editState.value.copy(skill = skill) }
    fun onUrlChange(url: String) { _editState.value = _editState.value.copy(projectUrl = url) }

    fun onStartDateSelected(date: String) { _editState.value = _editState.value.copy(startDate = date) }
    fun onEndDateSelected(date: String) { _editState.value = _editState.value.copy(endDate = date) }
    fun onIsCurrentChange(isCurrent: Boolean) { _editState.value = _editState.value.copy(isCurrent = isCurrent) }


    fun onUpdateClicked(newImageUri: Uri?) {
        viewModelScope.launch {
            val currentState = _editState.value

            if (currentState.startDate == "Tanggal Mulai" || currentState.startDate.isBlank()) {
                _editState.value = currentState.copy(error = "Tanggal mulai harus diisi.")
                return@launch
            }

            if (!currentState.isCurrent) {
                if (currentState.endDate == "Tanggal Selesai" || currentState.endDate.isBlank()) {
                    _editState.value = currentState.copy(error = "Tanggal selesai harus diisi.")
                    return@launch
                }

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                try {
                    val startDate = dateFormat.parse(currentState.startDate)
                    val endDate = dateFormat.parse(currentState.endDate)

                    if (startDate != null && endDate != null && startDate.after(endDate)) {
                        _editState.value = currentState.copy(error = "Tanggal mulai tidak boleh setelah tanggal selesai.")
                        return@launch
                    }
                } catch (e: Exception) {
                    _editState.value = currentState.copy(error = "Format tanggal tidak valid.")
                    return@launch
                }
            }

            if (currentState.title.isBlank()) {
                _editState.value = currentState.copy(error = "Judul portofolio tidak boleh kosong.")
                return@launch
            }
            if (currentState.skill.isBlank()) {
                _editState.value = currentState.copy(error = "Skill tidak boleh kosong.")
                return@launch
            }
            if (currentState.projectUrl.isBlank()) {
                _editState.value = currentState.copy(error = "URL project tidak boleh kosong.")
                return@launch
            }
            if (currentState.description.isBlank()) {
                _editState.value = currentState.copy(error = "Deskripsi tidak boleh kosong.")
                return@launch
            }

            _editState.value = currentState.copy(isLoading = true, error = null)

            val uid = auth.currentUser?.uid ?: return@launch

            try {
                val finalImageUrl = if (newImageUri != null) {
                    val inputStream = context.contentResolver.openInputStream(newImageUri)
                    val imageBytes = inputStream?.readBytes()
                    inputStream?.close()

                    if (imageBytes != null) {
                        val mimeType = context.contentResolver.getType(newImageUri)
                        val requestBody = imageBytes.toRequestBody(mimeType?.toMediaTypeOrNull() ?: "image/*".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("image", "portfolio_image.jpg", requestBody)
                        val response = imgurApiService.uploadImage("Client-ID 6933ca201b18ccf", imagePart)
                        if (response.success) {
                            response.data.link
                        } else {
                            _editState.value = _editState.value.copy(error = "Gagal mengupload gambar sampul.", isLoading = false)
                            return@launch
                        }
                    } else {
                        _editState.value.imageUrl
                    }
                } else {
                    _editState.value.imageUrl
                }

                val finalEndDate = if (currentState.isCurrent) "Sekarang" else currentState.endDate
                val dateRange = "${currentState.startDate} - $finalEndDate"

                val updatedPortfolio = PortfolioItem(
                    id = portfolioId,
                    userId = uid,
                    title = currentState.title,
                    description = currentState.description,
                    dateRange = dateRange,
                    skill = currentState.skill,
                    projectUrl = currentState.projectUrl,
                    imageUrl = finalImageUrl
                )

                updatePortfolioUseCase(uid, updatedPortfolio)
                    .onSuccess {
                        _editState.value = _editState.value.copy(isSuccess = true, isLoading = false)
                    }
                    .onFailure {
                        _editState.value = _editState.value.copy(error = it.message, isLoading = false)
                    }
            } catch (e: IOException) {
                _editState.value = _editState.value.copy(error = "Gagal unggah: Periksa koneksi internet.", isLoading = false)
            } catch (e: Exception) {
                _editState.value = _editState.value.copy(error = e.message ?: "Terjadi kesalahan.", isLoading = false)
            }
        }
    }

    fun onNavigationDone() {
        _editState.value = _editState.value.copy(isSuccess = false, error = null)
    }
}

data class EditPortfolioState(
    val title: String = "",
    val description: String = "",
    val startDate: String = "Tanggal Mulai",
    val endDate: String = "Tanggal Selesai",
    val imageUrl: String = "",
    val isCurrent: Boolean = false,
    val skill: String = "",
    val projectUrl: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)