package domain

case class BalanceReport (balance:Int,limit:Int,transactions:Seq[Transaction])
object BalanceReport {
  def from(tuple:(Client,Seq[Transaction])): BalanceReport = {
    BalanceReport(tuple._1.balance,tuple._1.limit,tuple._2)
  }
}
