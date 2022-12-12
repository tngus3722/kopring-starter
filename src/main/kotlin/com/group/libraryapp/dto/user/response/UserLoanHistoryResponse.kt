package com.group.libraryapp.dto.user.response

import com.group.libraryapp.constant.UserLoanStatus
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory

data class UserLoanHistoryResponse(
    val name: String,
    val books: List<BookHistoryResponse>,
) {

    companion object {
        fun from(user: User): UserLoanHistoryResponse {
            return UserLoanHistoryResponse(
                name = user.name,
                books = user.userLoanHistories.map { o -> BookHistoryResponse.from(o) }
                )
        }
    }
}

data class BookHistoryResponse(
    val name: String,
    val isReturn: Boolean,
) {
    // NOTE : 스터디 기록용 : 강의를 들으며 처음부터 정적 static Method와 N+1을 해결하도록 작성하여 26-31강 동안 코드 수정이 없음
    companion object {
        fun from(userLoanHistory: UserLoanHistory): BookHistoryResponse {
            return BookHistoryResponse(
                name = userLoanHistory.bookName,
                isReturn = userLoanHistory.status == UserLoanStatus.RETURNED
            )
        }
    }
}