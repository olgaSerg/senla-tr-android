package com.example.emptyproject
import android.util.Log

class Tester(@field:TesterAttribute(info = "Some attribute") private val param: String) {

    protected var protParam = 42
    @TesterMethod(description = "Some public method")
    fun doPublic() {
        Log.e("TAG", "public: $param")
    }

    protected fun doProtected() {
        Log.e("TAG", "protected: $param ($protParam)")
    }

    @TesterMethod(description = "Some private method", isInner = true)
    private fun doPrivate() {
        Log.e("TAG", "private: $param")
    }
}