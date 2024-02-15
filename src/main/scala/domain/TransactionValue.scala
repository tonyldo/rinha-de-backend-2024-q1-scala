package domain

case class TransactionValue(value:Int) {
  if (value< 0)
    throw new TransactionValueException("Value is negative")
}

object TransactionValue{
  def fromString(valueStr:String): TransactionValue = {
    try{
      TransactionValue(valueStr.toInt)
    } catch {
      case e:Exception => throw TransactionValueException("Value is not a integer!",e)
    }
  }
}
