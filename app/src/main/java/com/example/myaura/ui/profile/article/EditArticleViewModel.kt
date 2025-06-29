package com.example.myaura.ui.profile.article

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ImgurApiService
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.usecase.GetArticleUseCase
import com.example.myaura.domain.usecase.UpdateArticleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val updateArticleUseCase: UpdateArticleUseCase,
    private val auth: FirebaseAuth,
    private val imgurApiService: ImgurApiService,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _editState = MutableStateFlow(EditArticleState())
    val editState = _editState.asStateFlow()

    private val articleId: String = savedStateHandle.get<String>("articleId")!!

    init {
        loadArticleDetails()
    }

    private fun loadArticleDetails() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch
            getArticleUseCase(uid, articleId)
                .onSuccess { article ->
                    if (article != null) {
                        _editState.value = _editState.value.copy(
                            isLoading = false,
                            title = article.title,
                            subTitle = article.subTitle,
                            content = article.content,
                            imageUrl = article.imageUrl
                        )
                    } else {
                        _editState.value = _editState.value.copy(isLoading = false, error = "Artikel tidak ditemukan.")
                    }
                }
                .onFailure {
                    _editState.value = _editState.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun onTitleChange(title: String) { _editState.value = _editState.value.copy(title = title) }
    fun onSubTitleChange(subTitle: String) { _editState.value = _editState.value.copy(subTitle = subTitle) }
    fun onContentChange(content: String) { _editState.value = _editState.value.copy(content = content) }

    fun onUpdateClicked(newImageUri: Uri?) {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch

            try {
                val finalImageUrl = if (newImageUri != null) {
                    val inputStream = context.contentResolver.openInputStream(newImageUri)
                    val imageBytes = inputStream?.readBytes()
                    inputStream?.close()

                    if (imageBytes != null) {
                        val mimeType = context.contentResolver.getType(newImageUri)
                        val requestBody = imageBytes.toRequestBody(mimeType?.toMediaTypeOrNull() ?: "image/*".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("image", "article_cover.jpg", requestBody)
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

                val currentState = _editState.value
                val updatedArticle = Article(
                    id = articleId,
                    userId = uid,
                    title = currentState.title,
                    subTitle = currentState.subTitle,
                    content = currentState.content,
                    imageUrl = finalImageUrl
                )

                updateArticleUseCase(uid, updatedArticle)
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

data class EditArticleState(
    val title: String = "",
    val imageUrl: String = "",
    val subTitle: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)