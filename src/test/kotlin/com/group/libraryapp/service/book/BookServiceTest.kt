package com.group.libraryapp.service.book

import com.group.libraryapp.constant.BookType
import com.group.libraryapp.constant.UserLoanStatus
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
        val request = BookRequest("자바 ORM 표준 JPA 프로그래밍", BookType.COMPUTER)
        // when
        bookService.saveBook(request)
        // then
        val result = bookRepository.findAll();
        assertThat(result).hasSize(1)
        assertThat(result[0]).extracting("name").isEqualTo("자바 ORM 표준 JPA 프로그래밍")
        assertThat(result[0]).extracting("type").isEqualTo(BookType.COMPUTER)
    }

    @Test
    @DisplayName("책을 정상적으로 대여할 수 있다")
    fun loadBookTest() {
        // given
        val savedBook = bookRepository.save(Book.fixture("클린코드"))
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
        assertThat(result[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    @DisplayName("책을 정상적으로 대여가 실패한다")
    fun loanBookExceptionTest() {
        // given
        bookRepository.save(Book.fixture("클린코드"))
        val savedUser = userRepository.save(User("정수현", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "클린코드"))
        val request = BookLoanRequest("정수현", "클린코드")

        // when, then
        assertThrows<IllegalArgumentException> { bookService.loanBook(request) }
            .apply {
                assertThat(this.message).isEqualTo("현재 대출할 수 있는 책이 존재하지 않습니다.")
            }
    }

    @Test
    @DisplayName("책 반납이 정상적으로 동작한다")
    fun returnBookTest() {
        // given
        bookRepository.save(Book.fixture("클린코드"))
        val savedUser = userRepository.save(User("정수현", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "클린코드"))
        val request = BookReturnRequest("정수현", "클린코드")
        // when
        bookService.returnBook(request)
        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @DisplayName("빌린 책의 갯수를 정상 확인한다")
    @Test
    fun getCountLoanedBook() {
        // given
        val savedUser = userRepository.save(User("정수현", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "클린코드"),
                UserLoanHistory.fixture(savedUser, "클린코드2", UserLoanStatus.RETURNED)
            )
        )

        // when
        val count = bookService.getCountLoanedBook();
        // then
        assertThat(count).isEqualTo(1)
    }

    @DisplayName("분야별 책 권수를 정상 확인한다")
    @Test
    fun getBookStatistics() {
        // given
        bookRepository.saveAll(
            listOf(
                Book.fixture("클린코드"),
                Book.fixture("클린코드2", BookType.ECONOMY),
                Book.fixture("클린코드3", BookType.SCIENCE),
                Book.fixture("클린코드4"),
            )
        )

        // when
        val result = bookService.getBookStatistics();

        // then
        assertThat(result).hasSize(3)
        assertThat(result).extracting("type").containsExactlyInAnyOrder(BookType.COMPUTER, BookType.ECONOMY, BookType.SCIENCE)
        assertThat(result).extracting("count").containsExactlyInAnyOrder(1L,2L,1L)
    }
}