package domain

case class Client(name: String, limit: Int, balance: Int, transactions: List[Transaction])