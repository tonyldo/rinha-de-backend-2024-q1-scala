import domain.{Client, Transaction, TransactionDescription, TransactionType, TransactionValue}
import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDate
import java.time.temporal.ChronoUnit


class TransactionsServiceSuite extends AnyFunSuite {

  test("Verify debit transaction success") {
    val client1 = Client("client 1", 1000, 0, List.empty)
    val transaction1 = new Transaction(TransactionValue(10),TransactionType.DEBIT,TransactionDescription("Trans 1"))

    assert(TransactionService.doIt(client1,transaction1).balance == client1.balance - transaction1.money.value)
  }

  test("Verify credit transaction success") {
    val client1 = Client("client 1", 1000, 0, List.empty)
    val transaction1 = new Transaction(TransactionValue(10),TransactionType.CREDIT,TransactionDescription("Trans 1"))

    assert(TransactionService.doIt(client1,transaction1).balance == client1.balance + transaction1.money.value)
  }

  test("Verify debit transaction is valid") {
    val client1 = Client("client 1", 1000, 0, List.empty)
    val transaction1 = new Transaction(TransactionValue(1001),TransactionType.DEBIT,TransactionDescription("Trans 1"))

    val client2 = Client("client 2", 1000, 0, List.empty)
    val transaction2 = new Transaction(TransactionValue(1000),TransactionType.CREDIT,TransactionDescription("Trans 2"))

    val client3 = Client("client 3", 1001, 0, List.empty)
    val transaction3 = new Transaction(TransactionValue(1),TransactionType.DEBIT,TransactionDescription("Trans 3"))

    assert(!TransactionService.isValidDebit(client1,transaction1))
    assert(TransactionService.isValidDebit(client2,transaction2))
    assert(TransactionService.isValidDebit(client3,transaction3))
  }

  test("Return the top 10 transactions ordering by date when it executed") {
    val transactionBase = new Transaction(TransactionValue(1),TransactionType.DEBIT,TransactionDescription("Trans "))

    val client1 = Client("client 3", 1001, 0, List.empty)

    var clientCurrent=client1
    var listTransaction: List[Transaction] = List.empty

    for (i <- 1 to 12) yield {
      val newDate = if (i % 2 == 0) {
        LocalDate.now().plusDays(i)
      } else {
        LocalDate.now().minusDays(i)
      }

      listTransaction = transactionBase.copy(description = TransactionDescription("Trans " + i),date = newDate) +: listTransaction.take(9)
      clientCurrent = TransactionService.doIt(clientCurrent,listTransaction.head)
    }

    val transactionListResult = TransactionService.getLast10Transactions(clientCurrent)
    assert(transactionListResult.size <= 10)
    assert(transactionListResult == listTransaction.sortWith((t1,t2)=>t1.date.compareTo(t2.date)>0))
  }


}
