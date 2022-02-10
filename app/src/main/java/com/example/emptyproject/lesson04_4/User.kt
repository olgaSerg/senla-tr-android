package com.example.emptyproject.lesson04_4

class User(val name: String) {
    private val friends: MutableList<User> = mutableListOf()

    fun getFriends() = friends

    fun addFriend(friend: User) {
        friends.add(friend)
    }

    override fun toString(): String {
        return "User($name)"
    }
}