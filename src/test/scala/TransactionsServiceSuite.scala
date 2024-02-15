import domain.{Client, Transaction, TransactionDescription, TransactionType, TransactionValue}
import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDateTime

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

    val transactionList =
      for (i <- 1 to 12) yield {
        val newDate = if (i % 2 == 0) {
        LocalDateTime.now().plusSeconds(i)
        } else {
          LocalDateTime.now().minusSeconds(i)
        }
        transactionBase.copy(description = TransactionDescription("Trans " + i),date = newDate)
      }

    val client1 = Client("client 1", 1001, 0, List.empty)

    val client = transactionList.foldLeft(client1)((c,t)=>TransactionService.doIt(c,t))

    assert(client.balance == -12)
    assert(TransactionService.getLast10Transactions(client).size == 10)
  }
}
