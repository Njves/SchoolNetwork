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
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.RegisterService
import com.njves.schoolnetwork.Models.network.request.SchoolsListService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnActionBarUpdateListener
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import kotlinx.android.synthetic.main.fragment_register.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(), Callback<NetworkResponse<User>>{

    lateinit var onAuthPassedListener : OnAuthPassedListener
    lateinit var onActionBarUpdateListener : OnActionBarUpdateListener
    lateinit var edName : TextInputEditText
    lateinit var edEmail : TextInputEditText
    lateinit var edPass : TextInputEditText
    lateinit var edPassRetry : TextInputEditText
    lateinit var btnSubmit : Button
    companion object{
        const val TAG = "RegisterFragment"
        const val number = "schoolNumber"
        const val name = "name";
        const val email = "email"

    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAuthPassedListener) {
            onAuthPassedListener = context

        }
        else {
            throw RuntimeException(context.toString() + " must implement OnAuthPassedListener")
        }
        if(context is OnActionBarUpdateListener)
        {
            onActionBarUpdateListener = context
        }
        else{
            throw RuntimeException(context.toString() + " must implement OnActionBarUpdateListener")
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
        val v =  layoutInflater.inflate(R.layout.fragment_register, container, false)
        onActionBarUpdateListener.updateActionBar(context?.resources?.getString(R.string.auth_register)?: "Registration")
        if(savedInstanceState!=null)
        {
            val savedName = savedInstanceState.getString(name, "")
            val savedEmail = savedInstanceState.getString(email, "")
        }
        val progressBar = v.findViewById<ProgressBar>(R.id.progressBar)
        val spinnerSchoolNumber = v.findViewById<Spinner>(R.id.spinnerSchoolNumber)

        // Создаем адаптер для спиннера
        val adapter = ArrayAdapter<School>(context!!, R.layout.support_simple_spinner_dropdown_item)
        val call = NetworkService.instance.getRetrofit().create(SchoolsListService::class.java)
        // Получаем список школ с сервера
        call.callSchoolList().enqueue(object : Callback<List<School>>{
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

        spinnerSchoolNumber.adapter = adapter

        edName = v.findViewById(R.id.edName)
        edEmail= v.findViewById(R.id.edEmail)
        edPass = v.findViewById(R.id.edPass)
        edPassRetry = v.findViewById(R.id.edPassRetry)
        btnSubmit = v.findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
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


        }
        return v
    }
    override fun onFailure(call: Call<NetworkResponse<User>>, t: Throwable) {
        Log.d(TAG, "Request fail " + t)
        Toast.makeText(context, "Ошибка доступа к серверу", Toast.LENGTH_LONG).show()
        // Скрываем прогресс бар
        progressBar.visibility = View.INVISIBLE
    }

    override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {

        // Скрываем прогресс бар
        progressBar.visibility = View.INVISIBLE
        val code = response.body()?.code
        val message = response.body()?.message
        val dataObject = response.body()?.data
        // CODE 0 -  Успех, CODE 1 - Ошибка
        if(code==0) {
            Toast.makeText(context, "Вы успешно зарегистрировались", Toast.LENGTH_LONG).show()
            // Сообщаем о удачной регистрации
            onAuthPassedListener.onSuccess(dataObject?.uid)
        }
        else AuthErrorDialog.newInstance(message).show(activity?.supportFragmentManager, "error")
            if(response.code()!=200) AuthErrorDialog.newInstance(response.message()).show(activity?.supportFragmentManager, "error")
            // Создаем диалог ошибки если запрос прошел успешно
            else AuthErrorDialog.newInstance(message).show(activity?.supportFragmentManager, "error")


        }
    }



