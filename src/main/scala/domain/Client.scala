package domain

case class Client(id: Int, limit: Int, balance: Int, transactions: List[Transaction])