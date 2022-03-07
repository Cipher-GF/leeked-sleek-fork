package today.sleek.client.friend

import java.util.concurrent.CopyOnWriteArrayList
import today.sleek.client.friend.Friend

class FriendManager {

    val friends: CopyOnWriteArrayList<Friend> = CopyOnWriteArrayList()

    fun clearFriends() {
        friends.clear()
    }

    fun addFriend(friend: Friend) {
        friends.add(friend)
    }

    fun removeFriend(friend: String?) {
        friends.removeIf { friend1: Friend -> friend1.name.equals(friend, ignoreCase = true) }
    }

    fun isFriend(ign: String): Boolean {
        for (friend in friends) {
            if (friend.name == ign) {
                return true
            }
        }
        return false
    }

    fun getFriend(name: String): Friend? {
        for (friend in friends) {
            if (friend.name == name) {
                return friend
            }
        }
        return null
    }

}