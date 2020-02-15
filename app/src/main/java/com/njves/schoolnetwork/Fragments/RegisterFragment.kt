package com.njves.schoolnetwork.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.RegisterService
import com.njves.schoolnetwork.Models.network.request.SchoolsListService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.callback.UpdateToolbarTitleListener
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import kotlinx.android.synthetic.main.fragment_register.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    lateinit var onAuthPassedListener: OnAuthPassedListener
    lateinit var updateToolbarTitleListener: UpdateToolbarTitleListener
    lateinit var edName: TextInputEditText
    lateinit var edEmail: TextInputEditText
    lateinit var edPass: TextInputEditText
    lateinit var edPassRetry: TextInputEditText
    lateinit var fabNext: FloatingActionButton

    companion object {
        const val TAG = "RegisterFragment"
        const val number = "schoolNumber"
        const val name = "name";
        const val email = "email"

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAuthPassedListener) {
            onAuthPassedListener = context

        } else {
            throw RuntimeException(context.toString() + " must implement OnAuthPassedListener")
        }
        if (context is UpdateToolbarTitleListener) {
            updateToolbarTitleListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement UpdateToolbarTitleListener")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // Сохраняем состояние данных в полях
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(name, edName.text.toString())
        outState.putString(email, edEmail.text.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_register, container, false)
        updateToolbarTitleListener.updateActionBar(
            context?.resources?.getString(R.string.auth_register) ?: "Registration"
        )
        if (savedInstanceState != null) {
            val savedName = savedInstanceState.getString(name, "")
            val savedEmail = savedInstanceState.getString(email, "")
        }
        val progressBar = v.findViewById<ProgressBar>(R.id.progressBar)
        val spinnerSchoolNumber = v.findViewById<Spinner>(R.id.spinnerSchoolNumber)

        // Создаем адаптер для спиннера
        val adapter = ArrayAdapter<School>(context!!, R.layout.support_simple_spinner_dropdown_item)
        val call = NetworkService.instance.getRetrofit().create(SchoolsListService::class.java)
        // Получаем список школ с сервера
        call.callSchoolList().enqueue(object : Callback<List<School>> {
            override fun onFailure(call: Call<List<School>>, t: Throwable) {
                Toast.makeText(context, "Произашла ошибка получения списка школ", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Failue request to  get school list")
            }

            override fun onResponse(call: Call<List<School>>, response: Response<List<School>>) {
                val listSchool = response.body() ?: listOf()
                Log.d("RegisterFragment", listSchool.toString())
                adapter.addAll(listSchool)
            }

        })
        edName = v.findViewById(R.id.edName)
        edEmail = v.findViewById(R.id.edEmail)
        edPass = v.findViewById(R.id.edPass)
        edPassRetry = v.findViewById(R.id.edPassRetry)


        fabNext = v.findViewById(R.id.fabNext)
        fabNext.setOnClickListener {
            val item = spinnerSchoolNumber.selectedItem as School
            val user = User(
                null,
                edName.text.toString(),
                edEmail.text.toString(),
                edPass.text.toString(),
                edPassRetry.text.toString(),
                // Приводим элемент списка к номеру школы и получаем индек
                item.index
            )
            val registerService = NetworkService.instance.getRetrofit().create(RegisterService::class.java)
            val callRegister = registerService.callRegister(user)
            callRegister.enqueue(object:Callback<NetworkResponse<User>>{
                override fun onFailure(call: Call<NetworkResponse<User>>, t: Throwable) {
                    val errorDialog = AuthErrorDialog.newInstance("Прозашла ошибка сети")
                    errorDialog.show(activity?.supportFragmentManager,"errorDialog")
                }

                override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {
                    val code = response.body()?.code
                    val message = response.body()?.message
                    val objects = response.body()?.data
                    // код 0 - успех, код 1 -  ошибка
                    if(code==0) {
                        val gson = Gson()
                        val profileFragment = ProfileFragment.newInstance(gson.toJson(objects))
                        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.authContainer, profileFragment)
                            ?.addToBackStack(null)?.commit()
                        activity?.supportFragmentManager?.beginTransaction()?.hide(this@RegisterFragment)
                    }else{
                        Log.d("RegisterFragment", "Error: $message")
                        val errorDialog = AuthErrorDialog.newInstance(message)
                        errorDialog.show(activity?.supportFragmentManager, "errorDialog")
                    }
                }

            })

        }
        spinnerSchoolNumber.adapter = adapter



        return v
    }
}




