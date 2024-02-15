package domain

import java.time.LocalDate

case class Transaction(money: TransactionValue, transactionType: TransactionType, description: TransactionDescription, date: LocalDate) {

  def this(money: TransactionValue, transactionType: TransactionType, description: TransactionDescription) = {
    this(money, transactionType, description, LocalDate.now())
  }

}
