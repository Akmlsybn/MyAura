package com.example.myaura.ui.profile.article

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ImgurApiService
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.usecase.AddArticleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@HiltViewModel
class AddArticleViewModel @Inject constructor(
    private val addArticleUseCase: AddArticleUseCase,
    private val auth: FirebaseAuth,
    private val imgurApiService: ImgurApiService,
    @ApplicationContext private val context: Context
) : ViewModel(){
    private val _addArticleState = MutableStateFlow(AddArticleState())
    val addArticleState = _addArticleState.asStateFlow()

    fun onPostArticleClicked(title: String, subTitle: String, content: String, imageUri: Uri?) {
        viewModelScope.launch {
            if (title.isBlank() || content.isBlank()) {
                _addArticleState.value = AddArticleState(error = "Judul dan konten tidak boleh kosong.")
                return@launch
            }

            _addArticleState.value = AddArticleState(isLoading = true)

            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                _addArticleState.value = AddArticleState(error = "Pengguna tidak ditemukan.")
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
                        val imagePart = MultipartBody.Part.createFormData("image", "article_cover.jpg", requestBody)
                        val response = imgurApiService.uploadImage("Client-ID 6933ca201b18ccf", imagePart)

                        if (response.success) {
                            response.data.link
                        } else {
                            _addArticleState.value = AddArticleState(error = "Gagal mengupload gambar sampul: ${response.data}")
                            return@launch
                        }
                    } else {
                        ""
                    }
                } else {
                    ""
                }

                val article = Article(
                    userId = currentUserUid,
                    title = title,
                    subTitle = subTitle,
                    content = content,
                    imageUrl = imageUrl
                )

                addArticleUseCase(currentUserUid, article)
                    .onSuccess {
                        _addArticleState.value = AddArticleState(isSuccess = true)
                    }
                    .onFailure {
                        _addArticleState.value = AddArticleState(error = it.message)
                    }
            } catch (e: IOException) {
                _addArticleState.value = AddArticleState(error = "Gagal unggah: Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                _addArticleState.value = AddArticleState(error = e.message ?: "Terjadi kesalahan yang tidak diketahui.")
            }
        }
    }
    fun onNavigationDone() {
        _addArticleState.value = AddArticleState()
    }
}

data class AddArticleState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)