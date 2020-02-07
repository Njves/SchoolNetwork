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
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.LoginService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnActionBarUpdateListener
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    val TAG = "LoginFragment"
    lateinit var onAuthPassedListener : OnAuthPassedListener
    lateinit var onActionBarUpdateListener : OnActionBarUpdateListener
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAuthPassedListener) {
            onAuthPassedListener = context
        } else {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_login, container, false)
        onActionBarUpdateListener.updateActionBar(context?.resources?.getString(R.string.auth_login)?:"Login")
        // init views
        val edName = v.findViewById<TextInputEditText>(R.id.edName)
        val edPass = v.findViewById<TextInputEditText>(R.id.edPass)
        val btnSubmit = v.findViewById<Button>(R.id.btnSubmit)
        val checkboxRemMe = v.findViewById<CheckedTextView>(R.id.checkboxRemMe)
        val btnForgotPass = v.findViewById<TextView>(R.id.tvForgotPass)
        val progressBar = v.findViewById<ProgressBar>(R.id.progressBar)
        checkboxRemMe.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                checkboxRemMe.isChecked = !checkboxRemMe.isChecked
            }
        })
        val storage = AuthStorage(context)
        edName.setText(storage.getUserName()?:"")
        btnSubmit.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            val loginService = NetworkService.instance.getRetrofit().create(LoginService::class.java)
            val call = loginService.callLogin(edName.text.toString(),
                edPass.text.toString()
            )
            call.enqueue(object: Callback<NetworkResponse<User>>{
                override fun onFailure(call: Call<NetworkResponse<User>>, t: Throwable) {
                    /// Если ошибка запроса
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, "Произашла ошибка запроса", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }

                override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {
                    // CODE 0 - Успех CODE 1 - Ошибка
                    val code = response.body()?.code ?: 1
                    val storage = AuthStorage(context)
                    val message = response.body()?.message ?: "Неизвестная ошибка"
                    val dataObjects = response.body()?.data
                    progressBar.visibility = View.INVISIBLE
                    if(response.body()?.code==0)
                    {
                        if(checkboxRemMe.isChecked)
                        {
                            storage.setUserName(dataObjects?.name?:"")
                        }

                        onAuthPassedListener.onSuccess(dataObjects?.uid)
                    }
                    // Если данные неправильные
                    else
                    {
                        if(activity?.supportFragmentManager!=null)
                        {
                            AuthErrorDialog.newInstance(message).show(activity?.supportFragmentManager, null)
                        }

                    }

                }
            })


        })
        return v
    }


}