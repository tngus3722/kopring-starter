package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.constant.UserLoanStatus
import com.querydsl.jpa.impl.JPAQueryFactory

class UserLoanRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserLoanRepositoryCustom {

    override fun searchUserLoanHistoryBy(
        bookName: String,
        status: UserLoanStatus?,
    ): UserLoanHistory? {
        val userLoanHistory = QUserLoanHistory.userLoanHistory

        return queryFactory.select(userLoanHistory)
            .from(userLoanHistory)
            .where(userLoanHistory.bookName.eq(bookName)
                .and(userLoanHistory.status.eq(status)))
            .fetchFirst()
    }

    override fun count(status: UserLoanStatus): Long {
        val userLoanHistory = QUserLoanHistory.userLoanHistory

        return queryFactory.select(userLoanHistory.count())
            .from(userLoanHistory)
            .where(userLoanHistory.status.eq(status))
            .fetchOne() ?: 0L
    }
}