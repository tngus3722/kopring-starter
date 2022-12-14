package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.constant.UserLoanStatus

interface UserLoanRepositoryCustom {

    fun searchUserLoanHistoryBy(bookName: String, status: UserLoanStatus? = null): UserLoanHistory?

    fun count(status: UserLoanStatus): Long
}