package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory
import com.querydsl.jpa.impl.JPAQueryFactory

class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun findAllWithHistories(): List<User> {
        val user = QUser.user
        val userLoanHistory = QUserLoanHistory.userLoanHistory

        return queryFactory.select(user).distinct()
            .from(user)
            .leftJoin(userLoanHistory).on(userLoanHistory.user.id.eq(user.id)).fetchJoin()
            .fetch()
    }
}