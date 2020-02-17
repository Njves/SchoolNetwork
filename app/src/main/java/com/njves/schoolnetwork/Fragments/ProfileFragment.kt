package com.njves.schoolnetwork.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.NetworkService.Companion.TYPE_GET
import com.njves.schoolnetwork.Models.NetworkService.Companion.TYPE_POST
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.RequestProfilePosition
import com.njves.schoolnetwork.Models.network.models.auth.*
import com.njves.schoolnetwork.Models.network.models.profile.UserProfile
import com.njves.schoolnetwork.Models.network.request.PositionListService
import com.njves.schoolnetwork.Models.network.request.ProfileService


import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.callback.UpdateToolbarTitleListener
import com.njves.schoolnetwork.dialog.ClassChoiceDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class ProfileFragment : Fragment() {
    // TODO: При нажатие на подтвердить приложение вылетает с ошибкой о null user
    lateinit var edFN : TextInputEditText
    lateinit var edLN : TextInputEditText
    lateinit var edMN : TextInputEditText
    lateinit var btnSubmit : Button
    lateinit var spinnerPosition : Spinner
    lateinit var btnClassChoice : Button
    lateinit var tvProfileStatus : TextView
    lateinit var onAuthPassedListener : OnAuthPassedListener
    var schoolClass : String? = null
    companion object{
        const val TAG = "ProfileFragment"
        const val REQUEST_CODE_DIALOG_CLASS_CHOICE = 1
        fun newInstance(user : String) : Fragment
        {
            val fragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putString("user", user)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnAuthPassedListener)
        onAuthPassedListener = context

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val TAG = "ProfileFragment"
        val v = layoutInflater.inflate(R.layout.fragment_profile,container, false)
        tvProfileStatus = v.findViewById<TextView>(R.id.tvProfileStatus)
        edFN = v.findViewById<TextInputEditText>(R.id.edFN)
        edLN = v.findViewById<TextInputEditText>(R.id.edLN)
        edMN = v.findViewById<TextInputEditText>(R.id.edMN)
        spinnerPosition = v.findViewById<Spinner>(R.id.spinnerPosition)
        btnClassChoice = v.findViewById(R.id.btnClassChoice)
        btnSubmit = v.findViewById<Button>(R.id.btnSubmit)


        val storage = AuthStorage(context)
        val call = NetworkService.instance.getRetrofit().create(ProfileService::class.java)
        val postCall = call.getProfile(TYPE_GET,storage.getUserDetails()?:"")
        var profile : Profile? = null
        Log.d("ProfileFragment", arguments?.getString("user").toString())
        postCall.enqueue(object:Callback<NetworkResponse<Profile>>{
            override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NetworkResponse<Profile>>, response: Response<NetworkResponse<Profile>>) {
                if (response.body()?.code == 0) {
                    profile = response.body()?.data
                    profile?.let{
                        edFN.setText(it.firstName)
                        edLN.setText(it.lastName)
                        edMN.setText(it.middleName)

                    }
                }
            }
        })

        btnClassChoice.setOnClickListener{
            val dialog : DialogFragment = ClassChoiceDialog()
            dialog.setTargetFragment(this, REQUEST_CODE_DIALOG_CLASS_CHOICE)
            dialog.show(fragmentManager, "")
        }


        // Создаем объект запроса создания
        val positionListService = NetworkService.instance.getRetrofit().create(PositionListService::class.java)
        val callPosition = positionListService.callPositionList()
        // Выполняем запрос
        callPosition.enqueue(object: Callback<List<Position>>{
            override fun onFailure(call: Call<List<Position>>, t: Throwable) {
                Log.d(TAG, "An error has occurred request get position")

                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Position>>, response: Response<List<Position>>) {
                // Получаем список позиций и создаем с ним адаптер
                val listPositions = response.body() ?: listOf()

                val positionAdapter = ArrayAdapter<Position>(context!!, R.layout.support_simple_spinner_dropdown_item,
                    listPositions)

                spinnerPosition.adapter = positionAdapter
            }
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            // Проверка на успешность запроса
            val profileService = NetworkService.instance.getRetrofit().create(ProfileService::class.java)
            // TODO: Заменить position
            val item = spinnerPosition.selectedItem as Position
            val gson = Gson();
            val user = gson.fromJson<User>(arguments?.getString("user"), User::class.java)
            if (user == null) {
                val updateProfile = profileService.updateProfile(
                    RequestModel(
                        "UPDATE", Profile(
                            storage.getUserDetails(),
                            edFN.text.toString(),
                            edLN.text.toString(),
                            edMN.text.toString(),
                            item.index,
                            schoolClass ?: "0"
                        )
                    )
                )
                updateProfile.enqueue(object : Callback<NetworkResponse<Profile>> {
                    override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
                        Log.d("ProfileFragment", "Failure request: $t")
                        Toast.makeText(context, "Произашла ошибка запроса", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<NetworkResponse<Profile>>,
                        response: Response<NetworkResponse<Profile>>
                    ) {
                        val code = response.body()?.code
                        val message = response.body()?.message
                        Log.d("ProfileFragment", response.body().toString())
                        if(code==0)
                        Snackbar.make(v, "Профиль успешно обновлен", Snackbar.LENGTH_SHORT).show()
                        else{
                            Snackbar.make(v, "Ошибка: $message", Snackbar.LENGTH_SHORT).show()
                        }
                    }

                })

            } else {
                val callProfile = profileService.postProfile(
                    RequestProfileModel(
                        TYPE_POST,
                        UserProfile(
                            user, Profile(
                                storage.getUserDetails(),
                                edFN.text.toString(),
                                edLN.text.toString(),
                                edMN.text.toString(),
                                item.index,
                                schoolClass ?: "0"
                            )
                        )
                    )
                )
                callProfile.enqueue(object : Callback<NetworkResponse<UserProfile>> {
                    override fun onFailure(call: Call<NetworkResponse<UserProfile>>, t: Throwable) {
                        Log.d(TAG, "Failure request post profile: $t")
                        context?.let {
                            Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onResponse(
                        call: Call<NetworkResponse<UserProfile>>,
                        response: Response<NetworkResponse<UserProfile>>
                    ) {
                        if (response.body()?.code == 0) {
                            Toast.makeText(context, "You success create profile!", Toast.LENGTH_LONG).show()

                            onAuthPassedListener.onSuccess(response.body()?.data?.user?.uid)
                        }
                    }
                })
            }

        })
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DIALOG_CLASS_CHOICE -> {
                    val classNumber = data?.getIntExtra("number", 0)
                    val classChar = data?.getStringExtra("char")
                    schoolClass = "$classNumber$classChar"

                }
            }
        }
    }


}