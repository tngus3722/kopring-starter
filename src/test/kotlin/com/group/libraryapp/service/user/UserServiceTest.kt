package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) {


    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUserTest() {
        // given
        val userCreateRequest: UserCreateRequest = UserCreateRequest("정수현", null)
        // when
        userService.saveUser(userCreateRequest)
        // then
        val result: List<User> = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("정수현")
        assertThat(result[0].age).isEqualTo(null)
    }

    @Test
    fun getUsersTest() {
        // given
        userRepository.saveAll(
            listOf(
                User("A", 26),
                User("B", 23)
            )
        )
        // when
        val result: List<UserResponse> = userService.getUsers()
        // then
        assertThat(result).hasSize(2)
        assertThat(result).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(result).extracting("age").containsExactlyInAnyOrder(26, 23)
    }

    @Test
    fun updateUserNameTest() {
        // given
        val savedUser = userRepository.save(User("A", 25))
        val updateRequest = UserUpdateRequest(savedUser.id, "B")
        // when
        userService.updateUserName(updateRequest)
        // then
        val result = userRepository.findAll()[0]
        assertThat(result).extracting("name").isEqualTo("B")
    }

    @Test
    fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))
        // when
        userService.deleteUser("A")
        // then
        val result = userRepository.findAll()
        assertThat(result).isEmpty()
    }
}