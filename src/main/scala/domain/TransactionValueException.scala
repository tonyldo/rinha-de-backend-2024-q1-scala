package domain

case class TransactionValueException(msg: String, cause: Throwable) extends Exception {
  def this(msg: String) = {
    this(msg, new Exception)
  }
}
