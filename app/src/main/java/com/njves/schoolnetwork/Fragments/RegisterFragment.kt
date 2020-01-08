package com.njves.schoolnetwork.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.auth.AuthResponse
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.RegisterService
import com.njves.schoolnetwork.Models.network.request.SchoolsListService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnSuccessAuthListener
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(), Callback<AuthResponse>{
    val TAG = "RegisterFragment"
    lateinit var onAuthListener : OnSuccessAuthListener
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnSuccessAuthListener) {
            onAuthListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSuccessAuthListener")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  layoutInflater.inflate(R.layout.fragment_register, container, false)
        val progressBar = v.findViewById<ProgressBar>(R.id.progressBar)
        val spinnerSchoolNumber = v.findViewById<Spinner>(R.id.spinnerSchoolNumber)
        // Создаем экземпляр подготовленного хранилища

        // Получаем список школ с сервера

        // Создаем адаптер для спиннера
        val adapter = ArrayAdapter<School>(context!!, R.layout.support_simple_spinner_dropdown_item)
        val call = NetworkService.instance.getRetrofit().create(SchoolsListService::class.java)

        call.callSchoolList().enqueue(object : Callback<List<School>>{
            override fun onFailure(call: Call<List<School>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<School>>, response: Response<List<School>>) {
                val listSchool = response.body() ?: listOf()
                Log.d("RegisterFragment", listSchool.toString())
                adapter.addAll(listSchool)
            }

        })

        spinnerSchoolNumber.adapter = adapter

        val edName = v.findViewById<TextInputEditText>(R.id.edName)
        val edEmail = v.findViewById<TextInputEditText>(R.id.edEmail)
        val edPass = v.findViewById<TextInputEditText>(R.id.edPass)
        val edPassRetry = v.findViewById<TextInputEditText>(R.id.edPassRetry)
        val btnSubmit = v.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener({
            // Создаем объект запроса
            val registerService = NetworkService.instance.getRetrofit().create(RegisterService::class.java)
            val item = spinnerSchoolNumber.selectedItem as School?
            // Делаем запрос
            val call = registerService.callRegister(
                User(
                    null,
                    edName.text.toString(),
                    edEmail.text.toString(),
                    edPass.text.toString(),
                    edPassRetry.text.toString(),
                    // Приводим элемент списка к номеру школы и получаем индек
                    item?.index ?: 0
                )
            )
            // Показываем прогресс бар
            progressBar.visibility = View.VISIBLE

            // Получаем callback
            call.enqueue(this)


        })
        return v
    }
    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
        Log.d(TAG, "Request fail " + t)
        Toast.makeText(context, "Ошибка доступа к серверу", Toast.LENGTH_LONG).show()
        // Скрываем прогресс бар
        progressBar.visibility = View.INVISIBLE
    }

    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {

        // Скрываем прогресс бар
        progressBar.visibility = View.INVISIBLE
        val code = response.body()?.code
        val message = response.body()?.message
        val dataObject = response.body()?.data
        // CODE 0 -  Успех, CODE 1 - Ошибка
        if(code==0) {
            Toast.makeText(context, "Вы успешно авторизировались", Toast.LENGTH_LONG).show()
            // Перемещаем в меню авторизации
            onAuthListener.onSuccess(dataObject?.uid)
        }
        else
        {
            if(response.code()!=200)
            {
                // Создаем диалог ошибки если код отличается от успеха
                AuthErrorDialog.newInstance(response.message()).show(activity?.supportFragmentManager, "error")
            }
            else
            {
                // Создаем диалог ошибки если запрос прошел успешно
                AuthErrorDialog.newInstance(message).show(activity?.supportFragmentManager, "error")
            }

        }
    }


}