package domain

case class TransactionDescription(value: String){
  if (value.length>10)  throw TransactionDescriptionException("Description size is bigger than 10")
}
