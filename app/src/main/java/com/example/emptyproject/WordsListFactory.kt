package com.example.emptyproject

class WordsListFactory {
    fun createWordsList(): List<String> {
        return listOf(
            "ГРОДНО", "минск", "вИТЕБСК", "брест", "грОДНО", "ГоМель", "могилев", "бРест",
            "гродно", "минск", "ВИТЕБСК", "брест", "гродно", "ГОМель", "могиЛЕВ", "гродно",
            "минск", "витебск", "БРест", "гродно", "гОМЕль"
        )
    }
}