package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록이 정상적으로 동작한다")
    fun saveBookTest() {
        // given
        val request = BookRequest("자바 ORM 표준 JPA 프로그래밍")
        // when
        bookService.saveBook(request)
        // then
        val result = bookRepository.findAll();
        assertThat(result).hasSize(1)
        assertThat(result[0]).extracting("name").isEqualTo("자바 ORM 표준 JPA 프로그래밍")
    }

    @Test
    @DisplayName("책을 정상적으로 대여할 수 있다")
    fun loadBookTest() {
        // given
        val savedBook = bookRepository.save(Book("클린코드"))
        val savedUser = userRepository.save(User("정수현", null))
        val request = BookLoanRequest("정수현", "클린코드")
        // when
        bookService.loanBook(request)
        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo(savedBook.name)
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].user.name).isEqualTo(savedUser.name)
        assertThat(result[0].isReturn).isEqualTo(false)
    }

    @Test
    @DisplayName("책을 정상적으로 대여가 실패한다")
    fun loanBookExceptionTest() {
        // given
        val savedBook = bookRepository.save(Book("클린코드"))
        val savedUser = userRepository.save(User("정수현", null))
        val savedLoanHistory = userLoanHistoryRepository.save(UserLoanHistory(savedUser, "클린코드", false))
        val request = BookLoanRequest("정수현", "클린코드")

        // when, then
        assertThrows<IllegalArgumentException> { bookService.loanBook(request) }
            .apply {
                assertThat(this.message).isEqualTo("진작 대출되어 있는 책입니다")
            }
    }

    @Test
    @DisplayName("책 반납이 정상적으로 동작한다")
    fun returnBookTest() {
        // given
        val savedBook = bookRepository.save(Book("클린코드"))
        val savedUser = userRepository.save(User("정수현", null))
        val savedLoanHistory = userLoanHistoryRepository.save(UserLoanHistory(savedUser, "클린코드", false))
        val request = BookReturnRequest("정수현", "클린코드")
        // when
        bookService.returnBook(request)
        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].isReturn).isTrue
    }
}