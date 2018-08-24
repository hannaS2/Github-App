package com.example.janghanna.githubapp.ui

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.util.Log
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.*
import kotlinx.android.synthetic.main.activity_auth.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthActivity : AppCompatActivity() {

    companion object {
        val TAG = AuthActivity::class.java.simpleName

        const val CLIENT_ID = "c581f0efb60277f79b44"
        const val CLIENT_SECRET = "798d98d94d7e25b510e3ecf38a2167b5f32d9fe1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        oAuthText.setOnClickListener {
            val authUri = Uri.Builder().scheme("https")
                    .authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", CLIENT_ID)
                    .build()

            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, authUri)

        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        toast("onNewIntent")
        check(intent != null)
        check(intent?.data != null)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code") ?: throw IllegalStateException("no code!!")

        getAccessToken(code)
    }

    private fun getAccessToken(code: String) {
        Log.i(TAG, "getAccessToken: $code")

        val call = authApi.getAccessToken(CLIENT_ID, CLIENT_SECRET, code)
        call.enqueue({
            it.body()?.let {
                toast(it.toString())

                updateToken(this, it.accessToken)

                startActivity<MainActivity>()
                overridePendingTransition(R.anim.slide_left, R.anim.hold)

//                val githubApiCall = provideGithubApi(this).searchRepository("hello")
//                githubApiCall.enqueue({
//                    it.body()?.let {
//                        Log.i(TAG, it.toString())
//                        Log.i(TAG, "total_count: ${it.totalCount}")
//                        Log.i(TAG, it.items.toString())
//                    }
//
//                }, {
//
//                })


            }
        }, {
            toast(it.message.toString())
        })

    }

}

fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit, failure: (t: Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) = failure(t)
        override fun onResponse(call: Call<T>, response: Response<T>) = success(response)
    })
}