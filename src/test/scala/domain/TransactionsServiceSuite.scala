package domain

import org.scalatest.funsuite.AnyFunSuite
import services.TransactionService

import java.time.LocalDateTime

class TransactionsServiceSuite extends AnyFunSuite {

  test("Verify debit transaction success") {
    val client1 = Client(1, 1000, 0)
    val transaction1 = new Transaction(TransactionValue(10),TransactionType.DEBIT,TransactionDescription("Trans 1"))

    assert(client1.doIt(transaction1) == client1.balance - transaction1.money.value)
  }

  test("Verify credit transaction success") {
    val client1 = Client(1, 1000, 0)
    val transaction1 = new Transaction(TransactionValue(10),TransactionType.CREDIT,TransactionDescription("Trans 1"))

    assert(client1.doIt(transaction1) == client1.balance + transaction1.money.value)
  }

  test("Verify debit transaction is valid") {
    val client1 = Client(1, 1000, 0)
    val transaction1 = new Transaction(TransactionValue(1001),TransactionType.DEBIT,TransactionDescription("Trans 1"))

    val client3 = Client(3, 1001, 0)
    val transaction3 = new Transaction(TransactionValue(1),TransactionType.DEBIT,TransactionDescription("Trans 3"))

    assert(!client1.isValidDebit(transaction1))
    assert(client3.isValidDebit(transaction3))
  }

  test("10 transactions") {
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

    val client1 = Client(1, 1001, 0)

    val client = transactionList.foldLeft(client1)((c,t)=>c.copy(balance =c.doIt(t)))

    assert(client.balance == -12)
  }
}
