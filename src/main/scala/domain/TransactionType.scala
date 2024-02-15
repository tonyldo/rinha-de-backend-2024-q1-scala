package domain

sealed abstract class TransactionType

object TransactionType {
  case object CREDIT extends TransactionType
  case object DEBIT extends TransactionType

  def fromString(tType: String): TransactionType =
    tType.toLowerCase match {
      case "c" => CREDIT
      case "d" => DEBIT
      case _ => throw TransactionTypeException("Invalid transaction type.")
    }
}