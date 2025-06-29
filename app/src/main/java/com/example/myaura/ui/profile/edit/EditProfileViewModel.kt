package com.example.myaura.ui.profile.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ImgurApiService
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.usecase.GetUserProfileUseCase
import com.example.myaura.domain.usecase.SaveUserProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val imgurApiService: ImgurApiService,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _editState = MutableStateFlow(EditProfileState())
    val editState = _editState.asStateFlow()

    init {
        loadInitialProfile()
    }

    private fun loadInitialProfile() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch
            getUserProfileUseCase(uid).onSuccess { userProfile ->
                if (userProfile != null) {
                    _editState.value = _editState.value.copy(
                        isLoading = false,
                        name = userProfile.name,
                        job = userProfile.job,
                        tagline = userProfile.tagline,
                        bio = userProfile.bio,
                        profilePictureUrl = userProfile.profilePictureUrl,
                        instagram = userProfile.socialMediaLinks["instagram"] ?: "",
                        github = userProfile.socialMediaLinks["github"] ?: "",
                        linkedin = userProfile.socialMediaLinks["linkedin"] ?: ""
                    )
                } else {
                    _editState.value = _editState.value.copy(isLoading = false)
                }
            }.onFailure {
                _editState.value = _editState.value.copy(isLoading = false, error = it.message)
            }
        }
    }

    fun onNameChange(name: String) { _editState.value = _editState.value.copy(name = name) }
    fun onJobChange(job: String) { _editState.value = _editState.value.copy(job = job) }
    fun onTaglineChange(tagline: String) { _editState.value = _editState.value.copy(tagline = tagline) }
    fun onBioChange(bio: String) { _editState.value = _editState.value.copy(bio = bio) }
    fun onInstagramChange(url: String) { _editState.value = _editState.value.copy(instagram = url) }
    fun onGithubChange(url: String) { _editState.value = _editState.value.copy(github = url) }
    fun onLinkedinChange(url: String) { _editState.value = _editState.value.copy(linkedin = url) }

    fun onSaveClicked(newImageUri: Uri?)
    {
        viewModelScope.launch {
            val currentState = _editState.value
            if (currentState.name.isBlank()) {
                _editState.value = currentState.copy(error = "Nama tidak boleh kosong.")
                return@launch
            }
            if (currentState.job.isBlank()) {
                _editState.value = currentState.copy(error = "Pekerjaan tidak boleh kosong.")
                return@launch
            }
            if (currentState.tagline.isBlank()) {
                _editState.value = currentState.copy(error = "Tagline tidak boleh kosong.")
                return@launch
            }
            if (currentState.bio.isBlank()) {
                _editState.value = currentState.copy(error = "Bio tidak boleh kosong.")
                return@launch
            }
            _editState.value = _editState.value.copy(isLoading = true, error = null)
            val currentUserUid = auth.currentUser?.uid ?: return@launch

            try {
                val imageUrl = if (newImageUri != null) {
                    val inputStream = context.contentResolver.openInputStream(newImageUri)
                    val imageBytes = inputStream!!.readBytes()
                    inputStream.close()

                    val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", "profile_image.jpg", requestBody)

                    val response = imgurApiService.uploadImage("Client-ID 6933ca201b18ccf", imagePart)

                    if (response.success) {
                        response.data.link
                    } else {
                        _editState.value = _editState.value.copy(error = "Gagal mengupload gambar ke Imgur.", isLoading = false)
                        return@launch
                    }
                } else {
                    _editState.value.profilePictureUrl
                }

                val currentState = _editState.value
                val socialLinksMap = mapOf(
                    "instagram" to currentState.instagram,
                    "github" to currentState.github,
                    "linkedin" to currentState.linkedin
                ).filterValues { it.isNotBlank() }

                val userProfile = UserProfile(
                    uid = currentUserUid,
                    name = currentState.name,
                    job = currentState.job,
                    tagline = currentState.tagline,
                    bio = currentState.bio,
                    socialMediaLinks = socialLinksMap,
                    profilePictureUrl = imageUrl
                )

                saveUserProfileUseCase(currentUserUid, userProfile)
                    .onSuccess { _editState.value = _editState.value.copy(isSuccess = true, isLoading = false) }
                    .onFailure { _editState.value = _editState.value.copy(error = it.message, isLoading = false) }

            } catch (e: Exception) {
                android.util.Log.e("ImgurUploadError", "Error during image upload: ${e.message}", e)
                _editState.value = _editState.value.copy(error = e.message ?: "Terjadi kesalahan", isLoading = false)
            }
        }
    }

    fun onNavigationDone() {
        _editState.value = _editState.value.copy(isSuccess = false, error = null)
    }
}

data class EditProfileState(
    val name: String = "",
    val job: String = "",
    val tagline: String = "",
    val bio: String = "",
    val instagram: String = "",
    val github: String = "",
    val linkedin: String = "",
    val profilePictureUrl: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)