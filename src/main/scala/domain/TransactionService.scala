package domain

import domain.TransactionType.{CREDIT, DEBIT}

object TransactionService {
  def doIt(client: Client, transaction:Transaction):Client ={
    val currentBalance = client.balance
    val currentTransactions = client.transactions
    transaction.transactionType match {
       case DEBIT => if (isValidDebit(client,transaction)) client.copy(balance = currentBalance-transaction.money.value, transactions = transaction +: currentTransactions.take(9)) else throw TransactionLimitException("Limit exceeded!")
       case CREDIT => client.copy(balance = currentBalance+transaction.money.value, transactions = transaction +: currentTransactions.take(9))
    }
  }

  def isValidDebit(client: Client, transaction: Transaction):Boolean = {
    client.balance - transaction.money.value >= client.limit * -1
  }

  def getLast10Transactions(client: Client):List[Transaction] = {
    client.transactions.sortWith((t1,t2)=>t1.date.compareTo(t2.date)>0)
  }
}
