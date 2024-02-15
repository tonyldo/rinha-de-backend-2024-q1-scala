import domain.{TransactionDescription, TransactionDescriptionException, TransactionType, TransactionTypeException, TransactionValue, TransactionValueException}
import org.scalatest.funsuite.AnyFunSuite


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
}
