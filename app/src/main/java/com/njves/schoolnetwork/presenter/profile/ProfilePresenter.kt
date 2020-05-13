package com.njves.schoolnetwork.presenter.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.Models.ImageFileClass
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Position
import com.njves.schoolnetwork.Models.network.models.profile.Profile
import com.njves.schoolnetwork.Models.network.models.profile.ProfileWrapper
import com.njves.schoolnetwork.Models.network.request.PositionListService
import com.njves.schoolnetwork.Models.network.request.ProfileService
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.presenter.position.IPosition
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

class ProfilePresenter(private val iProfile: IProfile, private  val navigator: ProfileNavigator, private val storage : AuthStorage, private val iPosition: IPosition) {
    private var retrofit: Retrofit = NetworkService.instance.getRetrofit()
    private var profileService = retrofit.create(ProfileService::class.java)
    private var positionService = retrofit.create(PositionListService::class.java)

    fun makeProfile(uid: String, fn: String, ln: String, mn: String, pos: Int, posTitle: String, classValue: String?, avatarLink: String?){
        val profile = Profile(
            uid,
            fn,
            ln,
            mn,
            pos,
            posTitle,
            0,
            classValue,
            avatarLink
        )
        var type = "POST"
        if(storage.getIsProfile()) type = "UPDATE"
        val postCall = profileService.postProfile(ProfileWrapper(type, profile))
        postCall.enqueue(object : Callback<NetworkResponse<Profile?>>{
            override fun onResponse(call: Call<NetworkResponse<Profile?>>, response: Response<NetworkResponse<Profile?>>) {
                val code = response.body()?.code
                val message = response.body()?.message ?: "Неизвестная ошибка"
                val responseProfile = response.body()!!.data
                if (code == NetworkResponse.SUCCESS_RESPONSE) {
                    if(responseProfile!=null) {
                        storage.setLocalUserProfile(profile)
                        iProfile.onResponseProfile(responseProfile)
                    }else{
                        iProfile.onError("Профиль не был создан!!")
                    }
                } else {
                    iProfile.onError(message)
                }
            }
            override fun onFailure(call: Call<NetworkResponse<Profile?>>, t: Throwable) {
                iProfile.onFail(t)
            }
        })
    }

    fun getProfile(uid : String){
        val getCall = profileService.getProfile(NetworkService.TYPE_GET, uid)
        getCall.enqueue(object : Callback<NetworkResponse<Profile?>>{
            override fun onResponse(call: Call<NetworkResponse<Profile?>>, response: Response<NetworkResponse<Profile?>>) {
                val code = response.body()?.code
                val message = response.body()?.message ?: "Неизвестная ошибка"
                val profile = response.body()!!.data
                if (code == NetworkResponse.SUCCESS_RESPONSE) {
                    if(profile!=null){
                        iProfile.onResponseProfile(profile)
                    }else{
                        iProfile.onEmptyProfile()
                    }
                } else {
                    iProfile.onError(message)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Profile?>>, t: Throwable) {
                iProfile.onFail(t)
            }

        })
    }
    fun showDialog(dialog: DialogFragment){
        navigator.showDialogSelect(dialog)
    }
    fun getPositions() {
        val call = positionService.callPositionList()
        call.enqueue(object : Callback<List<Position>> {
            override fun onResponse(call: Call<List<Position>>, response: Response<List<Position>>) {
                val listPositions = response.body()
                if (listPositions != null) {
                    iPosition.onSuccess(listPositions)
                } else {
                    iPosition.onError("Не удалось получить список должностей")
                }
            }

            override fun onFailure(call: Call<List<Position>>, t: Throwable) {
                iPosition.onFail(t)
            }
        })
    }
    fun requestPhoto(requestCode: Int){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        navigator.requestPhoto(photoPickerIntent, requestCode)
    }

    fun uploadImage(context: Context,uri: Uri){
        val localPath = ImageFileClass.getPath(context, uri)
        val localFile = File(localPath)
        val part = RequestBody.create(MediaType.parse(context.contentResolver.getType(uri)!!) ,localFile)
        val file = MultipartBody.Part.createFormData("file", localFile.getName(), part)
        profileService.uploadImage(file).enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                iProfile.onFail(t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                iProfile.onError(response.body()?.string()!!)
            }
        })
    }
}