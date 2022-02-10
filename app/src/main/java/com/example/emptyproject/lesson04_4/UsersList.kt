package com.example.emptyproject.lesson04_4

fun main() {
    val h = hashMapOf(
        "Петя" to listOf("Вася", "Оля", "Маша"),
        "Вася" to listOf("Толя", "Петя"),
        "Оля" to listOf("Петя", "Маша", "Женя"),
        "Толя" to listOf("Вася", "Максим"),
        "Маша" to listOf("Толя", "Оля"),
        "Женя" to listOf("Оля", "Паша", "Катя"),
        "Паша" to listOf("Женя", "Катя"),
        "Максим" to listOf("Толя", "Алиса"),
        "Алиса" to listOf("Максим", "Катя"),
        "Катя" to listOf("Паша", "Женя", "Алиса"),
    )

    val socialNet = SocialNet()

    for (name in h.keys) {
        socialNet.addUser(User(name))
    }

    for (userName in h.keys) {
        val user = socialNet.findUser(userName)
        for (friendName in h.getValue(userName)) {
            val friend = socialNet.findUser(friendName)
            user.addFriend(friend)
        }
    }

    val user = socialNet.findUser("Петя")
    println(socialNet.findUserFriends(user))
}




