package com.group.libraryapp.domain.user

import com.group.libraryapp.constant.UserLoanStatus
import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.*

@Entity
class User(
    @Column(nullable = false)
    var name: String,
    val age: Int?,
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    init {
        if (this.name.isBlank()) {
            throw IllegalArgumentException("이름은 비어있을 수 없습니다.")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name, UserLoanStatus.LOANED))
    }

    fun returnBook(bookName: String) {
        this.userLoanHistories.first { userLoanHistory -> userLoanHistory.bookName == bookName }.doReturn()
    }
}