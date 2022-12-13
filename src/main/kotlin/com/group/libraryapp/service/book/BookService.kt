package com.group.libraryapp.service.book

import com.group.libraryapp.constant.UserLoanStatus
import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        val book = Book(request.name, request.type)
        bookRepository.save(book)
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        val book = bookRepository.findByName(request.bookName) ?: fail()
        // 해당 책의 대출이력이 있으며, 그 이력이 대출되어 있는 상태라면 대여 불가
        val userLoanHistoryList = userLoanHistoryRepository.findByBookName(request.bookName)
        if (userLoanHistoryList.isNotEmpty() && userLoanHistoryList.any { o -> o.status == UserLoanStatus.LOANED }) {
            throw IllegalArgumentException("현재 대출할 수 있는 책이 존재하지 않습니다.")
        }

        val user = userRepository.findByName(request.userName) ?: fail()

        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: fail()
        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun getCountLoanedBook(): Int {
        return userLoanHistoryRepository.countAllByStatus(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        return bookRepository.getStats()
    }
}