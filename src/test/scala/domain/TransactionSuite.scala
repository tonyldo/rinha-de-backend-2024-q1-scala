package domain

import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDateTime


class TransactionSuite extends AnyFunSuite {
  test("Verify if transaction value is positive") {
    assertThrows[TransactionValueException] {
      TransactionValue.fromString("-10")
    }
    assertThrows[TransactionValueException]{
      TransactionValue(-1)
    }
    assert(TransactionValue.fromString("10").value==10)
    assert(TransactionValue(11).value==11)
  }

  test("Verify if transaction description is invalid") {
    assertThrows[TransactionDescriptionException] {
      TransactionDescription("Trans 11111")
    }
    assert(TransactionDescription("Trans 1").value=="Trans 1")
  }

  test("Verify if transaction type is valid") {
    assertThrows[TransactionTypeException] {
      TransactionType.fromString("x")
    }
    assert(TransactionType.CREDIT == TransactionType.fromString("c"))
    assert(TransactionType.CREDIT == TransactionType.fromString("C"))
    assert(TransactionType.DEBIT == TransactionType.fromString("d"))
    assert(TransactionType.DEBIT == TransactionType.fromString("D"))
  }

  test("Verify debit transaction success") {
    val client1 = Client(1, Some(1000), Some(0))
    val transaction1 = new Transaction(TransactionValue(10),TransactionType.DEBIT,TransactionDescription("Trans 1"))

    assert(client1.doIt(transaction1) == client1.balance.get - transaction1.money.value)
  }

  test("Verify credit transaction success") {
    val client1 = Client(1, Some(1000), Some(0))
    val transaction1 = new Transaction(TransactionValue(10),TransactionType.CREDIT,TransactionDescription("Trans 1"))

    assert(client1.doIt(transaction1) == client1.balance.get + transaction1.money.value)
  }

  test("Verify debit transaction is valid") {
    val client1 = Client(1, Some(1000), Some(0))
    val transaction1 = new Transaction(TransactionValue(1001),TransactionType.DEBIT,TransactionDescription("Trans 1"))

    val client3 = Client(3, Some(1001), Some(0))
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

    val client1 = Client(1, Some(1001), Some(0))

    val client = transactionList.foldLeft(client1)((c,t)=>c.copy(balance = Some(c.doIt(t))))

    assert(client.balance.get == -12)
  }
}
