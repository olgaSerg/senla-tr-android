package com.example.emptyproject.lesson04_4

class SocialNet {
    private val userByName = HashMap<String, User>()

    fun addUser(user: User) {
        userByName[user.name] = user
    }

    fun findUser(name: String): User {
        return userByName.getValue(name)
    }

    fun findUserFriends(user: User): List<User> {
        val friendsList = mutableListOf<User>()
        for (friend in user.getFriends()) {
            friendsList.add(friend)
            for (indirectFriend in friend.getFriends()) {
                if (indirectFriend.name != user.name) {
                    friendsList.add(indirectFriend)
                }
            }
        }
        return friendsList.toSet().toList()
    }

}