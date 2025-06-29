package com.example.myaura.ui.profile.portfolio

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ImgurApiService
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.usecase.AddPortfolioUseCase
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
class AddPortfolioViewModel @Inject constructor(
    private val addPortfolioUseCase: AddPortfolioUseCase,
    private val auth: FirebaseAuth,
    private val imgurApiService: ImgurApiService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _addState = MutableStateFlow(AddPortfolioState())
    val addState = _addState.asStateFlow()

    fun onAddPortfolioClicked(
        title: String,
        startDateStr: String,
        endDateStr: String,
        isCurrent: Boolean,
        skill: String,
        projectUrl: String,
        description: String,
        imageUri: Uri?
    ) {
        if (startDateStr == "Tanggal Mulai") {
            _addState.value = AddPortfolioState(error = "Tanggal mulai harus diisi.")
            return
        }

        if (!isCurrent) {
            if (endDateStr == "Tanggal Selesai") {
                _addState.value = AddPortfolioState(error = "Tanggal selesai harus diisi.")
                return
            }

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            try {
                val startDate = dateFormat.parse(startDateStr)
                val endDate = dateFormat.parse(endDateStr)

                if (startDate != null && endDate != null && startDate.after(endDate)) {
                    _addState.value = AddPortfolioState(error = "Tanggal mulai tidak boleh setelah tanggal selesai.")
                    return
                }
            } catch (e: Exception) {
                _addState.value = AddPortfolioState(error = "Format tanggal tidak valid.")
                return
            }
        }

        if (title.isBlank()) {
            _addState.value = AddPortfolioState(error = "Judul portofolio tidak boleh kosong.")
            return
        }
        if (skill.isBlank()) {
            _addState.value = AddPortfolioState(error = "Skill tidak boleh kosong.")
            return
        }
        if (projectUrl.isBlank()) {
            _addState.value = AddPortfolioState(error = "URL project tidak boleh kosong.")
            return
        }
        if (description.isBlank()) {
            _addState.value = AddPortfolioState(error = "Deskripsi tidak boleh kosong.")
            return
        }

        viewModelScope.launch {
            _addState.value = AddPortfolioState(isLoading = true)
            val currentUserUid = auth.currentUser?.uid

            if (currentUserUid == null) {
                _addState.value = AddPortfolioState(error = "Pengguna tidak ditemukan.")
                return@launch
            }

            try {
                val imageUrl = if (imageUri != null) {
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    val imageBytes = inputStream?.readBytes()
                    inputStream?.close()

                    if (imageBytes != null) {
                        val mimeType = context.contentResolver.getType(imageUri)
                        val requestBody = imageBytes.toRequestBody(mimeType?.toMediaTypeOrNull() ?: "image/*".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("image", "portfolio_image.jpg", requestBody)
                        val response = imgurApiService.uploadImage("Client-ID 6933ca201b18ccf", imagePart)
                        if (response.success) {
                            response.data.link
                        } else {
                            _addState.value = AddPortfolioState(error = "Gagal mengupload gambar: ${response.data}")
                            return@launch
                        }
                    } else {
                        ""
                    }
                } else {
                    ""
                }

                val finalEndDate = if (isCurrent) "Saat Ini" else endDateStr
                val dateRange = "$startDateStr - $finalEndDate"

                val portfolioItem = PortfolioItem(
                    userId = currentUserUid,
                    title = title,
                    dateRange = dateRange,
                    skill = skill,
                    projectUrl = projectUrl,
                    description = description,
                    imageUrl = imageUrl
                )
                addPortfolioUseCase(currentUserUid, portfolioItem)
                    .onSuccess {
                        _addState.value = AddPortfolioState(isSuccess = true)
                    }
                    .onFailure {
                        _addState.value = AddPortfolioState(error = it.message)
                    }
            } catch (e: IOException) {
                _addState.value = AddPortfolioState(error = "Gagal unggah: Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                _addState.value = AddPortfolioState(error = e.message ?: "Terjadi kesalahan yang tidak diketahui.")
            }
        }
    }

    fun onNavigationDone() {
        _addState.value = AddPortfolioState()
    }
}

data class AddPortfolioState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)