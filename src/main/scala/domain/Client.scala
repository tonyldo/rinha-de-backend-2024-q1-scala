package domain

import domain.TransactionType.{CREDIT, DEBIT}

case class Client(id: Int, limit: Int, balance: Int) {

  def isValidDebit(transaction: Transaction):Boolean = {
    this.balance - transaction.money.value >= this.limit * -1
  }

  def doIt(transaction:Transaction):Int ={
    transaction.transactionType match {
      case DEBIT => if (isValidDebit(transaction)) this.balance-transaction.money.value else throw TransactionLimitException("Limit exceeded!")
      case CREDIT => this.balance+transaction.money.value
    }
  }
}