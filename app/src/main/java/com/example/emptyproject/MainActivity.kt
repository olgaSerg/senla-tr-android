package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.fragments.ProfileFragment
import com.example.emptyproject.models.Profile
import com.example.emptyproject.models.State

private var state: State = State()

class MainActivity : AppCompatActivity(), LoginFragment.OnDataSendListener,
    ProfileFragment.OnSendClickLogout {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadLoginFragment(state)
        }

        val testerClass = createClassTester()
        callMethods(testerClass)
        getConstructors(testerClass)
        getAttributes(testerClass)
        getMethods(testerClass)
    }

    private fun loadLoginFragment(state: State) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, LoginFragment.newInstance(state))
            commit()
        }
    }

    private fun createClassTester(): Class<*> {
        return Class.forName("com.example.emptyproject.Tester")
    }

    private fun callMethods(testerClass: Class<*>) {
        val tester = testerClass.constructors[0].newInstance("test")

        val methodDoPublic = Tester::class.java.getDeclaredMethod("doPublic")
        methodDoPublic.invoke(tester)

        val methodDoProtected = Tester::class.java.getDeclaredMethod("doProtected")
        methodDoProtected.invoke(tester)

        val methodDoPrivate = Tester::class.java.getDeclaredMethod("doPrivate")
        methodDoPrivate.isAccessible = true
        methodDoPrivate.invoke(tester)
    }

    private fun getConstructors(testerClass: Class<*>) {
        for (constructor in testerClass.constructors) {
            Log.e("constructors", constructor.toString())
        }
    }

    private fun getAttributes(testerClass: Class<*>) {
        val fields = testerClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            Log.e("attributes", field.toString())
            if (field.isAnnotationPresent(TesterAttribute::class.java)) {
                val annotation = field.getAnnotation(TesterAttribute::class.java)
                annotation?.toString()?.let { Log.e("annotation", it) }
            }
        }
    }

    private fun getMethods(testerClass: Class<*>) {
        val methods = testerClass.declaredMethods
        for (method in methods) {
            method.isAccessible = true
            Log.e("methods", method.toString())
            if (method.isAnnotationPresent(TesterMethod::class.java)) {
                val annotation = method.getAnnotation(TesterMethod::class.java)
                annotation?.toString()?.let { Log.e("annotation", it) }
            }
        }
    }

    override fun sendProfile(profile: Profile) {
        val profileFragment = ProfileFragment.newInstance(state)

        loadProfileFragment(profileFragment)
    }

    private fun loadProfileFragment(profileFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, profileFragment)
            commit()
        }
    }

    override fun clickLogout() {
        state = State()
        loadLoginFragment(state)
    }

    companion object {
        const val STATE = "state"
    }
}
