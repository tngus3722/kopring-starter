package com.group.libraryapp.service.user

import com.group.libraryapp.constant.UserLoanStatus
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {


    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @DisplayName("유저가 정상적으로 저장된다")
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

    @DisplayName("유저가 정상적으로 조회된다")
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

    @DisplayName("유저가 정상적으로 업데이트 된다")
    @Test
    fun updateUserNameTest() {
        // given
        val savedUser = userRepository.save(User("A", 25))
        val updateRequest = UserUpdateRequest(savedUser.id!!, "B")
        // when
        userService.updateUserName(updateRequest)
        // then
        val result = userRepository.findAll()[0]
        assertThat(result).extracting("name").isEqualTo("B")
    }

    @DisplayName("유저가 정상적으로 삭제된다")
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

    @DisplayName("대출기록이 없는 유저도 응답에 포함된다")
    @Test
    fun getUserLoanHistory() {
        // given
        userRepository.save(User("A", null))

        // when
        val results = userService.getUserLoanHistory()
        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @DisplayName("대출기록이 많은 유저도 응답이 정상동작한다")
    @Test
    fun getUserLoanHistory2() {
        // given
        val savedUserA = userRepository.save(User("A", null))
        val savedUserB = userRepository.save(User("B", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUserA, "클린코드"),
                UserLoanHistory.fixture(savedUserB, "이상한 나라의 엘리스", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(savedUserB, "운영체제"),
                UserLoanHistory.fixture(savedUserB, "C언어"),

            )
        )

        // when
        val results = userService.getUserLoanHistory().sortedBy  { o -> o.name }

        // then
        assertThat(results).hasSize(2)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).extracting("name")
            .containsExactlyInAnyOrder("클린코드")
        assertThat(results[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false)
        assertThat(results[1].name).isEqualTo("B")
        assertThat(results[1].books).extracting("name")
            .containsExactlyInAnyOrder("이상한 나라의 엘리스", "운영체제", "C언어")
        assertThat(results[1].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }
}